/**
 * Class Name: ConfigManager
 *
 * Description:
 * This class provides a centralized mechanism for managing application configuration settings.
 * It supports loading environment-specific configuration files and retrieving properties by key.
 * The implementation is flexible and extensible, allowing different configuration formats using the `ConfigurationProvider` interface.
 *
 * Key Features:
 * - Dynamically loads environment-specific configuration files.
 * - Retrieves property values with optional default fallbacks.
 * - Implements the `ConfigurationProvider` interface for extensibility.
 *
 * Dependencies:
 * - `PropertiesConfigurationProvider` for loading `.properties` files.
 * - Apache Log4j for logging configuration load operations.
 *
 * Usage:
 * - Use `ConfigManager.getProperty(String key)` to retrieve a property.
 * - Extend `ConfigurationProvider` for custom configuration formats (e.g., JSON, YAML).
 */

package com.orangehrm.testng.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigManager {

    private static final ConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
    private static final Properties properties = new Properties();
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);

    static {
        String environment = resolveEnvironment();
        properties.putAll(configurationProvider.loadConfiguration(environment));
    }

    /**
     * Retrieves a configuration property value by key with a default value.
     *
     * @param key          The property key.
     * @param defaultValue The default value if the key is not found.
     * @return The property value.
     */
    public static String getProperty(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }

    /**
     * Retrieves a configuration property value by key.
     *
     * @param key The property key.
     * @return The property value, or null if not found.
     */
    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * Resolves the application environment.
     *
     * @return The resolved environment name, defaults to "dev".
     */
    private static String resolveEnvironment() {
        String environment = System.getProperty("env", "dev").toLowerCase();
        logger.info("Resolved environment: {}", environment);
        return environment;
    }
}

/**
 * Interface: ConfigurationProvider
 *
 * Description:
 * This interface defines the contract for loading configuration settings.
 * Implementations can support different formats, such as properties, JSON, or YAML.
 */
interface ConfigurationProvider {
    Properties loadConfiguration(String environment);
}

/**
 * Class Name: PropertiesConfigurationProvider
 *
 * Description:
 * Implementation of `ConfigurationProvider` for loading `.properties` files.
 * Loads environment-specific configuration from the `config/` folder.
 *
 * Key Features:
 * - Reads `.properties` files based on the environment.
 * - Logs operations and errors during file load.
 */
class PropertiesConfigurationProvider implements ConfigurationProvider {
    private static final String CONFIG_FOLDER = "config/";
    private static final Logger logger = LogManager.getLogger(PropertiesConfigurationProvider.class);

    @Override
    public Properties loadConfiguration(String environment) {
        Properties properties = new Properties();
        String filePath = CONFIG_FOLDER + "config-" + environment + ".properties";

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (input != null) {
                properties.load(input);
                logger.info("Loaded configuration for environment: {}", environment);
            } else {
                logger.warn("Configuration file not found: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Error loading configuration file: {}", filePath, e);
            throw new RuntimeException("Failed to load configuration file.", e);
        }

        return properties;
    }
}
