package org.mendix.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mendix.dto.internals.Category;

import java.util.List;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "categories")
public class CategoryListWrapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "category")
    private List<Category> categories;

    public CategoryListWrapper(List<Category> categories) {
        this.categories = categories;
    }
}
