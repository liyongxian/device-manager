package com.emcc.deviceManager.param.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel(value = "新增Node参数")
public class AddNodesItem {
	@ApiModelProperty(value = "父节点唯一标识")
	private String parentNodeUri;
	
	@ApiModelProperty(value = "层次化引用节点的uri，由parentNode指向新的node")
	private String referenceTypeId;
	
	@ApiModelProperty(value = "新节点的浏览名")
	private String browseName;
	
	@ApiModelProperty(value = "新节点的节点类别，可选Object，Object Type，Variable，VariableType")
	private String nodeClass;
	
	@ApiModelProperty(value = "包含附加属性的扩展参数，依赖于要创建的节点类别")
	private Map<String, Object> nodeAttributes;
	
	@ApiModelProperty(value = "类型定义的uri，来定义那种类型的object或variable会被创建。如果nodeClass不是对象或者变量，该参数不会被设置")
	private String typeDefinition;

	public String getParentNodeUri() {
		return parentNodeUri;
	}

	public void setParentNodeUri(String parentNodeUri) {
		this.parentNodeUri = parentNodeUri;
	}

	public String getReferenceTypeId() {
		return referenceTypeId;
	}

	public void setReferenceTypeId(String referenceTypeId) {
		this.referenceTypeId = referenceTypeId;
	}

	public String getBrowseName() {
		return browseName;
	}

	public void setBrowseName(String browseName) {
		this.browseName = browseName;
	}

	public String getNodeClass() {
		return nodeClass;
	}

	public void setNodeClass(String nodeClass) {
		this.nodeClass = nodeClass;
	}

	public Map<String, Object> getNodeAttributes() {
		return nodeAttributes;
	}

	public void setNodeAttributes(Map<String, Object> nodeAttributes) {
		this.nodeAttributes = nodeAttributes;
	}

	public String getTypeDefinition() {
		return typeDefinition;
	}

	public void setTypeDefinition(String typeDefinition) {
		this.typeDefinition = typeDefinition;
	}

	@Override
	public String toString() {
		return "AddNodesItem [parentNodeUri=" + parentNodeUri
				+ ", referenceTypeId=" + referenceTypeId + ", browseName="
				+ browseName + ", nodeClass=" + nodeClass + ", nodeAttributes="
				+ nodeAttributes + ", typeDefinition=" + typeDefinition + "]";
	}
}
