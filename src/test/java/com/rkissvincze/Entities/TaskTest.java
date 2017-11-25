/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Entities;

import com.rkissvincze.Exceptions.*;
import java.time.LocalTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rkissvincze
 */
public class TaskTest {
    
    public TaskTest() {
    }
    
    /**
     * Test of getTaskID method, of class Task.
     * @throws com.timelogger.NotExpectedTimeOrderException
     * @throws com.timelogger.EmptyTimeFieldException
     */
    @Test (expected = NotExpectedTimeOrderException.class)
    public void testConstructor_NotExpectedTimeOrderException() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 = Task.fromString("", "", "08:45", "07:30");             
    }
    
    
    /**
     * Test of getTaskID method, of class Task.
     * @throws com.timelogger.NotExpectedTimeOrderException
     * @throws com.timelogger.EmptyTimeFieldException
     */
    @Test (expected = EmptyTimeFieldException.class)
    public void testConstructor_EmptyTimeFieldException() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("4545", "Valami test Task", "08:45");             
    }
    
    @Test 
    public void test_getMinPerTask() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("4545", "Valami test Task", "07:30", "08:45"); 
        long expectedResult = 75;
        long result = t1.getMinPerTask();
        assertEquals(expectedResult, result);
    }
    
    @Test (expected = InvalidTaskIdException.class)
    public void testConstructor_InvalidTaskIdException() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("154858", "Valami test Task", "07:30", "08:45"); 
    }
    
    @Test (expected = InvalidTaskIdException.class)
    public void testConstructor_InvalidTaskIdException2() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("154858", "Valami test Task", "07:30", "08:45"); 
        t1.setTaskID("454545");
    }    
    
    @Test (expected = InvalidTaskIdException.class)
    public void testConstructor_InvalidTaskIdException3() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("LT-154858", "Valami test Task", "07:30", "08:45"); 
    }
    
    @Test (expected = InvalidTaskIdException.class)
    public void testConstructor_InvalidTaskIdException4() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("LT-154858", "Valami test Task", "07:30", "08:45");
        t1.setTaskID("LT-");
    }
    
    @Test 
    public void test_getComment1() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("4545", null , "07:30", "08:45"); 
        String expectedResult = null;
        String result = t1.getComment();
        assertNull(result);
    }
    
    @Test 
    public void test_getComment2() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("4545", "" , "07:30", "08:45"); 
        String expectedResult = "";
        String result = t1.getComment();
        assertEquals(expectedResult, result);
    }
    
    @Test 
    public void test_roundMultipleQuarteHour1() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("4545", "Valami task" , "07:30", "07:50"); 
        LocalTime expectedResult = LocalTime.of(7, 45);
        LocalTime result = t1.getEndTime();
        assertEquals(expectedResult, result);
    }
    
    @Test 
    public void test_roundMultipleQuarteHour2() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("4545", "Valami task" , "07:13", "07:45"); 
        System.out.println(t1.getMinPerTask() + " minutes");
        assertTrue(t1.getMinPerTask() % 15 == 0);
    }
    
    @Test (expected = NotExpectedTimeOrderException.class)
    public void test_roundMultipleQuarteHour3() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("4545", "Valami task" , "07:13", "07:45");
        t1.setStartTime("07:56");
    }
   
    @Test (expected = NotExpectedTimeOrderException.class)
    public void test_roundMultipleQuarteHour4() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("4545", "Valami task" , "07:13", "07:45");
        t1.setEndTime("07:00");
    }
    
    @Test (expected = NotExpectedTimeOrderException.class)
    public void test_roundMultipleQuarteHour5() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException {
        Task t1 =  Task.fromString("4545", "Valami task" , "07:13", "07:45");
        t1.setEndTime(6, 0);
    }
    
    @Test
    public void setStartTime() throws NotExpectedTimeOrderException, NotExpectedTimeOrderException, EmptyTimeFieldException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException{
        Task t1 =  Task.fromString("4545", "Valami task" , "07:30", "07:45");
        t1.setStartTime("07:00");
        LocalTime exResult = LocalTime.of(7, 0);
        LocalTime Result = t1.getStartTime();
        assertEquals(exResult, Result);
    }
    
    @Test
    public void setStartTime2() throws NotExpectedTimeOrderException, NotExpectedTimeOrderException, EmptyTimeFieldException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException{
        Task t1 =  Task.fromString("4545", "Valami task" , "07:30", "07:45");
        t1.setEndTime("08:00");
        LocalTime exResult = LocalTime.of(8, 0);
        LocalTime Result = t1.getEndTime();
        assertEquals(exResult, Result);
    }
    
    @Test
    public void test_allTheFields() throws NotExpectedTimeOrderException, NotExpectedTimeOrderException, EmptyTimeFieldException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTaskIdException, NoTaskIdException{
        Task t1 =  Task.fromString("4545", "Valami task" , "07:30", "07:45");
        
        assertEquals("4545", t1.getTaskID());
        assertEquals("Valami task", t1.getComment());
        assertEquals("4545", t1.getTaskID());
        assertEquals(LocalTime.parse("07:30"), t1.getStartTime());
        assertEquals(LocalTime.parse("07:45"), t1.getEndTime());
    }
    
}
