package veniamin.shop.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import veniamin.shop.backend.dto.request.ProductCategoryCreateReqDTO;
import veniamin.shop.backend.entity.Product;
import veniamin.shop.backend.entity.ProductCategory;
import veniamin.shop.backend.exception.NotFoundException;
import veniamin.shop.backend.repository.ProductCategoryRepository;
import veniamin.shop.backend.repository.ProductRepository;
import veniamin.shop.backend.service.ProductCategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductCategory createCategory(ProductCategoryCreateReqDTO productCategoryCreateDTO) {
        ProductCategory category = new ProductCategory();
        category.setName(productCategoryCreateDTO.getName());
        category.setDescription(productCategoryCreateDTO.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        ProductCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена", "CATEGORY_NOT_FOUND"));
        List<Product> products = productRepository.findByProductCategory_Id(categoryId);
        for (Product product : products) {
            product.setIsActive(false);
        }
        productRepository.saveAll(products);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<ProductCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public ProductCategory updateCategory(Long categoryId, ProductCategory updatedCategory) {
        ProductCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена", "CATEGORY_NOT_FOUND"));
        if (updatedCategory.getName() != null) {
            category.setName(updatedCategory.getName());
        }
        if (updatedCategory.getDescription() != null) {
            category.setDescription(updatedCategory.getDescription());
        }
        return categoryRepository.save(category);
    }
}

