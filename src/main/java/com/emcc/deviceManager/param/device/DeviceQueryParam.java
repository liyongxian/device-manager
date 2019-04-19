package com.emcc.deviceManager.param.device;

import java.util.List;

public class DeviceQueryParam {
	private List<BaseNode> models;
	private BaseNode proline;
	public List<BaseNode> getModels() {
		return models;
	}
	public void setModels(List<BaseNode> models) {
		this.models = models;
	}
	public BaseNode getProline() {
		return proline;
	}
	public void setProline(BaseNode proline) {
		this.proline = proline;
	}
	
}
