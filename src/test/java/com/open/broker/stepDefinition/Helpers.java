package com.open.broker.stepDefinition;

import com.open.broker.Share.jdbcShare;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class Helpers {
    private jdbcShare r;
    private int i;

    public Helpers(jdbcShare r) {
        this.r = r;
    }

    private int columnSetCount(ResultSet columnSet) throws SQLException {
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
        return r.i = i;//return this.i = i;
    }

    public static boolean equalLists(List<String> a, List<String> b){
        // Check for sizes and nulls
        if (a == null && b == null) return true;

        if ((a == null && b!= null) || (a != null && b== null) || (a.size() != b.size()))
        {
            return false;
        }
        // Sort and compare the two lists
        Collections.sort(a);
        Collections.sort(b);
        return a.equals(b);
    }

}
