package com.rkissvincze.Entities;

import com.rkissvincze.Exceptions.NotNewDateException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class TimeLogger {
    
    @Id
    @GeneratedValue
    private int id;
    
    private String name; 
            
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<WorkMonth> months = new ArrayList<>();
      
    
   public TimeLogger(String name){
        this.name = name;
    }
        
    public boolean isNewMonth(WorkMonth workMonth){
        System.out.println("TImelogger.....isNewMonth..." + (months.stream().filter(i -> i.getMonthDate().equals( workMonth.getMonthDate() ) ).count() == 0) );
        return months.stream().filter(i -> i.getMonthDate().equals( workMonth.getMonthDate() ) ).count() == 0;      // változtatva a getMonthDate -ra
    }
    
    public void addMonth(WorkMonth workMonth) throws NotNewDateException{
        if( isNewMonth(workMonth) ){            
            months.add(workMonth);
            log.info("Added WokrMonth - " + workMonth.getMonthDate() );
        }else{ throw new NotNewDateException(" The month (" + workMonth.toString() + ") already exists. Give an another."); }
    }
}
