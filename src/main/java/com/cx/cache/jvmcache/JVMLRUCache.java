package com.cx.cache.jvmcache;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JVMLRUCache 项目中new一次后 全局使用
 * 
 * 
 * @param <K>
 * @param <V>
 */
public class JVMLRUCache<K, V> {
	private static final float hashTableLoadFactor = 0.75f;
	private LinkedHashMap<K, V> map;
	private int cacheSize;

	/**
	 * 构造函数初始化1个指定size的jvm缓存，项目中new一次后 全局使用
	 * 
	 * @param cacheSize
	 */
	public JVMLRUCache(int cacheSize) {
		this.cacheSize = cacheSize;
		int hashTableCapacity = (int) Math
				.ceil(cacheSize / hashTableLoadFactor) + 1;
		//accessOrder - 排序模式 - 对于访问顺序，为 true；对于插入顺序，则为 false 
		map = new LinkedHashMap<K, V>(hashTableCapacity, hashTableLoadFactor,
				false) {
			private static final long serialVersionUID = 1;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				return size() > JVMLRUCache.this.cacheSize;
			}
		};
	}

	public V get(K key) {
		return map.get(key);
	}

	public synchronized void put(K key, V value) {
		map.put(key, value);
	}

	public synchronized void remove(K key) {
		map.remove(key);
	}

	public synchronized void clear() {
		map.clear();
	}

	public synchronized int usedEntries() {
		return map.size();
	}

	public synchronized Collection<Map.Entry<K, V>> getAll() {
		return new ArrayList<Map.Entry<K, V>>(map.entrySet());
	}

	public synchronized Map<K, V> getMap() {
		return map;
	}
}
