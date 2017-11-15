/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Exceptions;

/**
 *
 * @author rkissvincze
 */
public class EmptyTimeFieldException extends Exception {

    public EmptyTimeFieldException() {
        super();
    }
    
    public EmptyTimeFieldException(String message) {
        super(message);
    }
}
