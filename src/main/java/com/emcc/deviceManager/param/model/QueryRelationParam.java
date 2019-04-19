package com.emcc.deviceManager.param.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

public class QueryRelationParam {
	private Integer depth = 1;
	private boolean forward;
	private String id;
	private List<String> relations;
	@ApiModelProperty(value = "目标节点通过指定关系连接的节点的属性键值对，如{\"uri\":\"/0/45\"}")
	private Map<String, String> relatedNodeAttribueFilter;
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public boolean isForward() {
		return forward;
	}
	public void setForward(boolean forward) {
		this.forward = forward;
	}
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
	public Map<String, String> getRelatedNodeAttribueFilter() {
		return relatedNodeAttribueFilter;
	}
	public void setRelatedNodeAttribueFilter(
			Map<String, String> relatedNodeAttribueFilter) {
		this.relatedNodeAttribueFilter = relatedNodeAttribueFilter;
	}
	
	
}
