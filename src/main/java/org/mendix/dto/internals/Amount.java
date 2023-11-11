package org.mendix.dto.internals;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Amount {

    @NotNull
    @JacksonXmlProperty(localName = "qty")
    private String quantity;

    @JacksonXmlProperty(localName = "unit")
    private UnitOfMeasure unit;
}
