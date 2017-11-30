/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.migoia.core.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author migoia
 */
public class Transaction {
	
	private boolean SQLDebug = false;

    Connection connection;

    Transaction(Connection connection) {
        this.connection = connection;
    }
    
    public void SQLDebug(boolean debug){
    	this.SQLDebug = debug;
    }

    public int execute(String sql){
        Statement s = null;
        try {
            s = connection.createStatement();
            return s.executeUpdate(sql);
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            try {
                connection.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            throw new RuntimeException("DataBaseErrorAccess:\n" + sql, ex);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public <R> R execute(Query<R> sql) {
        Statement s = null;
        ResultSet rs = null;
        sql.set(this);
        try {
            s = connection.createStatement();
            sql.prepare();
            if(SQLDebug || sql.SQLDebug()){
            	System.out.println(sql.sql);
            }
            rs = s.executeQuery(sql.sql);
            sql.set(rs);
            return sql.execute();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            try {
                connection.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            throw new RuntimeException("DataBaseErrorAccess: \n" + sql.sql, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public <R> R execute(Update<R> sql) {
        Statement s = null;
        sql.set(this);
        try {
            s = connection.createStatement();
            sql.prepare();
            if(SQLDebug || sql.SQLDebug()){
            	System.out.println(sql.sql);
            }
            s.executeUpdate(sql.sql);
            return sql.execute();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            try {
                connection.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            throw new RuntimeException("DataBaseErrorAccess: \n" + sql.sql, ex);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public <R> R execute(PQuery<R> sql) {
        ResultSet rs = null;
        sql.set(this);
        try {
            if(sql.ps == null){
                sql.ps = connection.prepareStatement(sql.sql);
            }
            sql.prepare();
            rs = sql.ps.executeQuery();
            sql.set(rs);
            return sql.execute();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            try {
                connection.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            throw new RuntimeException("DataBaseErrorAccess: \n" + sql.sql, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public <R> R execute(PUpdate<R> sql) {
        sql.set(this);
        try {
            if(sql.ps == null){
                sql.ps = connection.prepareStatement(sql.sql);
            }
            sql.prepare();
            sql.ps.executeUpdate();
            return sql.execute();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            try {
                connection.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            throw new RuntimeException("DataBaseErrorAccess: \n" + sql.sql, ex);
        }
    }
    
    public void close() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.commit();
                    connection.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("DataBaseTransactionError", ex);
            }
        }
    }

    public void exit() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.rollback();
                    connection.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("DataBaseTransactionError", ex);
            }
        }
    }
}
