package com.cardealership;

public class Constants {

    private Constants() {

    }

    public static final String SQL_SELECT = "Select * from car";
    public static final String SQL_SHOWTABLES = "Show tables";
    public static final String SQL_SHOWDATABASES = "Show databases";
    public static final String SQL_INSERT = "INSERT INTO car " + "VALUES (' ', ' ', ' ', ' ', ' ')";

    public static final String DB_URL_KEY = "url";
    public static final String DB_USER_KEY = "username";
    public static final String DB_PASS_KEY = "pass";
    public static final String DB_PASS_BAD_KEY = "pass.bad";
}