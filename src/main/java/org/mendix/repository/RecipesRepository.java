package org.mendix.repository;

import org.mendix.repository.model.RecipeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipesRepository extends PagingAndSortingRepository<RecipeEntity, Long> {

    Optional<RecipeEntity> findByTitle(String title);

    @Query("SELECT DISTINCT r FROM RecipeEntity r JOIN r.categories c WHERE c.categoryName = :categoryName")
    List<RecipeEntity> findByCategoryName(@Param("categoryName") String categoryName);

    boolean existsByTitle(String title);

    List<RecipeEntity> findByTitleContainingIgnoreCaseOrCategories_CategoryNameContainingIgnoreCase(String searchString, String searchString1);
}
