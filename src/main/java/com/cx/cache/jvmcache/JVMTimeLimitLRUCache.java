package com.cx.cache.jvmcache;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JVMLRUCache 项目中new一次后 全局使用
 * 
 * @param <K>
 * @param <V>
 */
public class JVMTimeLimitLRUCache<K, V> {
	private static final float hashTableLoadFactor = 0.75f;
	private LinkedHashMap<K, TimeLimitObject<V>> map;
	private int cacheSize;

	/**
	 * 构造函数初始化1个指定size的jvm缓存，项目中new一次后 全局使用
	 * 
	 * @param cacheSize
	 */
	public JVMTimeLimitLRUCache(int cacheSize) {
		this.cacheSize = cacheSize;
		int hashTableCapacity = (int) Math
				.ceil(cacheSize / hashTableLoadFactor) + 1;
		//accessOrder - 排序模式 - 对于访问顺序，为 true；对于插入顺序，则为 false 
		map = new LinkedHashMap<K, TimeLimitObject<V>>(hashTableCapacity,
				hashTableLoadFactor, false ) {
			private static final long serialVersionUID = 1;

			@Override
			protected boolean removeEldestEntry(
					Map.Entry<K, TimeLimitObject<V>> eldest) {
				return size() > JVMTimeLimitLRUCache.this.cacheSize;
			}
		};
	}

	public V get(K key) {
		TimeLimitObject<V> tlObj = map.get(key);
		if (tlObj == null) {
			return null;
		}
		V value = tlObj.getValue();
		if (value == null) {
			remove(key);
		}
		return value;
	}

	public synchronized void put(K key, V value) {
		TimeLimitObject<V> tlObj = new TimeLimitObject<V>(value, null);
		map.put(key, tlObj);
	}

	public synchronized void put(K key, V value, Date expiry) {
		TimeLimitObject<V> tlObj = new TimeLimitObject<V>(value, expiry);
		map.put(key, tlObj);
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

}

class TimeLimitObject<V> {
	private long losetime = 0;
	private V value;

	public TimeLimitObject(V value, Date expiry) {
		this.value = value;
		if (expiry != null) {
			this.losetime = expiry.getTime();
		}
	}

	public V getValue() {
		if (losetime == 0 || System.currentTimeMillis() < losetime) {
			return value;
		} else {
			return null;
		}
	}
}
