package org.mendix.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mendix.dto.RecipeMlRequest;
import org.mendix.dto.RecipeSearchCriteria;
import org.mendix.dto.response.RecipeListWrapper;
import org.mendix.service.RecipesManagementService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/recipes")
public class RecipesApi {

    private final RecipesManagementService recipesManagementService;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<RecipeMlRequest> createRecipe(@RequestBody RecipeMlRequest request) {
        RecipeMlRequest recipe = recipesManagementService.createRecipe(request);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<RecipeMlRequest> getRecipe(@PathVariable(value = "id") Long id) {
        RecipeMlRequest recipe = recipesManagementService.getRecipeById(id);
        if (recipe != null) {
            return ResponseEntity.ok(recipe);
        } else {
            return ResponseEntity.notFound()
                                 .build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<RecipeListWrapper> listRecipes(@RequestParam(value = "category", required = false) final String category) {
        List<RecipeMlRequest> recipes = StringUtils.isNotBlank(category)
                ? recipesManagementService.getRecipesByCategory(category)
                : recipesManagementService.getAll();

        if (recipes.isEmpty()) {
            return ResponseEntity.ok(new RecipeListWrapper(Collections.emptyList()));
        } else {
            return ResponseEntity.ok(new RecipeListWrapper(recipes));
        }
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<List<RecipeMlRequest>> searchRecipes(@RequestParam(value = "query") String query) {
        RecipeSearchCriteria criteria = new RecipeSearchCriteria(query);
        List<RecipeMlRequest> matchingRecipes = recipesManagementService.searchRecipes(criteria);
        return ResponseEntity.ok(matchingRecipes);
    }
}
