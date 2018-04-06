package com.open.broker.stepDefinition;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.open.broker.Share.jdbcShare;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class jdbcTest {

    private jdbcShare r;
    private String schema;
    private String testName;
    private int i;

    public jdbcTest(jdbcShare r) {
        this.r = r;
    }

    @Given("^jdbc connection with host:// \"(.+)\" port:\"(\\d+)\" database name \"(.+)\"$")
    public void jdbcConnectionSetup(String host, int port, String dbName) throws ClassNotFoundException, SQLException {

        r.jdbcConnection = "jdbc:sqlserver://";
        r.host = host + ":";
        r.port = port;
        r.dbname = "databaseName=" + dbName;
        r.connectionUrl = r.jdbcConnection + r.host + r.port + ";" + "integratedSecurity=true;" + r.dbname;

        try (Connection connection = DriverManager.getConnection(r.connectionUrl)) {
                r.schema = connection.getSchema();
                r.connection = connection;
        } catch (SQLException e) {
            System.out.println();
            e.printStackTrace();
        }
        if ((r.connection == null) || r.connection.isClosed()) {
            r.connection = DriverManager.getConnection(r.connectionUrl);
            r.connection.setAutoCommit(true);
        }
    }

    @Given("^set host:// \"(.+)\" port:\"(\\d+)\" database name \"(.+)\"$")
    public void setConnectionDetails(String host, int port, String dbName) {
        r.jdbcConnection = "jdbc:sqlserver://";
        r.host = host + ":";
        r.port = port;
        r.dbname = "databaseName=" + dbName;
        r.connectionUrl = r.jdbcConnection + r.host + r.port + ";" + "integratedSecurity=true;" + r.dbname;
    }

    @And("^set query source as file: \"(.*)\"$")
    public void setSqlQuerryFile(String name) throws IOException {
        r.sql = Files.readAllLines(Paths.get("src/test/resources/testData/" + name)).get(0);
    }

    @And("^execute SQL statement: \"(.*)\"$")
    public void statement(String sql) throws SQLException {

        Statement statement = null;
        try {
            statement = r.connection.createStatement();
            Boolean isRetrieved = statement.execute(sql);
            System.out.println("\nIs data retrieved: " + isRetrieved);
            r.resultSet = statement.executeQuery(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            while (r.resultSet.next()) {
                int broker_id = r.resultSet.getInt("broker_id");
                String shortname = r.resultSet.getString("shortname");
                String fullname = r.resultSet.getString("fullname");
                String bic_code = r.resultSet.getString("bic_code");
                String account_inn = r.resultSet.getString("account_inn");
                String account_corr = r.resultSet.getString("account_corr");
                String comments = r.resultSet.getString("comments");

                System.out.println("broker_id: " + broker_id);
                System.out.println("shortname: " + shortname);
                System.out.println("fullname: " + fullname);
                System.out.println("bic_code: " + bic_code);
                System.out.println("account_inn: " + account_inn);
                System.out.println("account_corr: " + account_corr);
                System.out.println("comments: " + comments);
            }
            if (statement != null) {
                statement.close();
            }
        }
//            resultSet.close();
//            statement.close();
//            connection.close();
    }

    @And("^execute new SQL statement: (.*)$")
    public void newStatement(String sql) throws SQLException, SQLServerException {

        Statement statement = null;
        try {
            statement = r.connection.createStatement();
            r.resultSet = statement.executeQuery(sql);
            System.out.println(String.valueOf(r.resultSet.getRow()));
        } catch (SQLException sqle) {
            do {
                System.out.println("\nMESSAGE: " + sqle.getMessage());
                System.out.println();
                sqle = sqle.getNextException();
            } while (sqle != null);
        }
    }

    @And("^execute query from file: \"(.*)\"$")
    public void StatementFromFile(String name) throws SQLException, SQLServerException, IOException {
        r.sql = Files.readAllLines(Paths.get("src/test/resources/testData/" + name)).get(0);

        try {
            Statement statement = r.connection.createStatement();
            r.resultSet = statement.executeQuery(r.sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @And("^get values from column names: string type \"(.*)\", string type \"(.*)\", int type \"(.*)\"$")
    public void getFieldsValues(String label, String label1, String label2) throws SQLException {
        while(r.resultSet.next()) {
            r.field = r.resultSet.getString(label);
            r.field2 = r.resultSet.getInt(label1);
            System.out.println(label + ": " + r.field);
            System.out.println(label1 + ": " + r.field2);

            String comments = r.resultSet.getString(label2);
            System.out.println(label2 + ": " + comments + "\n");
        }
    }

    @And("^create notification table with name \"(.*)\" and int column name \"(.*)\" and varchar column name \"(.*)\"$")
    public void notificationTable(String nTableName, String columnName, String secondColumnName) throws SQLException {
        r.nTableName = nTableName;
        r.columnName = columnName;
        r.secondColumnName = secondColumnName;

        String sql = "CREATE TABLE " + r.nTableName + " (" + columnName + " INT, [" + secondColumnName + "] VARCHAR(32)); INSERT INTO " + r.nTableName +
                " (" + columnName + "," + secondColumnName + ") VALUES (" + r.field2 + ", '" + r.field + "')";

        Statement statement = null;
        try {
            statement = r.connection.createStatement();
            r.resultSet = statement.executeQuery(sql);
        } catch (SQLException sqle){
        }
    }

    @And("^send notification table to email \"(.*)\" with subject \"(.*)\" and text \"(.*)\"$")
    public void sendNotificationTable(String email, String subject, String mailText) throws SQLException { //'matveyev@open.ru;ragimov@open.ru'

        String sql = "exec dbo.util_send_table @p_table = '" + r.nTableName +
                "', @p_columns = '" + r.columnName + "|" + r.secondColumnName + "',  @p_email = '" +
                email + "', @p_subject = '" + subject + "', @p_text = '<br/><br/>" + mailText + "';";

        Statement statement = null;
        try {
            statement = r.connection.createStatement();
            r.resultSet = statement.executeQuery(sql);
        } catch (SQLException sqle){
        }
    }

    @And("^delete notification table \"(.*)\"$")
    public void deleteNotificationTable(String nTableName) throws SQLException {
        r.nTableName = nTableName;

        String sql = "DROP TABLE " + nTableName + ";";  //#rpt2;";
        Statement statement = null;
        try {
            statement = r.connection.createStatement();
            Boolean isRetrieved =    statement.execute(sql);
            System.out.println("\nIs data retrieved: " + isRetrieved);
            r.resultSet = statement.executeQuery(sql);
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    @And("^execute SQL statement$")
    public void statement() throws SQLException {

        String sql = "select analytic_1, * from planned_transactions where account=70 and status_id=3 and analytic_1 is null and services_date >='20170101' order by 1 desc";
        Statement statement = null;
        try {
            statement = r.connection.createStatement();
            Boolean isRetrieved =    statement.execute(sql);
            System.out.println("\nIs data retrieved: " + isRetrieved);
            r.resultSet = statement.executeQuery(sql);
        } catch (SQLException sqle){
            sqle.printStackTrace();
        } finally {
            while (r.resultSet.next()) {
                int broker_id = r.resultSet.getInt("broker_id");
                String shortname = r.resultSet.getString("shortname");
                String fullname = r.resultSet.getString("fullname");
                int bic_code = r.resultSet.getInt("bic_code");
                int account_inn = r.resultSet.getInt("account_inn");
                int account_corr = r.resultSet.getInt("account_corr");
                String comments = r.resultSet.getString("comments");

                System.out.println("broker_id: " + broker_id);
                System.out.println("shortname: " + shortname);
                System.out.println("fullname: " + fullname);
                System.out.println("bic_code: " + bic_code);
                System.out.println("account_inn: " + account_inn);
                System.out.println("account_corr: " + account_corr);
                System.out.println("comments: " + comments );
                System.out.println("\n===================\n");
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    @And("^execute SQL statement 2$")
    public void statementwo() throws SQLException {

        String sql = "select analytic_1, * from planned_transactions where account=70 and status_id=3 and analytic_1 is null and services_date >='20170101' order by 1 desc";

        Statement statement = null;
        try {
            statement = r.connection.createStatement();
            Boolean isRetrieved = statement.execute(sql);
            System.out.println("\nIs data retrieved: " + isRetrieved);
            r.resultSet = statement.executeQuery(sql);
        } catch (SQLException sqle){
            sqle.printStackTrace();
        } finally {
            while (r.resultSet.next()) {
                String field = r.resultSet.getString("analytic_1");
                r.field2 = r.resultSet.getInt("id");
                System.out.println("\n" + "analytic_1: " + r.field);
                System.out.println("\nid: " + r.field2);

                String comments = r.resultSet.getString("comment");
                System.out.println("comment: " + comments );
                System.out.println("\n===================\n");
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    @And("^query execute and get values from column: \"(.*)\"$")
    public void getValues(String column) throws SQLException {

        try {
            r.connection = DriverManager.getConnection(r.connectionUrl);
            String schema = r.connection.getSchema();
            String selectSql = r.sql;

            try (Statement statement = r.connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {

                r.cols = new ArrayList<String>();
                while (resultSet.next()) {
                    r.cols.add(resultSet.getObject(column).toString());//387
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @And("^new query execute get values from column: \"(.*)\" and compare with previous column$")
    public void columnsCompare(String column2) {
        try {
            r.connection = DriverManager.getConnection(r.connectionUrl);
            String schema = r.connection.getSchema();
            String selectSql = r.sql;

            try (Statement statement = r.connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {

                r.cols2 = new ArrayList<String>();
                while (resultSet.next()) {
                    r.cols2.add(resultSet.getObject(column2).toString());//387
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String x = r.cols.toString();
        Helpers.equalLists(r.cols, r.cols2);
        System.out.println("Column 1: " + r.cols.toString() + "\n" + "And\n" + "Column 2: "  + r.cols2.toString() + "\nSuccessfully Compared");
    }

    @And("^the test's name is \"(.*)\"$")
    public void testName(String testName) {
        System.out.println("\nTest: " + testName + "\n");
        this.testName = testName;
    }
}