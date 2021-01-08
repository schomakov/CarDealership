package com.cardealership.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ResourcesUtil {

    private static final Logger LOGGER = Logger.getLogger(ResourcesUtil.class);

    public static void logException(Exception exception) {
        LOGGER.error(String.format("Exception Message: %s", exception.getMessage()));
    }

    public static void logSqlException(SQLException sqlException) {
        LOGGER.error(String.format("\nSQL State: %s\nException Message: %s", sqlException.getSQLState(), sqlException.getMessage()));
    }

    public static void logIoException(IOException exception) {
        LOGGER.error(String.format("Exception Message%s", exception.getMessage()));
    }

    public static void closeResource(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (SQLException sqlEx) {
                logSqlException(sqlEx);
            } catch (Exception ex) {
                logException(ex);
            }
        }
    }

    public static Properties loadPropertiesFile(String filePath) {
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream(filePath)) {
            prop.load(input);
        } catch (IOException io) {
            logIoException(io);
        }
//        prop.forEach((key, value) -> LOGGER.info("Key: " + key + ", Value: " + value));
        return prop;
    }
}

