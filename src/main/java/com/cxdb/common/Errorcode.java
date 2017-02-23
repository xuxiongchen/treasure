package com.cxdb.common;
/**
 * 错误code
 * @author chenxu
 *
 */
public interface Errorcode {
	int CODE_AUTH = 10;// 权限校验异常
	int CODE_PARAM = 11;// 请求参数不正确
	int CODE_JAVA = 12;// 普通异常
	int CODE_SERVICE = 101;// 通用业务异常
}
