package veniamin.shop.backend.dto;

import lombok.Data;

@Data
public class FileDTO {

    private Long id;

    private String remoteUrl;

    private String fileName;

    private String extension;

    private Long authorId;
}
