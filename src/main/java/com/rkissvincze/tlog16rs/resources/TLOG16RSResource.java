package com.rkissvincze.tlog16rs.resources;

import com.rkissvincze.Beans.TaskRB;
import com.rkissvincze.Beans.WorkMonthRB;
import com.rkissvincze.Entities.Task;
import com.rkissvincze.Entities.TimeLogger;
import com.rkissvincze.Entities.WorkDay;
import com.rkissvincze.Entities.WorkDayRB;
import com.rkissvincze.Entities.WorkMonth;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/timelogger")
public class TLOG16RSResource {
    
    TimeLogger timelogger;
    WorkMonth workMonth;
    WorkDay workDay;
    Task task;
    
    public TLOG16RSResource(){}
    
    
    @GET
    @Path("/workmonths")
    @Produces(MediaType.APPLICATION_JSON)
    public Response displaysWorkMoths(){
        try{
//            timelogger = new TimeLogger();
//            WorkMonth wm = new WorkMonth();
//            WorkDay wd = new WorkDay();
//            Task task = Task.fromString("4567", "Helloka", "07:45", "09:00");
//            wd.addTask(task);
//            wm.addWorkDay(wd);
//            timelogger.addMonth(wm);

            return Response.ok( timelogger.getMonths(), MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
            return Response.status(Response.Status.SEE_OTHER).build();
        }
   
    @POST
    @Path("/workmonths")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addWorkMonth(WorkMonthRB month){
        WorkMonth workMonth = WorkMonth.fromNumbers(month.getYear(), month.getMonth());
        try{
            timelogger.addMonth(workMonth);
            return Response.ok(month).build();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return Response.status(Response.Status.SEE_OTHER).build();
    }
    
    @POST
    @Path("/workmonths/workdays")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addworkDay( WorkDayRB workday ){
        
        try{
            WorkDay wd = WorkDay.fromNumbers( 
                        workday.getRequiredHour() * 60,
                        workday.getYear(), 
                        workday.getMonth(), 
                        workday.getDay());
            
            workMonth.addWorkDay( wd );
            return Response.ok( wd ).build();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return Response.status(Response.Status.SEE_OTHER).build();
    }
    
    @POST
    @Path("/workmonths/workdays/tasks/start")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTask( TaskRB task ){
        
        try{
            Task tsk = Task.fromString(
                    task.getTaskId(), 
                    task.getComment(), 
                    task.getStartTime(), 
                    task.getEndTime());
            
            workDay.addTask(tsk);
            return Response.ok( tsk ).build();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return Response.status(Response.Status.SEE_OTHER).build();
    }
    
    @GET
    @Path("/workmonths/{year}/{month}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response displaysDays( @PathParam("year") String year, @PathParam( "month" ) String month  ){
            WorkMonth wm = null;
            WorkDay wd = null;
        try{
            wm = WorkMonth.fromString("year", "month");
            if( timelogger.isNewMonth(wm) ) {
                return Response.ok(wm.getDays()).build();
            }else{
                List< WorkMonth > selectedMonth = timelogger.getMonths().stream().filter(
                        i -> i.getDate() == YearMonth.of(Integer.parseInt(year),
                        Integer.parseInt(month))).collect(Collectors.toList());
                wm = selectedMonth.get(0);
                return Response.ok(wm.getDays(), MediaType.APPLICATION_JSON).build();
            }           
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
            return Response.status(Response.Status.SEE_OTHER).build();
        }
    
    @GET
    @Path("/workmonths/{year}/{month}/{day}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response displaysTasks( @PathParam("year") String year,
                                   @PathParam( "month" ) String month, 
                                   @PathParam( "day" ) String day  ){
        WorkMonth wm = null;
        WorkDay wd = null;
        
        try{
            wd = WorkDay.fromString("0", year+month+day);
            if( workMonth.isNewDate( wd ) ) {
                return Response.ok( wd.getTasks() ).build();
            }else{
                
                for( WorkDay wDay : wm.getDays() ){
                    if( wDay.getActualDay().equals(wd.getActualDay()) )
                        wd = wDay;
                }
                                
                return Response.ok(wd.getTasks(), MediaType.APPLICATION_JSON).build();
            }           
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
            return Response.status(Response.Status.SEE_OTHER).build();
        }
    
    @PUT                                                                            /////////////////////////////
    @Path("/timelogger/workmonths/workdays/tasks/finish")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response finishTask(TaskRB task){
        WorkMonth wm = null;
        WorkDay wd = null;
        Task tsk = null;
        try{
            tsk = Task.fromString(task.getTaskId(), "", task.getStartTime(), task.getEndTime());
            if( workDay.getTasks().stream().filter(   i -> (
                    i.getTaskID().equals(task.getTaskId()) && 
                    i.getStartTime().equals(task.getStartTime()) )).count() == 0  ){
                //exits??   külső FV????? jobb lenne....
            }
                    
        }catch(Exception e){
            System.err.println(e.getMessage());
        }    
        return Response.status(Response.Status.SEE_OTHER).build();
    }
    
    @PUT                                                                            /////////////////////////////
    @Path("/timelogger/workmonths/workdays/tasks/modify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyTask(TaskRB task){
        WorkMonth wm = null;
        WorkDay wd = null;
        Task tsk = null;
        
        
        
        return Response.status(Response.Status.SEE_OTHER).build();
    }
    
}