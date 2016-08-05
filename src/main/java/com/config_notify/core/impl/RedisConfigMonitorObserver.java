package com.config_notify.core.impl;


import com.config_notify.core.ConfigChangeObserver;
import com.config_notify.core.ObservableMonitor;
import com.config_notify.rpc.redis.RedisShard;
import org.apache.log4j.Logger;

import java.util.Observable;

public class RedisConfigMonitorObserver implements ConfigChangeObserver {
    final static Logger logger = Logger.getLogger(RedisConfigMonitorObserver.class);
    private RedisShard redisShard;

    @Override
    public void update(Observable o, Object arg) {
        ObservableMonitor observableMonitor=(ObservableMonitor)o;
        String currConfigStr=observableMonitor.getData().get("confStr").toString();
        logger.error("@@@@@ ObservableMonitor Switch Redis Connections Start! currConfigStr:"+currConfigStr);
        //System.out.println("@@@@@ Switch Redis Connections Start! currConfigStr:"+currConfigStr);
        redisShard.buildShardPool(currConfigStr);
        logger.error("@@@@@ ObservableMonitor Switch Redis Connections Finished! currConfigStr:"+currConfigStr);

    }

    public RedisShard getRedisShard() {
        return redisShard;
    }

    public void setRedisShard(RedisShard redisShard) {
        this.redisShard = redisShard;
    }
}
