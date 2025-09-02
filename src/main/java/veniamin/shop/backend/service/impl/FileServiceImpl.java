package veniamin.shop.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import veniamin.shop.backend.dto.FileDTO;
import veniamin.shop.backend.exception.BadRequestException;
import veniamin.shop.backend.exception.NotFoundException;
import veniamin.shop.backend.exception.errors.BadRequestError;
import veniamin.shop.backend.exception.errors.NotFoundError;
import veniamin.shop.backend.entity.*;
import veniamin.shop.backend.repository.FileRepository;
import veniamin.shop.backend.repository.UserRepository;
import veniamin.shop.backend.service.FileService;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import veniamin.shop.backend.utils.MimeTypesUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    private Random random = new Random();

    @Value("${local.file.path}")
    private String uploadPath;

    @Value("${remote.file.path}")
    private String remoteUrl;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadPath));
            createDirectoriesForDiffFileTypes();
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    @Override
    @Transactional
    public FileDTO save(MultipartFile file) {
        try {
            Path root = Paths.get(uploadPath);
            if (!Files.exists(root)) {
                init();
                createDirectoriesForDiffFileTypes();
            }

            String fileName = random.nextInt(Integer.MAX_VALUE) + file.getOriginalFilename();

            Tika tika = new Tika();
            String mimeType = tika.detect(fileName);
            String extension = MimeTypesUtil.lookupExt(mimeType);
            if (extension == null) {
                throw new BadRequestException(BadRequestError.NOT_ALLOWED_EXTENSION);
            }
            File f = new File();
            for (FileType type : FileType.values()) {
                if (type.getAviableExtensions().contains(extension)) {
                    f.setFileType(type);
                    break;
                }
            }
            if (null == f.getFileType()) {
                f.setFileType(FileType.FILE);
            }

            String subDirName = f.getFileType().name().toLowerCase();
            f.setRemoteUrl("/api/file/name/" + fileName);
            Path pathToLocalFile = root.resolve(subDirName).resolve(fileName);

            f.setUrl(pathToLocalFile.toString());
            f.setExtension(extension);
            f.setFileName(fileName);
            f.setAuthor(getCurrentUser());
            f = fileRepository.save(f);

            Files.copy(file.getInputStream(), pathToLocalFile);

            FileDTO fileDTO = new FileDTO();
            fileDTO.setRemoteUrl(f.getRemoteUrl());
            fileDTO.setId(f.getId());
            fileDTO.setExtension(f.getExtension());
            fileDTO.setFileName(fileName);
            fileDTO.setAuthorId(f.getAuthor().getId());

            return fileDTO;
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public File findById(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundError.FILE_NOT_FOUND));
        checkAvailableForCurrentUser(file);

        return file;
    }

    @Override
    @Transactional
    public FileDTO getFileDTOById(Long id) {
        File file = fileRepository.findById(id).orElseThrow(() -> new NotFoundException(NotFoundError.FILE_NOT_FOUND));
        checkAvailableForCurrentUser(file);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName(file.getFileName());
        fileDTO.setExtension(file.getExtension());
        fileDTO.setRemoteUrl(file.getRemoteUrl());
        fileDTO.setId(file.getId());
        fileDTO.setAuthorId(file.getAuthor().getId());
        return fileDTO;
    }


    @Override
    @Transactional
    public ByteArrayResource getBytes(String name) {
        File file = fileRepository.findByUrlEndingWith(name)
                .orElseThrow(() -> new NotFoundException(NotFoundError.FILE_NOT_FOUND));
        checkAvailableForCurrentUser(file);
        ByteArrayResource byteArrayResource = getByteArrayResource(file);

        return byteArrayResource;
    }


    private ByteArrayResource getByteArrayResource(File file) {
        try {
            java.io.File f = new java.io.File(file.getUrl());

            if (f.exists() && f.canRead()) {
                byte[] bytes = FileCopyUtils.copyToByteArray(f);
                return new ByteArrayResource(bytes);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void createDirectoriesForDiffFileTypes() throws IOException {
        for (FileType fileType : FileType.values()) {
            String name = fileType.name().toLowerCase();
            Path path = Paths.get(uploadPath).resolve(name);
            if (!Files.exists(path)) {
                Files.createDirectories(Paths.get(uploadPath).resolve(name));
            }
        }
    }


    @Transactional
    protected User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NotFoundError.USER_NOT_FOUND));
    }

    private void checkAvailableForCurrentUser(File file) {
        User user = getCurrentUser();
        if (!user.getRoles().contains(Role.ADMIN)) {
            throw new BadRequestException(BadRequestError.FILE_DELETED);
        }
    }
}
