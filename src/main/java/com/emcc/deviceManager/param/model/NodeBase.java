package com.emcc.deviceManager.param.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeBase {
	@JsonProperty( "uri")
	private String uri;
	@JsonProperty( "BrowseName")
	public String browseName;
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getBrowseName() {
		return browseName;
	}
	public void setBrowseName(String browseName) {
		this.browseName = browseName;
	}
	
}
