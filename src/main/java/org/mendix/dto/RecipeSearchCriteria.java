package org.mendix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipeSearchCriteria {

    private String searchString;
}
