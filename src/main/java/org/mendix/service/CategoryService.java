package org.mendix.service;

import lombok.RequiredArgsConstructor;
import org.mendix.dto.internals.Category;
import org.mendix.repository.CategoryRepository;
import org.mendix.repository.model.CategoryEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mendix.utils.EntityMapper.mapToCategoryDto;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAll() {
        Iterable<CategoryEntity> allCategoriesFromDb = categoryRepository.findAll();
        if (allCategoriesFromDb.iterator().hasNext()) {
            return mapToCategoryDto(allCategoriesFromDb);
        }
        return Collections.emptyList();
    }

    private Category mapToDto(CategoryEntity categoryEntity) {
        Category category = new Category();
        category.setCategoryId(categoryEntity.getCategoryId());
        category.setCategoryName(categoryEntity.getCategoryName());

        return category;
    }
}
