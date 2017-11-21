/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkissvincze.tlog16rs.resources;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.rkissvincze.Entities.TestEntity;
import org.avaje.datasource.DataSourceConfig;

/**
 *
 * @author rkissvincze
 */
public class CreateDatabase {
    
    private EbeanServer ebeanServer;
    private DataSourceConfig dataSourceConfig;
    private ServerConfig serverConfig;
    
    public CreateDatabase(){
    
        dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver("org.mariadb.jdbc.Driver");
        dataSourceConfig.setUrl("jdbc:mariadb://127.0.0.1:3306/timelogger");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("katika");
        
//        dataSourceConfig.setUsername("timelogger");
//        dataSourceConfig.setPassword("633Ym2aZ5b9Wtzh4EJc4pANx");
        
        
        serverConfig = new ServerConfig();
        serverConfig.setName("timelogger");
        serverConfig.setDdlGenerate(true);      // the database will be generate 
        serverConfig.setDdlRun(true);
        serverConfig.setRegister(true);
        serverConfig.setDataSourceConfig(dataSourceConfig);
        serverConfig.addClass(TestEntity.class);
        serverConfig.setDefaultServer(true);
       
        ebeanServer = EbeanServerFactory.create(serverConfig);
    }

    public EbeanServer getEbeanServer() {
        return ebeanServer;
    }
    
    
}
