package com.config_notify.core;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConfigCronInstance {
    ScheduledExecutorService service ;
    ObservableCron observableCron;

    public ConfigCronInstance(ObservableCron observableCron) {
        this.observableCron = observableCron;
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(observableCron, 1, 1, TimeUnit.SECONDS);
    }







}
