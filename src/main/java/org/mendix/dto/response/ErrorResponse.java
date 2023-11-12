package org.mendix.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "errorresponse")
public class ErrorResponse {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "errors")
    private List<String> errors;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JacksonXmlProperty(localName = "timestamp")
    private Date timestamp;

    @JacksonXmlProperty(localName = "status")
    private String status;

    public ErrorResponse() {
        this.timestamp = new Date();
    }
}
