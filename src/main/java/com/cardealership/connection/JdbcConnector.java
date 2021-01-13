package com.cardealership.connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.cardealership.model.Car;
import com.cardealership.util.ResourcesUtil;
import org.apache.log4j.Logger;

import static com.cardealership.Constants.*;
import static com.cardealership.util.ResourcesUtil.*;

public class JdbcConnector {

    final static Logger LOGGER = Logger.getLogger(JdbcConnector.class);

    public static final Properties PROPERTIES = ResourcesUtil.loadPropertiesFile("src/main/resources/config.properties");

    public static Connection getConnection(String username, String pass) {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cardealership", username, pass);
        } catch (SQLException sqlEx) {
            logSqlException(sqlEx);
        }

        return connection;
    }

    public static void createTable() {
        Connection connection = getConnection(PROPERTIES.getProperty(DB_USER_KEY), PROPERTIES.getProperty(DB_PASS_KEY));
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS car (\n" +
                    "Id INT NOT NULL AUTO_INCREMENT,\n" +
                    "Brand VARCHAR(255),\n" +
                    "Model VARCHAR(255),\n" +
                    "Year VARCHAR(255),\n" +
                    "Color VARCHAR(255),\n" +
                    "PRIMARY KEY(id));");
        } catch (SQLException sqlEx) {
            logSqlException(sqlEx);
        } catch (Exception e) {
            logException(e);
        } finally {
            closeResource(statement);
            closeResource(connection);
        }
    }

    public static void dropTable() {
        Connection connection = getConnection(PROPERTIES.getProperty(DB_USER_KEY), PROPERTIES.getProperty(DB_PASS_KEY));
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS car");
        } catch (SQLException sqlEx) {
            logSqlException(sqlEx);
        } finally {
            closeResource(statement);
            closeResource(connection);
        }
    }

    public static void initTable() {
        Connection connection = getConnection(PROPERTIES.getProperty(DB_USER_KEY), PROPERTIES.getProperty(DB_PASS_KEY));
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO car (Brand, Model, Year, Color)" +
                    "VALUES ('Audi', 'A8', '2017', 'White')," +
                    "('BMW', 'M5', '2017', 'Black')," +
                    "('Mercedes', 'Sclass', '2017', 'Blue')," +
                    "('Opel', 'Vectra', '2017', 'Red')");
        } catch (Exception e) {
            logException(e);
        } finally {
            closeResource(statement);
            closeResource(connection);
        }
    }

    public static List<Car> getCars() {

        List<Car> listOfCars = new ArrayList<>();

        Connection connection = getConnection(PROPERTIES.getProperty(DB_USER_KEY), PROPERTIES.getProperty(DB_PASS_KEY));

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_SELECT);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String brand = resultSet.getString("Brand");
                String model = resultSet.getString("Model");
                String year = resultSet.getString("Year");
                String color = resultSet.getString("Color");

                Car obj = new Car();
                obj.setID(id);
                obj.setBrand(brand);
                obj.setModel(model);
                obj.setYear(year);
                obj.setColor(color);

                listOfCars.add(obj);

            }

            listOfCars.forEach(x -> LOGGER.info(x));
        } catch (SQLException sqlEx) {
            logSqlException(sqlEx);
        } finally {
            closeResource(resultSet);
            closeResource(preparedStatement);
            closeResource(connection);
        }
        return listOfCars;
    }

    public static boolean insertCar(Car car) {
        Connection connection = getConnection(PROPERTIES.getProperty(DB_USER_KEY), PROPERTIES.getProperty(DB_PASS_KEY));
        PreparedStatement preparedStatement = null;
        PreparedStatement anotherPreparedStatement = null;
        boolean isInserted = true;
        try {
            preparedStatement = connection.prepareStatement(" INSERT INTO car (id, Brand, Model, Year, Color)" + "VALUES (?, ?, ?, ?, ?)");
            connection.setAutoCommit(false); //default true

            preparedStatement.setInt(1,car.getId());
            preparedStatement.setString(2, car.getBrand());
            preparedStatement.setString(3,car.getModel());
            preparedStatement.setString(4, car.getYear());
            preparedStatement.setString(5, car.getColor());
            preparedStatement.executeUpdate();

            anotherPreparedStatement = connection.prepareStatement("update car set color = 'Red' where Brand = 'Citroen' ");
            anotherPreparedStatement.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);

        } catch (Exception e) {
            logException(e);
            isInserted = false;
        }
        return isInserted;
    }

    public static boolean updateCar(int id, String color) {
        Connection connection = getConnection(PROPERTIES.getProperty(DB_USER_KEY), PROPERTIES.getProperty(DB_PASS_KEY));
        PreparedStatement preparedStatement = null;
        boolean isUpdated = true;

        try {
            preparedStatement = connection.prepareStatement("update car set color = ? where id = ? ");

            preparedStatement.setString(1, color);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            logException(e);
            isUpdated = false;
        }
        return isUpdated;
    }

    public static boolean deleteCar(int id) {
        Connection connection = getConnection(PROPERTIES.getProperty(DB_USER_KEY), PROPERTIES.getProperty(DB_PASS_KEY));
        PreparedStatement preparedStatement = null;
        boolean isDeleted = true;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM Car WHERE id= ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            logException(e);
            isDeleted = false;
        }
        return isDeleted;
    }

    public static List<String> showTables() {

        List<String> listOfTables = new ArrayList<>();

        Connection connection = getConnection(PROPERTIES.getProperty(DB_USER_KEY), PROPERTIES.getProperty(DB_PASS_KEY));
        Statement stmt = null;
        ResultSet resultSet = null;

        try {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(SQL_SHOWTABLES);

            if (stmt.execute(SQL_SHOWTABLES)) {
                resultSet = stmt.getResultSet();
            }

            while (resultSet.next()) {
                LOGGER.info(resultSet.getString("Tables_in_cardealership"));
                listOfTables.add(resultSet.getString("Tables_in_cardealership"));
            }
        } catch (SQLException sqlEx) {
            logSqlException(sqlEx);
        } finally {
            closeResource(resultSet);
            closeResource(stmt);
            closeResource(connection);
        }

        return listOfTables;
    }

    @Deprecated
    public static List<String> showDatabases() {

        List<String> list = new ArrayList<>();

        Connection connection = getConnection(PROPERTIES.getProperty(DB_USER_KEY), PROPERTIES.getProperty(DB_PASS_KEY));
        Statement stmt = null;
        ResultSet resultSet = null;

        try {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(SQL_SHOWDATABASES);

            if (stmt.execute(SQL_SHOWDATABASES)) {
                resultSet = stmt.getResultSet();
            }

            while (resultSet.next()) {
                LOGGER.info(resultSet.getString("Database"));

                list.add(resultSet.getString("Database"));
            }
        } catch (SQLException sqlEx) {
            logSqlException(sqlEx);
        } finally {
            closeResource(resultSet);
            closeResource(stmt);
            closeResource(connection);
        }
        return list;
    }
}
