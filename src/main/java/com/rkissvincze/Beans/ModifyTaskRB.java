package com.rkissvincze.Beans;

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
public class ModifyTaskRB {
    
    int year;
    int month;
    int day;
    String TaskId;
    String startTime;
    String newTaskId;
    String newStartTime;
    String newEndTime;
    String newComment;
    
}
