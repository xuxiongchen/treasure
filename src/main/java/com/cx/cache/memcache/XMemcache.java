package com.cx.cache.memcache;

import java.util.Date;

import net.rubyeye.xmemcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cx.cache.memcache.assist.Config_XMemcache;
import com.cx.cache.memcache.assist.XMemcacheHelper;


/**
 * memcache客户端，项目中new一次后 全局使用
 * 
 */
public class XMemcache implements ICache {
	private final Logger logger = LoggerFactory.getLogger(XMemcache.class);
	private MemcachedClient client = null;

	/**
	 * 构造函数(从配置文件中加载配置项)初始化XMemcache，项目中new一次后 全局使用
	 * 
	 * @throws Exception
	 */
	public XMemcache() throws Exception {
		MemcachedClient client = XMemcacheHelper
				.initMemcachedClient(new Config_XMemcache());
		this.client = client;
	}

	/**
	 * 构造函数 初始化XMemcache，项目中new一次后 全局使用
	 * 
	 * @param config
	 * @throws Exception
	 */
	public XMemcache(Config_XMemcache config) throws Exception {
		MemcachedClient client = XMemcacheHelper.initMemcachedClient(config);
		this.client = client;
	}

	@Override
	public boolean add(String key, Object value) {
		boolean result = false;
		try {
			result = client.add(key, 0, value);
		} catch (Exception e) {
			logger.error("XMemcache.add faild", e);
		}
		return result;
	}

	@Override
	public boolean add(String key, Object value, Date expiry) {
		boolean result = false;
		long exp = expiry.getTime() / 1000;
		int intExp = (int) exp;
		try {
			result = client.add(key, intExp, value);
		} catch (Exception e) {
			logger.error("XMemcache.add faild", e);
		}
		return result;
	}

	@Override
	public boolean delete(String key) {
		boolean result = false;
		try {
			result = client.delete(key);
		} catch (Exception e) {
			logger.error("XMemcache.delete faild", e);
		}
		return result;
	}

	@Override
	public boolean set(String key, Object value) {
		boolean result = false;
		try {
			result = client.set(key, 0, value);
		} catch (Exception e) {
			logger.error("XMemcache.set faild", e);
		}
		return result;
	}

	@Override
	public boolean set(String key, Object value, Date expiry) {
		boolean result = false;
		long exp = expiry.getTime() / 1000;
		int intExp = (int) exp;
		try {
			result = client.set(key, intExp, value);
		} catch (Exception e) {
			logger.error("XMemcache.set faild", e);
		}
		return result;
	}

	@Override
	public boolean replace(String key, Object value) {
		boolean result = false;
		try {
			result = client.replace(key, 0, value);
		} catch (Exception e) {
			logger.error("XMemcache.replace faild", e);
		}
		return result;
	}

	@Override
	public boolean replace(String key, Object value, Date expiry) {
		boolean result = false;
		long exp = expiry.getTime() / 1000;
		int intExp = (int) exp;
		try {
			result = client.replace(key, intExp, value);
		} catch (Exception e) {
			logger.error("XMemcache.replace faild", e);
		}
		return result;
	}

	@Override
	public String getString(String key) {
		String result = null;
		try {
			result = client.get(key);
		} catch (Exception e) {
			logger.error("XMemcache.getString faild", e);
		}
		return result;
	}

	@Override
	public <T> T get(String key) {
		T result = null;
		try {
			result = client.get(key);
		} catch (Exception e) {
			logger.error("XMemcache.get faild", e);
		}
		return result;
	}

	@Override
	public boolean clearAll() {
		try {
			client.flushAll();
		} catch (Exception e) {
			logger.error("XMemcache.clearAll faild", e);
			return false;
		}
		return true;
	}

	@Override
	public void shutdown() {
		try {
			client.shutdown();
		} catch (Exception e) {
			logger.error("XMemcache.shutdown faild", e);
		}
	}

}
