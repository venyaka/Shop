package veniamin.shop.backend.dto.response;

import lombok.Data;
import veniamin.shop.backend.entity.FileType;

import java.util.List;

@Data
public class FileTypeExtensionsDTO {

    private List<String> extensions;

    private FileType fileType;
}
