/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.migoia.core.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author migoia
 * @param <R>
 */
public abstract class PUpdate <R> extends Update<R>{
    protected PreparedStatement ps;

    @Override
    protected R execute() throws SQLException{
        return null;
    }
}
