package veniamin.shop.backend.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserRespDTO {
    @NotBlank
    private Long id;

    private String email;

    private String firstName;

    private String lastName;
}