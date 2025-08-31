package veniamin.shop.backend.service;

import veniamin.shop.backend.dto.ProductDTO;
import veniamin.shop.backend.dto.request.ProductCreateReqDTO;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(ProductCreateReqDTO productCreateDTO);

    void deleteProduct(Long productId);

    ProductDTO updateProduct(Long productId, ProductDTO productDto);

    List<ProductDTO> getProducts(String name, Long categoryId, Double priceFrom, Double priceTo);
}
