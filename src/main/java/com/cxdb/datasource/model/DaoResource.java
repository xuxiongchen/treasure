package com.cxdb.datasource.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * 数据源
 * @author chenxu
 *
 */
public class DaoResource {
	public boolean startTransaction = false;
	public Connection con = null;
	public PreparedStatement pstat = null;
	public CallableStatement cstat = null;
	public Statement stat = null;
	public ResultSet rs = null;
}
