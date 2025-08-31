package veniamin.shop.backend.dto.request;

import lombok.Data;

@Data
public class ProductCategoryCreateReqDTO {
    
    private String name;

    private String description;
}
