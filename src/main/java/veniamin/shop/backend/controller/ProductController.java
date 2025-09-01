package veniamin.shop.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import veniamin.shop.backend.constant.PathConstants;
import veniamin.shop.backend.dto.ProductDTO;
import veniamin.shop.backend.dto.request.ProductCreateReqDTO;
import veniamin.shop.backend.service.ProductService;

import java.util.List;

@RestController
@RequestMapping(PathConstants.PRODUCT_CONTROLLER_PATH)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Создание нового товара")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateReqDTO productCreateDTO) {
        ProductDTO created = productService.createProduct(productCreateDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление товара по ID")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updated = productService.updateProduct(id, productDTO);
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
    public ResponseEntity<List<ProductDTO>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double priceFrom,
            @RequestParam(required = false) Double priceTo
    ) {
        List<ProductDTO> products = productService.getProducts(name, categoryId, priceFrom, priceTo);
        return ResponseEntity.ok(products);
    }
}

