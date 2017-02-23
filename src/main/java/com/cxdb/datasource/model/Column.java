package com.cxdb.datasource.model;
/**
 * 字典模型
 * @author chenxu
 *
 */
public class Column {
	private String columnName; /*字段名*/
	private int dataType; /*类型*/
	private String remarks; /*注释*/
	private int columnSize; /*长度*/
	private boolean nullAble; /*是否为空*/
	private String defaultValue; /*默认值*/
	private boolean autoIncrement; /*是否自增*/

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public boolean isNullAble() {
		return nullAble;
	}

	public void setNullAble(String nullAble) {
		if ("YES".equals(nullAble))
			this.nullAble = true;
		else
			this.nullAble = false;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(String autoIncrement) {
		if ("YES".equals(autoIncrement))
			this.autoIncrement = true;
		else
			this.autoIncrement = false;
	}

	@Override
	public String toString() {
		return "Column [columnName=" + columnName + ", dataType=" + dataType
				+ ", remarks=" + remarks + ", columnSize=" + columnSize
				+ ", nullAble=" + nullAble + ", defaultValue=" + defaultValue
				+ ", autoIncrement=" + autoIncrement + "]";
	}
}
