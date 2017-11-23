/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
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
    @JsonIgnore
    private int id;
    
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<WorkDay> days = new ArrayList<>();    
    
    @JsonIgnore    
    @Transient    
    private YearMonth date;    
    
    @Column(name = "date")
    private String monthDate;
    
    private long sumPerMonth;
    
    private long requiredMinPerMonth;
    
    private long extraMinPerMonth;
    
        
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

    public long getSumPerMonth() throws EmptyTimeFieldException {
        
        long summ = 0;
        //if ( sumPerMonth != 0 ) return sumPerMonth;        
        for( WorkDay workDay : days ){
            summ += workDay.getSumPerDay();
        }
        sumPerMonth = summ;
        return sumPerMonth;
    }

    public long getRequiredMinPerMonth() {
        
       // if( requiredMinPerMonth != 0 ) return requiredMinPerMonth;
        requiredMinPerMonth = days.stream().mapToLong(WorkDay::getRequiredMinPerDay).sum();
        return requiredMinPerMonth;
    }
    
    public long getExtraMinPerMonth() throws EmptyTimeFieldException{
        
       // if(extraMinPerMonth != 0) return extraMinPerMonth;
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
        if( this.date == null ) this.date =  convertOwnMontDateToYearMonth();        
        return days.stream().filter(i -> i.getActualDay().equals( workDay.getActualDay() )).count() == 0;
    }
    
    public boolean isSameMonth(WorkDay workDay){
        if( this.date == null ) this.date =  convertOwnMontDateToYearMonth();
        return date.getMonth() == workDay.getActualDay().getMonth();
        
    }
    
    public void addWorkDay(WorkDay workDay, boolean isWeekendEnabled) throws WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException, EmptyTimeFieldException{
        if( !isNewDate(workDay) ) throw new NotNewDateException(" This day (" + workDay.getActualDay() +") already exist. Give an another. ");
        if( isSameMonth(workDay) ){
        
            if( isWeekendEnabled || Util.isWeekDay(workDay) ){                
                days.add(workDay);
                System.out.println("Added a DAY....");
                sumPerMonth = 0;
                requiredMinPerMonth = 0;
                extraMinPerMonth = 0;
            }else { throw new WeekendNotEnabledException(" You should enable weekend work to add this day: " + workDay.toString()); }
            
        }else{ throw new NotTheSameMonthException(); }
    }
    
    public void addWorkDay(WorkDay workDay) throws WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException, EmptyTimeFieldException{
        addWorkDay(workDay, false);
    }

    @Override
    public String toString() {
        return "WorkMonth: " + monthDate;
    }    

    private YearMonth convertOwnMontDateToYearMonth() {
        return YearMonth.parse(this.getMonthDate());
    }
}
