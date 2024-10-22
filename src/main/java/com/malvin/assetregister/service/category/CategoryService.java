package com.malvin.assetregister.service.category;

import com.malvin.assetregister.entity.Category;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Category addCategory(Category request) {
        boolean existsByName = categoryRepository.existsByName(request.getName());
        Category category;
        if (existsByName) {
            category = fixCategory(request);
        }else {
           category = createCategory(request);
        }
        return categoryRepository.save(category);
    }

    private Category createCategory(Category request) {
        Category newCategory = new Category();
        newCategory.setName(request.getName());
        newCategory.setDescription(request.getDescription());
        newCategory.setDepreciation(request.getDepreciation());
        newCategory.setDepreciationMethod(request.getDepreciationMethod());
        return newCategory;
    }
    private Category fixCategory(Category request) {
        Category newCategory = new Category();
        newCategory.setDescription(request.getDescription());
        newCategory.setDepreciationMethod(request.getDepreciationMethod());
        return newCategory;
    }

    @Override
    public Category updateCategory(Category request,Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Oops!! category not found")
        );
        category.setDescription(request.getDescription());
        category.setDepreciation(request.getDepreciation());
        category.setDepreciationMethod(request.getDepreciationMethod());
        category.setName(request.getName());
        return categoryRepository.save(category) ;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository:: delete,
                ()-> {
            throw new ResourceNotFoundException("Oops!! category not found");}
        );
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Oops!! category not found")
        );
    }
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
