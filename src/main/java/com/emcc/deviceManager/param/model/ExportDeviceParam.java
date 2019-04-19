package com.emcc.deviceManager.param.model;

import java.io.Serializable;
import java.util.List;

public class ExportDeviceParam implements Serializable {
	private String path;
	private List<String> idList;

	public ExportDeviceParam() {
		super();
	}

	public ExportDeviceParam(String path, List<String> idList) {
		super();
		this.path = path;
		this.idList = idList;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}

	@Override
	public String toString() {
		return "ExportDeviceParam [path=" + path + ", idList=" + idList + "]";
	}

}
