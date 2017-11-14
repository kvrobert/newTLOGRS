package com.rkissvincze.tlog16rs.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author rkissvincze
 */
@Path("/hello")
public class hello {
    
    @JsonProperty
    @lombok.Getter
    String hello;
    
    public hello(){ this.hello = "Standard Hello"; }
    public hello(String message){ this.hello = message; }
    
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getGreeting() {
        return "Hello world from Robesz!";
    }
    
        
}
