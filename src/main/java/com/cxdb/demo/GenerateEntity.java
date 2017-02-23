package com.cxdb.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cxdb.common.ServiceException;
import com.cxdb.datasource.BaseDao;

/**
 * 自动生成entity
 * @author chenxu
 *
 */
public class GenerateEntity {


		private static Logger logger = LoggerFactory.getLogger(GenerateEntity.class);

		public static void main(String[] args) throws Exception {
			// 生成javabean
			try {
				new BaseDao()
						.generateJavaBean(
								"class_info",
								"j:/test",
								"com.cxdb.model");
			} catch (ServiceException e) {
			}

		}
}
