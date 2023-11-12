package org.mendix.dto.internals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {

    @NotNull
    @JacksonXmlProperty(localName = "amt")
    private Amount amount;

    @NotBlank
    private String item;
}
