package ru.fines.tools.fineschecker.config.shtrafyonline;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

public class ShtrafyOnlineSettings {

    private final String authorizeUrl;
    private final String apiUrl;
    private final Duration requestTimeout;
    private final String grzNumber;
    private final String grzRegion;
    private final String sts;

    @JsonCreator
    public ShtrafyOnlineSettings(@JsonProperty("authorize-url") String authorizeUrl,
                                 @JsonProperty("api-url") String apiUrl,
                                 @JsonProperty("request-timeout") String requestTimeout,
                                 @JsonProperty("grz-number") String grzNumber,
                                 @JsonProperty("grz-region") String grzRegion,
                                 @JsonProperty("sts") String sts) {
        this.authorizeUrl = authorizeUrl;
        this.apiUrl = apiUrl;
        this.requestTimeout = Duration.parse(requestTimeout);
        this.grzNumber = grzNumber;
        this.grzRegion = grzRegion;
        this.sts = sts;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public Duration getRequestTimeout() {
        return requestTimeout;
    }

    public String getGrzNumber() {
        return grzNumber;
    }

    public String getGrzRegion() {
        return grzRegion;
    }

    public String getSts() {
        return sts;
    }

}
