package com.example.sistemas.casalinda;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Alaeddin on 5/20/2017.
 */

public class ConnectionStr {
String ip,db,DBUserNameStr,DBPasswordStr;
    @SuppressLint("NewApi")
    public Connection connectionclasss()
    {
       ip = "190.110.214.14"; //Produccion
       // ip = "192.168.4.122"; //Prueba
        db = "siinf_casalindap_ec";
        DBUserNameStr = "Android";
        DBPasswordStr = "p!65or@lna#1522";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection = null;
        String ConnectionURL;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip +";databaseName="+ db + ";user=" + DBUserNameStr+ ";password=" + DBPasswordStr + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }}
