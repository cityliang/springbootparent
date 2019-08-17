package com.city.sprinbboot.database2code.com.xjs.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis 工具类
 */
public class RedisUtil {

    protected static ReentrantLock lockPool = new ReentrantLock();
    protected static ReentrantLock lockJedis = new ReentrantLock();

    private static Logger _log = LoggerFactory.getLogger(RedisUtil.class);

    // Redis服务器IP
    private static String IP = PropertiesFileUtil.getInstance("redis").get("master.redis.ip");

    // Redis的端口号
    private static int PORT = PropertiesFileUtil.getInstance("redis").getInt("master.redis.port");

    // 访问密码
    private static String PASSWORD = AESUtil.AESDecode(PropertiesFileUtil.getInstance("redis").get("master.redis.password"));
    // 可用连接实例的最大数目，默认值为8；
    // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = PropertiesFileUtil.getInstance("redis").getInt("master.redis.max_active");

    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = PropertiesFileUtil.getInstance("redis").getInt("master.redis.max_idle");

    // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = PropertiesFileUtil.getInstance("redis").getInt("master.redis.max_wait");

    // 超时时间
    private static int TIMEOUT = PropertiesFileUtil.getInstance("redis").getInt("master.redis.timeout");

    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = false;

    private static JedisPool jedisPool = null;

    /**
     * redis过期时间,以秒为单位
     */
    public final static int EXRP_HOUR = 60 * 60;            //一小时
    public final static int EXRP_DAY = 60 * 60 * 24;        //一天
    public final static int EXRP_MONTH = 60 * 60 * 24 * 30;    //一个月

