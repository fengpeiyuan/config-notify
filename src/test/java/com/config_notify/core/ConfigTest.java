package com.config_notify.core;


import com.config_notify.demo.RedisDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigTest {
    private ClassPathXmlApplicationContext context;
    private ScheduledExecutorService service;
    private RedisDao redisDao;
    @Before
    public void setup(){
        context = new ClassPathXmlApplicationContext(new String[] {"spring-config.xml"});
        context.start();
        redisDao=(RedisDao) context.getBean("redisDao");
        service= Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run() {
                String restDemo = redisDao.demoGet("demo");
                System.out.println(restDemo);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetDemo(){
        context.getBean("configInstance");
        System.out.print("running...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        assertEquals(ret,value);
    }




}
