CREATE TABLE `class_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '班级id',
  `school_id` int(10) DEFAULT NULL COMMENT '学校id',
  `class_no` int(10) DEFAULT NULL COMMENT '班级号',
  `class_grade` int(10) DEFAULT NULL COMMENT '年级',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `class_info_class_no_ceq` (`class_no`) USING BTREE,
  KEY `class_info_class_grade_ceq` (`class_grade`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=185 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='班级';

//模板sql建表