package com.cxdb.reflect.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解数据库字段或表名
 * @author chenxu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE,ElementType.FIELD})
public @interface cxdb_alias {
	String value();
}