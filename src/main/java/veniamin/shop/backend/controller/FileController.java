package veniamin.shop.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import veniamin.shop.backend.constant.PathConstants;
import veniamin.shop.backend.dto.FileDTO;
import veniamin.shop.backend.service.FileService;


@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.FILE_CONTROLLER_PATH)
@PreAuthorize("isAuthenticated()")
public class FileController {

    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Сохранить файл")
    public FileDTO save(@RequestPart("file") MultipartFile file) {
        return fileService.save(file);
    }

    @GetMapping("/{fileId}")
    @Operation(summary = "Получить url файла по айди", description = "Возвращает массив байтов")
    public FileDTO getFileDTOById(@PathVariable("fileId") Long fileId) {
        return fileService.getFileDTOById(fileId);
    }

    @GetMapping("/name/{fileName}")
    @Operation(summary = "Получить файл по его названию", description = "Возвращает массив байтов")
    public ByteArrayResource getFileByName(@PathVariable("fileName") String fileName) {
        return fileService.getBytes(fileName);
    }
}
