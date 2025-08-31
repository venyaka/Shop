package veniamin.shop.backend.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Long categoryId;

    private String categoryName;
}

