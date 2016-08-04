package com.config_notify.rpc.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RedisShard {
    private static Logger log = Logger.getLogger(RedisShard.class);
    private ShardedJedisPool pool;
    private String confStr;
    private Integer timeout=2000;
    private Integer maxActive;
    private Integer maxIdle;
    private Integer maxWait;
    private boolean testOnBorrow;

    public RedisShard() {
    }

    public void buildShardPool(){
        this.buildShardPool(null);
    }

    public void buildShardPool(String currConfStr) throws ExceptionInInitializerError{
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        //currConfStr first
        if(currConfStr!=null && !currConfStr.equals("")){
            this.confStr = currConfStr;
        }
        if(null == this.confStr || this.confStr.isEmpty())
            throw new ExceptionInInitializerError("The config string is empty！");
        List<String> confList = Arrays.asList(this.confStr.split("(?:\\s|,)+"));
        if (null == confList || confList.isEmpty())
            throw new ExceptionInInitializerError("confList is empty！");
        for (String address : confList) {
            if (address != null) {
                String[] addressArr = address.split(":");
                if (addressArr.length == 1)
                    throw new ExceptionInInitializerError(addressArr + " is not include host:port or host:port:passwd after split \":\"");
                String host = addressArr[0];
                int port = Integer.valueOf(addressArr[1]);
                JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port, this.timeout);
                log.info("confList:" + jedisShardInfo.toString());
                if (addressArr.length == 3 && !addressArr[2].isEmpty()) {
                    jedisShardInfo.setPassword(addressArr[2]);
                }
                shards.add(jedisShardInfo);
            }
        }

        //config for JedisPoolConfig
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(this.maxActive);
        config.setMaxIdle(this.maxIdle);
        config.setMaxWaitMillis(this.maxWait);
        config.setTestOnBorrow(this.testOnBorrow);

        this.pool = new ShardedJedisPool(config, shards);
        log.info("RedisShard init end.");
    }


    /**
     * set
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public String set(String key, byte[] value) throws Exception {
        if(key == null) throw new Exception("value sent to redis cannot be null");
        boolean flag = true;
        ShardedJedis j = null;
        String result = null;
        try {
            j = pool.getResource();
            result = j.getShard(key).set(key.getBytes("UTF-8"), value);
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(key).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }


    /**
     * set
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public String set(String key, String value) throws Exception {
        if(key == null) throw new Exception("value sent to redis cannot be null");
        boolean flag = true;
        ShardedJedis j = null;
        String result = null;
        try {
            j = pool.getResource();
            result = j.getShard(key).set(key.getBytes("UTF-8"), value.getBytes("UTF-8"));
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(key).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }

    /**
     * get
     * @param key
     * @return
     * @throws Exception
     */
    public String get(String key) throws Exception {
        if(key == null) throw new Exception("value sent to redis cannot be null");
        boolean flag = true;
        ShardedJedis j = null;
        String result = null;
        try {
            j = pool.getResource();
            byte[] r = j.getShard(key).get(key.getBytes("UTF-8"));
            result = new String(r, Charset.forName("UTF-8"));
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(key).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }

    /**
     * setbit
     * @param key
     * @param offset
     * @param value
     * @return
     * @throws Exception
     */
    public Boolean setbit(String key,long offset,Boolean value) throws Exception {
        Boolean result = null;
        ShardedJedis j = null;
        boolean flag = true;
        try {
            j = pool.getResource();
            result = j.getShard(key).setbit(key,offset,value);
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(key).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }


    /**
     * bitpos
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public Long bitpos(String key,boolean value) throws Exception {
        Long result = null;
        ShardedJedis j = null;
        boolean flag = true;
        try {
            j = pool.getResource();
            result = j.getShard(key).bitpos(key,value);
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(key).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }


    /**
     * bitcount
     * @param key
     * @return
     * @throws Exception
     */
    public Long bitcount(String key) throws Exception {
        Long result = null;
        ShardedJedis j = null;
        boolean flag = true;
        try {
            j = pool.getResource();
            result = j.getShard(key).bitcount(key);
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(key).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }


    /**
     * getbit
     * @param key
     * @param offset
     * @return
     * @throws Exception
     */
    public Boolean getbit(String key,long offset) throws Exception {
        Boolean result = null;
        ShardedJedis j = null;
        boolean flag = true;
        try {
            j = pool.getResource();
            result = j.getShard(key).getbit(key,offset);
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(key).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }


    /**
     * del
     * @param key
     * @return
     * @throws Exception
     */
    public Long del(String key) throws Exception {
        if(key == null) throw new Exception("value sent to redis cannot be null");
        boolean flag = true;
        ShardedJedis j = null;
        Long result = null;
        try {
            j = pool.getResource();
            result = j.getShard(key).del(key);
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(key).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }


    /**
     *
     * @param keyShard
     * @param sha1
     * @return
     * @throws Exception
     */
    public Boolean scriptExistsSingleShard(String keyShard, String sha1) throws Exception {
        if(null == keyShard ||sha1 == null) throw new Exception("keyShard or sha1 sent to redis cannot be null");
        boolean flag = true;
        ShardedJedis j = null;
        Boolean result = Boolean.FALSE;
        try {
            j = pool.getResource();
            result = j.getShard(keyShard).scriptExists(sha1);
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(keyShard).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }


    /**
     *
     * @param keyShard
     * @param script
     * @return
     * @throws Exception
     */
    public String scriptLoadSingleShard(String keyShard, String script) throws Exception {
        if(null == keyShard ||script == null) throw new Exception("keyShard or script sent to redis cannot be null");
        boolean flag = true;
        ShardedJedis j = null;
        String result = null;
        try {
            j = pool.getResource();
            result = j.getShard(keyShard).scriptLoad(script);
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(keyShard).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }


    /**
     *
     * @param keyShard
     * @param script
     * @param keyCount
     * @param params
     * @return
     * @throws Exception
     */
    public Object evalshaSingleShard(String keyShard, String script,int keyCount, String... params) throws Exception {
        if(null == keyShard ||script == null) throw new Exception("keyShard or script sent to redis cannot be null");
        boolean flag = true;
        ShardedJedis j = null;
        Object result = null;
        try {
            j = pool.getResource();
            result = j.getShard(keyShard).evalsha(script,keyCount, params);
        } catch (Exception ex) {
            flag = false;
            pool.returnBrokenResource(j);
            throw new Exception(ex+","+j.getShardInfo(keyShard).toString());
        } finally {
            if (flag) {
                pool.returnResource(j);
            }
        }
        return result;
    }






    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        RedisShard.log = log;
    }

    public ShardedJedisPool getPool() {
        return pool;
    }

    public void setPool(ShardedJedisPool pool) {
        this.pool = pool;
    }

    public String getConfStr() {
        return confStr;
    }

    public void setConfStr(String confStr) {
        this.confStr = confStr;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
}
