package org.mendix.repository.model;

import lombok.Data;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "recipes")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long recipeId;

    @Column(unique = true)
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<CategoryEntity> categories;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<IngredientEntity> ingredientEntities;

    @OneToOne(mappedBy = "recipe", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private StepEntity stepEntity;

    @Column
    private int yield;
}
