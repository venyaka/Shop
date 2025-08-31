package veniamin.shop.backend.entity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FileType {
    IMAGE("png", "jpeg", "svg", "webp", "jpg", "gif", "bmp"), PRESENTATION("pdf", "ppt", "pptx"), FILE;


    private List<String> aviableExtensions;

    FileType(String... aviableExtensionz) {
        this.aviableExtensions = Arrays.stream(aviableExtensionz).collect(Collectors.toList());
    }

    public List<String> getAviableExtensions() {
        return aviableExtensions;
    }
}
