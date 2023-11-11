package org.mendix.api.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mendix.dto.Recipe;
import org.mendix.dto.RecipeMlRequest;
import org.mendix.dto.internals.Head;
import org.mendix.dto.internals.Ingredient;
import org.mendix.exception.RecipeValidationException;
import org.mendix.repository.RecipesRepository;
import org.mendix.service.validation.RecipeValidator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeValidatorTest {

    @Mock
    private RecipesRepository recipesRepository;
    @InjectMocks
    private RecipeValidator recipeValidator;

    private RecipeMlRequest recipeMlRequest;
    private Recipe recipe;
    private Head head;

    @BeforeEach
    void setUp() {
        prepareRecipeMlRequest();
    }

    private void prepareRecipeMlRequest() {
        recipeMlRequest = new RecipeMlRequest();
        recipe = new Recipe();
        head = new Head();
        recipe.setHead(head);
        recipeMlRequest.setRecipe(recipe);
    }

    @Test
    void whenRecipeNameExists_thenThrowValidationException() {
        String title = "Existing Recipe";
        head.setTitle(title);
        when(recipesRepository.existsByTitle(title)).thenReturn(true);

        RecipeValidationException exception = assertThrows(
                RecipeValidationException.class,
                () -> recipeValidator.validate(recipeMlRequest)
        );
        assertTrue(exception.getMessage().contains("Recipe with this title already exists"));
    }

    @Test
    void whenNoCategoriesProvided_thenThrowValidationException() {
        head.setTitle("New Recipe");
        head.setCategories(Collections.emptyList());
        recipe.setIngredients(List.of(new Ingredient()));

        RecipeValidationException exception = assertThrows(
                RecipeValidationException.class,
                () -> recipeValidator.validate(recipeMlRequest)
        );
        assertTrue(exception.getMessage().contains("Recipe should contain at least one category"));
    }

    @Test
    void whenNoIngredientsProvided_thenThrowValidationException() {
        head.setTitle("New Recipe");
        head.setCategories(List.of("Category 1"));
        recipe.setIngredients(Collections.emptyList());

        RecipeValidationException exception = assertThrows(
                RecipeValidationException.class,
                () -> recipeValidator.validate(recipeMlRequest)
        );
        assertTrue(exception.getMessage().contains("Recipe should contain at least one ingredient"));
    }
}
