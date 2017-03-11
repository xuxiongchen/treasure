package com.cx.cache.demo.memcache;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cx.cache.demo.redis.JedisDemo;
import com.cx.cache.memcache.XMemcache;
import com.cx.cache.memcache.assist.Config_XMemcache;
import com.cx.common.CacheConstants;
import com.cx.common.ErrorCode;
import com.cx.common.exception.ServiceException;

/**
 * jedis使用DEMO
 * 
 */
public class MemecacheDemo {
	private static final Logger logger = LoggerFactory.getLogger(MemecacheDemo.class);
	public static XMemcache cache;
	public static void init()
			throws Exception {
		// 初始化
		String memcacheServers = "127.0.0.1:12000 192.168.1.3:12000";
		//需要开启redis 服务
		Config_XMemcache memcacheCacheConfig = new Config_XMemcache(memcacheServers, null, null);

		cache = new XMemcache(memcacheCacheConfig);
		
		//使用时一般在AppContext中创建，创建一次供全局使用
	}
	
	/**
	 * 
	 *   示范如何使用memecache缓存验证码 	
 	 * */
	public void saveValidateCode(String account, String validateCode) {
		
		long expiry=System.currentTimeMillis()+CacheConstants.MEMECHACE_CACHE_SECONDS_VALIDATECODE;
		//此处需要自己设定过期时间，与redis不同
		Date expiryDate=new Date(expiry);
		
		MemecacheDemo.cache.add(CacheConstants.MEMECHACE_CACHE_KEY_VALIDATECODE+ account, 
				validateCode,
				expiryDate);
	}

	public void checkValidateCode(String account, String validateCode)
			throws ServiceException {
		String validateCode_cache = getValidateCode(account);
		if (StringUtils.isBlank(validateCode)
				|| !validateCode.equals(validateCode_cache)) {
			throw new ServiceException(ErrorCode.CODE_VALIDATE_CODE, "验证码错误!");
		}
		destroyValidateCode(account);
	}

	public String getValidateCode(String account) {
		String validateCode = MemecacheDemo.cache
				.get(CacheConstants.MEMECHACE_CACHE_KEY_VALIDATECODE + account);
		return validateCode;
	}

	private void destroyValidateCode(String account) {
		MemecacheDemo.cache.delete(CacheConstants.MEMECHACE_CACHE_KEY_VALIDATECODE
				+ account);
	}



}
