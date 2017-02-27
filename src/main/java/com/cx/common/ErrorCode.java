package com.cx.common;

public class ErrorCode {
	public final static int CODE_AUTH = 10;// session校验失败,请重新登录！
	public final static int CODE_PARAM = 11;// 请求参数校验失败！
	public final static int CODE_JAVA = 12;// 服务器异常！
	public final static int CODE_AUTHORITY = 13;// 没有操作权限！

	public final static int CODE_FILE = 101;// 上传的文件格式错误
	public final static int CODE_PASSWORD = 102;// 用户名或密码错误
	public final static int CODE_VALIDATE_CODE = 103;// 验证码错误
	public final static int CODE_ACCOUNT_HAS_EXIST = 104;// 账号已存在
	public final static int CODE_WRONG_ORIGINAL_PASSWD = 109;// 原密码不正确
	public final static int CODE_DISATIVATED = 110;// 账号未激活
	public final static int CODE_URL_DISABLE = 111;// 链接失效
	public final static int CODE_ATIVATED = 112;   // 账号已激活 无法继续发送邮件

}
