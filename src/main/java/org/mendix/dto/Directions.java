package org.mendix.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Directions {

    @JacksonXmlProperty(localName = "step")
    private String step;
}
