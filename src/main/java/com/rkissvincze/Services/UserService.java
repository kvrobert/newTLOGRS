package com.rkissvincze.Services;



import com.rkissvincze.Exceptions.InvalidAccessTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import com.rkissvincze.User.UserInfo;
import java.net.URL;
import javax.ws.rs.core.Response;





public class UserService {

    final static String userInfoUrl = "https://prosperobert.eu.auth0.com/userinfo";
    final static String accessToken = "zd9N47prhCqFVXTezjjU_GthEZtKfShc";    
    
    
    public static String getUser( String accessToken ) throws InvalidAccessTokenException{         
         
        StringBuffer response = new StringBuffer();
        UserInfo pojo= null;

        try {
            URL obj = new URL(userInfoUrl);

            HttpURLConnection con =  (HttpURLConnection) obj.openConnection();
            con.addRequestProperty("Authorization",  accessToken);
            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
            String inputLine;

            while( ( inputLine = in.readLine() ) != null ){
                response.append(inputLine);
            }
            in.close();
        }catch (Exception ex) {
            throw new InvalidAccessTokenException("Permission denied! Invalid Acces Token!");
        }
            
            ObjectMapper objMapper = new ObjectMapper();

        try{   
            pojo = objMapper.readValue(response.toString(), UserInfo.class);
            System.out.println("Az emailcm..: " + pojo.emailVerified);
            System.out.println( pojo.emailVerified.equals("false") );            
         }catch (IOException ex) {
             System.out.println("Serice.....HIBA..." + ex.getMessage());
          }
        
        if( pojo.emailVerified ){
            return pojo.email;
        }
        else{
            System.out.println("Érvénytelen email...."+pojo.emailVerified );
            throw new InvalidAccessTokenException("Permission denied! Please verify your email address!");            
        }
    }
}
