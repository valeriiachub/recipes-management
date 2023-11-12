package org.mendix.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mendix.dto.Recipe;
import org.mendix.dto.RecipeMlRequest;
import org.mendix.dto.internals.Ingredient;
import org.mendix.exception.RecipeValidationException;
import org.mendix.repository.RecipesRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecipeValidator {

    private final RecipesRepository recipesRepository;

    public void validate(RecipeMlRequest recipeMlRequest) {
        List<String> validationErrors = new ArrayList<>();

        Recipe recipe = recipeMlRequest.getRecipe();
        if (recipesRepository.existsByTitle(recipeMlRequest.getRecipe().getHead().getTitle())) {
            validationErrors.add("Recipe with this title already exists.");
        }

        List<String> categories = recipe.getHead().getCategories();
        if (CollectionUtils.isEmpty(categories)) {
            validationErrors.add("Recipe should contain at least one category.");
        }

        List<Ingredient> ingredients = recipe.getIngredients();
        if (CollectionUtils.isEmpty(ingredients)) {
            validationErrors.add("Recipe should contain at least one ingredient.");
        } else {
            List<Ingredient> ingredientsWithEmptyItem = ingredients.stream()
                                                                   .filter(ingredient -> StringUtils.isBlank(ingredient.getItem()))
                                                                   .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(ingredientsWithEmptyItem)) {
                validationErrors.add("Some ingredients has empty item.");
            }
        }

        if (!CollectionUtils.isEmpty(validationErrors)) {
            throw new RecipeValidationException(String.join(", ", validationErrors));
        }
    }
}
