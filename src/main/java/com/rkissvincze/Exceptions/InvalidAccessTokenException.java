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
public class InvalidAccessTokenException extends Exception {

    public InvalidAccessTokenException(String invalid_Acces_Token) {
        super( invalid_Acces_Token);
    }
    
    public InvalidAccessTokenException() {
        super( );
    }
    
}
