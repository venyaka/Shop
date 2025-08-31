package veniamin.shop.backend.controller;

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
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateReqDTO productCreateDTO) {
        ProductDTO created = productService.createProduct(productCreateDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updated = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
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

