package ru.fines.tools.fineschecker.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fines.tools.fineschecker.config.shtrafyonline.ShtrafyOnlineSettings;
import ru.fines.tools.fineschecker.core.Json;
import ru.fines.tools.fineschecker.core.ServiceLivenessChecker;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

public class ShtrafyOnlineChecker implements ServiceLivenessChecker {

    private static final Logger log = LoggerFactory.getLogger(ShtrafyOnlineChecker.class);

    private final ShtrafyOnlineSettings settings;
    private final OkHttpClient httpClient;

    public ShtrafyOnlineChecker(ShtrafyOnlineSettings settings) {
        this.settings = settings;
        this.httpClient = new OkHttpClient.Builder()
            .readTimeout(settings.getRequestTimeout())
            .build();
    }

    @Override
    public String getServiceName() {
        return "Штрафы.Онлайн";
    }

    @Override
    public boolean isServiceAlive() {
        try {
            return obtainAuthorizedToken()
                .map(this::checkServiceAlive)
                .orElse(Boolean.FALSE);
        } catch (Throwable e) {
            log.error("Failed to check liveness", e);
            return false;
        }
    }

    private Optional<String> obtainAuthorizedToken() {
        return requestAuthorizedToken()
            .map(response -> response.findPath("data").findValue("access_token"))
            .map(JsonNode::asText);
    }

    private Optional<JsonNode> requestAuthorizedToken() {
        return request(settings.getAuthorizeUrl(), emptyMap());
    }

    private boolean checkServiceAlive(String accessToken) {
        return requestApi(
            ImmutableMap.<String, String>builder()
                .put("access_token", accessToken)
                .put("auto_number", settings.getGrzNumber())
                .put("region", settings.getGrzRegion())
                .put("registration_full", settings.getSts())
                .put("method", "reqs/new/auto")
                .put("v", "2")
                .build())
            .map(response -> response.findValue("reqs_id"))
            .map(JsonNode::asText)
            .flatMap(requestId -> requestApi(
                ImmutableMap.<String, String>builder()
                    .put("access_token", accessToken)
                    .put("reqs_id", requestId)
                    .put("method", "reqs/fines/check")
                    .put("v", "2")
                    .build())
                .map(response -> response.findValues("fines")))
            .map(fineNodes -> {
                log.info("Found fines: finesCount={}", fineNodes.size());
                return !fineNodes.isEmpty();
            })
            .orElse(Boolean.FALSE);
    }

    private Optional<JsonNode> requestApi(Map<String, String> params) {
        return request(settings.getApiUrl(), params);
    }

    private Optional<JsonNode> request(String url, Map<String, String> params) {
        try {
            log.debug("Try to request: url={}, params={}", url, params);
            HttpUrl httpUrl = withQueryParameters(HttpUrl.get(url).newBuilder(), params);
            String response = httpClient.newCall(new Request.Builder()
                .get()
                .url(httpUrl)
                .build())
                .execute()
                .body()
                .string();
            log.debug("Response was received: response={}", response);
            return Optional.of(Json.mapper().readTree(response));
        } catch (IOException e) {
            log.error("Failed to read response", e);
            return Optional.empty();
        }
    }

    private static HttpUrl withQueryParameters(HttpUrl.Builder urlBuilder, Map<String, String> params) {
        params.forEach(urlBuilder::addQueryParameter);
        return urlBuilder.build();
    }

}
