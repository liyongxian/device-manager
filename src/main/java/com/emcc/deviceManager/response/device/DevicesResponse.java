package com.emcc.deviceManager.response.device;

import java.util.List;

import com.emcc.deviceManager.param.device.Device;
import com.emcc.deviceManager.response.BaseResponse;

public class DevicesResponse extends BaseResponse {
	private List<Device> deviceList;

	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

	
}
