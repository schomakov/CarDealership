package com.cardealership.connection;

import com.cardealership.model.Car;
import com.cardealership.util.ResourcesUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Properties;

import static com.cardealership.Constants.*;
import static com.cardealership.connection.JdbcConnector.*;

public class TestJdbcConnector {
    @Before
    public void setUp () {
        dropTable();
        createTable();
        initTable();
    }

    @Test
    public void testGetConnection() {
        Properties prop = ResourcesUtil.loadPropertiesFile("src/main/resources/config.properties");
        Connection connectionOk = getConnection(prop.getProperty(DB_USER_KEY), prop.getProperty(DB_PASS_KEY));

        Assert.assertNotNull(connectionOk);
    }

    @Test
    public void testCreateTable() {
        createTable();
        Assert.assertTrue(showTables().contains("car"));
    }

    @Test
    public void testDropTable() {
        dropTable();
        Assert.assertFalse(showTables().contains("car"));
    }

    @Test
    public void testInitTable() {
        initTable();
        Assert.assertEquals(8, getCars().size());
    }

    @Test
    public void testGetCars() {

        Assert.assertEquals(getCars().get(1).getBrand(), "BMW");
    }

    @Test
    public void testUpdateCar() {
        updateCar(3, "Red");
        Assert.assertTrue(getCars().get(2).getColor().contains("Red"));
    }

    @Test
    public void testDeleteCar() {
        deleteCar(1);
        Assert.assertEquals(3, getCars().size());
    }

    @Test
    public void testInsertCar() {
        Car car = new Car();
        car.setID(5);
        car.setBrand("Citroen");
        car.setModel("C3");
        car.setYear("2005");
        car.setColor("Black");
        insertCar(car);
        Assert.assertEquals(5, getCars().size());
    }
}
