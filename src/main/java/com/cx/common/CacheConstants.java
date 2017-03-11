package com.cx.common;

/**
 * redis缓存配置
 * @author chenxu
 *
 */
public interface CacheConstants {
	// validateCode(redis 30分钟)
	public static final String REDIS_CACHE_KEY_VALIDATECODE = "VALIDATECODE#";
	public static final int REDIS_CACHE_SECONDS_VALIDATECODE = 30 * 60;

	public static final String MEMECHACE_CACHE_KEY_VALIDATECODE = "VALIDATECODE#";
	public static final int MEMECHACE_CACHE_SECONDS_VALIDATECODE = 30 * 60 * 100;
	
	// sessionid(redis2个小时)
	public static final String REDIS_CACHE_KEY_GUIDE_SESSIONID = "GUIDE_SESSIONID#";
	public static final int REDIS_CACHE_SECONDS_GUIDE_SESSIONID = 2 * 60 * 60;

	// sessionid(redis2个小时)
	public static final String REDIS_CACHE_KEY_CUSTOMER_SESSIONID = "CUSTOMER_SESSIONID#";
	public static final int REDIS_CACHE_SECONDS_CUSTOMER_SESSIONID = 2 * 60 * 60;

	// SupportedServiceAreas(JVM2分钟)
	public static final String JVM_CACHE_DICTIONARY_ENTITYS = "SUPPORTEDSERVICEAREAS";
	public static final int JVM_CACHE_MIN_DICTIONARY_ENTITYS = 2;

	// SupportedServiceArea_ENTITYS(JVM2分钟)
	public static final String JVM_CACHE_KEY_SUPPORTEDSERVICEAREA_ENTITYS = "SUPPORTEDSERVICEAREA_ENTITYS";
	public static final int JVM_CACHE_MINS_SUPPORTEDSERVICEAREA_ENTITYS = 2;

	// 导购的经纬度(redis15分钟)
	public static final String REDIS_CACHE_KEY_GUIDE_LATITUDE_LONGITUDE = "GUIDE_LATITUDE_LONGITUDE#";
	public static final int REDIS_CACHE_SECONDS_GUIDE_LATITUDE_LONGITUDE = 15 * 60;

	// 用户的经纬度(redis15分钟)
	public static final String REDIS_CACHE_KEY_CUSTOMER_LATITUDE_LONGITUDE = "CUSTOMER_LATITUDE_LONGITUDE#";
	public static final int REDIS_CACHE_SECONDS_CUSTOMER_LATITUDE_LONGITUDE = 15 * 60;

	// DictionaryConsumptionAmounts(JVM2分钟)
	public static final String JVM_CACHE_KEY_DICTIONARYCONSUMPTIONAMOUNTS = "DICTIONARYCONSUMPTIONAMOUNTS";
	public static final int JVM_CACHE_MINS_DICTIONARYCONSUMPTIONAMOUNTS = 2;

	// dictionaryGoodsTypes(JVM2分钟)
	public static final String JVM_CACHE_KEY_DICTIONARYGOODSTYPES = "DICTIONARYGOODSTYPES";
	public static final int JVM_CACHE_MINS_DICTIONARYGOODSTYPES = 2;

}
