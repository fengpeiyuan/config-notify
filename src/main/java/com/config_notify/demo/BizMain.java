package com.config_notify.demo;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BizMain {
    private static ClassPathXmlApplicationContext context;
    private static ScheduledExecutorService service;
    private static RedisDao redisDao;

    public static void main(String[] args){
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


        context.getBean("configInstance");
        System.out.print("running...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
