package org.mendix.repository.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "step")
@EqualsAndHashCode(exclude = "recipe")
public class StepEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;

    @OneToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String step;
}
