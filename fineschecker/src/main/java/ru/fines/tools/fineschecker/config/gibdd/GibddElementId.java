package ru.fines.tools.fineschecker.config.gibdd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GibddElementId {

    private final String id;
    private final String css;

    @JsonCreator
    public GibddElementId(@JsonProperty("id") String id, @JsonProperty("css-selector") String css) {
        this.id = id;
        this.css = css;
    }

    public String getId() {
        return id;
    }

    public String getCss() {
        return css;
    }

    @Override
    public String toString() {
        return "GibddElementId{" +
            "id='" + id + '\'' +
            ", css='" + css + '\'' +
            '}';
    }

}
