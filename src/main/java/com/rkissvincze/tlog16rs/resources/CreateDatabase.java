/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.tlog16rs.resources;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.rkissvincze.Entities.TestEntity;
import com.rkissvincze.tlog16rs.TLOG16RSConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import liquibase.Contexts;
import org.avaje.datasource.DataSourceConfig;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 *
 * @author rkissvincze
 */
public class CreateDatabase {
    
    private EbeanServer ebeanServer;
    private DataSourceConfig dataSourceConfig;
    private ServerConfig serverConfig;
    
    public CreateDatabase( TLOG16RSConfiguration config ){
        
        updateSchema( config );
    //    initDataSourceConfig(config);
    //    initServerConfig(config);
        
        
//        dataSourceConfig = new DataSourceConfig();
//        dataSourceConfig.setDriver("org.mariadb.jdbc.Driver");
//        dataSourceConfig.setUrl("jdbc:mariadb://127.0.0.1:3306/timelogger");
//        dataSourceConfig.setUsername("root");
//        dataSourceConfig.setPassword("katika");
        
//        dataSourceConfig.setUsername("timelogger");
//        dataSourceConfig.setPassword("633Ym2aZ5b9Wtzh4EJc4pANx");
        
        
//        serverConfig = new ServerConfig();
//        serverConfig.setName("timelogger");
//        serverConfig.setDdlGenerate(false);      // the database will be generate, if it TRUE
//        serverConfig.setDdlRun(false);           // the database will be generate, if it TRUE
//        serverConfig.setRegister(true);
//        serverConfig.setDataSourceConfig(dataSourceConfig);
//        serverConfig.addClass(TestEntity.class);
//        serverConfig.setDefaultServer(true);
//       
//        ebeanServer = EbeanServerFactory.create(serverConfig);
    }

    public EbeanServer getEbeanServer() {
        return ebeanServer;
    }
    
    private void initDataSourceConfig( TLOG16RSConfiguration config ){
        dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver(config.getDbDriver());
        dataSourceConfig.setUrl(config.getDbUrl());
        dataSourceConfig.setUsername(config.getDbUsername());
        dataSourceConfig.setPassword(config.getDbPassword());
    }
    private void initServerConfig( TLOG16RSConfiguration config ){
        serverConfig = new ServerConfig();
        serverConfig.setName("timelogger");
        serverConfig.setDdlGenerate(false);      // the database will be generate, if it TRUE
        serverConfig.setDdlRun(false);           // the database will be generate, if it TRUE
        serverConfig.setRegister(true);
        serverConfig.setDataSourceConfig(dataSourceConfig);
        serverConfig.addClass(TestEntity.class);
        serverConfig.setDefaultServer(true);
       
        ebeanServer = EbeanServerFactory.create(serverConfig);
    }
    
    private void updateSchema( TLOG16RSConfiguration config ){
        try {
                       
            //Connection connection = DriverManager.getConnection(config.getDbUrl(), config.getDbUsername(), config.getDbPassword());
            Connection connection = DriverManager.getConnection( "jdbc:mariadb://localhost:3306/timelogger?user=root&password=katika" );
            Liquibase liquBase = new Liquibase("migrations.xml", 
                                                new ClassLoaderResourceAccessor(), 
                                                new JdbcConnection(connection));
            liquBase.update(new Contexts());
        
        } catch (SQLException | LiquibaseException ex) {
            Logger.getLogger(CreateDatabase.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Liqubase ERROR..." + ex.getMessage());
        }
    }
}
