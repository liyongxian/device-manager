package com.emcc.deviceManager.param.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "浏览基础条件")
public class BrowseBase {
	@ApiModelProperty(value = "节点id")
	private String id;

	@ApiModelProperty(value = "要浏览的关系URI")
	private List<String> relations;
	
	@ApiModelProperty(value = "要返回的属性，为空则返回全体")
	private List<String> fields;
	
	@ApiModelProperty(value = "查询方向，true为前向，false为后向")
	private boolean forward;
	
	@ApiModelProperty(value = "是否包含子关系定义，默认为false")
	private boolean includeSubtype = false;

	@ApiModelProperty(value = "分页续传参数，该参数为optional")
	private Integer continuationpoint;
	
	@ApiModelProperty(value = "目标节点必须包含关系类型URI")
	private String hasSpecifyRelation;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getRelations() {
		return relations;
	}

	public void setRelations(List<String> relations) {
		this.relations = relations;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public boolean isForward() {
		return forward;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}

	public boolean isIncludeSubtype() {
		return includeSubtype;
	}

	public void setIncludeSubtype(boolean includeSubtype) {
		this.includeSubtype = includeSubtype;
	}

	public Integer getContinuationpoint() {
		return continuationpoint;
	}

	public void setContinuationpoint(Integer continuationpoint) {
		this.continuationpoint = continuationpoint;
	}
	
	public String getHasSpecifyRelation() {
		return hasSpecifyRelation;
	}

	public void setHasSpecifyRelation(String hasSpecifyRelation) {
		this.hasSpecifyRelation = hasSpecifyRelation;
	}

	@Override
	public String toString() {
		return "BrowseBase [id=" + id + ", relations=" + relations + ", fields=" + fields + ", forward=" + forward
				+ ", includeSubtype=" + includeSubtype + ", continuationpoint=" + continuationpoint
				+ ", hasSpecifyRelation=" + hasSpecifyRelation + "]";
	}
}
