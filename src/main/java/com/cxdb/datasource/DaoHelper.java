package com.cxdb.datasource;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cxdb.common.Errorcode;
import com.cxdb.common.ServiceException;
import com.cxdb.datasource.model.Column;
import com.cxdb.datasource.model.DaoResource;
import com.cxdb.datasource.model.PageBean;
import com.cxdb.datasource.model.Table;
import com.cxdb.datasource.pool.DBPoolManager;
import com.cxdb.reflect.Reflector;
import com.cxdb.reflect.factory.DefaultObjectFactory;
import com.cxdb.reflect.factory.ObjectFactory;
import com.cxdb.transation.EasyTransaction;


public class DaoHelper {
	private static final Logger logger = LoggerFactory
			.getLogger(DaoHelper.class);
	private static Set<Class<?>> simpleType = new HashSet<Class<?>>();
	public static Map<Integer, Class<?>> sql2javaType = new HashMap<Integer, Class<?>>();
	static {
		// simpleType
		simpleType.add(boolean.class);
		simpleType.add(Boolean.class);
		simpleType.add(String.class);
		simpleType.add(char.class);
		simpleType.add(Character.class);
		simpleType.add(byte.class);
		simpleType.add(Byte.class);
		simpleType.add(Integer.class);
		simpleType.add(int.class);
		simpleType.add(Long.class);
		simpleType.add(long.class);
		simpleType.add(Short.class);
		simpleType.add(short.class);
		simpleType.add(Float.class);
		simpleType.add(float.class);
		simpleType.add(Double.class);
		simpleType.add(double.class);
		simpleType.add(java.util.Date.class);
		simpleType.add(java.util.UUID.class);
		// sql2javaType
		sql2javaType.put(java.sql.Types.BIGINT, Long.class);
		sql2javaType.put(java.sql.Types.INTEGER, Integer.class);
		sql2javaType.put(java.sql.Types.ARRAY, java.sql.Array.class);
		sql2javaType.put(java.sql.Types.BLOB, java.sql.Blob.class);
		sql2javaType.put(java.sql.Types.CLOB, java.sql.Clob.class);
		sql2javaType.put(java.sql.Types.BOOLEAN, Boolean.class);
		sql2javaType.put(java.sql.Types.CHAR, String.class);
		sql2javaType.put(java.sql.Types.DATE, java.util.Date.class);
		sql2javaType.put(java.sql.Types.DECIMAL, Double.class);
		sql2javaType.put(java.sql.Types.SMALLINT, Integer.class);
		sql2javaType.put(java.sql.Types.TINYINT, Integer.class);
		sql2javaType.put(java.sql.Types.TIME, java.util.Date.class);
		sql2javaType.put(java.sql.Types.TIMESTAMP, java.util.Date.class);
		sql2javaType.put(java.sql.Types.VARCHAR, String.class);
		sql2javaType.put(java.sql.Types.NUMERIC, Double.class);

		sql2javaType.put(java.sql.Types.STRUCT, java.sql.Struct.class);
		sql2javaType.put(java.sql.Types.REAL, Float.class);
		sql2javaType.put(java.sql.Types.LONGVARCHAR, String.class);
		sql2javaType.put(java.sql.Types.FLOAT, Float.class);
		sql2javaType.put(java.sql.Types.DOUBLE, Double.class);

		sql2javaType.put(java.sql.Types.BINARY, byte[].class);
		sql2javaType.put(java.sql.Types.BIT, Boolean.class);
		sql2javaType.put(java.sql.Types.REF, Object.class);
		sql2javaType.put(java.sql.Types.VARBINARY, byte[].class);
		sql2javaType.put(java.sql.Types.LONGVARBINARY, byte[].class);
	}

	private static boolean checkedSimpleType(Class<?> t) {
		if (simpleType.contains(t)) {
			return true;
		}
		return false;
	}

	/**
	 * order by的正则表达式
	 */
	private static final String ORDER_REGEX = "(order|ORDER)\\s+(by|BY)\\s+(\\w+[.|_|'|,|(|)|:|\\w*]*(\\s+[DESC|desc|ASC|asc]*)*)(\\s*,\\s*\\w+[.|_|'|,|(|)|:|\\w*]*(\\s+[DESC|desc|ASC|asc]*)*)*\\s*";
	private static final String BLANK_CHAR = " ";

