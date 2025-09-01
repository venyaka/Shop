package veniamin.shop.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductCreateReqDTO {

    private String name;

    private String description;

    private Double price;

    private Long categoryId;

    private Boolean isActive;
}
