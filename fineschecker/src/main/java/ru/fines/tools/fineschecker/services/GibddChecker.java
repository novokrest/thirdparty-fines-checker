package ru.fines.tools.fineschecker.services;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fines.tools.fineschecker.config.gibdd.GibddElement;
import ru.fines.tools.fineschecker.config.gibdd.GibddElementId;
import ru.fines.tools.fineschecker.config.gibdd.GibddSettings;
import ru.fines.tools.fineschecker.core.ServiceLivenessChecker;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;

public class GibddChecker implements ServiceLivenessChecker {

    private static final Logger log = LoggerFactory.getLogger(GibddChecker.class);

    private final GibddSettings settings;

    public GibddChecker(GibddSettings settings) {
        this.settings = settings;
    }

    @Override
    public String getServiceName() {
        return "ГИБДД.рф";
    }

    @Override
    public boolean isServiceAlive() {
        WebDriver driver = null;
        try {
            log.debug("Before web driver");
//            driver = new ChromeDriver(getChromeOptions());
            driver = new FirefoxDriver();
            executeScript(driver);
            log.debug("After web driver");
            return checkIsAlive(driver);
        } catch (Throwable e) {
            log.error("Failed to check '" + getServiceName() + "' service", e);
            return false;
        } finally {
            if (driver != null) {
                try {
                    driver.close();
                } catch (Throwable e) {
                    log.error("Failed to close web driver", e);
                }
            }
        }
    }

    private void executeScript(WebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript(IOUtils.toString(getClass().getResource("/js/hack.js"), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * https://stackoverflow.com/a/56635123
     * https://github.com/RobCherry/docker-chromedriver/issues/15#issuecomment-459493825
     */
    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();

//        options.addArguments("--headless");
        options.addArguments("--whitelisted-ips");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-setuid-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-dev-shm-usage");

        options.setExperimentalOption("excludeSwitches", singletonList("enable-automation"));

        return options;
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        options.setLogLevel(FirefoxDriverLogLevel.TRACE);
        return options;
    }

    private boolean checkIsAlive(WebDriver driver) {
        log.info("Check is alive...");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        startSearch(driver);
        waitForSearchResult(driver);
        boolean isAlive = determineLivenessBySearch(driver);

        driver.manage().deleteAllCookies();
        return isAlive;
    }

    private void startSearch(WebDriver driver) {
        log.info("Starting search...");

        driver.get(settings.getUrl());

//        sendKeys(driver, settings.getGrzNumber());
//        sendKeys(driver, settings.getGrzRegion());
//        sendKeys(driver, settings.getSts());

        sleepMs(500);
        driver.findElement(searchBy(settings.getSearchButton())).click();
    }

    private static void sendKeys(WebDriver driver, GibddElement element) {
        WebElement webElement = driver.findElement(searchBy(element));
        webElement.click();
        webElement.sendKeys(element.getValue());
    }

    private static By searchBy(GibddElementId id) {
        if (id.getId() != null) {
            return By.id(id.getId());
        }
        if (id.getCss() != null) {
            return By.cssSelector(id.getCss());
        }
        throw new IllegalArgumentException("Invalid element ID: id=" + id);
    }

    private void waitForSearchResult(WebDriver driver) {
        log.info("Waiting for search result...");
        String searchTerminatedContent = String.format("ГРЗ: %s%s, СТС: %s",
            settings.getGrzNumber().getValue(), settings.getGrzRegion().getValue(), settings.getSts().getValue());
        long leftAttempts = settings.getSearchWaitTimeout().toMillis() / 1000 * 2;
        while (leftAttempts-- > 0) {
            log.info("Attempt: {}...", leftAttempts);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("Error occurred during waiting for search", e);
            }
            if (driver.getPageSource().contains(searchTerminatedContent)) {
                break;
            }
        }
    }

    private boolean determineLivenessBySearch(WebDriver driver) {
        return driver.getPageSource().contains(settings.getSuccessSearchText());
    }

    private static void sleepMs(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
