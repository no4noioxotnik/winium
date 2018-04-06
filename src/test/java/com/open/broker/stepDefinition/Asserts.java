package com.open.broker.stepDefinition;

import com.open.broker.Share.jdbcShare;
import cucumber.api.java.en.And;
import org.junit.Assert;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Asserts {
    private jdbcShare r;
    private int i;
    private Asserts resultSet;

    public Asserts (jdbcShare r) {
        this.r = r;
    }

    @And("^assert that column name: \"(.*)\" is null$")
    public void assertNotNull(String name) {
        r.name = name;
        Assert.assertEquals(r.field, null);
    }

    @And("^assert that response is empty$")
    public void assertResponceIsEmpty() throws SQLException {
        String countResp = String.valueOf(resultSetCount(r.resultSet));
        Assert.assertEquals("0", countResp);
            System.out.println("The response has: " + countResp + " results" + "\n");
    }

    @And("^assert that response is not empty$")
    public void assertResponseIsNotEmpty() throws SQLException {
        String countRes = String.valueOf(resultSetCount(r.resultSet));
        Assert.assertNotEquals("0", countRes);
        System.out.println("The response has: " + r.resultSet.getMetaData().getColumnCount() + " columns" + " and " + countRes + " results." + "\n");
    }

    private int resultSetCount(ResultSet resultSet) throws SQLException{
        try{
            int i = 0;
            while (r.resultSet.next()) {
                i++;
            }
            return i;
        } catch (Exception e){
            System.out.println("Error getting row count");
            e.printStackTrace();
        }
        return this.i = i;
    }

}
