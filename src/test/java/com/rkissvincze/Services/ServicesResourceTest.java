/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Services;

import com.rkissvincze.Beans.DeleteTaskRB;
import com.rkissvincze.Entities.Task;
import com.rkissvincze.Entities.TimeLogger;
import com.rkissvincze.Entities.WorkDay;
import com.rkissvincze.Entities.WorkMonth;
import com.rkissvincze.Exceptions.*;
import java.time.YearMonth;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author rkissvincze
 */
public class ServicesResourceTest {
    
    TimeLogger timelogger = new TimeLogger();
    
    
    
    public ServicesResourceTest() {
        
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test of isMonthExits method, of class ServicesResource.
     */
    @Test
    public void testIsMonthExits_EMptyTimelogger() {
        Assert.assertFalse(ServicesResource.isMonthExits(timelogger, 2017, 11));
    }
    
    @Test
    public void testIsMonthExits_Timelogger() throws NotNewDateException {
        WorkMonth workMonth = new WorkMonth();
        workMonth.setDate(YearMonth.of(2017, 11));
        timelogger.addMonth(workMonth);
        Assert.assertTrue(ServicesResource.isMonthExits(timelogger, 2017, 11));
    }
    /**
     * Test of createWorkMonth method, of class ServicesResource.
     */
    @Test
    public void testCreateWorkMonthInActualDate() {
        WorkMonth exceptWorkMonth = new WorkMonth();
        exceptWorkMonth.setDate(YearMonth.of(2017, 11));
        WorkMonth resultWorkMonth = ServicesResource.createWorkMonth(2017, 11);
        Assert.assertEquals(exceptWorkMonth.getDate(), resultWorkMonth.getDate());        
    }

    /**
     * Test of isDayExits method, of class ServicesResource.
     */
    @Test
    public void testIsDayExitsWithEmptyMOnth() {
        Assert.assertFalse(ServicesResource.isDayExits(timelogger, 2017, 11, 16));
    }
    @Test
    public void testIsDayExitsWithActualMOnth() {
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        Assert.assertFalse(ServicesResource.isDayExits(timelogger, 2017, 11, 16));        
    }
    @Test
    public void testIsDayExitsWithActualMOnthAndExitDay() throws 
            NegativeMinutesOfWorkException, FutureWorkException, 
            WeekendNotEnabledException, NotNewDateException, 
            NotTheSameMonthException{
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        timelogger.addMonth(wm);
        WorkDay wd = WorkDay.fromNumbers(450, 2017, 11, 16) ;
        wm.addWorkDay(wd);
        Assert.assertTrue(ServicesResource.isDayExits(timelogger, 2017, 11, 16));        
    }
    
    /**
     * Test of createWorkDay method, of class ServicesResource.
     */
    @Test
    public void testCreateWorkDay() throws Exception {
        WorkDay expectWorkDay = WorkDay.fromNumbers(450, 2017, 11, 15);
        WorkDay resultWorkDay = ServicesResource.createWorkDay( 450, 2017, 11, 15);
        Assert.assertEquals(expectWorkDay.getActualDay(), resultWorkDay.getActualDay());
        Assert.assertEquals(expectWorkDay.getSumPerDay(), resultWorkDay.getSumPerDay());
        Assert.assertEquals(expectWorkDay.getExtraMinPerDay(), resultWorkDay.getExtraMinPerDay());
    }

    /**
     * Test of isTaskExits method, of class ServicesResource.
     */
    @Test
    public void testIsTaskExits_3args() throws NotNewDateException, WeekendNotEnabledException, 
            NotTheSameMonthException, NegativeMinutesOfWorkException, 
            NegativeMinutesOfWorkException, FutureWorkException, 
            NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, 
            NoTaskIdException, NotSeparatedTimesException  {
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        timelogger.addMonth(wm);
        WorkDay wd = WorkDay.fromNumbers(450, 2017, 11, 16) ;
        wm.addWorkDay(wd);
        Assert.assertFalse(ServicesResource.isTaskExits(wd, "4546", "15:15"));
        // Added task..
        wd.addTask(Task.fromString("4548", "", "15:30", "16:30"));
        Assert.assertFalse(ServicesResource.isTaskExits(wd, "4546", "15:15"));
        // With same ID
        Assert.assertFalse(ServicesResource.isTaskExits(wd, "4548", "15:15"));
        // With same ID and startTime
        wd.addTask(Task.fromString("4548", "", "16:45", "17:30"));
        Assert.assertTrue(ServicesResource.isTaskExits(wd, "4548", "16:45"));
    }

    /**
     * Test of isTaskExits method, of class ServicesResource.
     */
    @Test
    public void testIsTaskExits_TimeLogger_DeleteTaskRB() throws 
            NotNewDateException, NegativeMinutesOfWorkException, FutureWorkException, 
            WeekendNotEnabledException, NotTheSameMonthException, 
            NotExpectedTimeOrderException, EmptyTimeFieldException, 
            NoTaskIdException, InvalidTaskIdException, NotSeparatedTimesException {
        WorkMonth wm = WorkMonth.fromNumbers(2017, 11);
        timelogger.addMonth(wm);
        WorkDay wd = WorkDay.fromNumbers(450, 2017, 11, 16) ;
        wm.addWorkDay(wd);
        
        DeleteTaskRB delTAsk = new DeleteTaskRB();
        delTAsk.setYear(2017);
        delTAsk.setMonth(11);
        delTAsk.setDay(16);
        delTAsk.setTaskId("4545");
        delTAsk.setStartTime("16:30");
        
        Assert.assertFalse( ServicesResource.isTaskExits(timelogger, delTAsk) );
        
        // Add task to WD
        wd.addTask(Task.fromString("4548", "", "15:30", "16:30"));
        Assert.assertFalse( ServicesResource.isTaskExits(timelogger, delTAsk) );
        // Add task with same date to WD
        wd.addTask(Task.fromString("4545", "", "16:30", "17:30"));
        Assert.assertTrue(ServicesResource.isTaskExits(timelogger, delTAsk) );
    }

    /**
     * Test of createTask method, of class ServicesResource.
     */
    @Test
    public void testCreateTask() throws Exception {
        Task esxpecTask = Task.fromString("4546", "", "15:45", "16:30");
        Task resTask = ServicesResource.createTask("4546", "", "15:45", "16:30");
        Assert.assertEquals(esxpecTask.getTaskID(), resTask.getTaskID());
        Assert.assertEquals(esxpecTask.getComment(), resTask.getComment());
        Assert.assertEquals(esxpecTask.getStartTime(), resTask.getStartTime());
        Assert.assertEquals(esxpecTask.getEndTime(), resTask.getEndTime());
        Assert.assertEquals(esxpecTask.getMinPerTask(), resTask.getMinPerTask());
    }
}
