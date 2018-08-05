package com.nci.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.cln.utils.Configration;
import com.cln.utils.StaticUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author ajun
 * 
 */
public class RedisUtil
{
	private static Logger log = Logger.getLogger(RedisUtil.class.getName());

	private static JedisPool pool;
	private static int DBIndex;
	private static String host;
	private static int port;
	private static int timeout;
	private static String pwd;

	static
	{
		DBIndex = 0;
		host = Configration.getProperty("redis_host");
		port = 6379;
		timeout = 10;
		pwd = Configration.getProperty("redis.cluster.auth");

		JedisPoolConfig config = new JedisPoolConfig();
		config.setTestOnBorrow(false);
		// 线程数量限制，IP地址，端口，超时时间
		pool = new JedisPool(config, host, port, timeout, pwd);

	}

	/**
	 * 清空Redis全部数据
	 * @throws Exception 
	 */
	public static void clear() throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			jedis.flushAll();
		}
		catch (Exception e)
		{
			log.error("clear:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 在list中添加key
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 * @throws Exception 
	 */
	public static void addItemToList(String key, byte[] value) throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			jedis.lpush(key.getBytes(), value);
		}
		catch (Exception e)
		{
			log.error("addItemToList:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 取出并删除list中key的�??
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public static byte[] delItemFromList(String key) throws Exception
	{
		Jedis jedis = null;
		byte[] value = null;
		try
		{
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			value = jedis.lpop(key.getBytes());
		}
		catch (Exception e)
		{
			log.error("delItemFromList:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 使用redis内置的map 添加key 如果已经存在key，则新�?�覆盖旧值，修改时可以使用这�?
	 * seconds（秒）表过期超时删除数据�?0表示永远不删�?
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 * @throws Exception 
	 */
	public static void set(String key, byte[] value, int seconds) throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			jedis.set(key.getBytes(), value);
			if (seconds > 0)
			{
				jedis.expire(key.getBytes(), seconds);
			}
		}
		catch (Exception e)
		{
			log.error("set:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 使用redis内置的map 添加key 如果已经存在key，则不添�? seconds（秒）表过期超时删除数据�?0表示永远不删�?
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 * @throws Exception 
	 */
	public static void setnx(String key, byte[] value, int seconds) throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			jedis.setnx(key.getBytes(), value);
			if (seconds > 0)
			{
				jedis.expire(key.getBytes(), seconds);
			}
		}
		catch (Exception e)
		{
			log.error("setnx:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 使用redis内置的map 不熟悉redis不建议用这个 将�?�value关联到key，并将key的生存时间设为seconds(以秒为单�?)�?
	 * 如果key 已经存在，SETEX命令将覆写旧值�?? seconds（秒）表过期超时删除数据�?0表示永远不删�?
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 * @throws Exception 
	 */
	public static void setex(String key, byte[] value, int seconds) throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			jedis.setex(key.getBytes(), seconds, value);
			if (seconds > 0)
			{
				jedis.expire(key.getBytes(), seconds);
			}
		}
		catch (Exception e)
		{
			log.error("setex:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 使用redis内置的map 获取key
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public static byte[] get(String key) throws Exception
	{
		Jedis jedis = null;
		byte[] s = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			s = jedis.get(key.getBytes());
			return s;
		}
		catch (Exception e)
		{
			log.error("get:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 使用redis内置的map 删除key
	 * 
	 * @param key
	 * @throws Exception 
	 */
	public static void del(String key) throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			jedis.del(key.getBytes());

		}
		catch (Exception e)
		{
			log.error("del:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}

	}

	/**
	 * Redis自增序列
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public static long getIncrement() throws Exception
	{
		String key = "REDIS";
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			return jedis.incr(key);

		}
		catch (Exception e)
		{
			log.error("getIncrement:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 在自己定义的map中存�?<key，value>
	 * 
	 * @param key
	 * @param map
	 * @throws Exception 
	 */
	public static void setBHashMap(String key, Map<String, byte[]> map) throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			if (map != null && !map.isEmpty())
			{
				for (Map.Entry<String, byte[]> entry : map.entrySet())
				{
					jedis.hset(key.getBytes(), entry.getKey().getBytes(), entry.getValue());
				}

			}
		}
		catch (Exception e)
		{
			log.error("setBHashMap:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 在自己定义的map中存�?<key，value>
	 * 
	 * @param key
	 * @param map
	 * @throws Exception 
	 */
	public static void removeBHashMap(String key, String field) throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			if (key != null && field != null)
			{
				jedis.hdel(key.getBytes(), field.getBytes());
			}
		}
		catch (Exception e)
		{
			log.error("removeBHashMap:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}


	/**
	 * 在自己定义的map中�?�过key获取�?
	 * 
	 * @param key
	 * @param map
	 * @throws Exception 
	 */
	public static byte[] getBHashMap(String key, String field) throws Exception
	{
		byte[] value = null;
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			value = jedis.hget(key.getBytes(), field.getBytes());
		}
		catch (Exception e)
		{
			log.error("getBHashMap:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 自己定义的map中获取全部的key�?
	 * 
	 * @param key
	 * @param map
	 * @throws Exception 
	 */
	public static Map<byte[], byte[]> getAllBHashMap(String key) throws Exception
	{
		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			map = jedis.hgetAll(key.getBytes());
		}
		catch (Exception e)
		{
			log.error("getAllBHashMap:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return map;

	}

	/**
	 * 设置map 存储用户信息
	 * 
	 * @param key
	 * @param map
	 * @throws Exception 
	 */
	public static void setSHashMap(String key, Map<String, String> map) throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			if (map != null && !map.isEmpty())
			{
				for (Map.Entry<String, String> entry : map.entrySet())
				{
					jedis.hset(key, entry.getKey(), entry.getValue());
//					jedis.hset(key.getBytes(), entry.getKey().getBytes(), entry.getValue());
				}

			}
		}
		catch (Exception e)
		{
			log.error("setSHashMap:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}

	}

	/**
	 * 获取hashmap
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, String> getSHashMap(String key) throws Exception
	{
		Map<String, String> map = null;
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			map = jedis.hgetAll(key);
			if(map != null && map.size()==0)
			{
				map = null;
			}
		}
		catch (Exception e)
		{
			log.error("getSHashMap:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return map;
	}
	
	/**
	 * 删除HashMap中域的�??
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public static void hdel(String key, String field) throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			jedis.hdel(key.getBytes(), field.getBytes());
		}
		catch (Exception e)
		{
			log.error("hdel:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 判断HashMap中域的�??
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public static boolean hexists(String key, String field) throws Exception
	{
		Jedis jedis = null;
		boolean flag = false;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			flag = jedis.hexists(key, field);
		}
		catch (Exception e)
		{
			log.error("hexists:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return flag;
	}

	/**
	 * 添加set
	 * 
	 * @param key
	 * @param set
	 * @throws Exception 
	 */
	public static void addSet(String key, Set<String> set) throws Exception
	{
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			if (set != null && !set.isEmpty())
			{
				for (String value : set)
				{
					/*
					 * for ( Iterator<String> memberItr =
					 * jedis.smembers(str).iterator();//返回key对应set的所有元素，结果是无序的
					 * memberItr.hasNext();){ final String member =
					 * memberItr.next(); if (!jedis.sismember(str, member)){
					 * jedis.srem(str, member); } }
					 */
					jedis.sadd(key, value);
				}
			}
		}
		catch (Exception e)
		{
			log.error("addSet:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	public static Set<String> getSet(String key) throws Exception
	{
		Set<String> sets = new HashSet<String>();
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			jedis.select(DBIndex);
			sets = jedis.smembers(key);
		}
		catch (Exception e)
		{
			log.error("getSet:"+e);
			throw new Exception(e);
		}
		finally
		{
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return sets;
	}
	
	/**
	 * 
	* @Title: main 
	* @author: xb
	* @Description: 入口�?
	* @param: @param args
	* @return: void
	 * @throws Exception 
	* @throws:
	 */
	public static void main(String[] args) throws Exception
	{
		System.out.println("�?�?----------");
		System.out.println("xx"+RedisUtil.get("yxb"));
		
/*		for(int i=0; i<10; i++)
		{
			RedisUtil.set("yqz1"+i, ("values"+i).getBytes(), 0);
			System.out.println("----------"+i);
		}*/
		
		for(int i=0; i<10; i++)
		{
			byte[] ss = RedisUtil.get("yqz1"+i);
			System.out.println("1----------"+new String(ss));
		}
		
		for(int i=0; i<10; i++)
		{
			Map<String, byte[]> xx = new HashMap<String, byte[]>();
			xx.put("lyp"+i, ("UUU"+i).getBytes());
			RedisUtil.setBHashMap("ddd", xx);
		}
		
		
//		for(int i=0; i<10; i++)
//		{
//			Map<String, byte[]> xx = null;
//			byte[] x2 = RedisUtil.getBHashMap("ddd", "lyp"+i);
//			
//			System.out.println("----"+new String("lyp"+i));
//		}
		
		Map<byte[], byte[]> xxx = RedisUtil.getAllBHashMap("ddd");
		System.out.println(xxx.toString());
		
	}
}
