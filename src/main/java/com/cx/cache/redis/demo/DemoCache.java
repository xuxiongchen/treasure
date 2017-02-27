package com.cx.cache.redis.demo;

import org.apache.commons.lang3.StringUtils;

import com.cx.cache.redis.CacheConstants;
import com.cx.common.ErrorCode;
import com.cx.common.exception.ServiceException;

/**
 * 使用例子
 * @author chenxu
 *
 */
//@Component
public class DemoCache {
	//@Autowired
	//private DemoCacheDao demoCacheDao;

	public void saveSessionid(int userId, String sessionid) {
		JedisDemo.cache.setex(
				CacheConstants.REDIS_CACHE_KEY_CUSTOMER_SESSIONID + userId,
				CacheConstants.REDIS_CACHE_SECONDS_CUSTOMER_SESSIONID,
				sessionid);
	}

	public String getSessionidByUserId(int userId) throws ServiceException {
		String sessionid = JedisDemo.cache
				.get(CacheConstants.REDIS_CACHE_KEY_CUSTOMER_SESSIONID + userId);
		if (StringUtils.isBlank(sessionid)) {
//			CustomerInfo customerInfo = customerInfoDao.getSessionid(userId);
//			if (customerInfo == null) {
//				throw new ServiceException(ErrorCode.CODE_AUTH, "请重新登录!");
//			}
//			sessionid = demoCacheDao.getSessionid();
			if (StringUtils.isNotBlank(sessionid)) {
				saveSessionid(userId, sessionid);
			}
		}
		return sessionid;
	}

	public void deleteSessionid(int userId) {
		JedisDemo.cache.del(CacheConstants.REDIS_CACHE_KEY_CUSTOMER_SESSIONID
				+ userId);

	}

	public void checkSessionid(int userId, String sessionid)
			throws ServiceException {
		// 校验
		if (userId <= 0) {
			throw new ServiceException(ErrorCode.CODE_PARAM, "userId 不能为空!");
		}
		if (StringUtils.isBlank(sessionid)) {
			throw new ServiceException(ErrorCode.CODE_PARAM, "sessionid 不能为空!");
		}
		// 检验userid和sessionid是否匹配
		String sessionid_db = getSessionidByUserId(userId);
		if (StringUtils.isBlank(sessionid_db)
				|| !sessionid_db.equals(sessionid)) {
			throw new ServiceException(ErrorCode.CODE_AUTH,
					"session校验失败,请重新登录!");
		}
	}

	public void saveCustomerLatitudeAndLongitude(int customerId,
			String latitude, String longitude) {
		String cacheKey = CacheConstants.REDIS_CACHE_KEY_CUSTOMER_LATITUDE_LONGITUDE
				+ customerId;
		JedisDemo.cache.setex(cacheKey,
				CacheConstants.REDIS_CACHE_SECONDS_CUSTOMER_LATITUDE_LONGITUDE,
				latitude + "#" + longitude);
	}

	public String getCustomerLatitudeAndLongitude(int customerId) {
		return JedisDemo.cache
				.get(CacheConstants.REDIS_CACHE_KEY_CUSTOMER_LATITUDE_LONGITUDE
						+ customerId);
	}
}
