package com.emcc.deviceManager.param.proline;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProlineModel {
	@JsonProperty( "WriteMask")
	public String writeMask;
	@JsonProperty( "NodeClass")
	public String nodeClass;
	@JsonProperty( "Description")
	public String description;
	@JsonProperty( "BrowseName")
	public String browseName;
	@JsonProperty( "DisplayName")
	public String displayName;
	@JsonProperty( "UserWriteMask")
	public String userWriteMask;
	@JsonProperty( "uri")
	public String uri;
	@JsonProperty( "IsAbstract")
	public boolean IsAbstract;
	public String getWriteMask() {
		return writeMask;
	}
	public void setWriteMask(String writeMask) {
		this.writeMask = writeMask;
	}
	public String getNodeClass() {
		return nodeClass;
	}
	public void setNodeClass(String nodeClass) {
		this.nodeClass = nodeClass;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBrowseName() {
		return browseName;
	}
	public void setBrowseName(String browseName) {
		this.browseName = browseName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUserWriteMask() {
		return userWriteMask;
	}
	public void setUserWriteMask(String userWriteMask) {
		this.userWriteMask = userWriteMask;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public boolean isIsAbstract() {
		return IsAbstract;
	}
	public void setIsAbstract(boolean isAbstract) {
		IsAbstract = isAbstract;
	}
	
	
	
}
