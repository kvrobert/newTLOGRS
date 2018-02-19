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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author rkissvincze
 */
public class ServicesResource {
    
    public static boolean isMonthExits( TimeLogger timelogger, int year, int month){
        
        System.out.println("Enter the is month exists...");        
        return !timelogger.isNewMonth( WorkMonth.fromNumbers(year, month) );
    }
    
    public static WorkMonth createWorkMonth(int year, int month){
        return WorkMonth.fromNumbers(year, month);
    }
    
    public static boolean isDayExits(TimeLogger timeLogger, int year, int month, int day){
        
        if( !isMonthExits(timeLogger, year, month) ) return false;
        int firstElement = 0;
       // String yearMonth = year + "-" + month;
        System.out.println("isDay existbol most a keresett nap adatai: " +year+month+day); 
       String yearMonth = YearMonth.of(year, month).toString();
        
        System.out.println("getTheMonth-ból.." + yearMonth );
        List<WorkMonth> monthSelected =  timeLogger.getMonths().stream()
                .filter(wm -> wm.getMonthDate().equals( yearMonth )) 
                .collect(Collectors.toList());
        WorkMonth workMonth =  monthSelected.get(firstElement); 
        System.out.println("getTheMonth-ból..a MEGtalált WM" + workMonth );
       // System.out.println("a megtalált hónap eslő napjának adatai..:" + workMonth.getDays().get(0).getActualDay());
        
        List<WorkDay> wdays = workMonth.getDays().stream().filter( wd -> wd.getActualDay().getDayOfMonth() == day ).collect(Collectors.toList());
        System.out.println("a Napok listájának mérete:" +wdays.size());
        
        return workMonth.getDays().stream().filter( 
                wd -> wd.getActualDay().equals( 
                LocalDate.of(year, month, day)) ).count() != 0;
    }
    
    public static WorkDay createWorkDay( int requiredMin, int year, int month, int day ){
        
        System.out.println("CREATE WORKDAY....in Service");
        try {        
            return WorkDay.fromNumbers( requiredMin, year, month, day);
        } catch (NegativeMinutesOfWorkException | FutureWorkException ex) {
            System.out.println(" HIBA A WD készítésnél.... ");
            Logger.getLogger(ServicesResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build()); 
        } 
    }
    
    public static boolean isTaskExits( WorkDay workday, String taskId, String startTime){        
        System.out.println("IsTask exist....WD-jjel...adatok..WD: "+ workday.toString() +"  tsk id:"+taskId +" startime: " +startTime);
        
        System.out.println("Parsolt LocalTIme: " + LocalTime.parse(startTime));
        //System.out.println("1 db WD LocalTImeje: " + workday.getTasks().get(0).getStartTime());
        return workday.getTasks().stream().filter( task -> ( 
                task.getTaskID().equals(taskId) && 
                task.getStartTime().equals( LocalTime.parse(
                startTime ))) ).count() != 0;
    }
    
    public static boolean isTaskExits(TimeLogger timelogger, DeleteTaskRB deleteTask){
                
        List<WorkMonth> wmonts = timelogger.getMonths().stream()
                        .filter( wms -> YearMonth.parse( wms.getMonthDate() ).getMonthValue() == deleteTask.getMonth() &&
                                YearMonth.parse( wms.getMonthDate() ).getYear() == deleteTask.getYear()  ).collect(Collectors.toList());
        System.out.println("From isTask exixt...WM size = " + wmonts.size() +" és adaotok: " + wmonts.toString());
        if ( wmonts.isEmpty() ){ System.out.println("WM Wmpty..."+wmonts.size() ); return false;}
        WorkMonth wm = wmonts.get(0); // the first element 
        
         List <WorkDay> workdays = wm.getDays().stream()
                .filter( wday -> wday.getActualDay().getDayOfMonth() 
                == deleteTask.getDay() ).collect(Collectors.toList());
        System.out.println("From isTask exixt...WD size = " + workdays.size() + "és adatok: "+ workdays.toString());
        if( workdays.isEmpty() ) { System.out.println("WD Wmpty..."+workdays.size()); return false; } 
        WorkDay wd = workdays.get(0); // the first element*/
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
    
    public static void updateWorkMonthStatistic(WorkMonth workMonth) throws EmptyTimeFieldException {
               
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
