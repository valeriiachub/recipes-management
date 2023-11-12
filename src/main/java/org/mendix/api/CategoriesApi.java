package org.mendix.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mendix.dto.internals.Category;
import org.mendix.dto.response.CategoryListWrapper;
import org.mendix.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categories")
public class CategoriesApi {

    private final CategoryService categoryService;

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<CategoryListWrapper> getAllCategories() {
        List<Category> allCategories = categoryService.getAll();
        return ResponseEntity.ok(new CategoryListWrapper(allCategories));
    }
}
