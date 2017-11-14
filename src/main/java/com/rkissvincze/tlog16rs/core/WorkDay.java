package com.rkissvincze.tlog16rs.core;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author rkissvincze
 */
@Getter
@NoArgsConstructor
public class WorkDay {
    
    @Setter private List<Task> tasks = new ArrayList<>();
    @Setter private long requiredMinPerDay = (long) (7.5 * 60);
    @Setter private LocalDate actualDay = LocalDate.now();
    private long sumPerDay;        
    
    public long getSumPerDay() throws EmptyTimeFieldException {
        
       long summ = 0;
        if( sumPerDay != 0 ) return sumPerDay;
       // return tasks.stream().mapToLong( task -> task.getMinPerTask() ).sum();     Exception miatt nem ment....
       for( Task task : tasks )
       {
           summ += task.getMinPerTask();
       }
       sumPerDay = summ;
       return sumPerDay;
    }
    
    public long getExtraMinPerDay() throws EmptyTimeFieldException{    
        return getSumPerDay() - getRequiredMinPerDay();
    }
    
    public LocalTime getLastTaskEndTime(){
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
    
    public void addTask(Task task) throws NotSeparatedTimesException, EmptyTimeFieldException{
        
        if( Util.isSeparatedTime(task, this) ) { System.out.println(Util.isSeparatedTime(task, this));
                throw new NotSeparatedTimesException("The start time of " + "" 
                        + "must be after the last task end time" );
        } 
        if( Util.isMultipleQuarterHour(task)){
            tasks.add(task);
            sumPerDay = 0;
            return;
        }         
    }

    @Override
    public String toString() {
        return "WorkDay: " + actualDay + ", requ: " + requiredMinPerDay + ", sum: " + sumPerDay;
    }
}
