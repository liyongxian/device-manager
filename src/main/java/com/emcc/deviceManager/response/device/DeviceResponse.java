package com.emcc.deviceManager.response.device;

import com.emcc.deviceManager.param.device.Device;
import com.emcc.deviceManager.response.BaseResponse;

public class DeviceResponse extends BaseResponse {
	private String uri;
	private Device device;
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	
}
