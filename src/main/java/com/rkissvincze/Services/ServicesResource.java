/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Services;

import com.rkissvincze.Beans.DeleteTaskRB;
import com.rkissvincze.Beans.ModifyTaskRB;
import com.rkissvincze.Entities.Task;
import com.rkissvincze.Entities.TimeLogger;
import com.rkissvincze.Entities.WorkDay;
import com.rkissvincze.Entities.WorkMonth;
import com.rkissvincze.Exceptions.EmptyTimeFieldException;
import com.rkissvincze.Exceptions.FutureWorkException;
import com.rkissvincze.Exceptions.InvalidTaskIdException;
import com.rkissvincze.Exceptions.NegativeMinutesOfWorkException;
import com.rkissvincze.Exceptions.NoTaskIdException;
import com.rkissvincze.Exceptions.NotExpectedTimeOrderException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author rkissvincze
 */
public class ServicesResource {
    
    public static boolean isMonthExits( TimeLogger timelogger, int year, int month){
        
        return !timelogger.isNewMonth( WorkMonth.fromNumbers(year, month) );
    }
    
    public static WorkMonth createWorkMonth(int year, int month){
        return WorkMonth.fromNumbers(year, month);
    }
    
    public static boolean isDayExits(TimeLogger timeLogger, int year, int month, int day){
        
        if( !isMonthExits(timeLogger, year, month) ) return false;
        
        WorkMonth workMonth = timeLogger.getMonths().stream().findFirst()
                .filter(wm -> wm.getDate().getMonthValue() == month ).get();
        
        return workMonth.getDays().stream().filter( 
                wd -> wd.getActualDay().equals( 
                LocalDate.of(year, month, day)) ).count() != 0;
    }
    
    public static WorkDay createWorkDay( int requiredMin, int year, int month, int day ) 
            throws NegativeMinutesOfWorkException, FutureWorkException{
        return WorkDay.fromNumbers( requiredMin, year, month, day);        
    }
    
    public static boolean isTaskExits( WorkDay workday, String taskId, String startTime){        
        return workday.getTasks().stream().filter( task -> ( 
                task.getTaskID().equals(taskId) && 
                task.getStartTime().equals( LocalTime.parse(
                startTime ))) ).count() != 0;
    }
    
    public static boolean isTaskExits(TimeLogger timelogger, DeleteTaskRB deleteTask){
        WorkMonth wm = timelogger.getMonths().stream().findFirst()
                        .filter( wmonth -> wmonth.getDate()
                        .getMonthValue() == deleteTask.getMonth() ).get();
        
        WorkDay wd = wm.getDays().stream().findFirst()
                .filter( wday -> wday.getActualDay().getDayOfMonth() 
                == deleteTask.getDay() ).get();
        
        return isTaskExits(wd, deleteTask.getTaskId(), deleteTask.getStartTime());
    }
    
    public static Task createTask( String taskId, String comment, String startTime, String endTime ) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException,
                    NoTaskIdException, InvalidTaskIdException{
        Task task = Task.fromString(taskId, comment, startTime, endTime);
        return task;    
    }
    
//    public static Task modifyTask( ModifyTaskRB taskmodifyRB ) throws             Talán nem is kell ez
//            NotExpectedTimeOrderException, EmptyTimeFieldException, 
//            NoTaskIdException, InvalidTaskIdException{
//        
//        Task task = Task.fromString(taskmodifyRB.getNewTaskId(), 
//                                    taskmodifyRB.getNewComment(), 
//                                    taskmodifyRB.getNewStartTime(), 
//                                    taskmodifyRB.getNewEndTime());
//        return task;    
//    }
    
    
}
