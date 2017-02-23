package com.cxdb.datasource.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cxdb.common.Errorcode;
import com.cxdb.common.ServiceException;
import com.cxdb.datasource.DaoHelper;
import com.mchange.v2.c3p0.DataSources;
/**
 * 连接池工具
 * @author chenxu
 *
 */
public class DBPoolManager {
	private final Logger logger = LoggerFactory.getLogger(DBPoolManager.class);
	// 单例
	private static DBPoolManager poolManager = null;

	private DBPoolManager() {
		// 初始化数据库连接池
		try {
			initpools();
		} catch (ServiceException e) {
			logger.error("init DBPoolManager() faild", e);
		}
	}

	public synchronized static DBPoolManager getInstance() {
		if (poolManager == null) {
			poolManager = new DBPoolManager();
		}
		return poolManager;
	}

	private static Map<String, DataSource> poollist = new ConcurrentHashMap<String, DataSource>();

	// 初始化数据库连接池
	private void initpools() throws ServiceException {
		try {
			Map<String, Map<String, String>> poolconfig = getPoolConfig();
			Set<String> sets = poolconfig.keySet();
			Iterator<String> iter = sets.iterator();
			while (iter.hasNext()) {
				String poolName = iter.next();
				Map<String, String> map = poolconfig.get(poolName);
				String driverClassName = StringUtils.strip(map
						.get("driverClassName"));
				String url = StringUtils.strip(map.get("url"));
				String user = StringUtils.strip(map.get("username"));
				String password = StringUtils.strip(map.get("password"));
				// 配置连接池
				Map<String, String> config = new HashMap<String, String>();

				String minPoolSize = StringUtils.defaultString(
						StringUtils.strip(map.get("minPoolSize")), "10");
				config.put("minPoolSize", minPoolSize);

				String maxPoolSize = StringUtils.defaultString(
						StringUtils.strip(map.get("maxPoolSize")), "200");
				config.put("maxPoolSize", maxPoolSize);

				String initialPoolSize = StringUtils.defaultString(
						StringUtils.strip(map.get("initialPoolSize")), "20");
				config.put("initialPoolSize", initialPoolSize);

				String checkoutTimeout = StringUtils.defaultString(
						StringUtils.strip(map.get("checkoutTimeout")), "1000");
				config.put("checkoutTimeout", checkoutTimeout);

				String maxIdleTime = StringUtils.defaultString(
						StringUtils.strip(map.get("maxIdleTime")), "60");
				config.put("maxIdleTime", maxIdleTime);

				String acquireIncrement = StringUtils.defaultString(
						StringUtils.strip(map.get("acquireIncrement")), "5");
				config.put("acquireIncrement", acquireIncrement);

				String acquireRetryAttempts = StringUtils.defaultString(
						StringUtils.strip(map.get("acquireRetryAttempts")),
						"30");
				config.put("acquireRetryAttempts", acquireRetryAttempts);

				String acquireRetryDelay = StringUtils
						.defaultString(
								StringUtils.strip(map.get("acquireRetryDelay")),
								"1000");
				config.put("acquireRetryDelay", acquireRetryDelay);

				String idleConnectionTestPeriod = StringUtils.defaultString(
						StringUtils.strip(map.get("idleConnectionTestPeriod")),
						"60");
				config.put("idleConnectionTestPeriod", idleConnectionTestPeriod);

				String maxStatements = StringUtils.defaultString(
						StringUtils.strip(map.get("maxStatements")), "200");
				config.put("maxStatements", maxStatements);

				String maxStatementsPerConnection = StringUtils
						.defaultString(StringUtils.strip(map
								.get("maxStatementsPerConnection")), "0");
				config.put("maxStatementsPerConnection",
						maxStatementsPerConnection);

				String numHelperThreads = StringUtils.defaultString(
						StringUtils.strip(map.get("numHelperThreads")), "5");
				config.put("numHelperThreads", numHelperThreads);
				logger.info(driverClassName);
				Class.forName(driverClassName);
				
				// 创建unpooledDataSource
				DataSource ds_unpooled = DataSources.unpooledDataSource(url,
						user, password);
				DataSource ds_pooled = DataSources.pooledDataSource(
						ds_unpooled, config);
				if (poollist.containsKey(poolName)) {
					DataSource old_pool = poollist.get(poolName);
					DataSources.destroy(old_pool);
					poollist.remove(poolName);
				}
				poollist.put(poolName, ds_pooled);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA, "initpools faild",
					e);
		}
	}

	/**
	 * 获取数据库配置
	 * 
	 * @throws ServiceException
	 */
	private Map<String, Map<String, String>> getPoolConfig()
			throws ServiceException {
		Map<String, Map<String, String>> maps = new ConcurrentHashMap<String, Map<String, String>>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(DBPoolManager.class.getClassLoader()
					.getResourceAsStream("dbpool.xml"));
			NodeList configList = doc.getElementsByTagName("dbpool");
			for (int m = 0; configList != null && m < configList.getLength(); m++) {
				Element config_el = (Element) configList.item(m);
				String configName = config_el.getAttribute("name");
				NodeList nList = config_el.getElementsByTagName("property");
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; nList != null && i < nList.getLength(); i++) {
					Element propertye_el = (Element) nList.item(i);
					String name = propertye_el.getAttribute("name");
					String value = propertye_el.getTextContent();
					map.put(name, value);
					logger.info(name+":"+value);
				}
				maps.put(configName, map);
			}
			String debugs = doc.getElementsByTagName("debug").item(0)
					.getTextContent();
			DaoHelper.setDebug(debugs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"getPoolConfig faild", e);
		}
		return maps;
	}

	private final static String defaultpoolname = "default";

	public Connection getConnection() throws ServiceException {
		DataSource dataSource = poollist.get(defaultpoolname);
		if (dataSource == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"getConnection faild:dataSource is null");
		} else {
			try {
				return dataSource.getConnection();
			} catch (SQLException e) {
				throw new ServiceException(Errorcode.CODE_JAVA,
						"getConnection faild:SQLException", e);
			}
		}
	}

	public Connection getConnection(String poolname) throws ServiceException {
		DataSource dataSource = null;
		if (StringUtils.isBlank(poolname)) {
			dataSource = poollist.get(defaultpoolname);
		} else {
			dataSource = poollist.get(poolname);
		}
		if (dataSource == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"getConnection faild:dataSource is null");
		} else {
			try {
				return dataSource.getConnection();
			} catch (SQLException e) {
				throw new ServiceException(Errorcode.CODE_JAVA,
						"getConnection faild:SQLException", e);
			}
		}
	}

	/* 销毁连接 */
	public void destroyPools() {
		try {
			for (Entry<String, DataSource> entry : poollist.entrySet()) {
				try {
					DataSource pool = entry.getValue();
					DataSources.destroy(pool);
				} catch (Exception e) {
					logger.error("destroyPools faild", e);
				}
			}
		} catch (Exception e) {
			logger.error("destroyPools faild", e);
		}
	}
}
