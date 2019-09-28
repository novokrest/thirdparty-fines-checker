package ru.fines.tools.fineschecker.config.gibdd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GibddElement extends GibddElementId {

    private final String value;

    @JsonCreator
    public GibddElement(@JsonProperty("id") String id,
                        @JsonProperty("css-selector") String cssSelector,
                        @JsonProperty("value") String value) {
        super(id, cssSelector);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
