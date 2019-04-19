package com.emcc.deviceManager.param.model;

import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;


@ApiModel(value = "Node访问参数")
public class AccessNodeParam extends BaseParam implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "查询id，支持一次查询多个节点")
	private List<String> ids;
	@ApiModelProperty(value = "显示的属性")
	private List<String> fields;
	

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "AccessNodeParam [ids=" + ids + ", fields=" + fields + "]";
	}

}
