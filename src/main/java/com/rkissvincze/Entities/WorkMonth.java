/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rkissvincze.Exceptions.WeekendNotEnabledException;
import com.rkissvincze.Exceptions.EmptyTimeFieldException;
import com.rkissvincze.Exceptions.NotTheSameMonthException;
import com.rkissvincze.Exceptions.NotNewDateException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rkissvincze
 */

@Entity
@Getter
@Setter
public class WorkMonth { 
    
    @Id
    @GeneratedValue
    private int id;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkDay> days = new ArrayList<>();
    
    @JsonProperty("WorkMonth")
    @Transient
    private YearMonth date = YearMonth.now();
    @Column(name = "date")
    private String monthDate;
    
    private long sumPerMonth;
    
    private long requiredMinPerMonth;
    
    private long extraMinPerMonth;
    
    
    public WorkMonth(){}
    
    private WorkMonth(int year, int month){        
        this.date = YearMonth.of(year, month);
        setMonthDate();
    }
    
    public static WorkMonth fromNumbers(int year, int month){
        return new WorkMonth( year, month );
    }
    
    private WorkMonth(String year, String month){        
        this.date = YearMonth.of( Integer.parseInt(year), Integer.parseInt(month) );   
        setMonthDate();
    }
    
    public static WorkMonth fromString(String year, String month){
        return new WorkMonth( year, month );
    }
    
    private WorkMonth(String yearMonth){        
        String year = yearMonth.substring(0, 4);
        String month = yearMonth.substring(4, 6);
        
        this.date = YearMonth.of( Integer.parseInt(year), Integer.parseInt(month) );
        setMonthDate();
    }
    
    public static WorkMonth fromString(String yearMonth ){
        return new WorkMonth( yearMonth );
    }

    protected long getSumPerMonth() throws EmptyTimeFieldException {
        
        long summ = 0;
        if ( sumPerMonth != 0 ) return sumPerMonth;        
        for( WorkDay workDay : days ){
            summ += workDay.getSumPerDay();
        }
        sumPerMonth = summ;
        return sumPerMonth;
    }

    protected long getRequiredMinPerMonth() {
        
        if( requiredMinPerMonth != 0 ) return requiredMinPerMonth;
        requiredMinPerMonth = days.stream().mapToLong(WorkDay::getRequiredMinPerDay).sum();
        return requiredMinPerMonth;
    }
    
    protected long getExtraMinPerMonth() throws EmptyTimeFieldException{
        
        if(extraMinPerMonth != 0) return extraMinPerMonth;
        extraMinPerMonth = getSumPerMonth() - getRequiredMinPerMonth();
        return extraMinPerMonth;
    }
    
    public void setMonthDate(){
        this.monthDate = date.toString();
    }
        
    public void setDate(YearMonth date) {
        this.date = date;
        setMonthDate();
    }
        
    public String getMonthDate(){
        return monthDate;
    }
    
    public boolean isNewDate(WorkDay workDay){
            
        return days.stream().filter(i -> i.getActualDay().equals( workDay.getActualDay() )).count() == 0;
    }
    
    public boolean isSameMonth(WorkDay workDay){
    
        return date.getMonth() == workDay.getActualDay().getMonth();
    }
    
    public void addWorkDay(WorkDay workDay, boolean isWeekendEnabled) throws WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException{
         System.out.println("Adding a DAY....");
        if( !isNewDate(workDay) ) throw new NotNewDateException(" This day (" + workDay.getActualDay() +") already exist. Give an another. ");
        if( isSameMonth(workDay) ){
        
            if( isWeekendEnabled || Util.isWeekDay(workDay) ){
                
                days.add(workDay);
                sumPerMonth = 0;
                requiredMinPerMonth = 0;
                extraMinPerMonth = 0;
            }else { throw new WeekendNotEnabledException(" You should enable weekend work to add this day: " + workDay.toString()); }
            
        }else{ throw new NotTheSameMonthException(); }
    }
    
    public void addWorkDay(WorkDay workDay) throws WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException{
        addWorkDay(workDay, false);
    }

    @Override
    public String toString() {
        return "WorkMonth: " + date;
    }
}
