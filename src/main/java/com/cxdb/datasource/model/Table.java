package com.cxdb.datasource.model;


public class Table {

	private String tableName;
	private String remarks;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "Table [tableName=" + tableName + ", remarks=" + remarks + "]";
	}

}
