package com.malvin.assetregister.service.category;

import com.malvin.assetregister.entity.Category;

import java.util.List;

public interface ICategoryService {
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategory(Long id);

    Category getCategoryByName(String name);
    Category getCategoryById(Long categoryId);
    List<Category> getAllCategories();
}
