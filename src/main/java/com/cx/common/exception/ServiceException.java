package com.cx.common.exception;

import com.cx.common.ErrorCode;


/**
 * 业务逻辑异常类,执行业务错误时抛出
 * 
 */
@SuppressWarnings("serial")
public class ServiceException extends Exception {
	private int errorcode;

	public int getErrorcode() {
		return errorcode;
	}

	/**
	 * 业务逻辑异常类,执行业务错误时抛出
	 * 
	 * @param message
	 *            异常信息
	 */
	public ServiceException(int errorcode, String message) {
		super(message);
		this.errorcode = errorcode;
	}

	/**
	 * 业务逻辑异常类,执行业务错误时抛出
	 * 
	 * @param cause
	 *            异常类
	 */
	public ServiceException(Throwable cause) {
		super(cause);
		if (cause instanceof ServiceException) {
			this.errorcode = ((ServiceException) cause).getErrorcode();
		} else {
			this.errorcode = ErrorCode.CODE_JAVA;
		}
	}

	/**
	 * 业务逻辑异常类,执行业务错误时抛出
	 * 
	 * @param message
	 *            异常信息
	 * @param cause
	 *            异常类
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		if (cause instanceof ServiceException) {
			this.errorcode = ((ServiceException) cause).getErrorcode();
		} else {
			this.errorcode = ErrorCode.CODE_JAVA;
		}
	}

	/**
	 * 业务逻辑异常类,执行业务错误时抛出
	 * 
	 * @param message
	 *            异常信息
	 * @param cause
	 *            异常类
	 */
	public ServiceException(int errorcode, String message, Throwable cause) {
		super(message, cause);
		this.errorcode = errorcode;
	}
}

