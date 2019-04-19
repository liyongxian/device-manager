package com.emcc.deviceManager.param.device;

public class AddDeviceParam {
	private String deviceUri;
	
	private String proLineUri;
	
	private Device device;

	public String getDeviceUri() {
		return deviceUri;
	}

	public void setDeviceUri(String deviceUri) {
		this.deviceUri = deviceUri;
	}

	public String getProLineUri() {
		return proLineUri;
	}

	public void setProLineUri(String proLineUri) {
		this.proLineUri = proLineUri;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "AddDeviceParam [deviceUri=" + deviceUri + ", proLineUri=" + proLineUri + ", device=" + device + "]";
	}
}
