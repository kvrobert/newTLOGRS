package com.rkissvincze.tlog16rs.resources;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.rkissvincze.tlog16rs.TLOG16RSConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.Contexts;
import org.avaje.datasource.DataSourceConfig;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author rkissvincze
 */
@Slf4j
public class CreateDatabase {    
    
    private DataSourceConfig dataSourceConfig;
        
    public CreateDatabase( TLOG16RSConfiguration config ){        
        
        updateSchema( config );
        initDataSourceConfig(config); 
        initServerConfig();
    }
    
    private void initDataSourceConfig( TLOG16RSConfiguration config ){
        
        dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver(config.getDbDriver());
        dataSourceConfig.setUrl(config.getDbUrl());
        dataSourceConfig.setUsername(config.getDbUsername());
        dataSourceConfig.setPassword(config.getDbPassword());
    }
    
    private void initServerConfig(){
        ServerConfig serverConfig;
        EbeanServer ebeanServer;
        serverConfig = new ServerConfig();
        serverConfig.setName("timelogger");
        serverConfig.setDdlGenerate(false);      // the database will be generate, if it TRUE
        serverConfig.setDdlRun(false);           // the database will be generate, if it TRUE
        serverConfig.setRegister(true);
        serverConfig.setDataSourceConfig(dataSourceConfig);
        serverConfig.setDefaultServer(true);
       
        ebeanServer = EbeanServerFactory.create(serverConfig);
    }
    
    private void updateSchema( TLOG16RSConfiguration config ){
        try(Connection connection = DriverManager.getConnection(
                                                config.getDbUrl(), 
                                                config.getDbUsername(), 
                                                config.getDbPassword()) ){           
            Liquibase liquBase = new Liquibase("migrations.xml", 
                                                new ClassLoaderResourceAccessor(), 
                                                new JdbcConnection(connection));
            liquBase.update(new Contexts());        
        }catch(SQLException | LiquibaseException ex) {            
            log.error(CreateDatabase.class.getName() + " " + ex.getMessage());                       
        }
    }
}
