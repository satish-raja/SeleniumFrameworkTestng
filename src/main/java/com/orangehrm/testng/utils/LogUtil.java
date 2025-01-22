
/**
 * Class Name: LogUtil
 * 
 * Description:
 * This utility class provides a flexible mechanism for logging messages at various levels.
 * It allows configuration of the logger's log level and logging of messages at different levels
 * such as INFO, DEBUG, ERROR, WARN, and TRACE.
 * 
 * Key Features:
 * - Supports dynamic logger configuration based on the desired log level.
 * - Provides methods for logging messages at different levels.
 * - Logger configuration and logging behavior are separated for better maintainability.
 * 
 * Dependencies:
 * - Apache Log4j for logging functionality.
 * - SLF4J can be used as an alternative logging framework if desired.
 */

package com.orangehrm.testng.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class LogUtil {

    private static final Logger logger = LogManager.getLogger(LogUtil.class);

    // Prevent instantiation
    private LogUtil() {}

    /**
     * Initializes the logger configuration based on the specified log level.
     *
     * @param logLevel The desired log level (INFO, DEBUG, ERROR, WARN, TRACE).
     */
    public static void initLogger(LogLevel logLevel) {
        Configurator.setLevel(LogUtil.class.getName(), logLevel.toLog4jLevel());
    }

    /**
     * Logs a message at the INFO level.
     *
     * @param message The message to log.
     */
    public static void logInfo(String message) {
        logger.info(message);
    }

    /**
     * Logs a message at the specified level.
     *
     * @param message The message to log.
     * @param logLevel The level at which to log the message.
     */
    public static void log(String message, LogLevel logLevel) {
        switch (logLevel) {
            case INFO:
                logger.info(message);
                break;
            case DEBUG:
                logger.debug(message);
                break;
            case ERROR:
                logger.error(message);
                break;
            case WARN:
                logger.warn(message);
                break;
            case TRACE:
                logger.trace(message);
                break;
            default:
                logger.info(message); // Default level is INFO
                break;
        }
    }

    /**
     * Retrieves the logger instance.
     *
     * @return The logger instance.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Enum for log levels, mapping to Log4j levels.
     */
    public enum LogLevel {
        INFO(org.apache.logging.log4j.Level.INFO),
        DEBUG(org.apache.logging.log4j.Level.DEBUG),
        ERROR(org.apache.logging.log4j.Level.ERROR),
        WARN(org.apache.logging.log4j.Level.WARN),
        TRACE(org.apache.logging.log4j.Level.TRACE);

        private final org.apache.logging.log4j.Level log4jLevel;

        LogLevel(org.apache.logging.log4j.Level log4jLevel) {
            this.log4jLevel = log4jLevel;
        }

        public org.apache.logging.log4j.Level toLog4jLevel() {
            return this.log4jLevel;
        }
    }
}
