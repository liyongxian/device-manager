package com.emcc.deviceManager.param.model;

import io.swagger.annotations.ApiModelProperty;

public class PatchJson {
	/**
	 * replace替换已有的数据； 
	 * remove：删除属性数据； 
	 * add：新增属性； 
	 * copy：从一个属性拷贝到另一个属性;
	 * move：从一个属性移到另一个属性，然后删除拷贝的属性;
	 */
	@ApiModelProperty(value = "操作类型，可选replace（更新已有属性)、remove（删除属性）、add（新增属性）")
	private String op;
	
	@ApiModelProperty(value = "属性Key")
	private String path;
	
	/**
	 * 当OP为remove时可能用户不会填写value，后面对null的处理会存在问题
	 */
	@ApiModelProperty(value = "要新增、更新的值，删除时无需填写")
	private Object value;
	
	@ApiModelProperty(value = "拷贝的来源路径，在copy和move操作时生效，现无效")
	private String from;

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String toString() {
		return "PatchJson [op=" + op + ", path=" + path + ", value=" + value
				+ ", from=" + from + "]";
	}

}
