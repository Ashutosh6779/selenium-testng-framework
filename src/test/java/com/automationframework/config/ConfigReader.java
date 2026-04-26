package com.automationframework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Centralised configuration management.
 * Loads framework settings from config.properties so no hardcoded values
 * exist in test code. Supports environment-specific overrides via
 * system properties (useful for CI/CD pipelines).
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties from: " + CONFIG_PATH, e);
        }
    }

    /**
     * Returns a config value. System properties (e.g., -Dbrowser=firefox)
     * take precedence over config.properties — this enables CI overrides.
     */
    public static String get(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.isEmpty()) {
            return systemProp;
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Config key not found: '" + key + "'. Add it to config.properties.");
        }
        return value.trim();
    }

    public static String getBrowser()       { return get("browser"); }
    public static String getBaseUrl()       { return get("base.url"); }
    public static int getImplicitWait()     { return Integer.parseInt(get("implicit.wait")); }
    public static int getExplicitWait()     { return Integer.parseInt(get("explicit.wait")); }
    public static boolean isHeadless()      { return Boolean.parseBoolean(get("headless")); }
    public static String getEnvironment()   { return get("environment"); }
}
