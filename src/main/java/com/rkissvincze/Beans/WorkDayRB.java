package com.rkissvincze.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author rkissvincze
 */
@Getter
@Setter
@NoArgsConstructor
public class WorkDayRB {
   
    int year;
    int month;
    int day;
    long requiredHour;
}
