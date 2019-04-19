package com.emcc.deviceManager.param.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value = "设备基本信息")
public class DeviceBaseInfo {
	@ApiModelProperty(value = "设备编号")
	private String id;
	@ApiModelProperty(value = "设备名称")
	private String name;
	@ApiModelProperty(value = "设备所属模型Uri")
	private String modelUri;
	@ApiModelProperty(value = "设备所属模型名称")
	private String modelName;	
	@ApiModelProperty(value = "设备图片")
	private String IconAddress;
	@ApiModelProperty(value = "位置")
	private String address;
	@ApiModelProperty(value = "责任人")
	private String manager;
	@ApiModelProperty(value = "维护周期")
	private String maintenancePeriod;
	@ApiModelProperty(value = "上次维护时间")
	private String lastMaintenanceTime;
	@ApiModelProperty(value = "状态")
	private String status="正常";	

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModelUri() {
		return modelUri;
	}
	public void setModelUri(String modelUri) {
		this.modelUri = modelUri;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}	
	
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getIconAddress() {
		return IconAddress;
	}
	public void setIconAddress(String iconAddress) {
		IconAddress = iconAddress;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMaintenancePeriod() {
		return maintenancePeriod;
	}
	public void setMaintenancePeriod(String maintenancePeriod) {
		this.maintenancePeriod = maintenancePeriod;
	}
	public String getLastMaintenanceTime() {
		return lastMaintenanceTime;
	}
	public void setLastMaintenanceTime(String lastMaintenanceTime) {
		this.lastMaintenanceTime = lastMaintenanceTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
