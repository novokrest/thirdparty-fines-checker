package ru.fines.tools.fineschecker.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import ru.fines.tools.fineschecker.config.gibdd.GibddSettings;
import ru.fines.tools.fineschecker.config.shtrafyonline.ShtrafyOnlineSettings;
import ru.fines.tools.fineschecker.config.telegram.TelegramSettings;
import ru.fines.tools.fineschecker.core.Json;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;

public class Config {

    private final Duration checkPeriod;
    private final GibddSettings gibdd;
    private final ShtrafyOnlineSettings shtrafyOnline;
    private final TelegramSettings telegramSettings;
    private final boolean shouldSendSuccessMessage;
    private final String availabilityMessageSuffix;
    private final String unavailabilityMessageSuffix;

    @JsonCreator
    public Config(@JsonProperty("check-period") String checkPeriod,
                  @JsonProperty("gibdd") GibddSettings gibdd,
                  @JsonProperty("shtrafy-online") ShtrafyOnlineSettings shtrafyOnline,
                  @JsonProperty("telegram") TelegramSettings telegramSettings,
                  @JsonProperty("should-send-availability-message") boolean shouldSendSuccessMessage,
                  @JsonProperty("availability-message-suffix") String availabilityMessageSuffix,
                  @JsonProperty("unavailability-message-suffix") String unavailabilityMessageSuffix) {
        this.checkPeriod = Duration.parse(checkPeriod);
        this.gibdd = gibdd;
        this.shtrafyOnline = shtrafyOnline;
        this.telegramSettings = telegramSettings;
        this.shouldSendSuccessMessage = shouldSendSuccessMessage;
        this.availabilityMessageSuffix = availabilityMessageSuffix;
        this.unavailabilityMessageSuffix = unavailabilityMessageSuffix;
    }

    public static Config readConfig(String configPath) {
        try {
            YAMLParser parser = new YAMLFactory().createParser(Config.class.getClassLoader().getResourceAsStream(configPath));
            return Json.mapper().readValue(parser, Config.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Duration getCheckPeriod() {
        return checkPeriod;
    }

    public GibddSettings getGibdd() {
        return gibdd;
    }

    public ShtrafyOnlineSettings getShtrafyOnline() {
        return shtrafyOnline;
    }

    public TelegramSettings getTelegramSettings() {
        return telegramSettings;
    }

    public boolean isShouldSendSuccessMessage() {
        return shouldSendSuccessMessage;
    }

    public String getAvailabilityMessageSuffix() {
        return availabilityMessageSuffix;
    }

    public String getUnavailabilityMessageSuffix() {
        return unavailabilityMessageSuffix;
    }

}
