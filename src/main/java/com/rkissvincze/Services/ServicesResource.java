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
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author rkissvincze
 */
public class ServicesResource {
    
    public static boolean isMonthExits( TimeLogger timelogger, int year, int month){
        
        System.out.println("Enter the is month exists...");
        System.out.println("The first MOnth..." + timelogger.getMonths().get(0));
        System.out.println("The Second MOnth..." + timelogger.getMonths().get(1));
        
        System.out.println("The last MOnth...getDate" + timelogger.getMonths().get(timelogger.getMonths().size()-1).getDate());
        System.out.println("The last MOnth...getMonthDate" + timelogger.getMonths().get(timelogger.getMonths().size()-1).getMonthDate());
        System.out.println("The last YeraMont Parser...:" + YearMonth.parse(timelogger.getMonths().get(timelogger.getMonths().size()-1).getMonthDate()));
        return !timelogger.isNewMonth( WorkMonth.fromNumbers(year, month) );
    }
    
    public static WorkMonth createWorkMonth(int year, int month){
        return WorkMonth.fromNumbers(year, month);
    }
    
    public static boolean isDayExits(TimeLogger timeLogger, int year, int month, int day){
//        if( !isMonthExits(timeLogger, year, month) ) return false;
//        WorkMonth workMonth = timeLogger.getMonths().stream().findFirst()
//                .filter(wm -> wm.getDate().getMonthValue() == month ).get();
        
        if( !isMonthExits(timeLogger, year, month) ) return false;
        int firstElement = 0;
        String yearMonth = year + "-" + month;
        System.out.println("getTheMonth-ból.." + yearMonth );
        List<WorkMonth> monthSelected =  timeLogger.getMonths().stream()
                .filter(wm -> wm.getMonthDate().equals( yearMonth )) 
                .collect(Collectors.toList());
        WorkMonth workMonth =  monthSelected.get(firstElement); 
        
        return workMonth.getDays().stream().filter( 
                wd -> wd.getActualDay().equals( 
                LocalDate.of(year, month, day)) ).count() != 0;
    }
    
    public static WorkDay createWorkDay( int requiredMin, int year, int month, int day )            
            throws NegativeMinutesOfWorkException, FutureWorkException{
        System.out.println("CREATE WORKDAY....in Service");
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
    
    public static void updateWorkDayAndWorkMonthStatistic(WorkDay WorkDay, WorkMonth workMonth) throws EmptyTimeFieldException{
        WorkDay.getSumPerDay();
        WorkDay.getExtraMinPerDay();
        
        workMonth.getSumPerMonth();
        workMonth.getRequiredMinPerMonth();
        workMonth.getExtraMinPerMonth();
    }
    public static void updateTaskStatistic(Task task) throws EmptyTimeFieldException{
        task.getMinPerTask();
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
