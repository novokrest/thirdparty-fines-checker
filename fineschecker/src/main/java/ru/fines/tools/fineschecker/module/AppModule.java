package ru.fines.tools.fineschecker.module;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.pengrad.telegrambot.TelegramBot;
import ru.fines.tools.fineschecker.config.Config;
import ru.fines.tools.fineschecker.config.gibdd.GibddSettings;
import ru.fines.tools.fineschecker.config.shtrafyonline.ShtrafyOnlineSettings;
import ru.fines.tools.fineschecker.config.telegram.TelegramSettings;
import ru.fines.tools.fineschecker.core.ServiceLivenessChecker;
import ru.fines.tools.fineschecker.services.GibddChecker;
import ru.fines.tools.fineschecker.services.LivenessCheckRunner;
import ru.fines.tools.fineschecker.services.ShtrafyOnlineChecker;
import ru.fines.tools.fineschecker.services.TelegramNotifier;

import javax.inject.Singleton;
import java.util.List;

public class AppModule extends AbstractModule {

    private final Config config;

    public AppModule(Config config) {
        this.config = config;
    }

    @Provides
    GibddSettings gibddSettings() {
        return config.getGibdd();
    }

    @Provides
    ShtrafyOnlineSettings shtrafyOnlineSettings() {
        return config.getShtrafyOnline();
    }

    @Provides
    TelegramSettings telegramSettings() {
        return config.getTelegramSettings();
    }

    @Provides
    @Singleton
    LivenessCheckRunner livenessCheckRunner(List<ServiceLivenessChecker> checkers,
                                            TelegramNotifier telegramNotifier) {
        return new LivenessCheckRunner(
            checkers,
            telegramNotifier,
            config.isShouldSendSuccessMessage(),
            config.getAvailabilityMessageSuffix(),
            config.getUnavailabilityMessageSuffix()
        );
    }

    @Provides
    @Singleton
    List<ServiceLivenessChecker> checkers(GibddChecker gibddChecker,
                                          ShtrafyOnlineChecker shtrafyOnlineChecker) {
        return ImmutableList.<ServiceLivenessChecker>builder()
            .add(gibddChecker)
//            .add(shtrafyOnlineChecker)
            .build();
    }

    @Provides
    @Singleton
    GibddChecker gibddChecker(GibddSettings settings) {
        return new GibddChecker(settings);
    }

    @Provides
    @Singleton
    ShtrafyOnlineChecker shtrafyOnline(ShtrafyOnlineSettings settings) {
        return new ShtrafyOnlineChecker(settings);
    }

    @Provides
    @Singleton
    TelegramNotifier telegramNotifier(TelegramBot telegramBot,
                                      TelegramSettings telegramSettings) {
        return new TelegramNotifier(telegramBot, telegramSettings.getChatId());
    }

    @Provides
    @Singleton
    TelegramBot telegramBot(TelegramSettings telegramSettings) {
        return new TelegramBot(telegramSettings.getBotToken());
    }

}
