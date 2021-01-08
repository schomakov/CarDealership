package com.cardealership.connection;

import com.cardealership.model.Car;

import java.util.Scanner;

import static com.cardealership.connection.JdbcConnector.*;
import static com.cardealership.connection.JdbcConnector.deleteCar;

public class Actions {

    public void runProgram() {
        createTable();
        informationMessage();
        selectOptions();
    }

    private void informationMessage() {
        LOGGER.info("Welcome to CarDealership admin panel!");
        LOGGER.info("List of available options: \n" +
                "1.Get information about cars in dealership. \n" +
                "2.Add new car to dealership. \n" +
                "3.Modify car in dealership. \n" +
                "4.Sell car from dealership. \n" +
                "5.Exit from admin panel!");
        LOGGER.info("The system is waiting for your selection: ");
    }

    private void informationAboutCars(Scanner scanner, int input, boolean isValidInput) {
        getCars();
        LOGGER.info("The system is waiting for your selection: ");
    }

    private void addNewCar(Scanner scanner, int input, boolean isValidInput) {
        Car car = new Car();
        car.setBrand(scanner.next());
        car.setModel(scanner.next());
        car.setYear(scanner.next());
        car.setColor(scanner.next());
        insertCar(car);
        LOGGER.info("The system is waiting for your selection: ");
    }

    private static void modifyCar(Scanner scanner, int input, boolean isValidInput) {
        updateCar(scanner.nextInt(), scanner.next());
        LOGGER.info("The system is waiting for your selection: ");
    }

    private void sellCar(Scanner scanner, int input, boolean isValidInput) {
        deleteCar(scanner.nextInt());
        LOGGER.info("The system is waiting for your selection: ");
    }

    private void wrongInput(Scanner scanner, int input, boolean isValidInput) {
        if (input < 1 || input > 5) {
            LOGGER.info("Invalid input!");
            LOGGER.info("The system is waiting for your selection: ");
        }
    }

    private void selectOptions() {

        boolean isValidInput = true;
        Scanner scanner = new Scanner(System.in);
        do {
            int input = scanner.nextInt();
            switch (input) {
                case 1:
                    informationAboutCars(scanner, input, false);
                    isValidInput = false;
                    break;
                case 2:
                    addNewCar(scanner, input, true);
                    isValidInput = false;
                    break;
                case 3:
                    modifyCar(scanner, input, true);
                    isValidInput = false;
                    break;
                case 4:
                    sellCar(scanner, input, true);
                    isValidInput = false;
                    break;
                case 5:
                    return;
                default:
                    wrongInput(scanner, input, false);
                    break;
            }
        } while (!isValidInput);
    }
}
