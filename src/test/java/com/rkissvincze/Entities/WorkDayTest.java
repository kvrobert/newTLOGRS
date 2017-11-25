/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Entities;

import com.rkissvincze.Exceptions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author rkissvincze
 */
public class WorkDayTest {

    /**
     * Test of getSumPerDay method, of class WorkDay.
     */
    @Test
    public void testGetExtraMinutesPerDay() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException {
        
        Task t1 =  Task.fromString("4545", "Valami task" , "07:30", "08:45");
        WorkDay wd1 = new WorkDay();
        wd1.addTask(t1);
        long exRes = -375;
        long result = wd1.getExtraMinPerDay();
        assertEquals(exRes, result);
    }
    
    @Test
    public void testGetExtraMinutesPerDay2() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("4545", "Valami task" , "07:30", "08:45");
        WorkDay wd1 = new WorkDay();        
        long result = wd1.getExtraMinPerDay();
        assertTrue(result < 0);
        assertTrue(result == -450);  
    }
    
    @Test(expected = NegativeMinutesOfWorkException.class)
    public void setRequiredMinPerDay () throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NegativeMinutesOfWorkException {
        Task t1 =  Task.fromString("4545", "Valami task" , "07:30", "08:45");
        WorkDay wd1 = new WorkDay();   
        wd1.setRequiredMinPerDay(-300);
    }
    
    @Test(expected = NegativeMinutesOfWorkException.class)
    public void setRequiredMinPerDay2 () throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NegativeMinutesOfWorkException, FutureWorkException {
        Task t1 = Task.fromString("4545", "Valami task" , "07:30", "08:45");
        WorkDay wd1 =  WorkDay.fromString("-300", "20151112"); 
        }
    @Test(expected = FutureWorkException.class)
    public void FutureDayException () throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NegativeMinutesOfWorkException, FutureWorkException {
        WorkDay wd1 = new WorkDay();   
        wd1.setActualDay(2018, 11, 12);
    }
    
    @Test(expected = FutureWorkException.class)
    public void FutureDayException2 () throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NegativeMinutesOfWorkException, FutureWorkException {
        WorkDay wd1 = WorkDay.fromString("350", "20191112");  
    }
    
    @Test
    public void testGetSumPerDay() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException {
        Task t1 = Task.fromString("4545", "Valami task" , "07:30", "08:45");
        Task t2 = Task.fromString("4545", "Valami task" , "08:45", "09:45");
        WorkDay wd1 = new WorkDay();
        wd1.addTask(t1);
        wd1.addTask(t2);
        long Expresult = 135;
        long result = wd1.getSumPerDay();
        assertEquals(Expresult, result);        
    }
    
    @Test
    public void getSumDay() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException {
        WorkDay wd1 = new WorkDay();
        long Expresult = 0;
        long result = wd1.getSumPerDay();
        assertEquals(Expresult, result);        
    }
    
    @Test
    public void endTimeOfLastTask() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException {
        Task t1 = Task.fromString("4545", "Valami task" , "07:30", "08:45");
        Task t2 = Task.fromString("4545", "Valami task" , "09:30", "11:45");
        WorkDay wd1 = new WorkDay();
        wd1.addTask(t1);
        wd1.addTask(t2);
        LocalTime Expresult = LocalTime.of(11, 45);
        LocalTime result = wd1.getLastTaskEndTime();
        assertEquals(Expresult, result);        
    }
    
    @Test
    public void endTimeOfLastTask2() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException {
        WorkDay wd1 = new WorkDay();
        LocalTime Expresult = null;
        LocalTime result = wd1.getLastTaskEndTime();
        assertEquals(Expresult, result);        
    }
    
    @Test(expected = NotSeparatedTimesException.class)
    public void notSeparatedTimeException1() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException {
        Task t1 = Task.fromString("4545", "Valami task" , "07:30", "08:45");
        Task t2 = Task.fromString("4545", "Valami task" , "08:30", "09:45");
        WorkDay wd1 = new WorkDay();
        wd1.addTask(t1);
        wd1.addTask(t2);
    }
    
    @Test
    public void workDayCreation1() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException {
        WorkDay wd1 = new WorkDay();
        LocalDate expResult = LocalDate.now();
        LocalDate Result = wd1.getActualDay();
        long expResultLong = 450;  
        long ResultLong = wd1.getRequiredMinPerDay();  
        assertEquals(expResult, Result);
        assertEquals(expResultLong, ResultLong);
    }
    
    
    @Test
    public void workDayCreation2() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException, NegativeMinutesOfWorkException, FutureWorkException {
        WorkDay wd1 = WorkDay.fromString("300", "20151212");
        LocalDate expResult = LocalDate.of(2015, 12, 12);
        LocalDate Result = wd1.getActualDay();
        long expResultLong = 300;  
        long ResultLong = wd1.getRequiredMinPerDay();  
        assertEquals(expResult, Result);
        assertEquals(expResultLong, ResultLong);
    }
    
    @Test
    public void workDayCreation3() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException, NegativeMinutesOfWorkException, FutureWorkException {
        WorkDay wd1 =  WorkDay.fromNumbers(300, 2015, 12, 12);
        LocalDate expResult = LocalDate.of(2015, 12, 12);
        LocalDate Result = wd1.getActualDay();
        long expResultLong = 300;  
        long ResultLong = wd1.getRequiredMinPerDay();  
        assertEquals(expResult, Result);
        assertEquals(expResultLong, ResultLong);
    }
    
    @Test
    public void workDaySetter() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException, NegativeMinutesOfWorkException, FutureWorkException {
        WorkDay wd1 =  WorkDay.fromNumbers(300, 2015, 12, 12);
        
        wd1.setActualDay(2012, 10, 9);
        wd1.setRequiredMinPerDay(432);
        
        LocalDate expResult = LocalDate.of(2012, 10, 9);
        LocalDate Result = wd1.getActualDay();
        long expResultLong = 432;  
        long ResultLong = wd1.getRequiredMinPerDay();  
        assertEquals(expResult, Result);
        assertEquals(expResultLong, ResultLong);
    }
    
    @Test(expected = EmptyTimeFieldException.class)
    public void workDayTaskTest() throws NotSeparatedTimesException, EmptyTimeFieldException{
        Task ts1 = Task.fromString("1234");
        WorkDay wd = new WorkDay();
        wd.addTask(ts1);
        wd.getSumPerDay();
        ts1.getMinPerTask();
    }
    
    @Test(expected = NotSeparatedTimesException.class)
    public void testNonSeparateTimeEx() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException {
        Task t1 = Task.fromString("4545", "Valami task" , "08:45", "09:50");
        Task t2 = Task.fromString("4545", "Valami task" , "08:20", "08:45");
        WorkDay wd1 = new WorkDay();
        wd1.addTask(t1);
        System.out.println("t1: " + t1.toString());
        System.out.println("t2: " + t2.toString());
        wd1.addTask(t2);            
        
    }
    
}
