package veniamin.shop.backend.dto.response;

import lombok.Data;

@Data
public class ProductRespDTO {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Long categoryId;

    private String categoryName;

    private Boolean isActive;

    private Long imageId;

    private String imageUrl;
}
