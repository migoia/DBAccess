/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.migoia.core.database;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author migoia
 */
public class DataSource {
    
    BasicDataSource ds = new BasicDataSource();

    public DataSource(File file) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (Exception ex) {
            Logger.getLogger("com.migoia.core.DB").log(Level.SEVERE, file.getAbsolutePath(), ex);
            throw new RuntimeException("DB.NoConnectFile");
        }
        String driver = properties.getProperty("driver");
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        if (driver == null) {
            Logger.getLogger("com.migoia.core.DB").log(Level.SEVERE, "DB.InvalidDriver");
            throw new RuntimeException("DB.InvalidDriver");
        }
        if (url == null) {
            Logger.getLogger("com.migoia.core.DB").log(Level.SEVERE, null, "DB.InvalidURL");
            throw new RuntimeException("DB.InvalidURL");
        }
        ds.setDriverClassName(driver);
        Logger.getLogger("com.migoia.core.DB").log(Level.CONFIG, driver);
        ds.setUrl(url);
        Logger.getLogger("com.migoia.core.DB").log(Level.CONFIG, url);
        if (username != null) {
            ds.setUsername(username);
            if (password != null) {
                ds.setPassword(password);
            }
        }
        ds.setDefaultAutoCommit(false);
    }

    public Transaction getTransaction(){
        try {
            Connection connection = ds.getConnection();
//            System.out.print(ds.getNumActive());
//            System.out.print("/");
//            System.out.print(ds.getMaxActive());
//            System.out.print("(active)");
//            System.out.print(ds.getMaxIdle());
//            System.out.print("/");
//            System.out.print(ds.getNumIdle());
//            System.out.print("(idle)");
            return new Transaction(connection);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <E>E execute(Query<E> query){
        Transaction tr = null;
        E e = null;
        try {
            tr = new Transaction(ds.getConnection());
            e = tr.execute(query);
            tr.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return e;
    }

    public <E>E execute(PQuery<E> query){
        Transaction tr = null;
        E e = null;
        try {
            tr = new Transaction(ds.getConnection());
            e = tr.execute(query);
            tr.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return e;
    }

    public <E>E execute(Update<E> update){
        Transaction tr = null;
        E e = null;
        try {
            tr = new Transaction(ds.getConnection());
            e = tr.execute(update);
            tr.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return e;
    }
}
