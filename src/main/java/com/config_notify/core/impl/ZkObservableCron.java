package com.config_notify.core.impl;

import com.config_notify.core.ConfigChangeObserver;
import com.config_notify.core.ObservableCron;


public class ZkObservableCron extends ObservableCron {

    public ZkObservableCron(ConfigChangeObserver configChangeObserver) {
        super(configChangeObserver);
    }

    @Override
    public void run() {

    }
}
