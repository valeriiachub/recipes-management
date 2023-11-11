package org.mendix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JacksonXmlRootElement(localName = "recipeml")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeMlRequest {

    @NotNull
    @JacksonXmlProperty(localName = "recipe")
    private Recipe recipe;
}
