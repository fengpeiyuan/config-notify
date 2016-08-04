package com.config_notify.core;


import java.util.HashMap;
import java.util.Map;
import java.util.Observable;


public abstract class ObservableMonitor extends Observable {
    private ConfigChangeObserver configChangeObserver;
    private Map data = new HashMap();

    public ObservableMonitor(ConfigChangeObserver configChangeObserver) {
        this.configChangeObserver = configChangeObserver;
        this.addObserver(configChangeObserver);
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

}
