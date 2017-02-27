package com.cx.cache.jvmcache;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对java的ConcurrentHashMap简单封装；处理掉key或value为null的报错
 * 
 * @param <K>
 * @param <V>
 */
public class ConcurrentMapImpl<K, V> extends ConcurrentHashMap<K, V> implements
		Map<K, V> {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean contains(Object value) {
		if (value != null) {
			return super.contains(value);
		} else {
			return false;
		}
	}

	@Override
	public boolean containsKey(Object key) {
		if (key != null) {
			return super.containsKey(key);
		} else {
			return false;
		}
	}

	@Override
	public boolean containsValue(Object value) {
		if (value != null) {
			return super.containsValue(value);
		} else {
			return false;
		}
	}

	@Override
	public V put(K key, V value) {
		if (key != null && value != null) {
			return super.put(key, value);
		} else {
			return null;
		}
	}

	@Override
	public V putIfAbsent(K key, V value) {
		if (key != null && value != null) {
			return super.putIfAbsent(key, value);
		} else {
			return null;
		}
	}

	@Override
	public V get(Object key) {
		if (key != null) {
			return super.get(key);
		} else {
			return null;
		}
	}

	@Override
	public V remove(Object key) {
		if (key != null) {
			return super.remove(key);
		} else {
			return null;
		}
	}

	@Override
	public boolean remove(Object key, Object value) {
		if (key != null && value != null) {
			return super.remove(key, value);
		} else {
			return false;
		}
	}

	@Override
	public V replace(K key, V value) {
		if (key != null && value != null) {
			return super.replace(key, value);
		} else {
			return null;
		}
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		if (key != null && oldValue != null && newValue != null) {
			return super.replace(key, oldValue, newValue);
		} else {
			return false;
		}
	}
}
