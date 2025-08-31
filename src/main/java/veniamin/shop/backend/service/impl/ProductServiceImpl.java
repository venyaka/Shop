package veniamin.shop.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import veniamin.shop.backend.dto.ProductDTO;
import veniamin.shop.backend.dto.request.ProductCreateReqDTO;
import veniamin.shop.backend.entity.File;
import veniamin.shop.backend.entity.FileType;
import veniamin.shop.backend.entity.Product;
import veniamin.shop.backend.entity.ProductCategory;
import veniamin.shop.backend.exception.BadRequestException;
import veniamin.shop.backend.exception.NotFoundException;
import veniamin.shop.backend.exception.errors.BadRequestError;
import veniamin.shop.backend.repository.ProductRepository;
import veniamin.shop.backend.repository.ProductCategoryRepository;
import veniamin.shop.backend.service.FileService;
import veniamin.shop.backend.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final FileService fileService;

    @Override
    public ProductDTO createProduct(ProductCreateReqDTO productCreateDTO) {
        if (productCreateDTO.getName() == null || productCreateDTO.getName().isEmpty()) {
            throw new BadRequestException("Требуется указать название продукта", "PRODUCT_NAME_REQUIRED");
        }
        if (productCreateDTO.getPrice() == null) {
            throw new BadRequestException("Требуется указать цену продукта", "PRODUCT_PRICE_REQUIRED");
        }
        Product product = new Product();
        product.setName(productCreateDTO.getName());
        product.setDescription(productCreateDTO.getDescription());
        product.setPrice(productCreateDTO.getPrice());

        if ((null != productCreateDTO.getImageId())) {
            File newAvatar = fileService.findById(productCreateDTO.getImageId());

            if (!(FileType.IMAGE).equals(newAvatar.getFileType())) {
                throw new BadRequestException(BadRequestError.FILE_MUST_BE_IMAGE);
            }

            product.setImage(newAvatar);
        }


        if (productCreateDTO.getIsActive() != null) {
            product.setIsActive(productCreateDTO.getIsActive());
        } else {
            product.setIsActive(true);
        }
        if (productCreateDTO.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(productCreateDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена", "CATEGORY_NOT_FOUND"));
            product.setProductCategory(category);
        }
        Product savedProduct = productRepository.save(product);
        return toDTO(savedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Продукт не найден", "PRODUCT_NOT_FOUND");
        }
        productRepository.deleteById(productId);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Продукт не найден", "PRODUCT_NOT_FOUND"));
        if (productDto.getName() != null) {
            product.setName(productDto.getName());
        }
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getPrice() != null) {
            product.setPrice(productDto.getPrice());
        }
        if (productDto.getIsActive() != null) {
            product.setIsActive(productDto.getIsActive());
        }
        if (productDto.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена", "CATEGORY_NOT_FOUND"));
            product.setProductCategory(category);
        }

        if ((null != productDto.getImageId())) {
            File newAvatar = fileService.findById(productDto.getImageId());
            if (!(FileType.IMAGE).equals(newAvatar.getFileType())) {
                throw new BadRequestException(BadRequestError.FILE_MUST_BE_IMAGE);
            }
            product.setImage(newAvatar);
        }

        Product savedProduct = productRepository.save(product);
        return toDTO(savedProduct);
    }

    @Override
    public List<ProductDTO> getProducts(String name, Long categoryId, Double priceFrom, Double priceTo) {
        Specification<Product> spec = (root, query, cb) -> cb.conjunction();
        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("productCategory").get("id"), categoryId));
        }
        if (priceFrom != null) {
            spec = spec.and((root, query, cb) -> cb.ge(root.get("price"), priceFrom));
        }
        if (priceTo != null) {
            spec = spec.and((root, query, cb) -> cb.le(root.get("price"), priceTo));
        }
        List<Product> products = productRepository.findAll(spec);
        return products.stream().map(this::toDTO).toList();
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        if (product.getProductCategory() != null) {
            dto.setCategoryId(product.getProductCategory().getId());
            dto.setCategoryName(product.getProductCategory().getName());
        }
        return dto;
    }
}
