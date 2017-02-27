package com.cx.cache.memcache;


import java.util.Date;

public interface ICache {
	/**
	 * 添加一个Object到缓存中.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, Object value);

	/**
	 * 方法概要: 添加一个指定的值到缓存，并设定失效日期
	 * 
	 * @param key
	 * @param value
	 * @param expiry
	 * @return
	 */
	public boolean add(String key, Object value, Date expiry);

	/**
	 * 方法概要: 添加一个指定的值到缓存，并设定失效时间，距离现在的秒数
	 * 
	 * @param key
	 * @param value
	 * @param exp
	 */

	/**
	 * 方法概要: 删除缓存中对应的值
	 * 
	 * @param key
	 * @return
	 */
	public boolean delete(String key);

	public boolean set(String key, Object value);

	public boolean set(String key, Object value, Date expiry);

	/**
	 * 方法概要: 替换缓存中的值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean replace(String key, Object value);

	/**
	 * 
	 * 方法概要: 替换缓存中的值,并设置失效日期
	 * 
	 * @param key
	 * @param value
	 * @param expiry
	 * @return
	 */
	public boolean replace(String key, Object value, Date expiry);

	public String getString(String key);

	/**
	 * 根据指定的关键字获取对象.
	 * 
	 * @param key
	 * @return
	 */
	public <T> T get(String key);

	/**
	 * 
	 * 方法概要:清空所有对象
	 * 
	 * @return
	 */
	public boolean clearAll();

	public void shutdown();

}
