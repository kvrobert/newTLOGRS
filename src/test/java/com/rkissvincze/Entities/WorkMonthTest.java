/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Entities;

import com.rkissvincze.Exceptions.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rkissvincze
 */
public class WorkMonthTest {
    
    public WorkMonthTest() {
    }

    @Test
    public void workDayTest1() throws NotExpectedTimeOrderException, 
            EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, 
            NotSeparatedTimesException, WeekendNotEnabledException, 
            NotNewDateException, NotTheSameMonthException{
    
        Task tsk1 = Task.fromString("4556", "Helloka", "07:30", "08:45");
        WorkDay wd1 =  WorkDay.fromNumbers(420, 2017, 11, 10);
        wd1.addTask(tsk1);
        
        WorkDay wd2 = WorkDay.fromNumbers(420, 2017, 11, 2);
        Task tsk2 = Task.fromString("5656", "valami", "08:45", "09:45");
        wd2.addTask(tsk2);
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        wm.addWorkDay(wd1);
        wm.addWorkDay(wd2);
        
        long expResult = 135;
        long Result = wm.getSumPerMonth();
        assertEquals(expResult, Result);
    }
    
    @Test
    public void getSumPerMonthWithEmptyData() throws NotExpectedTimeOrderException, 
            EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, 
            NotSeparatedTimesException{
    
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        long expResult = 0;
        long Result = wm.getSumPerMonth();
        assertEquals(expResult, Result);
    }
    
    @Test
    public void getExtraMinPerMonthTest() throws NotExpectedTimeOrderException, 
            EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, NotSeparatedTimesException, 
            WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException{
    
        Task tsk1 = Task.fromString("4556", "Helloka", "07:30", "08:45");
        WorkDay wd1 =  WorkDay.fromNumbers(420, 2017, 11, 6);
        wd1.addTask(tsk1);
        
        WorkDay wd2 = WorkDay.fromNumbers(420, 2017, 11, 2);
        Task tsk2 =  Task.fromString("5656", "valami", "08:45", "09:45");
        wd2.addTask(tsk2);
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        wm.addWorkDay(wd1);
        wm.addWorkDay(wd2);
        
        long expResult = -705;
        long Result = wm.getExtraMinPerMonth();
        assertEquals(expResult, Result);
    }
    
     @Test
    public void getExtraMInPerMOntWithEmptyMonth() throws 
            NotExpectedTimeOrderException, EmptyTimeFieldException, 
            InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, 
            NotSeparatedTimesException{
    
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        long expResult = 0;
        long Result = wm.getExtraMinPerMonth();
        assertEquals(expResult, Result);
    }
    
    @Test
    public void getRequiredminPerMOnth() throws NotExpectedTimeOrderException, 
            EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, 
            NotSeparatedTimesException, WeekendNotEnabledException, 
            NotNewDateException, NotTheSameMonthException{
    
        Task tsk1 = Task.fromString("4556", "Helloka", "07:30", "08:45");
        WorkDay wd1 = WorkDay.fromNumbers(420, 2017, 11, 6);
        wd1.addTask(tsk1);
        
        WorkDay wd2 = WorkDay.fromNumbers(420, 2017, 11, 2);
        Task tsk2 = Task.fromString("5656", "valami", "08:45", "09:45");
        wd2.addTask(tsk2);
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        wm.addWorkDay(wd1);
        wm.addWorkDay(wd2);
        
        long expResult = 840;
        long Result = wm.getRequiredMinPerMonth();
        assertEquals(expResult, Result);
    }
    
    @Test
    public void getRequiredminPerMOnth2() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, NegativeMinutesOfWorkException, FutureWorkException, NotSeparatedTimesException{
    
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        long expResult = 0;
        long Result = wm.getRequiredMinPerMonth();
        assertEquals(expResult, Result);
    }
    
    @Test
    public void getSumPerMonth_getSUmPerMIn() throws NotExpectedTimeOrderException, 
            EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, 
            NotSeparatedTimesException, WeekendNotEnabledException, 
            NotNewDateException, NotTheSameMonthException{
    
        Task tsk1 = Task.fromString("4556", "Helloka", "07:30", "08:45");
        WorkDay wd1 = WorkDay.fromNumbers(420, 2017, 11, 8);
        wd1.addTask(tsk1);
        
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        wm.addWorkDay(wd1);
        assertEquals(wd1.getSumPerDay(), wm.getSumPerMonth());
        assertTrue(wd1.getSumPerDay() == wm.getSumPerMonth());
    }
    
