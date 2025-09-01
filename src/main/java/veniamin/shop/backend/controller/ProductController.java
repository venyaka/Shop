package veniamin.shop.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import veniamin.shop.backend.constant.PathConstants;
import veniamin.shop.backend.dto.ProductDTO;
import veniamin.shop.backend.dto.request.ProductCreateReqDTO;
import veniamin.shop.backend.dto.response.ProductRespDTO;
import veniamin.shop.backend.service.ProductService;

import java.util.List;

@RestController
@RequestMapping(PathConstants.PRODUCT_CONTROLLER_PATH)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Создание нового товара")
    public ResponseEntity<ProductRespDTO> createProduct(@ModelAttribute ProductCreateReqDTO productCreateDTO, @RequestParam(name = "image", required = false) MultipartFile image) {
        ProductRespDTO created = productService.createProduct(productCreateDTO, image);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление товара по ID")
    public ResponseEntity<ProductRespDTO> updateProduct(@PathVariable Long id, @ModelAttribute ProductDTO productDTO, @RequestParam(name = "image", required = false) MultipartFile image) {
        ProductRespDTO updated = productService.updateProduct(id, productDTO, image);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление товара по ID")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Получение списка товаров с фильтрацией по имени, категории и цене")
    public ResponseEntity<List<ProductRespDTO>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double priceFrom,
            @RequestParam(required = false) Double priceTo
    ) {
        List<ProductRespDTO> products = productService.getProducts(name, categoryId, priceFrom, priceTo);
        return ResponseEntity.ok(products);
    }
}

