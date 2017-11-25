package com.rkissvincze.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.rkissvincze.Exceptions.EmptyTimeFieldException;
import com.rkissvincze.Exceptions.NotSeparatedTimesException;
import com.rkissvincze.Exceptions.NegativeMinutesOfWorkException;
import com.rkissvincze.Exceptions.FutureWorkException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author rkissvincze
 */
@Slf4j
@Entity
@Getter
@Setter
@JsonRootName("WorkDays")
public class WorkDay {
    
    @Id
    @GeneratedValue
    private int id;
    
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();
    
    private long requiredMinPerDay = (long) (7.5 * 60);
    @JsonProperty("WorkDay")
    private LocalDate actualDay = LocalDate.now();
    
    private long sumPerDay;
    
    private long extraMinPerDay;
    
    
    public WorkDay(){}
    
    private WorkDay(long reqiredMinPerDay, LocalDate actualDay) throws NegativeMinutesOfWorkException, FutureWorkException{
        this.requiredMinPerDay = reqiredMinPerDay;
        this.actualDay = actualDay;
        
        if( reqiredMinPerDay < 0 ) throw new NegativeMinutesOfWorkException();
        if( Util.isFutureDay(this) ) throw new FutureWorkException();
    }
    
    public static WorkDay fromNumberAndLocalDate(long reqiredMinPerDay, LocalDate actualDay) throws NegativeMinutesOfWorkException, FutureWorkException{
        return new WorkDay( reqiredMinPerDay, actualDay );    
    }
      
    private WorkDay(long requiredMinperDay, int year, int month, int day) throws NegativeMinutesOfWorkException, FutureWorkException{            
        this(requiredMinperDay, LocalDate.of(year, month, day) );
    }
    public static WorkDay fromNumbers( long requiredMinperDay, int year, int month, int day ) throws NegativeMinutesOfWorkException, FutureWorkException{
        return new WorkDay( requiredMinperDay, year, month, day );
    }
    
    private WorkDay(String requiredMinperDay, String actualDay) throws NegativeMinutesOfWorkException, FutureWorkException{
        
        this( Long.parseLong(requiredMinperDay), LocalDate.parse(actualDay, DateTimeFormatter.BASIC_ISO_DATE) );
    }
    
    public static WorkDay fromString( String requiredMinperDay, String actualDay ) throws NegativeMinutesOfWorkException, FutureWorkException{
        return new WorkDay( requiredMinperDay, actualDay );
    }    

    public long getSumPerDay() throws EmptyTimeFieldException {
        
       long summ = 0;  
       for( Task task : tasks )
       {
           summ += task.getMinPerTask();
       }
       sumPerDay = summ;
       return sumPerDay;
    }    
       
    public long getExtraMinPerDay() throws EmptyTimeFieldException{
            
        extraMinPerDay = getSumPerDay() - getRequiredMinPerDay();
        return extraMinPerDay;
    }
    
    protected LocalTime getLastTaskEndTime(){
        
        Task lastTask;
        if( tasks.size() > 0 ) {
            lastTask = tasks.get(0);        
            for(Task tsk : tasks){
            if( tsk.getStartTime().isAfter(lastTask.getStartTime()) ) lastTask = tsk;
            }
            return lastTask.getEndTime();
        }    
        return null;    // not elegant..yet..  epoch date is better...
    }

    public void setRequiredMinPerDay(long requiredMinPerDay) 
            throws NegativeMinutesOfWorkException {
        if( requiredMinPerDay < 0 ) 
            throw new NegativeMinutesOfWorkException("The required daily minute musn't be negative"); 
        this.requiredMinPerDay = requiredMinPerDay;
    }

    public void setActualDay(int year, int month, int day) throws FutureWorkException {
        this.actualDay = LocalDate.of(year, month, day);
        
        if( Util.isFutureDay(this) ) throw new FutureWorkException();
    }          
    
    public void addTask(Task task) throws NotSeparatedTimesException, EmptyTimeFieldException{
        
        if( Util.isSeparatedTime(task, this) ) { System.out.println(Util.isSeparatedTime(task, this));
                throw new NotSeparatedTimesException("The start time of " + "" 
                        + "must be after the last task end time" );
        } 
        if( Util.isMultipleQuarterHour(task)){
            tasks.add(task);
            log.info("Added Task...");
            sumPerDay = 0;
            extraMinPerDay = 0;
        }         
    }

    @Override
    public String toString() {
        return "WorkDay: " + actualDay + ", requ: " + requiredMinPerDay + ", sum: " + sumPerDay;
    }
    
    
}
