package com.cxdb.demo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cxdb.common.ServiceException;
import com.cxdb.datasource.BaseDao;


@Repository
public class ClassInfoDao extends BaseDao {


	/**
	 * 删除学生信息
	 * @param id
	 * @throws ServiceException
	 */
	public void deleteClass(int id) throws ServiceException {
		this.delete("delete from class_info where id=?", id);

	}


	/**
	 * 查询是否存在班级
	 * @param ClassInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean hasExistClass(ClassInfo ClassInfo) throws ServiceException {
		int value = getInt("select count(1) from class_info where 1=1 AND class_grade=? AND class_no=? AND school_id=?",
				 ClassInfo.getClassGrade(), ClassInfo.getClassNo(), ClassInfo.getSchoolId());
		return value == 0 ? false : true;
	}

	/**
	 * 获取学生列表
	 * @param querySqlBuffer
	 * @param queryParams
	 * @return
	 * @throws ServiceException
	 */
	public List<ClassInfo> getClassList(String querySqlBuffer, List<Object> queryParams) throws ServiceException {
		return list(querySqlBuffer, ClassInfo.class, queryParams);
	}

	/**
	 * 导入学生头像
	 * 
	 * @param classId，班级id
	 * @param studentNo,班级内学生编号
	 * @param url，头像地址
	 * @throws ServiceException 
	 */
	public void importClassStuImg(String classId, String studentNo, String url) throws ServiceException {
		String sql = "update student_info b inner join (select s.* from student_class sc left join student_info s "
				+ "on s.id = sc.student_id where 1=1 and sc.class_id = ? and s.student_class_no = ?) a "
				+ "on b.id = a.id set b.class_portrait_photo = ? ";
		update(sql, Integer.valueOf(classId),Integer.valueOf(studentNo),url);
	}

}
