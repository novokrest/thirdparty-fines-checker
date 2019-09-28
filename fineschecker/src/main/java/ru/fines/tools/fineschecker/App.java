package ru.fines.tools.fineschecker;

import ru.fines.tools.fineschecker.config.Config;

import java.util.Optional;

import static ru.fines.tools.fineschecker.config.Config.readConfig;

public class App {

    public static void main(String[] args) {
        Config config = readConfig(getConfigPath());
        new AppLauncher(config).run();
    }

    private static String getConfigPath() {
        return Optional.ofNullable(System.getProperty("config.path"))
            .orElse("config/fines-checker.yml");
    }

}
