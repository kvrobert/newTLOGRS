package com.rkissvincze.tlog16rs;

import com.rkissvincze.Entities.TimeLogger;
import com.rkissvincze.Services.UserService;
import com.rkissvincze.tlog16rs.resources.CreateDatabase;
import com.rkissvincze.tlog16rs.resources.TLOG16RSResource;
import com.rkissvincze.tlog16rs.resources.TLOG16RSResource_greeting;
import com.rkissvincze.tlog16rs.resources.hello;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TLOG16RSApplication extends Application<TLOG16RSConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TLOG16RSApplication().run(args);
    }

    @Override
    public String getName() {
        return "TLOG16RS";
    }

    @Override
    public void initialize(final Bootstrap<TLOG16RSConfiguration> bootstrap) {                
    }

    @Override
    public void run(final TLOG16RSConfiguration configuration,
                    final Environment environment) {
        CreateDatabase createDatabase = new CreateDatabase( configuration  );
        //environment.jersey().register(new TLOG16RSResource( new TimeLogger("Robesz") ));
        environment.jersey().register(new TLOG16RSResource( new TimeLogger() )); // így most nem lesz default user
       // environment.jersey().register(new hello());
      //  environment.jersey().register(new TLOG16RSResource_greeting());  
    }

}
