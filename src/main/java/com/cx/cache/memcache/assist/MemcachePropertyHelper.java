package com.cx.cache.memcache.assist;

import java.util.Properties;

import com.cx.common.PropertiesLoader;


public class MemcachePropertyHelper {
	private static final String CONFIG_FIle = "memcache.properties";

	private static Properties properties = null;

	static {
		properties = PropertiesLoader.loadProperties(CONFIG_FIle);
	}

	public static String getValue(String key) {
		return properties.getProperty(key);
	}
}
