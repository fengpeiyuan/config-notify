package com.config_notify.demo;


import com.config_notify.rpc.redis.RedisShard;
import org.apache.log4j.Logger;

public class RedisDao {
	private RedisShard redisShard;
	final static Logger logger = Logger.getLogger(RedisDao.class);

	public String demoSet(String key,String value) {
		try {
			return this.getRedisShard().set(key,value);
		}catch (Exception e){
			logger.error("exception in demoSet,",e);
			return null;
		}
	}

	public String demoGet(String key) {
		try {
			return this.getRedisShard().get(key);
		}catch (Exception e){
			logger.error("exception in demoGet,",e);
			return null;
		}
	}
	
	public RedisShard getRedisShard() {
		return redisShard;
	}

	public void setRedisShard(RedisShard redisShard) {
		this.redisShard = redisShard;
	}
	
	
	
	
}
