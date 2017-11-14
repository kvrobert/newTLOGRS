/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.tlog16rs.resources;

import com.fasterxml.jackson.module.afterburner.util.MyClassLoader;
import javassist.bytecode.Bytecode;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.*;

/**
 *
 * @author rkissvincze
 */
@Slf4j
public class SLF4j_teszt {
    int oldT;
    int t;
    static final Logger LOG = LoggerFactory.getLogger(MyClassLoader.class);
    
    
    
    public void setTemperature(Integer temperature) {
       
        
        System.out.println("degug előtt..."); 
       oldT = t;
       t = temperature;
       log.debug(" Temperature set to {}. Old temperature was {} ", t, oldT);
       LOG.debug(" LOG....Temperature set to {}. Old temperature was {} ", t, oldT);
        System.out.println("degug után...");
        
       
       if( temperature.intValue() > 50 ){
           log.info("Temperature has risen above 50 degress");
           log.error("Temperature has risen above 50 degress..ERRROR");
           LOG.error("LOG....Temperature has risen above 50 degress..ERRROR");
           
       }
       
    }
  
    public static void main(String[] args) {
        SLF4j_teszt tesz = new SLF4j_teszt();
        tesz.setTemperature(59);
    }
    
    
}
