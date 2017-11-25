/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rkissvincze.Exceptions.EmptyTimeFieldException;
import com.rkissvincze.Exceptions.InvalidTaskIdException;
import com.rkissvincze.Exceptions.NoTaskIdException;
import com.rkissvincze.Exceptions.NotExpectedTimeOrderException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;


/**
 *
 * @author rkissvincze
 */
@Entity
@Getter
@Setter
public class Task {
    
    @Id
    @GeneratedValue
    private int id;
    
    @JsonProperty
    private String taskID;
    @JsonProperty
    private LocalTime startTime;
    @JsonProperty
    private LocalTime endTime;
    @JsonProperty
    private String comment;
    
    private long minPerTask;
    
   
    public Task(String taskId, String comment, LocalTime startTime, LocalTime endtime) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException{
                    
        this.taskID = taskId;
        this.comment = comment;
        this.startTime = startTime;
        this.endTime = endtime; 
        if( Util.isTimeNull(this) ) throw  new EmptyTimeFieldException(" Timefield is empty, must fill out by " + this.toString() );
        if( !Util.isCorrectTimeOrder(this) ) throw new NotExpectedTimeOrderException(" The start time must be before the endtime ");
        if( !this.isValidTaskId() ) throw new InvalidTaskIdException("Invalid task-ID by " + this.toString() + "task. It must be in 1234 or LT-1234 form");
        if( this.getTaskID() == null || this.getTaskID().equals("") ) throw new NoTaskIdException();
        if( !Util.isMultipleQuarterHour(this) ) {
        
        this.endTime =  Util.roundToMultipleQuarterHour(this.getStartTime(), this.getEndTime());
        }
    }
    
    private Task(String taskId, String comment, int startHour, int startMin, int endHour, int endMin) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException{        
    
        this( taskId, comment, LocalTime.of(startHour, startMin), LocalTime.of(endHour, endMin));
    }
    
    public static Task fromStringAndIntDate(String taskId, String comment, int startHour, int startMin, int endHour, int endMin) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException{
        return new Task( taskId,  comment,  startHour,  startMin,  endHour,  endMin);
    }
    
    private Task(String taskId, String commnet, String startTime, String endTime) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException{
        
        this( taskId, commnet, LocalTime.parse(startTime, DateTimeFormatter.ISO_TIME), LocalTime.parse(endTime, DateTimeFormatter.ISO_TIME)  );        
    }
    
    public static Task fromString(String taskId, String commnet, String startTime, String endTime) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, NoTaskIdException, InvalidTaskIdException {
        return new Task( taskId, commnet, startTime, endTime );
    }
    
    private Task(String taskId, String commnet, String startTime) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException{
        
        this( taskId, commnet, LocalTime.parse(startTime, DateTimeFormatter.ISO_TIME), null  );        
    }
    
    public static Task fromString( String taskId, String commnet, String startTime ) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException{
        return new Task( taskId, commnet, startTime );
    }
    
    private Task(String taskId, String commnet, LocalTime startTime) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException{
        
        this( taskId, commnet, startTime, null  );
    }
    
    public static Task fromStringAndLocalDate(String taskId, String commnet, LocalTime startTime) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, NoTaskIdException{
        return new Task( taskId, commnet, startTime);
    }
    
    private Task(String taskId){    
        this.taskID = taskId;
    }
    
    public static Task fromString(String taskId){
        return new Task( taskId );
    }

    public long getMinPerTask() throws EmptyTimeFieldException{
        
        if( startTime == null && endTime == null ) throw new EmptyTimeFieldException();
            
        minPerTask = ChronoUnit.MINUTES.between(startTime, endTime);
        return minPerTask;
        
    }

    public void setTaskID(String taskID) throws InvalidTaskIdException {
        this.taskID = taskID;
        if( !this.isValidTaskId() ) 
            throw new InvalidTaskIdException("Invalid task ID is of " + 
                    this.toString() + "task. It must be in 1234 or LT-1234 form");
    }

    public void setStartTime(LocalTime startTime) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException {
        
        this.startTime = startTime;
        if( !Util.isCorrectTimeOrder(this) ) 
            throw new NotExpectedTimeOrderException("The start time must be before the endtime ");
    }
    
    public void setStartTime(int hour, int minnute) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException {
                
        this.setStartTime(LocalTime.of(hour, minnute));
    }
    
    public void setStartTime(String startTime) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException {
        
        if( endTime.equals("") ) throw new EmptyTimeFieldException();
        this.setStartTime(LocalTime.parse(startTime, DateTimeFormatter.ISO_TIME));
    }
            
    public void setEndTime(LocalTime endTime) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException {
        this.endTime = endTime;
        if( !Util.isCorrectTimeOrder(this) ) 
            throw new NotExpectedTimeOrderException("The start time must be before the endtime ");
    }
    
    public void setEndTime(int hour, int minnute) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException {
        
        this.setEndTime(LocalTime.of(hour, minnute));
    }
    
    public void setEndTime(String endTime) 
            throws NotExpectedTimeOrderException, EmptyTimeFieldException {
        
        if( endTime.equals("") ) throw new EmptyTimeFieldException();
        this.setEndTime(LocalTime.parse(endTime, DateTimeFormatter.ISO_TIME));
    }
          
    protected boolean isValidTaskId(){
    
        return taskID.matches("\\d{4}") || taskID.matches("LT-\\d{4}");
    }

    @Override
    public String toString() {
        return "Task: " + taskID + ", " + comment + ", " + startTime + ", " + endTime ;
    }
    
    
}
