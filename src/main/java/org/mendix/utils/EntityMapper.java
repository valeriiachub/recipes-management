package org.mendix.utils;

import lombok.experimental.UtilityClass;
import org.mendix.dto.Recipe;
import org.mendix.dto.RecipeMlRequest;
import org.mendix.dto.internals.Amount;
import org.mendix.dto.internals.Category;
import org.mendix.dto.internals.Head;
import org.mendix.dto.internals.Ingredient;
import org.mendix.repository.model.CategoryEntity;
import org.mendix.repository.model.IngredientEntity;
import org.mendix.repository.model.RecipeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class EntityMapper {

    public static List<Category> mapToCategoryDto(Iterable<CategoryEntity> allCategoriesFromDb) {
        List<Category> categoryList = new ArrayList<>();
        for (CategoryEntity categoryEntity : allCategoriesFromDb) {
            Category category = mapToDto(categoryEntity);
            categoryList.add(category);
        }

        return categoryList;
    }

    public static List<String> mapToCategoryNames(Set<CategoryEntity> categories) {
        return categories.stream()
                         .map(CategoryEntity::getCategoryName)
                         .collect(Collectors.toList());
    }

    public static List<RecipeMlRequest> mapToRecipeDto(Iterable<RecipeEntity> allRecipesFromDb) {
        List<RecipeMlRequest> recipeList = new ArrayList<>();
        for (RecipeEntity recipeEntity : allRecipesFromDb) {
            RecipeMlRequest recipe = mapToDto(recipeEntity);
            recipeList.add(recipe);
        }

        return recipeList;
    }

    public static RecipeMlRequest mapToDto(RecipeEntity recipeEntity) {
        Recipe recipe = new Recipe();

        Head head = new Head();
        head.setTitle(recipeEntity.getTitle());
        head.setYield(recipeEntity.getYield());
        head.setCategories(EntityMapper.mapToCategoryNames(recipeEntity.getCategories()));

        recipe.setHead(head);
        recipe.setIngredients(mapToIngredients(recipeEntity.getIngredientEntities()));

        RecipeMlRequest recipeMlRequest = new RecipeMlRequest();
        recipeMlRequest.setRecipe(recipe);

        return recipeMlRequest;
    }

    public static List<Ingredient> mapToIngredients(Set<IngredientEntity> ingredientEntityEntities) {
        return ingredientEntityEntities.stream()
                                       .map(entity -> {
                                           Ingredient ingredient = new Ingredient();

                                           Amount amount = new Amount();
                                           amount.setQuantity(entity.getQuantity());
                                           amount.setUnit(entity.getUnit());
                                           ingredient.setAmount(amount);
                                           ingredient.setItem(entity.getDescription());

                                           return ingredient;
                                       })
                                       .collect(Collectors.toList());
    }

    private Category mapToDto(CategoryEntity categoryEntity) {
        Category category = new Category();
        category.setCategoryId(categoryEntity.getCategoryId());
        category.setCategoryName(categoryEntity.getCategoryName());

        return category;
    }
}
