package com.rkissvincze.Services;



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
    
    
    public static String getUser( String accessToken ){         
         
        StringBuffer response = new StringBuffer();
        UserInfo pojo= null;

        try {
            URL obj = new URL(userInfoUrl);

            HttpURLConnection con =  (HttpURLConnection) obj.openConnection();
            con.addRequestProperty("Authorization",  accessToken);
            int responseCode = con.getResponseCode();
            System.out.println("Service...Sending GET rew to urel..: " + userInfoUrl);
            System.out.println("Serice.....Rsponse code...: " + responseCode);

            BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
            String inputLine;

            while( ( inputLine = in.readLine() ) != null ){
                response.append(inputLine);
            }
            in.close();
            System.out.println( "Serice.....A válasz... " + response.toString() );
        }catch (Exception ex) {
            System.out.println("Serice.....HIBA..." + ex.getMessage());
        }
            System.out.println("==============================");
            ObjectMapper objMapper = new ObjectMapper();

        try{   
            pojo = objMapper.readValue(response.toString(), UserInfo.class);
            System.out.println("Serice.....Emailcím: " + pojo.email );             
            System.out.println("Serice.....Email megerőstett?: " + pojo.emailVerified );
         }catch (IOException ex) {
             System.out.println("Serice.....HIBA..." + ex.getMessage());
          }
        
        return pojo.email;
    }
}
