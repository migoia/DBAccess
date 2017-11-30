/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.migoia.core.database;

import java.sql.SQLException;

/**
 *
 * @author migoia
 * @param <R>
 */
public abstract class Update <R>{
    protected Transaction transaction;
    protected String sql;
    private boolean SQLDebug = false;

    protected void prepare() throws SQLException{
        
    }
    
    protected R execute() throws SQLException{
        return null;
    }
    
    public boolean SQLDebug(){
    	return SQLDebug;
    }
    
    public void SQLDebug(boolean debug){
    	this.SQLDebug = debug;
    }

    
    void set(Transaction transaction){
        this.transaction = transaction;
    }
    
    final char quote = '\'';
    final String NULL = "NULL";
    
    final protected String set(String value){
        if(value == null || value.length()==0) {
            return NULL;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(quote);
        for(int i=0; i<value.length(); i++){
            char c = value.charAt(i);
            if(c==quote){
                sb.append(c).append(c);
            } else {
                sb.append(c);
            }
        }
        sb.append(quote);
        return sb.toString();
    }

    final protected String set(Integer value){
        if(value == null) {
            return NULL;
        }
        return value.toString();
    }
    
    final protected String set(Enum value){
        if(value == null) {
            return NULL;
        }
        return value.toString();
    }
    
    final protected String set(java.util.Date value){
        if(value == null) {
            return "NULL";
        }
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(value);
        return "TO_DATE("+quote + date + quote+",'YYYY-MM-DD')";
    }
    
    final protected String set(java.util.Collection<String> list){
        StringBuilder sb = new StringBuilder();
        String prefix = " in (";
        for(String s: list){
            sb.append(prefix).append(set(s));
            prefix = ",";
        }
        return sb.append(')').toString();
    }
}
