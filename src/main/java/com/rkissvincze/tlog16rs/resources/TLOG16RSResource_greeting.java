package com.rkissvincze.tlog16rs.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/prec")
public class TLOG16RSResource_greeting {
    
    @lombok.Getter
    @JsonProperty
    private String message;
    
    public TLOG16RSResource_greeting(){}
    
    public TLOG16RSResource_greeting(String message){
        this.message = message;
    }
    
   @Path("/hello")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
    public String getHello(){
       return "Hello Word!!!";
   }
    
   @Path( "/path_param/{name}" )
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public String getHelloName( @PathParam( value = "name" ) String name ){
       return "Hello " + name + " from Word!";
   }
   
   @Path("/query_param")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public String getQueryName( @DefaultValue("Word") @QueryParam("name") String name ){
       return "Hello " + name;
   }
   
   @Path("/hello_json")
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public hello getJsonHello()
   {
       return new hello("Hello Json Word");
   }
   
}
