package com.cxdb.datasource.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cxdb.common.utils.DateCoverd;


/**
 * 查询结果集封装类
 * 
 * @author chenxu
 * 
 */
public class EasyRowSet {
	/**
	 * 保存查询结果中个column的索引
	 */
	private Map<Integer, String> columnIndexs = null;
	/**
	 * 保存获取的查询记录
	 */
	private List<LinkedHashMap<String, Object>> resultList = null;

	/**
	 * 记录当前结果集的下标
	 */
	private int currentIndex = -1;

	public EasyRowSet(ResultSet rs) throws SQLException {
		columnIndexs = new HashMap<Integer, String>();
		resultList = new ArrayList<LinkedHashMap<String, Object>>();
		populate(rs);
	}

	/**
	 * 封装结果集
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	private void populate(ResultSet rs) throws SQLException {
		if (rs != null) {
			try {
				ResultSetMetaData md = rs.getMetaData();
				int columnCount = md.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					String columname = md.getColumnLabel(i);
					if (StringUtils.isBlank(columname)) {
						columname = md.getColumnName(i);
					}
					columnIndexs.put(i, columname);
				}
				// rowData Map;
				while (rs.next()) {
					LinkedHashMap<String, Object> rowData = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= columnCount; i++) {
						rowData.put(columnIndexs.get(i), rs.getObject(i));
					}
					resultList.add(rowData);
				}
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	/**
	 * 关闭结果集
	 */
	public void close() {
		if (resultList != null) {
			resultList.clear();
		}
		if (columnIndexs != null) {
			columnIndexs.clear();
		}
	}

	/**
	 * 获取查询记录条数
	 * 
	 * @return
	 */
	public int size() {
		if (resultList != null)
			return resultList.size();
		else
			return 0;
	}

	/**
	 * 获取指定索引的结果集,index从0开始
	 * 
	 * @param index
	 *            从0开始
	 * @return
	 */
	public Map<String, Object> getRowData(int index) {
		return resultList.get(index);
	}

	/**
	 * 判断是否有下一个记录
	 * 
	 * @return
	 */
	public boolean next() {
		if (currentIndex >= size() - 1) {
			return false;
		} else {
			currentIndex++;
			return true;

		}
	}

	public Map<String, Object> getCurrentRowData() {
		return resultList.get(currentIndex);
	}

	/**
	 * 通过查询字段获取String类型的返回值
	 * 
	 * @param columnName
	 * @return
	 */
	public String getString(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		Object obj = rowData.get(columnName);
		if (obj != null)
			if (obj instanceof byte[]) {
				return new String((byte[]) obj);
			} else {
				return obj.toString();
			}
		else
			return null;
	}

	public Date getDate(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		Date value = null;
		Object returnvalue = rowData.get(columnName);
		if (returnvalue != null) {
			if (returnvalue instanceof byte[]) {
				value = DateCoverd.toDate(getString(columnName), "yyyy-MM-dd");
			} else {
				value = (Date) returnvalue;
			}
		}
		return value;
	}

	public Time getTime(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		Time value = null;
		Object returnvalue = rowData.get(columnName);
		if (returnvalue != null) {
			if (returnvalue instanceof Timestamp) {
				long l = ((Timestamp) returnvalue).getTime();
				return new Time(l);
			} else if (returnvalue instanceof byte[]) {
				value = new Time(DateCoverd.toDate(getString(columnName),
						"hh:mm:ss").getTime());
			} else {
				value = (Time) returnvalue;
			}
		}
		return value;
	}

	public Timestamp getTimestamp(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		Timestamp value = null;
		Object returnvalue = rowData.get(columnName);
		if (returnvalue != null) {
			if (returnvalue instanceof byte[]) {
				value = new Timestamp(DateCoverd.toDate(getString(columnName),
						"yyyy-mm-dd hh:mm:ss").getTime());
			} else {
				value = (Timestamp) returnvalue;
			}
		}
		return value;
	}

	/**
	 * 通过查询字段获取int类型的返回值
	 * 
	 * @param columnName
	 * @return
	 */

