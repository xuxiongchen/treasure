package com.cx.cache.memcache.assist;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class XMemcacheHelper {

	public static MemcachedClient initMemcachedClient(Config_XMemcache config)
			throws Exception {
		MemcachedClient client = null;
		int[] weights = null;
		// 处理权重
		if (config.getWeights() != null
				&& config.getWeights().trim().length() > 0) {
			String[] weights_tmp = config.getWeights().split(",");
			if (weights_tmp != null && weights_tmp.length > 0) {
				weights = new int[weights_tmp.length];
				for (int i = 0; i < weights_tmp.length; i++) {
					weights[i] = Integer.parseInt(weights_tmp[i]);
				}
			}
		}
		// 构建MemcachedClientBuilder
		MemcachedClientBuilder builder = null;
		if (config.getServers() != null
				&& config.getServers().trim().length() > 0) {
			List<InetSocketAddress> addressList = AddrUtil.getAddresses(config
					.getServers());
			if (weights != null && weights.length == addressList.size()) {
				builder = new XMemcachedClientBuilder(addressList, weights);
			} else {
				builder = new XMemcachedClientBuilder(addressList);
			}
		} else if (config.getServerswithslave() != null
				&& config.getServerswithslave().trim().length() > 0) {
			Map<InetSocketAddress, InetSocketAddress> addressMap = AddrUtil
					.getAddressMap(config.getServerswithslave());
			if (weights != null && weights.length == addressMap.size()) {
				builder = new XMemcachedClientBuilder(addressMap, weights);
			} else {
				builder = new XMemcachedClientBuilder(addressMap);
			}
		} else {
			throw new Exception(
					"all of memservers & memserverswithslave is null");
		}
		// 设置链接池大小
		builder.setConnectionPoolSize(config.getConnectionpoolsize());
		// 设置链接超时时间
		builder.setConnectTimeout(config.getConnecttimeout());
		// 设置cache操作超时时间
		builder.setOpTimeout(config.getOptimeout());
		client = builder.build();
		return client;
	}
}
