/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Exceptions;

/**
 *
 * @author Robesz
 */
public class NotTheSameMonthException extends Exception {

    public NotTheSameMonthException() {
        super();
    }
    public NotTheSameMonthException(String message) {
        super(message);
    }
}
