/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.migoia.core.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author migoia
 */
public abstract class PQuery<R> extends PUpdate<R>{
    protected PreparedStatement ps;
    protected ResultSet result;
    
    void set(ResultSet result){
        this.result = result;
    }
    
}