    @Test
    public void getSumPerMonth_getSUmPerMInWithWeekenEnebled() throws 
            NotExpectedTimeOrderException, EmptyTimeFieldException, 
            InvalidTaskIdException, NoTaskIdException, NegativeMinutesOfWorkException, 
            FutureWorkException, NotSeparatedTimesException, WeekendNotEnabledException, 
            NotNewDateException, NotTheSameMonthException{
    
        Task tsk1 = Task.fromString("4556", "Helloka", "07:30", "08:45");
        WorkDay wd1 = WorkDay.fromNumbers(420, 2016, 8, 28);
        wd1.addTask(tsk1);
        
        WorkMonth wm =  WorkMonth.fromNumbers(2016, 8);
        wm.addWorkDay(wd1, true);
        assertEquals(wd1.getSumPerDay(), wm.getSumPerMonth());
        assertTrue(wd1.getSumPerDay() == wm.getSumPerMonth());
    }
    
    @Test(expected = WeekendNotEnabledException.class)
    public void getSumPerMonth_getSUmPerMInWithWeekenFalse() throws 
            NotExpectedTimeOrderException, EmptyTimeFieldException, 
            InvalidTaskIdException, NoTaskIdException, NegativeMinutesOfWorkException, 
            FutureWorkException, NotSeparatedTimesException, WeekendNotEnabledException, 
            NotNewDateException, NotTheSameMonthException{
    
        Task tsk1 = Task.fromString("4556", "Helloka", "07:30", "08:45");
        WorkDay wd1 = WorkDay.fromNumbers(420, 2016, 8, 28);
        wd1.addTask(tsk1);
        WorkMonth wm = WorkMonth.fromNumbers(2016, 8);
        wm.addWorkDay(wd1);
    }
    
    @Test(expected = NotNewDateException.class)
    public void NotNewDateException() throws NotExpectedTimeOrderException, 
            EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, NotSeparatedTimesException, 
            WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException{
    
        WorkDay wd1 = WorkDay.fromNumbers(420, 2017, 11, 8);        
        WorkDay wd2 = WorkDay.fromNumbers(420, 2017, 11, 8);        
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        wm.addWorkDay(wd1);
        wm.addWorkDay(wd2);
        System.out.println();
    }   
    
    @Test(expected = NotTheSameMonthException.class)
    public void NotTheSameMonthException () throws NotExpectedTimeOrderException, 
            EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, NotSeparatedTimesException, 
            WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException{
    
        WorkDay wd1 = WorkDay.fromNumbers(420, 2017, 11, 8);        
        WorkDay wd2 = WorkDay.fromNumbers(420, 2017, 10, 10);        
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        wm.addWorkDay(wd1);
        wm.addWorkDay(wd2);
        System.out.println();
    }
    
    @Test(expected = EmptyTimeFieldException.class)
    public void emptyTimeFiledException1() throws NotExpectedTimeOrderException, 
            EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, 
            NotSeparatedTimesException, WeekendNotEnabledException, 
            NotNewDateException, NotTheSameMonthException{
    
        Task tsk1 = Task.fromString("4545");
        WorkDay wd1 = WorkDay.fromNumbers(420, 2016, 8, 11);
        wd1.addTask(tsk1);
        WorkMonth wm = WorkMonth.fromNumbers(2016, 8);
        wm.addWorkDay(wd1);
    }
    
    @Test(expected = EmptyTimeFieldException.class)
    public void emptyTimeFiledException2() throws NotExpectedTimeOrderException, 
            EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, 
            NotSeparatedTimesException, WeekendNotEnabledException, 
            NotNewDateException, NotTheSameMonthException{
    
        Task tsk1 = Task.fromString("4545", "valami", "15:15");
        WorkDay wd1 = WorkDay.fromNumbers(420, 2016, 8, 11);
        wd1.addTask(tsk1);
        WorkMonth wm = WorkMonth.fromNumbers(2016, 8);
        wm.addWorkDay(wd1);
    }
    
    @Test(expected = EmptyTimeFieldException.class)
    public void emptyTimeFiledException3() throws NotExpectedTimeOrderException, 
            EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, 
            NegativeMinutesOfWorkException, FutureWorkException, NotSeparatedTimesException, 
            WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException{
    
        Task tsk1 = Task.fromString("4545", "valami", "15:15", "16:00");
        WorkDay wd1 = WorkDay.fromNumbers(420, 2016, 8, 11);
        wd1.addTask(tsk1);
        WorkMonth wm = WorkMonth.fromNumbers(2016, 8);
        wm.addWorkDay(wd1);
        tsk1.setEndTime("");
    }
}
