package com.rkissvincze.tlog16rs.resources;

import com.avaje.ebean.Ebean;
import com.rkissvincze.Beans.DeleteTaskRB;
import com.rkissvincze.Beans.ModifyTaskRB;
import com.rkissvincze.Beans.TaskRB;
import com.rkissvincze.Beans.WorkMonthRB;
import com.rkissvincze.Entities.Task;
import com.rkissvincze.Entities.TimeLogger;
import com.rkissvincze.Entities.WorkDay;
import com.rkissvincze.Beans.WorkDayRB;
import com.rkissvincze.Entities.WorkMonth;
import com.rkissvincze.Exceptions.EmptyTimeFieldException;
import com.rkissvincze.Exceptions.FutureWorkException;
import com.rkissvincze.Exceptions.InvalidTaskIdException;
import com.rkissvincze.Exceptions.NegativeMinutesOfWorkException;
import com.rkissvincze.Exceptions.NoTaskIdException;
import com.rkissvincze.Exceptions.NotExpectedTimeOrderException;
import com.rkissvincze.Exceptions.NotNewDateException;
import com.rkissvincze.Exceptions.NotSeparatedTimesException;
import com.rkissvincze.Exceptions.NotTheSameMonthException;
import com.rkissvincze.Exceptions.WeekendNotEnabledException;
import com.rkissvincze.Services.ServicesResource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/timelogger")
@Slf4j
public class TLOG16RSResource {
    
    TimeLogger timelogger;
    
    public TLOG16RSResource( TimeLogger timeLogger ){
               
        timelogger = Ebean.find(TimeLogger.class)
                          .where().eq("name", "Robesz")
                          .findUnique();  
        if (timelogger == null){
        this.timelogger = timeLogger;
        Ebean.save(timelogger);
        }
    }    
    
    @GET
    @Path("/workmonths")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response displaysWorkMoths(){
        log.info("Calling displaysWorkMoths() method");
        return Response.ok( timelogger.getMonths(), 
                            MediaType.APPLICATION_JSON).build();        
    }
   
    @POST
    @Path("/workmonths")                                // HIBAKEZELÉS??????
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addWorkMonth(WorkMonthRB month){        
        WorkMonth workMonth = WorkMonth.fromNumbers(month.getYear(), 
                                                    month.getMonth());
        try{
            timelogger.addMonth(workMonth);
            Ebean.save(timelogger);
            return Response.ok(month).build();
        }catch(NotNewDateException e){
            System.err.println(e.getMessage());
            log.error(e.getMessage());
            Response.serverError();
        }
        return Response.status(Response.Status.SEE_OTHER).build();
    }
    
    @POST
    @Path("/workmonths/workdays")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addworkDay( WorkDayRB workday ){
        WorkMonth wm = null;
        WorkDay wd = null;
        try{
            wm = createNewMonthOrGetTheExisting(workday.getYear(), 
                                                workday.getMonth());            
            wd = createNewDayOrGetTheExisting(workday.getYear(), 
                                              workday.getMonth(), 
                                              workday.getDay(), 
                                              (int) workday.getRequiredHour(),wm);           
            ServicesResource.updateWorkDayAndWorkMonthStatistic(wd, wm);
            Ebean.save(wm);            
            Ebean.save(timelogger);      
            return Response.ok( wd, MediaType.APPLICATION_JSON).build();
        }catch( WeekendNotEnabledException | NotNewDateException | 
                NotTheSameMonthException |NegativeMinutesOfWorkException | 
                FutureWorkException | EmptyTimeFieldException e){
            System.err.println(e.getMessage());
            log.error(e.getMessage());
        } 
        return Response.status(Response.Status.SEE_OTHER).build();
    }
        
