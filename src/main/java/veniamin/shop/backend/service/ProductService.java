package veniamin.shop.backend.service;

import org.springframework.web.multipart.MultipartFile;
import veniamin.shop.backend.dto.ProductDTO;
import veniamin.shop.backend.dto.request.ProductCreateReqDTO;
import veniamin.shop.backend.dto.response.ProductRespDTO;

import java.util.List;

public interface ProductService {

    ProductRespDTO createProduct(ProductCreateReqDTO productCreateDTO, MultipartFile image);

    void deleteProduct(Long productId);

    ProductRespDTO updateProduct(Long productId, ProductDTO productDto, MultipartFile image);

    List<ProductRespDTO> getProducts(String name, Long categoryId, Double priceFrom, Double priceTo);
}
