package org.apertereports.dao.utils;

import java.util.Calendar;
import java.util.Map;
import org.apertereports.common.ConfigurationConstants;
import org.apertereports.dao.ConfigurationDAO;

/**
 * A static cache for configuration entries. Thread-safe.
 */
public final class ConfigurationCache implements ConfigurationConstants {

    /**
     * Map cache.
     */
    private static Map<String, String> configuration;
    /**
     * Date when the cache expires.
     */
    static Calendar validUntil;

    private ConfigurationCache() {
    }

    /**
     * Gets a single configuration entry corresponding to given key.
     *
     * @param key Entry key
     * @return Entry value
     */
    public static String getValue(String key) {
        if (key == null) {
            return null;
        }
        init();

        synchronized (ConfigurationCache.class) {
            if (configuration.containsKey(key)) {
                return configuration.get(key);
            } else {
                return null;
            }
        }
    }

    /**
     * Initializes the cache. Retrieves the configuration from database and sets
     * the expiration date.
     */
    private static synchronized void init() {
        if (configuration == null || Calendar.getInstance().after(validUntil)) {
            configuration = ConfigurationDAO.fetchAllToMap();
            String timeout = configuration.get(CONFIGURATION_CACHE_TIMEOUT_IN_MINUTES);
            if (timeout == null) {
                timeout = "15";
            }
            validUntil = Calendar.getInstance();
            validUntil.add(Calendar.MINUTE, Integer.valueOf(timeout));
        }
    }

    /**
     * Returns all the configuration entries as map.
     *
     * @return the configuration The configuration entries as map
     */
    public static Map<String, String> getConfiguration() {
        init();
        return configuration;
    }
}
