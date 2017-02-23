package com.cxdb.demo;

import java.util.*;

import com.cxdb.reflect.annotations.cxdb_alias;
import com.cxdb.reflect.annotations.cxdb_ignore;

/*  */
@cxdb_alias("class_info")
public class ClassInfo {
	private java.lang.Integer id;//remark:班级id;length:10; not null,default:null
	@cxdb_alias("school_id")
	private java.lang.Integer schoolId;//remark:学校id;length:10
	@cxdb_alias("class_no")
	private java.lang.Integer classNo;//remark:班级号;length:10
	@cxdb_alias("class_grade")
	private java.lang.Integer classGrade;//remark:年级;length:10
	@cxdb_alias("create_time")
	private java.util.Date createTime;//remark:创建时间;length:0
	@cxdb_ignore
	@cxdb_alias("update_time")
	private java.util.Date updateTime;//remark:更新时间;length:0; not null,default:CURRENT_TIMESTAMP

	public ClassInfo() {
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return id;
	}

	public void setSchoolId(java.lang.Integer schoolId) {
		this.schoolId = schoolId;
	}

	public java.lang.Integer getSchoolId() {
		return schoolId;
	}

	public void setClassNo(java.lang.Integer classNo) {
		this.classNo = classNo;
	}

	public java.lang.Integer getClassNo() {
		return classNo;
	}

	public void setClassGrade(java.lang.Integer classGrade) {
		this.classGrade = classGrade;
	}

	public java.lang.Integer getClassGrade() {
		return classGrade;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}
}