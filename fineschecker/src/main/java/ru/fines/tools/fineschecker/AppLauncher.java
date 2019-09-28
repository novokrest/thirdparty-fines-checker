package ru.fines.tools.fineschecker;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.fines.tools.fineschecker.config.Config;
import ru.fines.tools.fineschecker.core.ScheduleRunner;
import ru.fines.tools.fineschecker.module.AppModule;
import ru.fines.tools.fineschecker.services.LivenessCheckRunner;

public class AppLauncher {

    private final Config config;
    private final Injector injector;
    private boolean isRunning;

    public AppLauncher(Config config) {
        this.config = config;
        this.injector = Guice.createInjector(new AppModule(config));
    }

    public void run() {
        if (isRunning) {
            return;
        }

        LivenessCheckRunner livenessCheckRunner = injector.getInstance(LivenessCheckRunner.class);
        ScheduleRunner.run(livenessCheckRunner::run, config.getCheckPeriod());
        isRunning = true;
    }

}
