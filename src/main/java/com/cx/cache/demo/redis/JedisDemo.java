package com.cx.cache.demo.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cx.cache.redis.JedisCache;
import com.cx.cache.redis.assist.Config_JedisCache;
import com.cx.common.CacheConstants;
import com.cx.common.ErrorCode;
import com.cx.common.exception.ServiceException;

/**
 * jedis使用DEMO
 * 
 */
public class JedisDemo {
	private static final Logger logger = LoggerFactory.getLogger(JedisDemo.class);
	public static JedisCache cache;
	public static void init()
			throws Exception {
		// 初始化
		String redisServers = "127.0.0.1:6379";
		//需要开启redis 服务
		Config_JedisCache jedisCacheConfig = new Config_JedisCache( redisServers, null);

		cache = new JedisCache(jedisCacheConfig);
		
		//使用时一般在AppContext中创建，创建一次供全局使用
	}
	
	/**
	 * 
	 *   示范如何使用redis缓存验证码 
	 * */
	public void saveValidateCode(String account, String validateCode) {
		JedisDemo.cache.setex(CacheConstants.REDIS_CACHE_KEY_VALIDATECODE
				+ account, CacheConstants.REDIS_CACHE_SECONDS_VALIDATECODE,
				validateCode);
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
		String validateCode = JedisDemo.cache
				.get(CacheConstants.REDIS_CACHE_KEY_VALIDATECODE + account);
		return validateCode;
	}

	private void destroyValidateCode(String account) {
		JedisDemo.cache.del(CacheConstants.REDIS_CACHE_KEY_VALIDATECODE
				+ account);
	}



}
