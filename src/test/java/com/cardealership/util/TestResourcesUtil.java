package com.cardealership.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

import static com.cardealership.Constants.*;
import static com.cardealership.util.ResourcesUtil.loadPropertiesFile;

public class TestResourcesUtil {

    @Test
    public void testLoadPropertiesFile() {
        Properties prop = loadPropertiesFile("src/main/resources/config.properties");
        Assert.assertNotNull(prop);
        Assert.assertEquals(prop.getProperty(DB_URL_KEY), "jdbc:mysql://127.0.0.1:3306/cardealership");
        Assert.assertEquals(prop.getProperty(DB_USER_KEY), "root");
        Assert.assertEquals(prop.getProperty(DB_PASS_KEY), "970322#Sc");
    }
}
