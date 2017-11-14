package com.rkissvincze.tlog16rs.resources;

import com.google.common.net.MediaType;
import io.dropwizard.testing.junit.ResourceTestRule;
import javassist.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.junit.*;

/**
 *
 * @author rkissvincze
 */
public class helloTest {
    
   @Rule   // ez egy tesztje az osztálynak...
    public ResourceTestRule resource = ResourceTestRule.builder()
            .addResource(new hello()).build();
    
    @Rule
    public ResourceTestRule resultPrec = ResourceTestRule
            .builder()
            .addResource(new TLOG16RSResource()).build();
   
   

    
    public helloTest() {
    }

    /**
     * Test of getGreeting method, of class hello.
     */
    @Test
    public void testGetGreeting() {
        String expected = "Hello world from Robesz!";
        // Obtain client from @Rule
        Client client = resource.client();
        // Get webTarget from client using URI of root resource.
        WebTarget helloTarget = client.target("http://localhost:8080/hello");
        // To invoke response  we use Invocation.Builder
        // and specify the media type of representation asked from resource.
        Invocation.Builder builder = helloTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN);
        // Obtain response
        Response response = builder.get();
        
        // do assertions
        Assert.assertEquals(Response.Status.OK, response.getStatusInfo());
        Assert.assertEquals(200, response.getStatus());
        String actual = response.readEntity(String.class);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetGreeting2() {
        String expected = "Hello world from Robesz!";
        String actual = resource.client()
                        .target("http://localhost:8080/hello")
                        .request(javax.ws.rs.core.MediaType.TEXT_PLAIN)
                        .get(String.class);
        Assert.assertEquals(expected, actual);
        
    }
    
    @Test
    public void testPrecogHello() {
        String expected = "Hello Word!!!";
        String actual = resultPrec.client()
                        .target("http://localhost:8080/prec/hello")
                        .request(javax.ws.rs.core.MediaType.TEXT_PLAIN)
                        .get(String.class);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = javax.ws.rs.NotFoundException.class)   //Mindenképp kell paraméter
    public void testPrecHelloPathParamNameBlank(){
        String expected = "Hello ";
        String result = resultPrec.client()
                .target("http://localhost:8080/prec/path_param/")
                .request(javax.ws.rs.core.MediaType.TEXT_PLAIN)
                .get(String.class);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testPrecHelloPathParamNameWithParam(){
        String expected = "Hello Robeszka from Word!";
        String result = resultPrec.client()
                .target("http://localhost:8080/prec/path_param/Robeszka")
                .request(javax.ws.rs.core.MediaType.TEXT_PLAIN)
                .get(String.class);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testPrecQueryParamNoParam(){
        String expc = "Hello Word";
        String res = resultPrec.client()
                .target("http://localhost:8080/prec/query_param")
                .request(javax.ws.rs.core.MediaType.TEXT_PLAIN)
                .get(String.class);
        Assert.assertEquals(expc, res);
        
    }
    @Test
    public void testPrecQueryParamWithParam(){
        String expc = "Hello Baba";
        String res = resultPrec.client()
                .target("http://localhost:8080/prec/query_param/?name=Baba")
                .request(javax.ws.rs.core.MediaType.TEXT_PLAIN)
                .get(String.class);
        Assert.assertEquals(expc, res);
       
    }
}
 