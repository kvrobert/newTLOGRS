package com.rkissvincze.tlog16rs.resources;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.rkissvincze.Entities.TestEntity;
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
import java.time.YearMonth;
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
    private EbeanServer ebeanServer;
    
    public TLOG16RSResource( TimeLogger timeLogger ){
        this.timelogger = timeLogger;
    }
    
    
    @GET
    @Path("/workmonths")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response displaysWorkMoths(){        
        return Response.ok( timelogger.getMonths(), MediaType.APPLICATION_JSON).build();        
    }
   
    @POST
    @Path("/workmonths")                                // HIBAKEZELÉS??????
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addWorkMonth(WorkMonthRB month){
        WorkMonth workMonth = WorkMonth.fromNumbers(month.getYear(), month.getMonth());
        try{
            timelogger.addMonth(workMonth);
            return Response.ok(month).build();
        }catch(NotNewDateException e){
            Response.serverError();
            System.err.println(e.getMessage());
            log.error(e.getMessage());
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
//            wd = WorkDay.fromNumbers( 
//                workday.getRequiredHour(),      //MIN!!!!
//                workday.getYear(), 
//                workday.getMonth(), 
//                workday.getDay());
        wm = createNewMonthOrGetTheExisting(workday.getYear(), workday.getMonth());
            System.out.println("A WÖRKÓNT" + wm);
        wd = createNewDayOrGetTheExisting(workday.getYear(), workday.getMonth(), workday.getDay(), wm);
            System.out.println("A WORKDÉJ: " + wd);       
            return Response.ok( wd, MediaType.APPLICATION_JSON).build();
        }catch( WeekendNotEnabledException | NotNewDateException | 
                NotTheSameMonthException |NegativeMinutesOfWorkException | FutureWorkException e){
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
                    task.getYear(), task.getMonth(), task.getDay(), wm);
        }catch (NotNewDateException | WeekendNotEnabledException | 
                NotTheSameMonthException | NegativeMinutesOfWorkException | 
                FutureWorkException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }
        try {
            tsk = createNewTaskOrGetTheExisting(wd, task);
        }catch ( NoTaskIdException | InvalidTaskIdException | 
                NotSeparatedTimesException | EmptyTimeFieldException | 
                NotExpectedTimeOrderException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }    
        
        return Response.ok(tsk, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/workmonths/{year}/{month}")         
    @Consumes(MediaType.APPLICATION_JSON)
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
        System.out.println("ENTERING..Y/M/D.." + year + month+day);
        if( !year.matches("\\d{4}")  || !month.matches("\\d{2}") || !day.matches("\\d{2}")) 
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
                    Integer.parseInt(day), wm);
            System.out.println("Y/M/D...WD = :" + wd);
        }catch (NotNewDateException | WeekendNotEnabledException | 
                NotTheSameMonthException | NegativeMinutesOfWorkException | 
                FutureWorkException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }
        return Response.ok(wd.getTasks(), MediaType.APPLICATION_JSON).build();
    }
    
    @PUT                                                                            
    @Path("/workmonths/workdays/tasks/finish")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response finishTask(ModifyTaskRB taskFinish){
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
                    taskFinish.getDay(), wm);
        }catch (NotNewDateException | WeekendNotEnabledException | 
                NotTheSameMonthException | NegativeMinutesOfWorkException | 
                FutureWorkException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
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
    @Path("/workmonths/workdays/tasks/modify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyTask(ModifyTaskRB taskRB) {
        WorkMonth wm = null;       
        WorkDay wd = null;
        Task tsk = null;
        
        try {
            wm = createNewMonthOrGetTheExisting(
                    taskRB.getYear(), 
                    taskRB.getMonth());
        }catch (NotNewDateException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }        
        try {
            wd = createNewDayOrGetTheExisting( 
                    taskRB.getYear(), 
                    taskRB.getMonth(), 
                    taskRB.getDay(), wm);
        }catch (NotNewDateException | WeekendNotEnabledException | 
                NotTheSameMonthException | NegativeMinutesOfWorkException | 
                FutureWorkException ex) {
            System.err.println(ex.getMessage());
            log.error(ex.getMessage());
        }        
        try {
            tsk = createNewTaskOrGetTheExisting(wd, taskRB);
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
     
      if( !ServicesResource.isTaskExits(timelogger, deleteTask) ) 
          return Response.status(Response.Status.SEE_OTHER).build();
      WorkDay wd = getTheWorkDay(
              deleteTask.getYear(), 
              deleteTask.getMonth(), 
              deleteTask.getDay());
      Task tsk = getTheTask(wd, deleteTask.getTaskId(), deleteTask.getStartTime());
      wd.getTasks().remove(tsk);
      return Response.ok().build();
    }
    
    @DELETE                                                                        
    @Path("/workmonths/")
    public Response deleteAllTheWorkmonts(){
        timelogger.getMonths().clear();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @POST
    @Path("/save/test")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createEntity( String entityText ){
        System.out.println("SAVE...." + entityText);
        TestEntity entityTest = new TestEntity();
        entityTest.setText(entityText);
        Ebean.save(entityTest);
//        entityText = entityText + "helloka";
        return Response.ok(entityText).build();
    }
    
    
    /*
     *  ::: SERVICES ::
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
                taskRB.getStartTime())){
            
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

    protected WorkDay createNewDayOrGetTheExisting(int year, int month, int day, WorkMonth wm) 
            throws WeekendNotEnabledException, NegativeMinutesOfWorkException, 
            NotTheSameMonthException, NotNewDateException, FutureWorkException {
        WorkDay wd;
        if( !ServicesResource.isDayExits(timelogger, year, month, day)){
            wd = ServicesResource.createWorkDay(450, year, month, day);
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
        return timelogger.getMonths().stream().findFirst()
                .filter(wm -> wm.getDate().equals(
                YearMonth.of(year, month))).get();
    }
    
    protected WorkDay getTheWorkDay( int year, int month, int day ){
        WorkMonth wm = getTheMonth(year, month);
        System.out.println("GetTHEWOKMÓNTH aus GETTHEWORKDÉJ..." + wm);
        return wm.getDays().stream().findAny().filter(wd ->                     // nem jó a keresés-- sehol sem ez a STREAMes..FOR 
        wd.getActualDay().equals(LocalDate.of(year, month, day))).get();
    }

    protected Task getTheTask(WorkDay workDay, String taskId, String startTime) {
        return workDay.getTasks().stream().findFirst().filter( tsk -> 
                tsk.getTaskID().equals(taskId) && 
                tsk.getStartTime().equals( LocalTime.parse(startTime) ) ).get();
    }
    
}