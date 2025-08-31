package veniamin.shop.backend.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import veniamin.shop.backend.dto.FileDTO;
import veniamin.shop.backend.dto.response.FileTypeExtensionsDTO;
import veniamin.shop.backend.entity.File;
import veniamin.shop.backend.entity.FileType;

import java.util.List;

public interface FileService {

    FileDTO save(MultipartFile file);

    File findById(Long id);

    FileDTO getFileDTOById(Long fileId);

    ByteArrayResource getBytes(String name);

}
