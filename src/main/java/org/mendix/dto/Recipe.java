package org.mendix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import org.mendix.dto.internals.Head;
import org.mendix.dto.internals.Ingredient;

import java.util.List;

import javax.validation.constraints.NotNull;

@Data
@JacksonXmlRootElement(localName = "recipeml")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {

    @NotNull
    @JacksonXmlProperty(localName = "head")
    private Head head;

    @NotNull
    @JacksonXmlElementWrapper(localName = "ingredients")
    @JacksonXmlProperty(localName = "ing")
    private List<Ingredient> ingredients;

    @NotNull
    @JacksonXmlProperty(localName = "directions")
    private Directions directions;
}
