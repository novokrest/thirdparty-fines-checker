package ru.fines.tools.fineschecker.config.gibdd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

public class GibddSettings {

    private final String url;
    private final GibddElement grzNumber;
    private final GibddElement grzRegion;
    private final GibddElement sts;
    private final GibddElementId searchButton;
    private final Duration searchWaitTimeout;
    private final String successSearchText;

    @JsonCreator
    public GibddSettings(@JsonProperty("url") String url,
                         @JsonProperty("grz-number") GibddElement grzNumber,
                         @JsonProperty("grz-region") GibddElement grzRegion,
                         @JsonProperty("sts") GibddElement sts,
                         @JsonProperty("search-button") GibddElementId searchButton,
                         @JsonProperty("search-wait-timeout") String searchWaitTimeout,
                         @JsonProperty("success-search-text") String successSearchText) {
        this.url = url;
        this.grzNumber = grzNumber;
        this.grzRegion = grzRegion;
        this.sts = sts;
        this.searchButton = searchButton;
        this.searchWaitTimeout = Duration.parse(searchWaitTimeout);
        this.successSearchText = successSearchText;
    }

    public String getUrl() {
        return url;
    }

    public GibddElement getGrzNumber() {
        return grzNumber;
    }

    public GibddElement getGrzRegion() {
        return grzRegion;
    }

    public GibddElement getSts() {
        return sts;
    }

    public GibddElementId getSearchButton() {
        return searchButton;
    }

    public Duration getSearchWaitTimeout() {
        return searchWaitTimeout;
    }

    public String getSuccessSearchText() {
        return successSearchText;
    }
}
