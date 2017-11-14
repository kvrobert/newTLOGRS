/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.tlog16rs.core;

/**
 *
 * @author rkissvincze
 */
public class NoTaskIdException extends Exception {

    public NoTaskIdException() {
        super();
    }
    public NoTaskIdException(String message) {
        super(message);
    }
}
