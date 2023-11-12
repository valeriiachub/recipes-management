package org.mendix.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mendix.dto.RecipeMlRequest;

import java.util.List;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "recipes")
public class RecipeListWrapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "recipeml")
    private List<RecipeMlRequest> recipes;

    public RecipeListWrapper(List<RecipeMlRequest> recipes) {
        this.recipes = recipes;
    }
}
