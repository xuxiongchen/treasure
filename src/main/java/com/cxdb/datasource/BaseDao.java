package com.cxdb.datasource;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cxdb.common.Errorcode;
import com.cxdb.common.ServiceException;
import com.cxdb.datasource.model.Column;
import com.cxdb.datasource.model.DaoResource;
import com.cxdb.datasource.model.EasyRowSet;
import com.cxdb.datasource.model.PageBean;
import com.cxdb.datasource.model.Table;
import com.sun.media.jfxmedia.logging.Logger;


/**
 * Dao基类，每次使用都需要new 不能作为单例使用
 * 
 */
public class BaseDao {
	private String poolname;

	public BaseDao() {
	}

	/**
	 * @param poolname
	 *            ：链接池的名称
	 */
	public BaseDao(String poolname) {
		this.poolname = poolname;
	}

	private DaoResource getDaoResource() throws ServiceException, SQLException {
		return DaoHelper.getDaoResource(poolname);
	}

	private void close() {
		DaoHelper.closeDaoResource();
	}

	// add

	/**
	 * @param obj
	 *            ：需要添加到数据库的对象实体
	 * @throws ServiceException
	 */
	public void add(Object obj) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.add null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getInsertsql(obj, params);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.add faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
	}

	/**
	 * @param sql
	 *            ：insert语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @throws ServiceException
	 */
	public void add(String sql, Object... params) throws ServiceException {
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.add faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
	}

	/**
	 * @param sql
	 *            ：insert语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @throws ServiceException
	 */
	public void add(String sql, List<Object> params) throws ServiceException {
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.add faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
	}

	/**
	 * @param obj
	 *            ：需要添加到数据库的对象实体
	 * @return
	 * @throws ServiceException
	 */
	public int addReturnGeneratedKey(Object obj) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.addReturnGeneratedKey null object");
		}
		int generatedKey = -1;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getInsertsql(obj, params);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.pstat.executeUpdate();
			ResultSet keys = daoResource.pstat.getGeneratedKeys();
			if (keys.next()) {
				generatedKey = keys.getInt(1);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.addReturnGeneratedKey faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return generatedKey;
	}

	/**
	 * @param sql
	 *            ：insert语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public int addReturnGeneratedKey(String sql, Object... params)
			throws ServiceException {
		int generatedKey = -1;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.pstat.executeUpdate();
			ResultSet keys = daoResource.pstat.getGeneratedKeys();
			if (keys.next()) {
				generatedKey = keys.getInt(1);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.addReturnGeneratedKey faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return generatedKey;
	}

	/**
	 * @param sql
	 *            ：insert语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public int addReturnGeneratedKey(String sql, List<Object> params)
			throws ServiceException {
		int generatedKey = -1;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.pstat.executeUpdate();
			ResultSet keys = daoResource.pstat.getGeneratedKeys();
			if (keys.next()) {
				generatedKey = keys.getInt(1);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.addReturnGeneratedKey faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return generatedKey;
	}

	/**
	 * @param obj
	 *            ：需要添加到数据库的对象实体
	 * @return
	 * @throws ServiceException
	 */
	public long addReturnGeneratedKeyLong(Object obj) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.addReturnGeneratedKey null object");
		}
		long generatedKey = -1;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getInsertsql(obj, params);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.pstat.executeUpdate();
			ResultSet keys = daoResource.pstat.getGeneratedKeys();
			if (keys.next()) {
				generatedKey = keys.getLong(1);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.addReturnGeneratedKeyLong faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return generatedKey;
	}

	/**
	 * @param sql
	 *            ：insert语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public long addReturnGeneratedKeyLong(String sql, Object... params)
			throws ServiceException {
		long generatedKey = -1;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.pstat.executeUpdate();
			ResultSet keys = daoResource.pstat.getGeneratedKeys();
			if (keys.next()) {
				generatedKey = keys.getLong(1);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.addReturnGeneratedKeyLong faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return generatedKey;
	}

	/**
	 * @param sql
	 *            ：insert语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public long addReturnGeneratedKeyLong(String sql, List<Object> params)
			throws ServiceException {
		long generatedKey = -1;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.pstat.executeUpdate();
			ResultSet keys = daoResource.pstat.getGeneratedKeys();
			if (keys.next()) {
				generatedKey = keys.getLong(1);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.addReturnGeneratedKeyLong faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return generatedKey;
	}

	// delete
	/**
	 * @param obj
	 *            ：需要从数据库中删除的对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public int delete(Object obj, String... whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.delete null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getDeletesql(obj, params, whereproperties);
		int count = 0;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.delete faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * @param obj
	 *            ：需要从数据库中删除的对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public int delete(Object obj, List<String> whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.delete null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getDeletesql(obj, params, whereproperties);
		int count = 0;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.delete faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * @param sql
	 *            ：delete语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public int delete(String sql, Object... params) throws ServiceException {
		int count = 0;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.delete faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * @param sql
	 *            ：delete语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public int delete(String sql, List<Object> params) throws ServiceException {
		int count = 0;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.delete faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	// update
	/**
	 * 更新对象中的属性值！=null的属性！
	 * 
	 * @param obj
	 *            ：需要更新的对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public int update(Object obj, String... whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.update null object");
		}
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper
				.getUpdatesql(obj, params, false, whereproperties);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.update faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * 更新对象中的属性值！=null的属性！
	 * 
	 * @param obj
	 *            ：需要从数据库中删除的对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public int update(Object obj, List<String> whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.update null object");
		}
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper
				.getUpdatesql(obj, params, false, whereproperties);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.update faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * @param sql
	 *            ：update语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public int update(String sql, Object... params) throws ServiceException {
		int count = 0;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.update faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * @param sql
	 *            ：update语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public int update(String sql, List<Object> params) throws ServiceException {
		int count = 0;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.update faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * 更新对象中的属性值！=null的属性！
	 * 
	 * @param obj
	 *            ：需要从数据库中删除的对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public boolean updateReturnResult(Object obj, String... whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.updateReturnResult null object");
		}
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper
				.getUpdatesql(obj, params, false, whereproperties);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.updateReturnResult faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新对象中的属性值！=null的属性！
	 * 
	 * @param obj
	 *            ：需要从数据库中删除的对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public boolean updateReturnResult(Object obj, List<String> whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.updateReturnResult null object");
		}
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper
				.getUpdatesql(obj, params, false, whereproperties);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.updateReturnResult faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param sql
	 *            ：update语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public boolean updateReturnResult(String sql, Object... params)
			throws ServiceException {
		int count = 0;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.updateReturnResult faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param sql
	 *            ：update语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public boolean updateReturnResult(String sql, List<Object> params)
			throws ServiceException {
		int count = 0;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.updateReturnResult faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param obj
	 *            ：需要更新的对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public int updateAllProperties(Object obj, String... whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.update null object");
		}
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getUpdatesql(obj, params, true, whereproperties);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.update faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * @param obj
	 *            ：需要从数据库中删除的对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public int updateAllProperties(Object obj, List<String> whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.update null object");
		}
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getUpdatesql(obj, params, true, whereproperties);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.update faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * 更新对象中的属性值！=null的属性！
	 * 
	 * @param obj
	 *            ：需要从数据库中删除的对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public boolean updateAllPropertiesReturnResult(Object obj,
			String... whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.updateReturnResult null object");
		}
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getUpdatesql(obj, params, true, whereproperties);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.updateReturnResult faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新对象中的属性值！=null的属性！
	 * 
	 * @param obj
	 *            ：需要从数据库中删除的对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public boolean updateAllPropertiesReturnResult(Object obj,
			List<String> whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.updateReturnResult null object");
		}
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getUpdatesql(obj, params, true, whereproperties);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			count = daoResource.pstat.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.updateReturnResult faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	// query
	/**
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public int count(Object obj, String... whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.count null object");
		}
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getCountsql(obj, params, whereproperties);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			daoResource.rs = daoResource.pstat.executeQuery();
			if (daoResource.rs != null && daoResource.rs.next()) {
				count = daoResource.rs.getInt(1);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.count faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public int count(Object obj, List<String> whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.getCount null object");
		}
		int count = 0;
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getCountsql(obj, params, whereproperties);
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			if (daoResource.rs != null && daoResource.rs.next()) {
				count = daoResource.rs.getInt(1);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.getCount faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public int getInt(String sql, Object... params) throws ServiceException {
		int count = 0;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			if (daoResource.rs != null && daoResource.rs.next()) {
				count = daoResource.rs.getInt(1);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.getInt faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public int getInt(String sql, List<Object> params) throws ServiceException {
		int count = 0;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			if (daoResource.rs != null && daoResource.rs.next()) {
				count = daoResource.rs.getInt(1);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.getInt faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return count;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(T obj, String... whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.get null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, params, whereproperties);
		T resultobj = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			resultobj = (T) DaoHelper.parseResultObj(obj.getClass(),
					daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.get faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return resultobj;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(T obj, List<String> whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.get null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, params, whereproperties);
		T resultobj = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			resultobj = (T) DaoHelper.parseResultObj(obj.getClass(),
					daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.get faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return resultobj;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param clazz
	 *            ：查询结果对象的Class
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public <T> T get(String sql, Class<T> clazz, Object... params)
			throws ServiceException {
		T obj = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			obj = DaoHelper.parseResultObj(clazz, daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.get faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return obj;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param clazz
	 *            ：查询结果对象的Class
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public <T> T get(String sql, Class<T> clazz, List<Object> params)
			throws ServiceException {
		T obj = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			obj = DaoHelper.parseResultObj(clazz, daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.get faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return obj;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> getRowSet(Object obj, String... whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.getRowSet null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, params, whereproperties);
		Map<String, Object> rowSet = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			EasyRowSet easyRowSet = new EasyRowSet(daoResource.rs);
			rowSet = easyRowSet.getRowData(0);
			easyRowSet.close();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.getRowSet faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowSet;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> getRowSet(Object obj,
			List<String> whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.getRowSet null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, params, whereproperties);
		Map<String, Object> rowSet = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			EasyRowSet easyRowSet = new EasyRowSet(daoResource.rs);
			rowSet = easyRowSet.getRowData(0);
			easyRowSet.close();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.getRowSet faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowSet;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> getRowSet(String sql, Object... params)
			throws ServiceException {
		Map<String, Object> rowSet = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			EasyRowSet easyRowSet = new EasyRowSet(daoResource.rs);
			rowSet = easyRowSet.getRowData(0);
			easyRowSet.close();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.getRowSet faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowSet;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> getRowSet(String sql, List<Object> params)
			throws ServiceException {
		Map<String, Object> rowSet = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			EasyRowSet easyRowSet = new EasyRowSet(daoResource.rs);
			rowSet = easyRowSet.getRowData(0);
			easyRowSet.close();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.getRowSet faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowSet;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> list(T obj, String... whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.list null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, params, whereproperties);
		List<T> array = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = (List<T>) DaoHelper.parseResultList(obj.getClass(),
					daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.list faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> list(T obj, List<String> whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.list null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, params, whereproperties);
		List<T> array = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = (List<T>) DaoHelper.parseResultList(obj.getClass(),
					daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.list faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param clazz
	 *            ：查询结果对象的Class
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public <T> List<T> list(String sql, Class<T> clazz, Object... params)
			throws ServiceException {
		List<T> array = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = DaoHelper.parseResultList(clazz, daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.list faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param clazz
	 *            ：查询结果对象的Class
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public <T> List<T> list(String sql, Class<T> clazz, List<Object> params)
			throws ServiceException {
		List<T> array = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = DaoHelper.parseResultList(clazz, daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.list faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSet(Object obj, String... whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSet null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, params, whereproperties);
		EasyRowSet rowset = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSet faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSet(Object obj, List<String> whereproperties)
			throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSet null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, params, whereproperties);
		EasyRowSet rowset = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSet faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSet(String sql, Object... params)
			throws ServiceException {
		EasyRowSet rowset = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSet faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSet(String sql, List<Object> params)
			throws ServiceException {
		EasyRowSet rowset = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSet faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param pagenum
	 *            ：当前的页码
	 * @param pagesize
	 *            ：每一页的数量
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> listByPage(T obj, int pagenum, int pagesize,
			String... whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, pagenum, pagesize, params,
				whereproperties);
		List<T> array = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = (List<T>) DaoHelper.parseResultList(obj.getClass(),
					daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param pagenum
	 *            ：当前的页码
	 * @param pagesize
	 *            ：每一页的数量
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> listByPage(T obj, int pagenum, int pagesize,
			List<String> whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, pagenum, pagesize, params,
				whereproperties);
		List<T> array = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = (List<T>) DaoHelper.parseResultList(obj.getClass(),
					daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param clazz
	 *            ：查询结果对象的Class
	 * @param pagenum
	 *            ：当前的页码
	 * @param pagesize
	 *            ：每一页的数量
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public <T> List<T> listByPage(String sql, Class<T> clazz, int pagenum,
			int pagesize, Object... params) throws ServiceException {
		List<T> array = null;
		try {
			sql = DaoHelper.getPageSql(sql, pagenum, pagesize);
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = DaoHelper.parseResultList(clazz, daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param clazz
	 *            ：查询结果对象的Class
	 * @param pagenum
	 *            ：当前的页码
	 * @param pagesize
	 *            ：每一页的数量
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public <T> List<T> listByPage(String sql, Class<T> clazz, int pagenum,
			int pagesize, List<Object> params) throws ServiceException {
		List<T> array = null;
		try {
			sql = DaoHelper.getPageSql(sql, pagenum, pagesize);
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = DaoHelper.parseResultList(clazz, daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param pageBean
	 *            ：分页实体
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> listByPage(PageBean pageBean, T obj,
			String... whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage null object");
		}
		// 查询总量
		int totalNum = count(obj, whereproperties);
		pageBean.setTotalNum(totalNum);
		// 查询你分页数据
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(pageBean, obj, params,
				whereproperties);
		List<T> array = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = (List<T>) DaoHelper.parseResultList(obj.getClass(),
					daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param pageBean
	 *            ：分页实体
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> listByPage(PageBean pageBean, T obj,
			List<String> whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage null object");
		}
		// 查询总量
		int totalNum = count(obj, whereproperties);
		pageBean.setTotalNum(totalNum);
		// 查询你分页数据
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(pageBean, obj, params,
				whereproperties);
		List<T> array = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = (List<T>) DaoHelper.parseResultList(obj.getClass(),
					daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param pageBean
	 *            ：分页实体
	 * @param sql
	 *            ：select语句
	 * @param clazz
	 *            ：查询结果对象的Class
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public <T> List<T> listByPage(PageBean pageBean, String sql,
			Class<T> clazz, Object... params) throws ServiceException {
		// 查询总量
		String countsql = DaoHelper.getCountsql(sql);
		int totalNum = getInt(countsql, params);
		pageBean.setTotalNum(totalNum);
		// 查询你分页数据
		List<T> array = null;
		try {
			sql = DaoHelper.getPageSql(sql, pageBean);
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = DaoHelper.parseResultList(clazz, daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param pageBean
	 *            ：分页实体
	 * @param sql
	 *            ：select语句
	 * @param clazz
	 *            ：查询结果对象的Class
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public <T> List<T> listByPage(PageBean pageBean, String sql,
			Class<T> clazz, List<Object> params) throws ServiceException {
		// 查询总量
		String countsql = DaoHelper.getCountsql(sql);
		int totalNum = getInt(countsql, params);
		pageBean.setTotalNum(totalNum);
		// 查询你分页数据
		List<T> array = null;
		try {
			sql = DaoHelper.getPageSql(sql, pageBean);
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			array = DaoHelper.parseResultList(clazz, daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage faild", e);
		} finally {
			close();
		}
		if (array == null) {
			array = new ArrayList<T>();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return array;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param pagenum
	 *            ：当前的页码
	 * @param pagesize
	 *            ：每一页的数量
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSetByPage(Object obj, int pagenum, int pagesize,
			String... whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSetByPage null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, pagenum, pagesize, params,
				whereproperties);
		EasyRowSet rowset = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSetByPage faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param obj
	 *            ：对象实体
	 * @param pagenum
	 *            ：当前的页码
	 * @param pagesize
	 *            ：每一页的数量
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSetByPage(Object obj, int pagenum, int pagesize,
			List<String> whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSetByPage null object");
		}
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(obj, pagenum, pagesize, params,
				whereproperties);
		EasyRowSet rowset = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSetByPage faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param pagenum
	 *            ：当前的页码
	 * @param pagesize
	 *            ：每一页的数量
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSetByPage(String sql, int pagenum, int pagesize,
			Object... params) throws ServiceException {
		EasyRowSet rowset = null;
		try {
			sql = DaoHelper.getPageSql(sql, pagenum, pagesize);
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSetByPage faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param sql
	 *            ：select语句
	 * @param pagenum
	 *            ：当前的页码
	 * @param pagesize
	 *            ：每一页的数量
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSetByPage(String sql, int pagenum, int pagesize,
			List<Object> params) throws ServiceException {
		EasyRowSet rowset = null;
		try {
			sql = DaoHelper.getPageSql(sql, pagenum, pagesize);
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param pageBean
	 *            ：分页实体
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSetByPage(PageBean pageBean, Object obj,
			String... whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage null object");
		}
		// 查询总量
		int totalNum = count(obj, whereproperties);
		pageBean.setTotalNum(totalNum);
		// 查询你分页数据
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(pageBean, obj, params,
				whereproperties);
		EasyRowSet rowset = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSetByPage faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param pageBean
	 *            ：分页实体
	 * @param obj
	 *            ：对象实体
	 * @param whereproperties
	 *            ：作为where条件的实体属性名，可多项，如果没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSetByPage(PageBean pageBean, Object obj,
			List<String> whereproperties) throws ServiceException {
		if (obj == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listByPage null object");
		}
		// 查询总量
		int totalNum = count(obj, whereproperties);
		pageBean.setTotalNum(totalNum);
		// 查询你分页数据
		List<Object> params = new ArrayList<Object>();
		String sql = DaoHelper.getQuerysql(pageBean, obj, params,
				whereproperties);
		EasyRowSet rowset = null;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSetByPage faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param pageBean
	 *            ：分页实体
	 * @param sql
	 *            ：select语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSetByPage(PageBean pageBean, String sql,
			Object... params) throws ServiceException {
		// 查询总量
		String countsql = DaoHelper.getCountsql(sql);
		int totalNum = getInt(countsql, params);
		pageBean.setTotalNum(totalNum);
		// 查询你分页数据
		EasyRowSet rowset = null;
		try {
			sql = DaoHelper.getPageSql(sql, pageBean);
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSetByPage faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	/**
	 * @param pageBean
	 *            ：分页实体
	 * @param sql
	 *            ：select语句
	 * @param params
	 *            ：sql中？对应的参数，没有参数则传null
	 * @return
	 * @throws ServiceException
	 */
	public EasyRowSet listRowSetByPage(PageBean pageBean, String sql,
			List<Object> params) throws ServiceException {
		// 查询总量
		String countsql = DaoHelper.getCountsql(sql);
		int totalNum = getInt(countsql, params);
		pageBean.setTotalNum(totalNum);
		// 查询你分页数据
		EasyRowSet rowset = null;
		try {
			sql = DaoHelper.getPageSql(sql, pageBean);
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			DaoHelper.setParamValue(daoResource.pstat, params);
			daoResource.rs = daoResource.pstat.executeQuery();
			rowset = new EasyRowSet(daoResource.rs);
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.listRowSetByPage faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return rowset;
	}

	// batch
	/**
	 * @param sql
	 *            ：需要批量执行的sql域名
	 * @param paramslist
	 *            ：各条sql中？对应的参数
	 * @return
	 * @throws ServiceException
	 */
	public int[] excuteBatch(String sql, List<Object[]> paramslist)
			throws ServiceException {
		if (paramslist == null) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.excuteBatch null object");
		}
		int[] counts = new int[paramslist.size()];
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.pstat = daoResource.con.prepareStatement(sql);
			for (Object[] params : paramslist) {
				DaoHelper.setParamValue(daoResource.pstat, params);
				daoResource.pstat.addBatch();
				// 写日志
				DaoHelper.writelog(sql, params);
			}
			counts = daoResource.pstat.executeBatch();
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.excuteBatch faild", e);
		} finally {
			close();
		}
		return counts;
	}

	// procedure
	// 只有输入IN参数，没有输出OUT参数
	/**
	 * @param sql
	 *            ：call语句
	 * @param params
	 *            ：call语句？对应的参数，没有则传null
	 * @return
	 * @throws ServiceException
	 */
	public boolean procedure(String sql, Object... params)
			throws ServiceException {
		boolean result = false;
		try {
			DaoResource daoResource = getDaoResource();
			daoResource.cstat = daoResource.con.prepareCall(sql);
			DaoHelper.setParamValue(daoResource.cstat, params);
			daoResource.cstat.execute();
			result = true;
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.procedure faild", e);
		} finally {
			close();
		}
		// 写日志
		DaoHelper.writelog(sql, params);
		return result;
	}

	// 生成javabean
	/**
	 * @param todir
	 *            ：生成的java实体类存放的磁盘目录
	 * @param packagename
	 *            ：java实体类的包名
	 * @throws ServiceException
	 */
	public void generateJavaBean(String todir, String packagename)
			throws ServiceException {
		try {
			DaoResource daoResource = getDaoResource();
			DatabaseMetaData dbmetadata = daoResource.con.getMetaData();
			ResultSet rs = dbmetadata.getTables(null, null, null,
					new String[] { "TABLE" });
			List<Table> tablelist = new ArrayList<Table>();
			while (rs.next()) {
				Table table = new Table();
				table.setTableName(rs.getString("TABLE_NAME"));
				table.setRemarks(rs.getString("REMARKS"));
				System.out.println(rs.getString("TABLE_NAME"));
				tablelist.add(table);
			}
			rs.close();
			for (int i = 0; i < tablelist.size(); i++) {
				Table table = tablelist.get(i);
				rs = dbmetadata.getColumns(null, null, table.getTableName(),
						null);
				List<Column> columnlist = new ArrayList<Column>();
				while (rs.next()) {
					Column column = new Column();
					column.setColumnName(rs.getString("COLUMN_NAME"));
					column.setDataType(rs.getInt("DATA_TYPE"));
					column.setRemarks(rs.getString("REMARKS"));
					column.setColumnSize(rs.getInt("COLUMN_SIZE"));
					column.setNullAble(rs.getString("IS_NULLABLE"));
					column.setDefaultValue(rs.getString("COLUMN_DEF"));
					column.setAutoIncrement(rs.getString("IS_AUTOINCREMENT"));
					columnlist.add(column);
				}
				DaoHelper.generateJavaBean(todir, packagename, table,
						columnlist);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.generateJavaBean faild", e);
		} finally {
			close();
		}
	}

	/**
	 * @param tablename
	 *            ：表名
	 * @param todir
	 *            ：生成的java实体类存放的磁盘目录
	 * @param packagename
	 *            ：java实体类的包名
	 * @throws ServiceException
	 */
	public void generateJavaBean(String tablename, String todir,
			String packagename) throws ServiceException {
		try {
			DaoResource daoResource = getDaoResource();
			DatabaseMetaData dbmetadata = daoResource.con.getMetaData();
			ResultSet rs = dbmetadata.getTables(null, null, tablename,
					new String[] { "TABLE" });
			List<Table> tablelist = new ArrayList<Table>();
			while (rs.next()) {
				Table table = new Table();
				table.setTableName(rs.getString("TABLE_NAME"));
				table.setRemarks(rs.getString("REMARKS"));
				tablelist.add(table);
			}
			rs.close();
			for (int i = 0; i < tablelist.size(); i++) {
				Table table = tablelist.get(i);
				rs = dbmetadata.getColumns(null, null, table.getTableName(),
						null);
				List<Column> columnlist = new ArrayList<Column>();
				while (rs.next()) {
					Column column = new Column();
					column.setColumnName(rs.getString("COLUMN_NAME"));
					column.setDataType(rs.getInt("DATA_TYPE"));
					column.setRemarks(rs.getString("REMARKS"));
					column.setColumnSize(rs.getInt("COLUMN_SIZE"));
					column.setNullAble(rs.getString("IS_NULLABLE"));
					column.setDefaultValue(rs.getString("COLUMN_DEF"));
					//column.setAutoIncrement(rs.getString("IS_AUTOINCREMENT"));
					columnlist.add(column);
				}
				DaoHelper.generateJavaBean(todir, packagename, table,
						columnlist);
			}
		} catch (Exception e) {
			throw new ServiceException(Errorcode.CODE_JAVA,
					"BaseDao.generateJavaBean faild", e);
		} finally {
			close();
		}
	}
}
