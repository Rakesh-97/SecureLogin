package com.example.choudhary.mobilepass;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionClass {
//10.0.2.2:3306
    String url = "jdbc:mysql://10.0.2.2:3306/registerdemo";
    String un = "root";
    String password = "";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        //String ConnURL = null;
        try {

            Class.forName ("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, un, password);
            //conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRORee", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROE", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROe", e.getMessage());
        }
        return conn;
    }
}
