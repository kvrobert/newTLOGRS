/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Services;

import com.rkissvincze.Beans.WorkMonthRB;
import com.rkissvincze.Entities.TimeLogger;
import com.rkissvincze.Entities.WorkDay;
import com.rkissvincze.Entities.WorkDayRB;
import com.rkissvincze.Entities.WorkMonth;
import com.rkissvincze.Exceptions.FutureWorkException;
import com.rkissvincze.Exceptions.NegativeMinutesOfWorkException;

/**
 *
 * @author rkissvincze
 */
public class ServicesResource {
    
    public static boolean isMonthExits( TimeLogger timelogger, WorkMonth workMonth ){
        
        return !timelogger.isNewMonth(workMonth);
    }
    
    public static WorkMonth createWorkMonth(WorkMonthRB workMontRB){
            return WorkMonth.fromNumbers(workMontRB.getYear(), workMontRB.getMonth());
    }
    
    public static boolean isNewDay(TimeLogger timeLogger, WorkDay workDay){
        
        WorkMonth workMonth = timeLogger.getMonths().stream().findFirst()
                .filter(month -> month.getDate().getMonthValue() == 
                        workDay.getActualDay().getMonthValue() ).get();
        
        return workMonth.getDays().stream().filter( 
                day -> day.getActualDay().equals(workDay.getActualDay()) )
                .count() == 0;
    }
    
    public static WorkDay createWorkDay( WorkDayRB workDayRB ) throws NegativeMinutesOfWorkException, FutureWorkException{
        return WorkDay.fromNumbers( workDayRB.getRequiredHour() * 60,
                                    workDayRB.getYear(), 
                                    workDayRB.getMonth(), 
                                    workDayRB.getDay());        
    }
    
}
