package com.emcc.deviceManager.exception;

/**
 * 参数不合法异常
 * @author zhanghh
 * 2018年3月8日
 */
public class ParamException  extends Exception{
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public ParamException(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "ParamException [message=" + message + "]";
	}
}
