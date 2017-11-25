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
public class TimeLoggerTest {
    
    public TimeLoggerTest() {
    }

    /**
     * Test of getMonths method, of class TimeLogger.
     */
    @Test
    public void testGetSumPErMonths() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException, WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException {
        
        Task task1 = Task.fromStringAndIntDate("5642", "aaa", 7, 30, 10, 30);
        WorkDay wd = new WorkDay();
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        wd.addTask(task1);
        wm.addWorkDay(wd,true);
        
        TimeLogger tm = new TimeLogger();
        tm.addMonth(wm);
        assertTrue(task1.getMinPerTask() == tm.getMonths().get(0).getSumPerMonth());       
    }
    
    @Test(expected = NotNewDateException.class)
    public void NotNewMonthException() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException, NotSeparatedTimesException, WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException {
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        WorkMonth wm2 = WorkMonth.fromNumbers(2017, 11);
        
        TimeLogger tm = new TimeLogger();
        tm.addMonth(wm);
        tm.addMonth(wm2);
    }    
}
