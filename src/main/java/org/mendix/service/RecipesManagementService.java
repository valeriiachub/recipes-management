package org.mendix.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.mendix.dto.RecipeMlRequest;
import org.mendix.dto.RecipeSearchCriteria;
import org.mendix.dto.internals.Head;
import org.mendix.dto.internals.Ingredient;
import org.mendix.exception.RecipeValidationException;
import org.mendix.repository.CategoryRepository;
import org.mendix.repository.RecipesRepository;
import org.mendix.repository.model.CategoryEntity;
import org.mendix.repository.model.IngredientEntity;
import org.mendix.repository.model.RecipeEntity;
import org.mendix.repository.model.StepEntity;
import org.mendix.service.validation.RecipeValidator;
import org.mendix.utils.EntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import static org.mendix.utils.EntityMapper.mapToRecipeDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipesManagementService {

    private final RecipesRepository recipeRepository;

    private final CategoryRepository categoryRepository;

    private final RecipeValidator recipeValidator;

    public List<RecipeMlRequest> getAll() {
        Iterable<RecipeEntity> allRecipesFromDb = recipeRepository.findAll();
        if (allRecipesFromDb.iterator().hasNext()) {
            return mapToRecipeDto(allRecipesFromDb);
        }
        return Collections.emptyList();
    }

    public RecipeMlRequest getRecipeById(Long id) {
        Optional<RecipeEntity> recipe = recipeRepository.findById(id);
        return recipe.map(EntityMapper::mapToDto)
                     .orElse(null);
    }

    public List<RecipeMlRequest> getRecipesByCategory(String category) {
        List<RecipeEntity> entities = recipeRepository.findByCategoryName(category);
        return entities.stream()
                       .map(EntityMapper::mapToDto)
                       .collect(Collectors.toList());
    }

    public List<RecipeMlRequest> searchRecipes(RecipeSearchCriteria criteria) {
        String searchString = criteria.getSearchString();
        List<RecipeEntity> entitiesBySearchCriteria =
                recipeRepository.findByTitleContainingIgnoreCaseOrCategories_CategoryNameContainingIgnoreCase(searchString, searchString);
        return entitiesBySearchCriteria.stream()
                                       .map(EntityMapper::mapToDto)
                                       .collect(Collectors.toList());
    }

    @Transactional
    public RecipeMlRequest createRecipe(RecipeMlRequest request) {
        recipeValidator.validate(request);
        RecipeEntity recipeEntity = prepareRecipeEntity(request);
        recipeRepository.save(recipeEntity);

        return request;
    }

    @Transactional
    public void createRecipes(List<RecipeMlRequest> requests) {
        List<RecipeEntity> recipesToSave = new ArrayList<>();
        for (RecipeMlRequest request : requests) {
            try {
                recipeValidator.validate(request);
            } catch (RecipeValidationException e) {
                log.info("Invalid recipe. Reason {}", e.getMessage());
                continue;
            }
            RecipeEntity recipeEntity = prepareRecipeEntity(request);
            recipesToSave.add(recipeEntity);
        }

        if (!CollectionUtils.isEmpty(recipesToSave)) {
            ListUtils.partition(recipesToSave, 10).forEach(recipeRepository::saveAll);
        }
    }

    private RecipeEntity prepareRecipeEntity(RecipeMlRequest request) {
        RecipeEntity recipeEntity = new RecipeEntity();
        Head head = request.getRecipe().getHead();

        recipeEntity.setTitle(head.getTitle());
        recipeEntity.setYield(head.getYield());
        recipeEntity.setCategories(getCategoryEntities(request));
        recipeEntity.setIngredientEntities(getIngredientEntities(request, recipeEntity));
        recipeEntity.setStepEntity(getStepEntity(request, recipeEntity));
        return recipeEntity;
    }

    private Set<IngredientEntity> getIngredientEntities(RecipeMlRequest request, RecipeEntity recipeEntity) {
        Set<IngredientEntity> ingredientEntities = new HashSet<>();
        for (Ingredient ingredient : request.getRecipe().getIngredients()) {
            IngredientEntity ingredientEntity = convertToIngredientEntity(ingredient);
            ingredientEntity.setRecipe(recipeEntity);
            ingredientEntities.add(ingredientEntity);
        }
        return ingredientEntities;
    }

    private StepEntity getStepEntity(RecipeMlRequest request, RecipeEntity recipeEntity) {
        StepEntity stepEntity = new StepEntity();
        stepEntity.setRecipe(recipeEntity);
        stepEntity.setStep(request.getRecipe().getDirections().getStep());

        return stepEntity;
    }

    private Set<CategoryEntity> getCategoryEntities(RecipeMlRequest request) {
        Set<CategoryEntity> categoryEntities = new HashSet<>();
        for (String categoryName : request.getRecipe().getHead().getCategories()) {
            Optional<CategoryEntity> byCategoryName = categoryRepository.findByCategoryName(categoryName);
            if (byCategoryName.isPresent()) {
                categoryEntities.add(byCategoryName.get());
            } else {
                CategoryEntity categoryEntity = categoryRepository.save(new CategoryEntity(categoryName));
                categoryEntities.add(categoryEntity);
            }
        }
        return categoryEntities;
    }

    private IngredientEntity convertToIngredientEntity(Ingredient ingredient) {
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setDescription(ingredient.getItem());
        ingredientEntity.setQuantity(ingredient.getAmount().getQuantity());
        ingredientEntity.setUnit(ingredient.getAmount().getUnit());

        return ingredientEntity;
    }
}
