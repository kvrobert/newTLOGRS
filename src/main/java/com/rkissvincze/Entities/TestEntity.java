package com.rkissvincze.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rkissvincze
 */
@Entity
@Getter
@Setter
public class TestEntity {
    
    private String text;
    
    @Id
    @GeneratedValue
    private int id;      
    
}
