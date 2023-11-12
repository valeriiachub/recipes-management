package org.mendix.dto.internals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
import org.mendix.exception.EnumSerializeException;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public enum UnitOfMeasure {

    POUND(List.of("pound")),
    TABLESPOONS(List.of("tablespoons", "tablespoon")),
    TEASPOON(List.of("teaspoon", "teaspoons")),
    CAN(List.of("can")),
    CUP(List.of("cup", "cups")),
    PACKAGE(List.of("package")),
    PIECE(List.of(""));

    private final List<String> values;

    @JsonValue
    public List<String> getValue() {
        return values;
    }

    @JsonCreator
    public static UnitOfMeasure forValue(String value) {
        return Arrays.stream(values())
                     .filter(dimensionUnit -> dimensionUnit.getValue().contains(value))
                     .findAny()
                     .orElseThrow(() -> new EnumSerializeException(value));
    }
}
