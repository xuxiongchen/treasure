package com.cx.cache.demo.jvm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cx.cache.jvmcache.JVMTimeLimitLRUCache;
import com.cx.common.CacheConstants;
import com.cx.common.exception.ServiceException;
import com.cx.common.utils.DateCoverd;

/**
 * 
 * 一般我们会选择将字典值缓存到jvm中
 * 
 * @author chenxu
 *
 */
public class JvmCache {
	private static final JVMTimeLimitLRUCache<String, List<Dictionary>> CACHE_DICTIONARY_ENTITYS = new JVMTimeLimitLRUCache<String, List<Dictionary>>(
			1);
	
	public List<Dictionary> getServiceAreas()
			throws ServiceException {
		List<Dictionary> serviceAreas = CACHE_DICTIONARY_ENTITYS
				.get(CacheConstants.JVM_CACHE_DICTIONARY_ENTITYS);
		if (serviceAreas == null) {
			
			//serviceAreas = dictionaryDao.listAll();
			//TODO 从数据库中获取得到字典值
			
			if (serviceAreas == null) {
				serviceAreas = new ArrayList<Dictionary>();
			}
			CACHE_DICTIONARY_ENTITYS
					.put(CacheConstants.JVM_CACHE_DICTIONARY_ENTITYS,
							serviceAreas,
							DateCoverd
									.addMinute(
											new Date(),
											CacheConstants.JVM_CACHE_MIN_DICTIONARY_ENTITYS));
		}
		return serviceAreas;
	}
}
/**
 * 
 * 自定义的字典实体
 * 
 * @author chenxu
 *
 */
class Dictionary{
	
	private String id;
	private String code;
	private String value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}