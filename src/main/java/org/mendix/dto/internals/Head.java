package org.mendix.dto.internals;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class Head {

    private String title;

    @JacksonXmlElementWrapper(localName = "categories")
    @JacksonXmlProperty(localName = "cat")
    private List<String> categories;

    private int yield;

}
