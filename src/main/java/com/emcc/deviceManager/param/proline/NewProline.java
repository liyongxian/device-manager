package com.emcc.deviceManager.param.proline;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "新增产线参数")
public class NewProline {
	@ApiModelProperty(value = "产线名称")
	private String name;
	@ApiModelProperty(value = "上级产线uri")
	private String parentUri;
	@ApiModelProperty(value = "产线描述")
	private String description;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentUri() {
		return parentUri;
	}
	public void setParentUri(String parentUri) {
		this.parentUri = parentUri;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
