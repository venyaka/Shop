package veniamin.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import veniamin.shop.backend.constant.PathConstants;
import veniamin.shop.backend.dto.request.ProductCategoryCreateReqDTO;
import veniamin.shop.backend.entity.ProductCategory;
import veniamin.shop.backend.service.ProductCategoryService;

import java.util.List;

@RestController
@RequestMapping(PathConstants.PRODUCT_CATEGORY_CONTROLLER_PATH)
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryService categoryService;

    @PostMapping
    public ResponseEntity<ProductCategory> createCategory(@RequestBody ProductCategoryCreateReqDTO productCategoryCreateDTO) {
        ProductCategory created = categoryService.createCategory(productCategoryCreateDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategory> updateCategory(@PathVariable Long id, @RequestBody ProductCategory category) {
        ProductCategory updated = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllCategories() {
        List<ProductCategory> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}

