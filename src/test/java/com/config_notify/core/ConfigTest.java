package com.config_notify.core;


import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigTest {
    private ClassPathXmlApplicationContext context;

    @Before
    public void setup(){
        context = new ClassPathXmlApplicationContext(new String[] {"spring-config.xml"});
        context.start();
    }

    @Test
    public void testGetDemo(){
        ConfigInstance configInstance = (ConfigInstance) context.getBean("configInstance");
        System.out.print("running");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        assertEquals(ret,value);
    }




}
