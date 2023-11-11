package org.mendix.repository.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mendix.dto.internals.UnitOfMeasure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ingredients")
@EqualsAndHashCode(exclude = "recipe")
public class IngredientEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    @Column
    private String quantity;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private UnitOfMeasure unit;

    @Column
    private String description;
}
