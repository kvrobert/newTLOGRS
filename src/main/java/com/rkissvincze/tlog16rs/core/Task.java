/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.tlog16rs.core;


import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 *
 * @author rkissvincze
 */

@Getter
@Setter
@NoArgsConstructor
public class Task {
    
    private String taskID;
    private LocalTime startTime;
    private LocalTime endTime;
    private String comment;        
    
    public long getMinPerTask() throws EmptyTimeFieldException{        
        if( startTime == null && endTime == null ) throw new EmptyTimeFieldException();            
        return ChronoUnit.MINUTES.between(startTime, endTime);        
    }
      
    public boolean isValidTaskId(){    
        return taskID.matches("\\d{4}") || taskID.matches("LT-\\d{4}");
    }

    @Override
    public String toString() {
        return "Task: " + taskID + ", " + comment + ", " + startTime + ", " + endTime ;
    }
    
    
}