	static String getInsertsql(Object obj, List<Object> params)
			throws ServiceException {
		StringBuffer insertsql = new StringBuffer();
		try {
			insertsql.append("insert into ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			insertsql.append(reflector.getTableName());
			StringBuffer colPart = new StringBuffer();
			StringBuffer pstatvalues = new StringBuffer();
			String[] propertyNames = reflector.getGetablePropertyNames();
			int num = 1;
			for (int i = 0; i < propertyNames.length; i++) {
				String propertyName = propertyNames[i];
				if (!reflector.fieldIgnore(propertyName)
						&& checkedSimpleType(reflector
								.getGetterType(propertyName))) {
					Object value = reflector.getGetInvoker(propertyName)
							.invoke(obj, null);
					if (value != null) {
						if (num > 1) {
							colPart.append(",");
							pstatvalues.append(",");
						}
						colPart.append(reflector.getColumn(propertyName));
						pstatvalues.append("?");
						params.add(value);
						num++;
					}
				}
			}
			insertsql.append(" (");
			insertsql.append(colPart);
			insertsql.append(") values (");
			insertsql.append(pstatvalues);
			insertsql.append(")");
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getInsertsql faild", e);
		}
		return insertsql.toString();
	}

	static String getDeletesql(Object obj, List<Object> params,
			String... whereproperties) throws ServiceException {
		StringBuffer deletesql = new StringBuffer();
		try {
			deletesql.append("delete from ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			deletesql.append(reflector.getTableName());
			if (whereproperties != null && whereproperties.length > 0) {
				int num = 1;
				for (int i = 0; i < whereproperties.length; i++) {
					String whereproperty = whereproperties[i];
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						deletesql.append(" where ");
					} else {
						deletesql.append(" and ");
					}
					deletesql.append(reflector.getColumn(whereproperty));
					deletesql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getDeletesql faild", e);
		}
		return deletesql.toString();
	}

	static String getDeletesql(Object obj, List<Object> params,
			List<String> whereproperties) throws ServiceException {
		StringBuffer deletesql = new StringBuffer();
		try {
			deletesql.append("delete from ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			deletesql.append(reflector.getTableName());
			if (whereproperties != null && whereproperties.size() > 0) {
				int num = 1;
				for (int i = 0; i < whereproperties.size(); i++) {
					String whereproperty = whereproperties.get(i);
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						deletesql.append(" where ");
					} else {
						deletesql.append(" and ");
					}
					deletesql.append(reflector.getColumn(whereproperty));
					deletesql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getDeletesql faild", e);
		}
		return deletesql.toString();
	}

	static String getUpdatesql(Object obj, List<Object> params,
			boolean allProperties, String... whereproperties)
			throws ServiceException {
		StringBuffer updatesql = new StringBuffer();
		try {
			updatesql.append("update ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			updatesql.append(reflector.getTableName());
			updatesql.append(" set ");
			List<String> wherepropertieslist = new ArrayList<String>();
			if (whereproperties != null && whereproperties.length > 0) {
				wherepropertieslist = (List<String>) Arrays
						.asList(whereproperties);
			}
			String[] propertyNames = reflector.getGetablePropertyNames();
			int num = 1;
			for (int i = 0; i < propertyNames.length; i++) {
				String propertyName = propertyNames[i];
				if (!wherepropertieslist.contains(propertyName)
						&& !reflector.fieldIgnore(propertyName)
						&& checkedSimpleType(reflector
								.getGetterType(propertyName))) {
					Object value = reflector.getGetInvoker(propertyName)
							.invoke(obj, null);
					if (value != null || allProperties) {
						if (num > 1) {
							updatesql.append(",");
						}
						updatesql.append(reflector.getColumn(propertyName));
						updatesql.append("=?");
						params.add(value);
						num++;
					}
				}
			}
			// where
			if (whereproperties != null && whereproperties.length > 0) {
				num = 1;
				for (int i = 0; i < whereproperties.length; i++) {
					String whereproperty = whereproperties[i];
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						updatesql.append(" where ");
					} else {
						updatesql.append(" and ");
					}
					updatesql.append(reflector.getColumn(whereproperty));
					updatesql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getUpdatesql faild", e);
		}
		return updatesql.toString();
	}

	static String getUpdatesql(Object obj, List<Object> params,
			boolean allProperties, List<String> whereproperties)
			throws ServiceException {
		StringBuffer updatesql = new StringBuffer();
		try {
			updatesql.append("update ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			updatesql.append(reflector.getTableName());
			updatesql.append(" set ");
			List<String> wherepropertieslist = new ArrayList<String>();
			if (whereproperties != null && whereproperties.size() > 0) {
				wherepropertieslist = whereproperties;
			}
			String[] propertyNames = reflector.getGetablePropertyNames();
			int num = 1;
			for (int i = 0; i < propertyNames.length; i++) {
				String propertyName = propertyNames[i];
				if (!wherepropertieslist.contains(propertyName)
						&& !reflector.fieldIgnore(propertyName)
						&& checkedSimpleType(reflector
								.getGetterType(propertyName))) {
					Object value = reflector.getGetInvoker(propertyName)
							.invoke(obj, null);
					if (value != null || allProperties) {
						if (num > 1) {
							updatesql.append(",");
						}
						updatesql.append(reflector.getColumn(propertyName));
						updatesql.append("=?");
						params.add(value);
						num++;
					}
				}
			}
			// where
			if (whereproperties != null && whereproperties.size() > 0) {
				num = 1;
				for (int i = 0; i < whereproperties.size(); i++) {
					String whereproperty = whereproperties.get(i);
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						updatesql.append(" where ");
					} else {
						updatesql.append(" and ");
					}
					updatesql.append(reflector.getColumn(whereproperty));
					updatesql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getUpdatesql faild", e);
		}
		return updatesql.toString();
	}

	static String getCountsql(Object obj, List<Object> params,
			String... whereproperties) throws ServiceException {
		StringBuffer countsql = new StringBuffer();
		try {
			countsql.append("select count(*) from ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			countsql.append(reflector.getTableName());
			if (whereproperties != null && whereproperties.length > 0) {
				int num = 1;
				for (int i = 0; i < whereproperties.length; i++) {
					String whereproperty = whereproperties[i];
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						countsql.append(" where ");
					} else {
						countsql.append(" and ");
					}
					countsql.append(reflector.getColumn(whereproperty));
					countsql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getCountsql faild", e);
		}
		return countsql.toString();
	}

	static String getCountsql(Object obj, List<Object> params,
			List<String> whereproperties) throws ServiceException {
		StringBuffer countsql = new StringBuffer();
		try {
			countsql.append("select count(*) from ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			countsql.append(reflector.getTableName());
			if (whereproperties != null && whereproperties.size() > 0) {
				int num = 1;
				for (int i = 0; i < whereproperties.size(); i++) {
					String whereproperty = whereproperties.get(i);
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						countsql.append(" where ");
					} else {
						countsql.append(" and ");
					}
					countsql.append(reflector.getColumn(whereproperty));
					countsql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getCountsql faild", e);
		}
		return countsql.toString();
	}

	static String getCountsql(String sql) throws ServiceException {
		StringBuilder countsql = new StringBuilder();
		countsql.append("select count(*) totalnum from ( ");
		countsql.append(sql.replaceAll(ORDER_REGEX, BLANK_CHAR));
		countsql.append(" ) as tcount");
		return countsql.toString();
	}

	static String getQuerysql(Object obj, List<Object> params,
			String... whereproperties) throws ServiceException {
		StringBuffer querysql = new StringBuffer();
		try {
			querysql.append("select * from ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			querysql.append(reflector.getTableName());
			if (whereproperties != null && whereproperties.length > 0) {
				int num = 1;
				for (int i = 0; i < whereproperties.length; i++) {
					String whereproperty = whereproperties[i];
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						querysql.append(" where ");
					} else {
						querysql.append(" and ");
					}
					querysql.append(reflector.getColumn(whereproperty));
					querysql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getQuerysql faild", e);
		}
		return querysql.toString();
	}

	static String getQuerysql(PageBean pageBean, Object obj,
			List<Object> params, String... whereproperties)
			throws ServiceException {
		StringBuffer querysql = new StringBuffer();
		try {
			querysql.append("select * from ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			querysql.append(reflector.getTableName());
			if (whereproperties != null && whereproperties.length > 0) {
				int num = 1;
				for (int i = 0; i < whereproperties.length; i++) {
					String whereproperty = whereproperties[i];
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						querysql.append(" where ");
					} else {
						querysql.append(" and ");
					}
					querysql.append(reflector.getColumn(whereproperty));
					querysql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
			// 分页查询
			querysql.append(" limit ");
			querysql.append(pageBean.getPageSize());
			querysql.append(" offset ");
			querysql.append(pageBean.getStartNum());
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getQuerysql faild", e);
		}
		return querysql.toString();
	}

	static String getQuerysql(Object obj, List<Object> params,
			List<String> whereproperties) throws ServiceException {
		StringBuffer querysql = new StringBuffer();
		try {
			querysql.append("select * from ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			querysql.append(reflector.getTableName());
			if (whereproperties != null && whereproperties.size() > 0) {
				int num = 1;
				for (int i = 0; i < whereproperties.size(); i++) {
					String whereproperty = whereproperties.get(i);
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						querysql.append(" where ");
					} else {
						querysql.append(" and ");
					}
					querysql.append(reflector.getColumn(whereproperty));
					querysql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getQuerysql faild", e);
		}
		return querysql.toString();
	}

	static String getQuerysql(PageBean pageBean, Object obj,
			List<Object> params, List<String> whereproperties)
			throws ServiceException {
		StringBuffer querysql = new StringBuffer();
		try {
			querysql.append("select * from ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			querysql.append(reflector.getTableName());
			if (whereproperties != null && whereproperties.size() > 0) {
				int num = 1;
				for (int i = 0; i < whereproperties.size(); i++) {
					String whereproperty = whereproperties.get(i);
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						querysql.append(" where ");
					} else {
						querysql.append(" and ");
					}
					querysql.append(reflector.getColumn(whereproperty));
					querysql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
			// 分页查询
			querysql.append(" limit ");
			querysql.append(pageBean.getPageSize());
			querysql.append(" offset ");
			querysql.append(pageBean.getStartNum());
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getQuerysql faild", e);
		}
		return querysql.toString();
	}

	static String getQuerysql(Object obj, int pagenum, int pagesize,
			List<Object> params, String... whereproperties)
			throws ServiceException {
		StringBuffer querysql = new StringBuffer();
		try {
			querysql.append("select * from ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			querysql.append(reflector.getTableName());
			if (whereproperties != null && whereproperties.length > 0) {
				int num = 1;
				for (int i = 0; i < whereproperties.length; i++) {
					String whereproperty = whereproperties[i];
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						querysql.append(" where ");
					} else {
						querysql.append(" and ");
					}
					querysql.append(reflector.getColumn(whereproperty));
					querysql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
			// 处理分页
			if (pagenum <= 0) {
				pagenum = 1;
			}
			int startnum = (pagenum - 1) * pagesize;
			querysql.append(" limit ");
			querysql.append(pagesize);
			querysql.append(" offset ");
			querysql.append(startnum);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getQuerysql faild", e);
		}
		return querysql.toString();
	}

	static String getQuerysql(Object obj, int pagenum, int pagesize,
			List<Object> params, List<String> whereproperties)
			throws ServiceException {
		StringBuffer querysql = new StringBuffer();
		try {
			querysql.append("select * from ");
			Reflector reflector = Reflector.forClass(obj.getClass());
			querysql.append(reflector.getTableName());
			if (whereproperties != null && whereproperties.size() > 0) {
				int num = 1;
				for (int i = 0; i < whereproperties.size(); i++) {
					String whereproperty = whereproperties.get(i);
					Object value = reflector.getGetInvoker(whereproperty)
							.invoke(obj, null);
					// if (value != null) {
					if (num == 1) {
						querysql.append(" where ");
					} else {
						querysql.append(" and ");
					}
					querysql.append(reflector.getColumn(whereproperty));
					querysql.append("=?");
					params.add(value);
					num++;
					// }
				}
			}
			// 处理分页
			if (pagenum <= 0) {
				pagenum = 1;
			}
			int startnum = (pagenum - 1) * pagesize;
			querysql.append(" limit ");
			querysql.append(pagesize);
			querysql.append(" offset ");
			querysql.append(startnum);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.getQuerysql faild", e);
		}
		return querysql.toString();
	}

	static String getPageSql(String sql, PageBean pageBean) {
		StringBuilder pagesql = new StringBuilder();
		pagesql.append(sql);
		pagesql.append(" limit ");
		pagesql.append(pageBean.getPageSize());
		pagesql.append(" offset ");
		pagesql.append(pageBean.getStartNum());
		return pagesql.toString();
	}

	static String getPageSql(String sql, int pagenum, int pagesize) {
		StringBuilder pagesql = new StringBuilder();
		pagesql.append(sql);
		int startnum = (pagenum - 1) * pagesize;
		pagesql.append(" limit ");
		pagesql.append(pagesize);
		pagesql.append(" offset ");
		pagesql.append(startnum);
		return pagesql.toString();
	}

	protected static void setParamValue(PreparedStatement pstat,
			List<Object> params) throws SQLException {
		if (params != null && params.size() > 0) {
			int i = 0;
			for (Object param : params) {
				++i;
				if (param == null) {
					pstat.setObject(i, param);
					continue;
				}
				if (param instanceof String) {
					pstat.setString(i, (String) param);
				} else if (param instanceof Integer) {
					pstat.setInt(i, ((Integer) param).intValue());
				} else if (param instanceof Long) {
					pstat.setLong(i, ((Long) param).longValue());
				} else if (param instanceof Float) {
					pstat.setFloat(i, ((Float) param).floatValue());
				} else if (param instanceof Double) {
					pstat.setDouble(i, ((Double) param).doubleValue());
				} else if (param instanceof java.sql.Date) {
					pstat.setDate(i, (java.sql.Date) param);
				} else if (param instanceof java.util.Date) {
					java.sql.Timestamp tmsp = new java.sql.Timestamp(
							((java.util.Date) param).getTime());
					pstat.setTimestamp(i, tmsp);
				} else {
					pstat.setObject(i, param);
				}
			}
		}
	}

	protected static void setParamValue(PreparedStatement pstat,
			Object... params) throws SQLException {
		if (params != null && params.length > 0) {
			int i = 0;
			for (Object param : params) {
				++i;
				if (param == null) {
					pstat.setObject(i, param);
					continue;
				}
				if (param instanceof String) {
					pstat.setString(i, (String) param);
				} else if (param instanceof Integer) {
					pstat.setInt(i, ((Integer) param).intValue());
				} else if (param instanceof Long) {
					pstat.setLong(i, ((Long) param).longValue());
				} else if (param instanceof Float) {
					pstat.setFloat(i, ((Float) param).floatValue());
				} else if (param instanceof Double) {
					pstat.setDouble(i, ((Double) param).doubleValue());
				} else if (param instanceof java.sql.Date) {
					pstat.setDate(i, (java.sql.Date) param);
				} else if (param instanceof java.util.Date) {
					java.sql.Timestamp tmsp = new java.sql.Timestamp(
							((java.util.Date) param).getTime());
					pstat.setTimestamp(i, tmsp);
				} else {
					pstat.setObject(i, param);
				}
			}
		}
	}

	protected static void setParamValue(CallableStatement cstat,
			Object... params) throws SQLException {
		if (params != null && params.length > 0) {
			int i = 0;
			for (Object param : params) {
				++i;
				if (param == null) {
					cstat.setObject(i, param);
					continue;
				}
				if (param instanceof String) {
					cstat.setString(i, (String) param);
				} else if (param instanceof Integer) {
					cstat.setInt(i, ((Integer) param).intValue());
				} else if (param instanceof Long) {
					cstat.setLong(i, ((Long) param).longValue());
				} else if (param instanceof Float) {
					cstat.setFloat(i, ((Float) param).floatValue());
				} else if (param instanceof Double) {
					cstat.setDouble(i, ((Double) param).doubleValue());
				} else if (param instanceof java.sql.Date) {
					cstat.setDate(i, (java.sql.Date) param);
				} else if (param instanceof java.util.Date) {
					java.sql.Timestamp tmsp = new java.sql.Timestamp(
							((java.util.Date) param).getTime());
					cstat.setTimestamp(i, tmsp);
				} else {
					cstat.setObject(i, param);
				}
			}
		}
	}

	protected static <T> T parseResultObj(Class<T> clazz, ResultSet rs)
			throws ServiceException {
		T obj = null;
		try {
			if (rs != null) {
				ResultSetMetaData md = rs.getMetaData();
				int columnCount = md.getColumnCount();
				Reflector reflector = Reflector.forClass(clazz);
				if (rs.next()) {
					ObjectFactory objectFactory = new DefaultObjectFactory();
					obj = objectFactory.create(clazz);
					for (int i = 1; i <= columnCount; i++) {
						String columname = md.getColumnLabel(i);
						if (StringUtils.isBlank(columname)) {
							columname = md.getColumnName(i);
						}
						String propertyname = reflector.getFieldName(columname);
						if (reflector.hasSetter(propertyname)) {
							Object columnvalue = rs.getObject(i);
							if (columnvalue != null) {
								try {
									Class<?> fieldtype = reflector
											.getSetterType(propertyname);

									if (fieldtype.equals(Short.class)
											|| fieldtype.equals(short.class)) {
										columnvalue = rs.getShort(i);
									} else if (fieldtype.equals(Byte.class)
											|| fieldtype.equals(byte.class)) {
										columnvalue = rs.getByte(i);
									} else if (fieldtype.equals(Integer.class)
											|| fieldtype.equals(int.class)) {
										columnvalue = rs.getInt(i);
									} else if (fieldtype.equals(Long.class)
											|| fieldtype.equals(long.class)) {
										columnvalue = rs.getLong(i);
									} else if (fieldtype.equals(Float.class)
											|| fieldtype.equals(float.class)) {
										columnvalue = rs.getFloat(i);
									} else if (fieldtype.equals(Double.class)
											|| fieldtype.equals(double.class)) {
										columnvalue = rs.getDouble(i);
									} else if (fieldtype.equals(Boolean.class)
											|| fieldtype.equals(boolean.class)) {
										columnvalue = rs.getBoolean(i);
									} else if (fieldtype.equals(Date.class)) {
										columnvalue = rs.getTimestamp(i);
									} else if (fieldtype.equals(String.class)) {
										columnvalue = rs.getString(i);
									}

									reflector
											.getSetInvoker(propertyname)
											.invoke(obj,
													new Object[] { columnvalue });
								} catch (Exception e) {
									throw new ServiceException(
											Errorcode.CODE_JAVA,
											"DaoHelper.parseResultObj faild,columname:"
													+ columname, e);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.parseResultObj faild", e);
		}
		return obj;
	}

	protected static <T> List<T> parseResultList(Class<T> clazz, ResultSet rs)
			throws ServiceException {
		List<T> array = new ArrayList<T>();
		try {
			if (rs != null) {
				ResultSetMetaData md = rs.getMetaData();
				int columnCount = md.getColumnCount();
				Reflector reflector = Reflector.forClass(clazz);
				while (rs.next()) {
					ObjectFactory objectFactory = new DefaultObjectFactory();
					T obj = objectFactory.create(clazz);
					for (int i = 1; i <= columnCount; i++) {
						String columname = md.getColumnLabel(i);
						if (StringUtils.isBlank(columname)) {
							columname = md.getColumnName(i);
						}
						String propertyname = reflector.getFieldName(columname);
						if (reflector.hasSetter(propertyname)) {
							Object columnvalue = rs.getObject(i);
							if (columnvalue != null) {
								try {
									Class<?> fieldtype = reflector
											.getSetterType(propertyname);

									if (fieldtype.equals(Short.class)
											|| fieldtype.equals(short.class)) {
										columnvalue = rs.getShort(i);
									} else if (fieldtype.equals(Byte.class)
											|| fieldtype.equals(byte.class)) {
										columnvalue = rs.getByte(i);
									} else if (fieldtype.equals(Integer.class)
											|| fieldtype.equals(int.class)) {
										columnvalue = rs.getInt(i);
									} else if (fieldtype.equals(Long.class)
											|| fieldtype.equals(long.class)) {
										columnvalue = rs.getLong(i);
									} else if (fieldtype.equals(Float.class)
											|| fieldtype.equals(float.class)) {
										columnvalue = rs.getFloat(i);
									} else if (fieldtype.equals(Double.class)
											|| fieldtype.equals(double.class)) {
										columnvalue = rs.getDouble(i);
									} else if (fieldtype.equals(Boolean.class)
											|| fieldtype.equals(boolean.class)) {
										columnvalue = rs.getBoolean(i);
									} else if (fieldtype.equals(Date.class)) {
										columnvalue = rs.getTimestamp(i);
									} else if (fieldtype.equals(String.class)) {
										columnvalue = rs.getString(i);
									}

									reflector
											.getSetInvoker(propertyname)
											.invoke(obj,
													new Object[] { columnvalue });
								} catch (Exception e) {
									throw new ServiceException(
											Errorcode.CODE_JAVA,
											"DaoHelper.parseResultList faild,columname:"
													+ columname, e);
								}
							}
						}
					}
					array.add(obj);
				}
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"DaoHelper.parseResultList faild", e);
		}
		return array;
	}

	protected static void generateJavaBean(String todir, String packagename,
			Table table, List<Column> columnlist) throws IOException {
		StringBuilder str = new StringBuilder();
		StringBuilder getsetstr = new StringBuilder();
		String Classname = getClassName(table.getTableName());
		// packagename
		if (packagename != null && !packagename.trim().equals("")) {
			str.append("package " + packagename + ";\n\n");
		}
		// import
		str.append("import java.util.*;\n");
		str.append("import com.cxdb.reflect.annotations.*;\n\n");
		// class

		str.append("/* " + table.getRemarks() + " */\n");
		str.append("@cxdb_alias(\"" + table.getTableName() + "\")\n");
		str.append("public class " + Classname + " {");
		for (Column column : columnlist) {
			String propertyName = getPropertyName(column.getColumnName());
			Class<?> typeClass = sql2javaType.get(column.getDataType());
			String typeName = "";
			if (!checkedSimpleType(typeClass)) {
				typeName = typeClass.getSimpleName();
			} else {
				typeName = typeClass.getName();
			}
			// property
			if (column.isAutoIncrement()) {
				str.append("\n\t@cxdb_ignore");
			} else if ("CURRENT_TIMESTAMP".equalsIgnoreCase(column
					.getDefaultValue())) {
				str.append("\n\t@cxdb_ignore");
			}
			if (!propertyName.equals(column.getColumnName())) {
				str.append("\n\t@cxdb_alias(\"" + column.getColumnName()
						+ "\")");
			}
			str.append("\n\tprivate " + typeName + " " + propertyName
					+ ";//remark:" + column.getRemarks() + ";length:"
					+ column.getColumnSize());
			if (!column.isNullAble()) {
				str.append("; not null,default:" + column.getDefaultValue());
			}
			// getsetstr
			getsetstr.append("\n\n\t" + "public void set"
					+ (propertyName.charAt(0) + "").toUpperCase()
					+ propertyName.substring(1) + "(" + typeName + " "
					+ propertyName + ") {");
			getsetstr.append("\n\t\tthis." + propertyName + " = "
					+ propertyName + ";\n\t}");
			getsetstr.append("\n\n\t" + "public " + typeName + " get"
					+ (propertyName.charAt(0) + "").toUpperCase()
					+ propertyName.substring(1) + "() {");
			getsetstr.append("\n\t\treturn " + propertyName + ";\n\t}");
		}
		str.append("\n\n\tpublic " + Classname + "() {");
		str.append("\n\t}");
		str.append(getsetstr);
		str.append("\n}");
		File dir = new File(todir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(todir + "/" + Classname + ".java");
		FileUtils.writeStringToFile(file, str.toString(), "UTF-8");
	}

	private static String getClassName(String tableName) {
		StringBuilder className = new StringBuilder();
		String[] chars = tableName.split("");
		boolean upCase = true;
		for (String achar : chars) {
			if (achar == null || "".equals(achar)) {
				continue;
			}
			if (!achar.equals("_")) {
				if (upCase) {
					className.append(achar.toUpperCase());
					upCase = false;
				} else {
					className.append(achar);
				}
			} else {
				upCase = true;
			}
		}
		return className.toString();
	}

	private static String getPropertyName(String columnName) {
		StringBuilder propertyName = new StringBuilder();
		String[] chars = columnName.split("");
		boolean upCase = false;
		int num = 1;
		for (String achar : chars) {
			if (achar == null || "".equals(achar)) {
				continue;
			}
			if (!achar.equals("_")) {
				if (num == 1) {
					propertyName.append(achar.toLowerCase());
				} else if (upCase) {
					propertyName.append(achar.toUpperCase());
					upCase = false;
				} else {
					propertyName.append(achar);
				}
			} else {
				upCase = true;
			}
			num++;
		}
		return propertyName.toString();
	}

	/**
	 * 销毁数据库链接池资源
	 */
	public static void destroyDBPools() {
		DBPoolManager.getInstance().destroyPools();
	}

	/* ===================================事务管理======================== */
	/**
	 * 当前线程的数据库链接资源对象
	 */
	private static final ThreadLocal<DaoResource> currentDaoResources = new ThreadLocal<DaoResource>();

	/**
	 * 开启事务
	 */
	public static void startTransaction() {
		DaoResource daoResource = currentDaoResources.get();
		if (daoResource == null) {
			daoResource = new DaoResource();
			currentDaoResources.set(daoResource);
		}
		daoResource.startTransaction = true;
	}

	/**
	 * 提交事务
	 * 
	 * @throws ServiceException
	 */
	public static void commitTransaction() throws ServiceException {
		DaoResource daoResource = currentDaoResources.get();
		if (daoResource == null) {
			return;
		}
		// 关闭事务开关
		daoResource.startTransaction = false;
		// 链接不为空则提交事务；提交成功后关闭链接；错误需要抛出以便业务端进行rollback
		if (daoResource.con != null) {
			try {
				// 提交事务
				daoResource.con.commit();
				// 提交事务成功后关闭链接
				try {
					if (!daoResource.con.isClosed()) {
						if (!daoResource.con.getAutoCommit()) {
							daoResource.con.setAutoCommit(true);
						}
						daoResource.con.close();
						daoResource.con = null;
					}
				} catch (Exception e) {
				}
			} catch (Exception e) {
				throw new ServiceException(Errorcode.CODE_JAVA,
						"BaseDao.commitTransaction faild", e);
			}
		}
		// 提交事务成功后删除线程中的DaoResource
		currentDaoResources.remove();
	}

	/**
	 * 回滚事务
	 * 
	 * @throws ServiceException
	 */
	public static void rollbackTransaction() throws ServiceException {
		DaoResource daoResource = currentDaoResources.get();
		if (daoResource == null) {
			return;
		}
		// 关闭事务开关
		daoResource.startTransaction = false;
		// 链接不为空则回滚事务；无论是否回滚成功都关闭链接；错误需要抛出以便业务端进行处理
		if (daoResource.con != null) {
			try {
				// 回滚事务
				daoResource.con.rollback();
			} catch (Exception e) {
				throw new ServiceException(Errorcode.CODE_JAVA,
						"BaseDao.rollbackTransaction faild", e);
			} finally {
				// 无论是否回滚成功都关闭链接
				try {
					if (!daoResource.con.isClosed()) {
						if (!daoResource.con.getAutoCommit()) {
							daoResource.con.setAutoCommit(true);
						}
						daoResource.con.close();
						daoResource.con = null;
					}
				} catch (Exception e) {
				}
				// 回滚失败也要删除线程中的DaoResource
				currentDaoResources.remove();
			}
		} else {
			// 删除线程中的DaoResource
			currentDaoResources.remove();
		}
	}

	static DaoResource getDaoResource(String poolname) throws ServiceException,
			SQLException {
		DaoResource daoResource = currentDaoResources.get();
		if (daoResource == null) {
			daoResource = new DaoResource();
			currentDaoResources.set(daoResource);
		}
		if (daoResource.con == null) {
			daoResource.con = DBPoolManager.getInstance().getConnection(
					poolname);
		}
		if (daoResource.startTransaction) {
			daoResource.con.setAutoCommit(false);
		}
		return daoResource;
	}

	static void closeDaoResource() {
		DaoResource daoResource = currentDaoResources.get();
		if (daoResource == null) {
			return;
		}
		if (daoResource.rs != null) {
			try {
				daoResource.rs.close();
			} catch (Exception e) {
			}
			daoResource.rs = null;
		}
		if (daoResource.stat != null) {
			try {
				daoResource.stat.close();
			} catch (Exception e) {
			}
			daoResource.stat = null;
		}
		if (daoResource.pstat != null) {
			try {
				daoResource.pstat.close();
			} catch (Exception e) {
			}
			daoResource.pstat = null;
		}
		if (daoResource.cstat != null) {
			try {
				daoResource.cstat.close();
			} catch (Exception e) {
			}
			daoResource.cstat = null;
		}
		// 未开启事务时需要;关闭链接；删除线程中的DaoResource
		if (!daoResource.startTransaction) {
			try {
				if (daoResource.con != null && !daoResource.con.isClosed()) {
					if (!daoResource.con.getAutoCommit()) {
						daoResource.con.setAutoCommit(true);
					}
					daoResource.con.close();
					daoResource.con = null;
				}
			} catch (Exception e) {
			}
			// 删除线程中的DaoResource
			currentDaoResources.remove();
		}
	}

	/* ===================================debug模式设置======================== */
	private static boolean debug = false;// 是否开启执行sql的debug日志

	/**
	 * 查询当前是否为Debug模式
	 * 
	 * @return
	 */
	public static boolean isDebug() {
		return debug;
	}

	/**
	 * @param debugs
	 *            1:开启debug;其他:关闭dubug
	 */
	public static void setDebug(String debugs) {
		if ("1".equals(debugs)) {
			debug = true;
		} else {
			debug = false;
		}
	}

	/**
	 * 动态开启debug模式
	 * 
	 * @param debugmode
	 */
	public static void setDebug(boolean debugmode) {
		debug = debugmode;
	}

	/* ===================================设置当前操作sql的用户名======================== */

	private static final ThreadLocal<String> currentUserNames = new ThreadLocal<String>();

	/**
	 * 设置当前线程的所有数据库操作的用户名称（用于后台记录用户的数据库操作记录）
	 * 
	 * @param userName
	 */
	public static void setCurrentUserName(String userName) {
		currentUserNames.set(userName);
	}

	static String getCurrentUserName() {
		return currentUserNames.get();
	}

	/* ===================================打印日志======================== */

	static void writelog(String sql, List<Object> params) {
		// 开关
		if (debug) {
			String userName = getCurrentUserName();
			StringBuilder sb = new StringBuilder();
			sb.append(userName);
			sb.append("#");
			sb.append(sql);
			if (params != null && params.size() > 0) {
				sb.append("#");
				for (int i = 0; i < params.size(); i++) {
					Object obj = params.get(i);
					if (obj != null) {
						sb.append(obj.toString());
					} else {
						sb.append("null");
					}
					if (i < (params.size() - 1)) {
						sb.append(",");
					}
				}
			}
			logger.debug(sb.toString());
		}
	}

	static void writelog(String sql, Object... params) {
		// 开关
		if (debug) {
			String userName = getCurrentUserName();
			StringBuilder sb = new StringBuilder();
			sb.append(userName);
			sb.append("#");
			sb.append(sql);
			if (params != null && params.length > 0) {
				sb.append("#");
				for (int i = 0; i < params.length; i++) {
					Object obj = params[i];
					if (obj != null) {
						sb.append(obj.toString());
					} else {
						sb.append("null");
					}
					if (i < (params.length - 1)) {
						sb.append(",");
					}
				}
			}
			logger.debug(sb.toString());
		}
	}

	/**
	 * 通过EasyTransaction执行事务
	 * 
	 * @param transaction
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public static <T> T excuteTransaction(EasyTransaction<T> transaction,
			Object... condition) throws Exception {
		T result = null;
		try {
			// 开始事务
			DaoHelper.startTransaction();
			// 执行业务
			result = transaction.excute(condition);
			// 提交事务
			DaoHelper.commitTransaction();
		} catch (Exception e) {
			// 回滚事务
			try {
				DaoHelper.rollbackTransaction();
			} catch (ServiceException e1) {
				logger.error(
						"DaoHelper.excuteTransaction rollbackTransaction faild",
						e1);
			}
			throw e;
		}
		return result;
	}
}
