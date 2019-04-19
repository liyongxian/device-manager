package com.emcc.deviceManager.response;

/**
 * 没有实际用途，只作为父类类型指向子类对象时使用
 *
 * @author zhanghh
 * @date 2018/5/10 20:33
 */
public class BaseResponse {
    private boolean success;
    
    private String message;

    private String errorCode;
    
    private Object result;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

    
}
