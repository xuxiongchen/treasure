
package com.cx.cache.memcache.assist;

import org.apache.commons.lang3.StringUtils;


public class Config_XMemcache {
	/* memcache服务端节点；多个节点之间用空格分隔； 例如：host1:12000 host2:12000 */
	private String servers;
	/* 带有备用节点的memcache服务端节点；多个节点之间用空格分隔，主备节点之间使用逗号分隔； */
	/* 例如：host1:12000,host2:12000 host2:12001,host1:12001 */
	private String serverswithslave;
	/* memcache服务端各节点的权重;为null时将各服务器的权重设置为相等;多个使用逗号分隔 */
	private String weights;
	/* 链接池大小 */
	private int connectionpoolsize = 3;
	/* 操作超时时间 */
	private int optimeout = 2000;
	/* 链接超时时间 */
	private int connecttimeout = 15000;

	/**
	 * 构造函数，从配置文件中加载配置项
	 */
	public Config_XMemcache() {
		this.servers = MemcachePropertyHelper.getValue("servers");
		this.serverswithslave = MemcachePropertyHelper
				.getValue("serverswithslave");
		this.weights = MemcachePropertyHelper.getValue("weights");
		if (StringUtils.isNumeric(MemcachePropertyHelper
				.getValue("connectionpoolsize"))) {
			this.connectionpoolsize = Integer.parseInt(MemcachePropertyHelper
					.getValue("connectionpoolsize"));
		}
		if (StringUtils.isNumeric(MemcachePropertyHelper.getValue("optimeout"))) {
			this.optimeout = Integer.parseInt(MemcachePropertyHelper
					.getValue("optimeout"));
		}
		if (StringUtils.isNumeric(MemcachePropertyHelper
				.getValue("connecttimeout"))) {
			this.connecttimeout = Integer.parseInt(MemcachePropertyHelper
					.getValue("connecttimeout"));
		}
	}

	/**
	 * @param servers
	 *            ：memcache服务端节点；多个节点之间用空格分隔； ==>例如：host1:12000 host2:12000
	 * @param serverswithslave
	 *            ：带有备用节点的memcache服务端节点；多个节点之间用空格分隔，主备节点之间使用逗号分隔；
	 *            ==>例如：host1:12000,host2:12000 host2:12001,host1:12001
	 * @param weights
	 *            ：memcache服务端各节点的权重;为null时将各服务器的权重设置为相等;多个使用逗号分隔
	 * @throws Exception
	 */
	public Config_XMemcache(String servers, String serverswithslave,
			String weights) {
		this.servers = servers;
		this.serverswithslave = serverswithslave;
		this.weights = weights;
	}

	public int getConnectionpoolsize() {
		return connectionpoolsize;
	}

	public void setConnectionpoolsize(int connectionpoolsize) {
		if (connectionpoolsize > 0) {
			this.connectionpoolsize = connectionpoolsize;
		}
	}

	public int getOptimeout() {
		return optimeout;
	}

	public void setOptimeout(int optimeout) {
		if (optimeout >= 500) {
			this.optimeout = optimeout;
		}
	}

	public int getConnecttimeout() {
		return connecttimeout;
	}

	public void setConnecttimeout(int connecttimeout) {
		if (connecttimeout >= 1000) {
			this.connecttimeout = connecttimeout;
		}
	}

	public String getServers() {
		return servers;
	}

	public String getServerswithslave() {
		return serverswithslave;
	}

	public String getWeights() {
		return weights;
	}

}