	public int getInt(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		Integer value = null;
		Object returnvalue = rowData.get(columnName);
		if (returnvalue != null) {
			if (returnvalue instanceof Long) {
				value = ((Long) returnvalue).intValue();
			} else if (returnvalue instanceof String) {
				if (StringUtils.isBlank((String) returnvalue)) {
					value = 0;
				} else {
					value = Integer.parseInt((String) returnvalue);
				}
			} else {
				value = (Integer) returnvalue;
			}
		} else {
			value = 0;
		}
		return value;

	}

	public long getLong(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		Long value = null;
		Object returnvalue = rowData.get(columnName);
		if (returnvalue != null) {
			if (returnvalue instanceof String) {
				if (StringUtils.isBlank((String) returnvalue)) {
					value = 0L;
				} else {
					value = Long.parseLong((String) returnvalue);
				}
			} else if (returnvalue instanceof BigDecimal) {
				value = ((BigDecimal) returnvalue).longValue();
			} else if (returnvalue instanceof Integer) {
				value = Long.parseLong(returnvalue + "");
			} else {
				value = (Long) returnvalue;
			}
		} else {
			value = 0L;
		}
		return value;

	}

	public short getShort(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		Short value = null;
		Object returnvalue = rowData.get(columnName);
		if (returnvalue != null)
			if (returnvalue instanceof String) {
				if (StringUtils.isBlank((String) returnvalue)) {
					value = 0;
				} else {
					value = Short.parseShort((String) returnvalue);
				}
			} else {
				value = (Short) returnvalue;
			}
		else
			value = 0;
		return value;

	}

	public float getFloat(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		Float value = null;
		Object returnvalue = rowData.get(columnName);
		if (returnvalue != null)
			if (returnvalue instanceof String) {
				if (StringUtils.isBlank((String) returnvalue)) {
					value = 0.0F;
				} else {
					value = Float.parseFloat((String) returnvalue);
				}
			} else {
				value = (Float) returnvalue;
			}
		else
			value = 0.0F;
		return value;

	}

	public double getDouble(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		Double value = null;
		Object returnvalue = rowData.get(columnName);
		if (returnvalue != null)
			if (returnvalue instanceof String) {
				if (StringUtils.isBlank((String) returnvalue)) {
					value = 0.0D;
				} else {
					value = Double.parseDouble((String) returnvalue);
				}
			} else {
				value = (Double) returnvalue;
			}
		else
			value = 0.0D;
		return value;
	}

	public boolean getBoolean(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		Boolean value = null;
		Object returnvalue = rowData.get(columnName);
		if (returnvalue != null)
			value = (Boolean) returnvalue;
		else
			value = false;
		return value;
	}

	public byte[] getByte(String columnName) {
		Map<String, Object> rowData = getCurrentRowData();
		byte[] value = null;
		Object returnvalue = rowData.get(columnName);
		if (returnvalue != null) {
			value = (byte[]) returnvalue;
		}
		return value;
	}

	/**
	 * 通过查询记录下标获取String类型的返回值
	 * 
	 * @param columnName
	 * @return
	 */
	public String getString(int index) {
		String columnName = columnIndexs.get(index);
		return getString(columnName);
	}

	public Date getDate(int index) {
		String columnName = columnIndexs.get(index);
		return getDate(columnName);
	}

	public Time getTime(int index) {
		String columnName = columnIndexs.get(index);
		return getTime(columnName);
	}

	public Timestamp getTimestamp(int index) {
		String columnName = columnIndexs.get(index);
		return getTimestamp(columnName);
	}

	public int getInt(int index) {
		String columnName = columnIndexs.get(index);
		return getInt(columnName);
	}

	public long getLong(int index) {
		String columnName = columnIndexs.get(index);
		return getLong(columnName);
	}

	public float getFloat(int index) {
		String columnName = columnIndexs.get(index);
		return getFloat(columnName);
	}

	public double getDouble(int index) {
		String columnName = columnIndexs.get(index);
		return getDouble(columnName);
	}

	public short getShort(int index) {
		String columnName = columnIndexs.get(index);
		return getShort(columnName);
	}

	public boolean getBoolean(int index) {
		String columnName = columnIndexs.get(index);
		return getBoolean(columnName);
	}

	public byte[] getByte(int index) {
		String columnName = columnIndexs.get(index);
		return getByte(columnName);
	}

}
