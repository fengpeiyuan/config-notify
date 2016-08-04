package com.config_notify.core.impl;

import com.config_notify.core.ConfigChangeObserver;
import com.config_notify.core.ObservableCron;
import com.config_notify.rpc.redis.RedisShard;
import org.apache.log4j.Logger;

public class ConsulCronImpl extends ObservableCron {
    final static Logger logger = Logger.getLogger(ConsulCronImpl.class);
    private RedisShard redisShard;
    private String connUrl;

    public ConsulCronImpl(ConfigChangeObserver configChangeUpdater) {
        super(configChangeUpdater);
    }

    @Override
    public void run() {
//        String param="";
//        HttpClientTools httpClientTools = new HttpClientTools();
//        Boolean rst = httpClientTools.executeGetMethod(connUrl,param,"UTF-8");
//        if(rst) {
//            String res = httpClientTools.getStrGetResponseBody();
//            if(res!=null&&!res.equals("")){
//                String oriConfStr=redisShard.getConfStr();
//                List<ConsulResponseVO> consulResponseVOList = JSON.parseArray(res,ConsulResponseVO.class);
//                ConsulResponseVO consulResponseVO = consulResponseVOList.get(0);
//                String currConfigStr=consulResponseVO.getValue();//TODO base64
//                logger.info("currConfigStr:"+currConfigStr);
//                if(!oriConfStr.equals(currConfigStr)){
//                    logger.error("##### Config Changed! currConfigStr:"+currConfigStr+",oriConfStr:"+oriConfStr);
//                    this.getData().put("confStr",currConfigStr);
//                    setChanged();
//                    notifyObservers();
//                }
//
//            }
//
//        }else{
//                logger.equals(httpClientTools.getErrorInfo().toString());
//        }

    }


    public RedisShard getRedisShard() {
        return redisShard;
    }

    public void setRedisShard(RedisShard redisShard) {
        this.redisShard = redisShard;
    }

    public String getConnUrl() {
        return connUrl;
    }

    public void setConnUrl(String connUrl) {
        this.connUrl = connUrl;
    }
}
