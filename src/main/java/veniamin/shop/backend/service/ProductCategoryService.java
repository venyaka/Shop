package veniamin.shop.backend.service;

import veniamin.shop.backend.dto.request.ProductCategoryCreateReqDTO;
import veniamin.shop.backend.entity.ProductCategory;
import java.util.List;

public interface ProductCategoryService {

    ProductCategory createCategory(ProductCategoryCreateReqDTO productCategoryCreateDTO);

    void deleteCategory(Long categoryId);

    List<ProductCategory> getAllCategories();

    ProductCategory updateCategory(Long categoryId, ProductCategory updatedCategory);
}