    /**
     * 初始化Redis连接池
     */
    private static void initialPool() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, IP, PORT, TIMEOUT);
        } catch (Exception e) {
            _log.error("First create JedisPool error : " + e);
        }
    }

    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void poolInit() {
        if (null == jedisPool) {
            initialPool();
        }
    }


    /**
     * 同步获取Jedis实例
     *
     * @return Jedis
     */
    public synchronized static Jedis getJedis() {
        poolInit();
        Jedis jedis = null;
        try {
            if (null != jedisPool) {
                jedis = jedisPool.getResource();
                try {
                    jedis.auth(PASSWORD);
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            _log.error("Get jedis error : " + e);
        }
        return jedis;
    }

    /**
     * 设置 String
     *
     * @param key
     * @param value
     */
    public synchronized static void set(String key, String value) {
        try {
            value = StringUtils.isBlank(value) ? "" : value;
            Jedis jedis = getJedis();
            jedis.set(key, value);
            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    /**
     * 根据Map集合设置 map
     *
     * @param dataMap
     */
    public synchronized static void set(Map<String, Map<String, String>> dataMap) {
        try {
            Jedis jedis = getJedis();
            Iterator<String> iter = dataMap.keySet().iterator();
            String key;
            while (iter.hasNext()) {
                key = iter.next();
                jedis.hmset(key, dataMap.get(key));
            }
            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    /**
     * 根据List<Map>集合设置 map
     *
     * @param dataMap
     */
    public synchronized static void set(List<Map<String, String>> dataMap) {
        try {
            Jedis jedis = getJedis();
            String key;
            for (Map<String, String> map : dataMap) {
                key = map.get("ORGCODE");
                jedis.hmset(key, map);
            }
            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    /**
     * 设置 map
     *
     * @param dataMap
     */
    public synchronized static void set(String key, Map<String, String> map) {
        try {
            Jedis jedis = getJedis();
            jedis.hmset(key, map);
            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    /**
     * 获取Map值
     *
     * @param key
     * @return
     */
    public synchronized static List<String> getMap(String key) {
        Jedis jedis = getJedis();
        if (null == jedis) {
            return null;
        }
        List<String> value = jedis.hvals(key);
        jedis.close();
        return value;
    }

    /**
     * 获取Map值
     *
     * @param key
     * @param fields
     * @return
     */
    public synchronized static List<String> getMap(String key, String... fields) {
        Jedis jedis = getJedis();
        if (null == jedis) {
            return null;
        }
        List<String> value = jedis.hmget(key, fields);
        jedis.close();
        return value;
    }

    /**
     * 根据list集合设置List
     *
     * @param listDate
     */
    public synchronized static void setList(List<String> listDate) {
        try {
            Jedis jedis = getJedis();
            for (String date : listDate) {
                jedis.lpush(date.split("-")[0], date);
            }
            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    /**
     * 根据Map集合删除缓存
     *
     * @param listDate
     */
    public synchronized static void delList(Map<String, String> data) {
        try {
            Jedis jedis = getJedis();
            Collection<String> coll = data.values();
            for (String value : coll) {
                jedis.del(value);
            }
            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    /**
     * 根据key获取list所有的值
     *
     * @param key
     * @return
     */
    public synchronized static List<String> getList(String key) {
        Jedis jedis = getJedis();
        if (null == jedis) {
            return null;
        }
        List<String> value = jedis.lrange(key, 0, -1);
        jedis.close();
        return value;
    }

    /**
     * 根据key获取list指定获范围值
     *
     * @param key
     * @param start 起始位置
     * @param end   结束位置
     * @return
     */
    public synchronized static List<String> getList(String key, int start, int end) {
        Jedis jedis = getJedis();
        if (null == jedis) {
            return null;
        }
        List<String> value = jedis.lrange(key, start, end);
        jedis.close();
        return value;
    }

    /**
     * 设置 byte[]
     *
     * @param key
     * @param value
     */
    public synchronized static void set(byte[] key, byte[] value) {
        try {
            Jedis jedis = getJedis();
            jedis.set(key, value);
            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    /**
     * 设置 String 过期时间
     *
     * @param key
     * @param value
     * @param seconds 以秒为单位
     */
    public synchronized static void set(String key, String value, int seconds) {
        try {
            value = StringUtils.isBlank(value) ? "" : value;
            Jedis jedis = getJedis();
            jedis.setex(key, seconds, value);
            jedis.close();
        } catch (Exception e) {
            _log.error("Set keyex error : " + e);
        }
    }

    /**
     * 设置 byte[] 过期时间
     *
     * @param key
     * @param value
     * @param seconds 以秒为单位
     */
    public synchronized static void set(byte[] key, byte[] value, int seconds) {
        try {
            Jedis jedis = getJedis();
            jedis.set(key, value);
            jedis.expire(key, seconds);
            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    /**
     * 获取String值
     *
     * @param key
     * @return value
     */
    public synchronized static String get(String key) {
        Jedis jedis = getJedis();
        if (null == jedis) {
            return null;
        }
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public synchronized static boolean isExists(String key) {
        Jedis jedis = getJedis();
        boolean isTrue = jedis.exists(key);
        jedis.close();
        return isTrue;
    }

    /**
     * 获取byte[]值
     *
     * @param key
     * @return value
     */
    public synchronized static byte[] get(byte[] key) {
        Jedis jedis = getJedis();
        if (null == jedis) {
            return null;
        }
        byte[] value = jedis.get(key);
        jedis.close();
        return value;
    }

    /**
     * 删除值
     *
     * @param key
     */
    public synchronized static void remove(String key) {
        try {
            Jedis jedis = getJedis();
            jedis.del(key);
            jedis.close();
        } catch (Exception e) {
            _log.error("Remove keyex error : " + e);
        }
    }

    /**
     * 删除值
     *
     * @param key
     */
    public synchronized static void remove(byte[] key) {
        try {
            Jedis jedis = getJedis();
            jedis.del(key);
            jedis.close();
        } catch (Exception e) {
            _log.error("Remove keyex error : " + e);
        }
    }

    /**
     * lpush
     *
     * @param key
     * @param key
     */
    public synchronized static void lpush(String key, String... strings) {
        try {
            Jedis jedis = RedisUtil.getJedis();
            jedis.lpush(key, strings);
            jedis.close();
        } catch (Exception e) {
            _log.error("lpush error : " + e);
        }
    }

    /**
     * lrem
     *
     * @param key
     * @param count
     * @param value
     */
    public synchronized static void lrem(String key, long count, String value) {
        try {
            Jedis jedis = RedisUtil.getJedis();
            jedis.lrem(key, count, value);
            jedis.close();
        } catch (Exception e) {
            _log.error("lpush error : " + e);
        }
    }

    /**
     * sadd
     *
     * @param key
     * @param value
     * @param seconds
     */
    public synchronized static void sadd(String key, String value, int seconds) {
        try {
            Jedis jedis = RedisUtil.getJedis();
            jedis.sadd(key, value);
            jedis.expire(key, seconds);
            jedis.close();
        } catch (Exception e) {
            _log.error("sadd error : " + e);
        }
    }

    /**
     * 设置 Object
     *
     * @param key
     * @param value
     * @param seconds 以秒为单位
     */
    public synchronized static void setObject(String key, Object object, int seconds) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(object);
            Jedis jedis = getJedis();
            jedis.setex(key, seconds, byteArrayOutputStream.toString("ISO-8859-1"));
            jedis.close();
            out.close();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    /**
     * 设置 Object
     *
     * @param key
     * @param value
     */
    public synchronized static void setObject(String key, Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(object);
            Jedis jedis = getJedis();
            jedis.set(key, byteArrayOutputStream.toString("ISO-8859-1"));
            jedis.close();
            out.close();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    /**
     * 获取Object值
     *
     * @param key
     * @return value
     */
    public synchronized static Object getObject(String key) {
        try {
            Jedis jedis = getJedis();
            if (null == jedis) {
                return null;
            }
            String value = jedis.get(key);
            if (null == value || "".equalsIgnoreCase(value)) {
                jedis.close();
                return null;
            }
            byte[] bytes = value.getBytes("ISO-8859-1");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            jedis.close();
            objectInputStream.close();
            byteArrayInputStream.close();
            return object;
        } catch (IOException e) {
            _log.error("getObject error : " + e);
        } catch (ClassNotFoundException e) {
            _log.error("getObject error : " + e);
        }
        return null;

    }

    /**
     * 刷新单个Object值
     *
     * @param key
     * @param value
     */
    public synchronized static void refreshObject(String key, Object object) {
        try {
            Jedis jedis = getJedis();
            if (null == jedis) {
                return;
            }
            jedis.del(key);
            if (null != object) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
                out.writeObject(object);
                jedis.set(key, byteArrayOutputStream.toString("ISO-8859-1"));
                out.close();
                byteArrayOutputStream.close();
            }
            jedis.close();

        } catch (IOException e) {
            _log.error("getObject error : " + e);
        }


    }

    /**
     * 根据Map集合设置 map
     *
     * @param dataMap
     */
    public synchronized static void setMap(String key, String field, Integer value) {
        try {
            if (null == key || "".equalsIgnoreCase(key)) {
                return;
            }
            Jedis jedis = getJedis();
            String isLock = null;
            do { //针对问题1，使用循环
                int timeout = 10;
                String $key = "room_lock";
                long $value = System.currentTimeMillis(); //分配一个随机的值针对问题3
                //第一个参数 key
                //第二个参数  value
                //第三个参数 "NX" - 仅在键不存在时设置键  "XX" - 只有在键已存在时才设置。
                //第四个参数   "EX" seconds − 设置指定的到期时间(以秒为单位) "PX" milliseconds - 设置指定的到期时间(以毫秒为单位)。
                //第五个参数   过期时间
                isLock = jedis.set($key, String.valueOf($value), "nx", "ex", timeout);//ex 秒
                if ("OK".equalsIgnoreCase(isLock)) {
                    //TODO 执行内部代码
                    String num = jedis.hget(key, field);
                    if (null != num && !"".equalsIgnoreCase(num)) {
                        Integer count = Integer.valueOf(num) + Integer.valueOf(value);
                        jedis.hset(key, field, count.toString());
                    } else {
                        jedis.hset(key, field, value.toString());
                    }
                    if (String.valueOf($value).equalsIgnoreCase(jedis.get($key))) { //防止提前过期，误删其它请求创建的锁
                        jedis.del($key);
                        break;//执行成功删除key并跳出循环
                    }
                } else {
                    Thread.sleep(1000); //睡眠，降低抢锁频率，缓解redis压力，针对问题2
                }
            } while (null == isLock);

            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }


    /**
     * 通过key模糊查询获取Object值
     *
     * @param key
     * @return value
     */
    public synchronized static Object getObjectVague(String key) {
        try {
            Jedis jedis = getJedis();
            if (null == jedis) {
                return null;
            }
            Set<String> set = jedis.keys(key + "-*");
            if (set.size() == 0) {
                return null;
            }
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                key = it.next();
            }

            String value = jedis.get(key);
            if (null == value || "".equalsIgnoreCase(value)) {
                jedis.close();
                return null;
            }
            byte[] bytes = value.getBytes("ISO-8859-1");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            jedis.close();
            objectInputStream.close();
            byteArrayInputStream.close();
            return object;
        } catch (IOException e) {
            _log.error("getObject error : " + e);
        } catch (ClassNotFoundException e) {
            _log.error("getObject error : " + e);
        }
        return null;

    }

    public synchronized static void setHash(String key, Map<String, String> dataMap) {
        try {
            Jedis jedis = getJedis();
            Iterator<String> iter = dataMap.keySet().iterator();
            String key1;
            while (iter.hasNext()) {
                key1 = iter.next();
                jedis.hset(key, key1, dataMap.get(key1));
            }
            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }

    public synchronized static Boolean hexists(String key, String valuekey) {
        try {
            Jedis jedis = getJedis();
            Boolean value = jedis.hexists(key, valuekey);
            jedis.close();
            return value;
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
        return false;
    }

    public synchronized static String getHash(String key, String valuekey) {
        try {
            Jedis jedis = getJedis();
            String value = jedis.hget(key, valuekey);
            jedis.close();
            return value;
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
        return null;
    }

    public synchronized static void delHash(String key) {
        try {

            Jedis jedis = getJedis();
            jedis.hvals(key);
            jedis.close();
        } catch (Exception e) {
            _log.error("Set key error : " + e);
        }
    }
}