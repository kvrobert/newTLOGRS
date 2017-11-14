/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.tlog16rs.core;

/**
 *
 * @author Robesz
 */
public class NotNewDateException extends Exception {

    public NotNewDateException() {
        super();
    }
    
    public NotNewDateException(String message) {
        super(message);
    }
    
}
