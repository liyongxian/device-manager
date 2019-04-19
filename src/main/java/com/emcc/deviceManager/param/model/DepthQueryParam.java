package com.emcc.deviceManager.param.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel(value = "深度查询参数")
public class DepthQueryParam extends BrowseBase{
	@ApiModelProperty(value = "Node查询条件为and还是or，默认为and")
	private String andOr;
	
	@ApiModelProperty(value = "搜索深度，默认为1")
	private Integer depth = 1;
	
	@ApiModelProperty(value = "目标节点通过指定关系连接的节点的属性键值对，如{\"uri\":\"/0/45\"}")
	private Map<String, String> relatedNodeAttribueFilter;
	
	
	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	
	public Map<String, String> getRelatedNodeAttribueFilter() {
		return relatedNodeAttribueFilter;
	}

	public void setRelatedNodeAttribueFilter(Map<String, String> relatedNodeAttribueFilter) {
		this.relatedNodeAttribueFilter = relatedNodeAttribueFilter;
	}
	

	public String getAndOr() {
		return andOr;
	}

	public void setAndOr(String andOr) {
		this.andOr = andOr;
	}

	@Override
	public String toString() {
		return "DepthQueryParam [ andOr=" + andOr + ", depth=" + depth
				+ ", relatedNodeAttribueFilter=" + relatedNodeAttribueFilter  + "]";
	}
}
