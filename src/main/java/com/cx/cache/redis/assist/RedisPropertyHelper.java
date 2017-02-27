package com.cx.cache.redis.assist;

import java.util.Properties;

import com.cx.common.PropertiesLoader;


public class RedisPropertyHelper {
	private static final String CONFIG_FIle = "redis.properties";

	private static Properties properties = null;

	static {
		properties = PropertiesLoader.loadProperties(CONFIG_FIle);
	}

	public static String getValue(String key) {
		return properties.getProperty(key);
	}
}