    @POST
    @Path("/workmonths/workdays/tasks/start")       
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTask( TaskRB task ){
        int reqMinDay = 0;
        WorkMonth wm = null;       
        WorkDay wd = null;
        Task tsk = null;        
        try {
            wm = createNewMonthOrGetTheExisting(
                    task.getYear(), task.getMonth());
        }catch (NotNewDateException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }        
        try {
            wd = createNewDayOrGetTheExisting( 
                task.getYear(), task.getMonth(), task.getDay(), reqMinDay, wm);
            System.out.println("Start TASK....WÖRKDÉJ...." + wd);
        }catch (NotNewDateException | WeekendNotEnabledException | 
                NotTheSameMonthException | NegativeMinutesOfWorkException | 
                FutureWorkException | EmptyTimeFieldException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        } 
        try {
            tsk = createNewTaskOrGetTheExisting(wd, task);
            ServicesResource.updateTaskStatistic(tsk);
            ServicesResource.updateWorkDayAndWorkMonthStatistic(wd, wm);            
            Ebean.save(wd);  
            Ebean.update(wm);
            Ebean.save(timelogger);
        }catch ( NoTaskIdException | InvalidTaskIdException | 
                NotSeparatedTimesException | EmptyTimeFieldException | 
                NotExpectedTimeOrderException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }  
        return Response.ok(tsk, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/workmonths/{year}/{month}")         /// innnen..... 
    @Consumes(MediaType.APPLICATION_JSON)       /// + tesztelések SOAP + old, form commandLineUI
    @Produces(MediaType.APPLICATION_JSON)
    public Response displaysDays( @PathParam("year") String year, 
                                  @PathParam( "month" ) String month  ){
        if( !year.matches("\\d{4}")  || !month.matches("\\d{2}") ) 
            return Response.status(Response.Status.CONFLICT).build();

        WorkMonth wm = null; 
        
        try {
            wm = createNewMonthOrGetTheExisting( 
                    Integer.parseInt(year), 
                    Integer.parseInt(month));
        }catch (NotNewDateException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }        
        return Response.ok(wm.getDays(), MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/workmonths/{year}/{month}/{day}")       
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response displaysTasks( @PathParam("year") String year,
                                   @PathParam( "month" ) String month, 
                                   @PathParam( "day" ) String day  ){
        int reqMinDay = 0;
        System.out.println("ENTERING..Y/M/D.." + year + month+day);
        if( !year.matches("\\d{4}")  || 
            !month.matches("\\d{2}") || 
            !day.matches("\\d{2}")) 
                return Response.status(Response.Status.CONFLICT).build();
        WorkMonth wm = null;       
        WorkDay wd = null;
        try {
            wm = createNewMonthOrGetTheExisting( 
                    Integer.parseInt(year), 
                    Integer.parseInt(month) );
            System.out.println("Y/M/D...WM = :" + wm);
        }catch (NotNewDateException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }        
        try {
            wd = createNewDayOrGetTheExisting(
                    
                    Integer.parseInt(year), 
                    Integer.parseInt(month), 
                    Integer.parseInt(day),
                    reqMinDay, wm);
            System.out.println("Y/M/D...WD = :" + wd);
        }catch (NotNewDateException | WeekendNotEnabledException | 
                NotTheSameMonthException | NegativeMinutesOfWorkException | 
                FutureWorkException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        } catch (EmptyTimeFieldException ex) {
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok(wd.getTasks(), MediaType.APPLICATION_JSON).build();
    }
    
    @PUT                                                                            
    @Path("/workmonths/workdays/tasks/finish")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response finishTask(ModifyTaskRB taskFinish){
        int reqMinDay = 0;
        WorkMonth wm = null;       
        WorkDay wd = null;
        Task tsk = null;
        
        try {
            wm = createNewMonthOrGetTheExisting(
                    taskFinish.getYear(), 
                    taskFinish.getMonth());
        }catch (NotNewDateException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }        
        try {
            wd = createNewDayOrGetTheExisting(                     
                    taskFinish.getYear(), 
                    taskFinish.getMonth(), 
                    taskFinish.getDay(),
                    reqMinDay, wm);
        }catch (NotNewDateException | WeekendNotEnabledException | 
                NotTheSameMonthException | NegativeMinutesOfWorkException | 
                FutureWorkException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        } catch (EmptyTimeFieldException ex) {        
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            tsk = createNewTaskOrGetTheExisting(wd, taskFinish);
        }catch ( NoTaskIdException | InvalidTaskIdException | 
                NotSeparatedTimesException | EmptyTimeFieldException | 
                NotExpectedTimeOrderException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }
        try {
            tsk.setTaskID(taskFinish.getNewTaskId());
            tsk.setComment(taskFinish.getNewComment());
            tsk.setStartTime(taskFinish.getNewStartTime());
            tsk.setEndTime(taskFinish.getNewEndTime());
        } catch ( EmptyTimeFieldException | InvalidTaskIdException | 
                NotExpectedTimeOrderException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }                 
        return Response.ok(tsk, MediaType.APPLICATION_JSON).build();
        
    }
    
    @PUT                                                                         
    @Path("/workmonths/workdays/tasks/modify")          /// EZT KELL MÉG
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyTask(ModifyTaskRB taskRB) {
        int reqMinDay = 0;
        WorkMonth wm = null;       
        WorkDay wd = null;
        Task tsk = null;
        
        try {
            wm = createNewMonthOrGetTheExisting(
                    taskRB.getYear(), 
                    taskRB.getMonth());
            System.out.println("GetTheMóntInTaskModiy.." + wm);
        }catch (NotNewDateException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }        
        try {
            wd = createNewDayOrGetTheExisting(                     
                    taskRB.getYear(), 
                    taskRB.getMonth(), 
                    taskRB.getDay(),
                    reqMinDay, wm);
            System.out.println("GetTheDÉJInTaskModiy.." + wd);
        }catch (NotNewDateException | WeekendNotEnabledException | 
                NotTheSameMonthException | NegativeMinutesOfWorkException | 
                FutureWorkException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        } catch (EmptyTimeFieldException ex) {        
            Logger.getLogger(TLOG16RSResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            tsk = createNewTaskOrGetTheExisting(wd, taskRB);
             System.out.println("GetTheTASKinTaskModiy.." + tsk);
        }catch ( NoTaskIdException | InvalidTaskIdException | 
                NotSeparatedTimesException | EmptyTimeFieldException | 
                NotExpectedTimeOrderException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }
        
        try {
            tsk.setTaskID(taskRB.getNewTaskId());
            tsk.setComment(taskRB.getNewComment());
            tsk.setStartTime(taskRB.getNewStartTime());
            tsk.setEndTime(taskRB.getNewEndTime());
        } catch ( EmptyTimeFieldException | InvalidTaskIdException |  
                NotExpectedTimeOrderException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }                 
        return Response.ok(tsk, MediaType.APPLICATION_JSON).build();
    }

    @PUT                                                                         
    @Path("/workmonths/workdays/tasks/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTask( DeleteTaskRB deleteTask ){
      
      if( !ServicesResource.isTaskExits(timelogger, deleteTask) ){ 
          System.out.println("Delete TASK....task not exist.." + deleteTask);
          return Response.status(Response.Status.SEE_OTHER).build();
          }
      WorkDay wd = getTheWorkDay(
              deleteTask.getYear(), 
              deleteTask.getMonth(), 
              deleteTask.getDay());
      System.out.println("Delete TASK....task exist..Workday" + wd);
      Task tsk = getTheTask(wd, deleteTask.getTaskId(), deleteTask.getStartTime()); // OKÉÉ
      System.out.println("Delete TASK....task to delete.." + tsk);
      wd.getTasks().remove(tsk);
      return Response.ok().build();
    }
    
    @DELETE                                                                        
    @Path("/workmonths/")
    public Response deleteAllTheWorkmonts(){
        Ebean.deleteAll( timelogger.getMonths() );
        timelogger = Ebean.find(TimeLogger.class, 1);        
        return Response.status(Response.Status.NO_CONTENT).build();
    }   
    
    /*
     *                      ::: SERVICES ::
     *
     */      
    
    protected Task createNewTaskOrGetTheExisting(WorkDay wd, ModifyTaskRB taskRB) throws 
            NoTaskIdException, InvalidTaskIdException, NotSeparatedTimesException, 
            EmptyTimeFieldException, NotExpectedTimeOrderException {
        Task tsk;
        if( !ServicesResource.isTaskExits(
                wd, 
                taskRB.getTaskId(), 
                taskRB.getStartTime())){
            
            tsk = ServicesResource.createTask(
                    taskRB.getNewTaskId(), 
                    taskRB.getNewComment(), 
                    taskRB.getNewStartTime(), 
                    taskRB.getNewEndTime());
            wd.addTask(tsk);
        }else{
            tsk = getTheTask(wd, taskRB.getTaskId(), taskRB.getStartTime());
        }
        return tsk;
    }
    
    protected Task createNewTaskOrGetTheExisting(WorkDay wd, TaskRB taskRB) throws 
            NotExpectedTimeOrderException, EmptyTimeFieldException, NoTaskIdException, 
            InvalidTaskIdException, NotSeparatedTimesException {
        Task tsk;
        if( !ServicesResource.isTaskExits(
                wd, 
                taskRB.getTaskId(), 
                taskRB.getStartTime())) {            
            tsk = ServicesResource.createTask(
                    taskRB.getTaskId(), 
                    taskRB.getComment(), 
                    taskRB.getStartTime(), 
                    taskRB.getEndTime());
            wd.addTask(tsk);
        }else{
            tsk = getTheTask(
                    wd, 
                    taskRB.getTaskId(), 
                    taskRB.getStartTime());
        }        
        return tsk;
    }

    protected WorkDay createNewDayOrGetTheExisting( int year, int month, int day, int reqMinDay, WorkMonth wm) 
            throws WeekendNotEnabledException, NegativeMinutesOfWorkException, 
            NotTheSameMonthException, NotNewDateException, FutureWorkException, EmptyTimeFieldException {
        WorkDay wd;
        if( !ServicesResource.isDayExits(timelogger, year, month, day)){
            System.out.println("THE DAY NOT EXITS.....I CREATE IT");
            wd = ServicesResource.createWorkDay(reqMinDay, year, month, day);            
            wm.addWorkDay(wd);
        }else{
            System.out.println("THE DAY EXITS.....");
            wd = getTheWorkDay( year, month, day );
            System.out.println("I GIVE THE EXITS WORKDÉJ:..: " + wd);
        } 
        return wd;
    }

    protected WorkMonth createNewMonthOrGetTheExisting(int year, int month) throws NotNewDateException {
        WorkMonth wm;
        if( !ServicesResource.isMonthExits(timelogger,year, month )){
            wm = ServicesResource.createWorkMonth(year, month);
            timelogger.addMonth(wm);
        }else{
            wm = getTheMonth(year, month);
        }
        return wm;
    }

    protected WorkMonth getTheMonth(int year, int month) {
        int firstElement = 0;
        String yearMonth = year + "-" + month;
        System.out.println("getTheMonth-ból.." + yearMonth );
        List<WorkMonth> monthSelected =  timelogger.getMonths().stream()
                .filter(wm -> wm.getMonthDate().equals( yearMonth ))       
                .collect(Collectors.toList());
        return monthSelected.get(firstElement);
    }
    
    protected WorkDay getTheWorkDay( int year, int month, int day ){
        WorkMonth wm = getTheMonth(year, month);
        int firstElement = 0;
        System.out.println("GetTHEWOKMÓNTH aus GETTHEWORKDÉJ..." + wm);
        List<WorkDay> daySelected = wm.getDays().stream().filter( 
                wDay -> wDay.getActualDay().equals( LocalDate.of(year, month, day) ))
                .collect(Collectors.toList());
        return daySelected.get(firstElement);
        }
    

    protected Task getTheTask(WorkDay workDay, String taskId, String startTime) {
        int firstElement = 0;
        List<Task> taskSelected = workDay.getTasks().stream().filter( tsk -> 
                tsk.getTaskID().equals(taskId) && 
                tsk.getStartTime().equals( LocalTime.parse(startTime) ) ).collect(Collectors.toList());
        return taskSelected.get(firstElement);
    }
          
}