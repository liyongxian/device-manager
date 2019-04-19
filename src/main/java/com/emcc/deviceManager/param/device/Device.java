package com.emcc.deviceManager.param.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "设备参数")
public class Device {
	@ApiModelProperty(value = "设备编号")
	private String id;
	@ApiModelProperty(value = "设备名称")
	private String name;
	@ApiModelProperty(value = "设备所属模型Uri")
	private String modelUri;
	@ApiModelProperty(value = "设备所属模型名称")
	private String modelName;
	@ApiModelProperty(value = "设备所属产线")
	private String prolineUri;
	@ApiModelProperty(value = "设备所属产线名称")
	private String prolineName;
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
	@ApiModelProperty(value = "设备描述")
	private String description;	
	@ApiModelProperty(value = "服务提供商")
	private String serviceContact;
	@ApiModelProperty(value = "服务提供商联系人")
	private String serviceProvider;
	@ApiModelProperty(value = "联系人电话")
	private String contactPhone;
	@ApiModelProperty(value = "联系人邮箱")
	private String contactMailAddress;
	
	
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
	public String getProlineUri() {
		return prolineUri;
	}
	public void setProlineUri(String prolineUri) {
		this.prolineUri = prolineUri;
	}
	public String getProlineName() {
		return prolineName;
	}
	public void setProlineName(String prolineName) {
		this.prolineName = prolineName;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getServiceProvider() {
		return serviceProvider;
	}
	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
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
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
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
	public String getServiceContact() {
		return serviceContact;
	}
	public void setServiceContact(String serviceContact) {
		this.serviceContact = serviceContact;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactMailAddress() {
		return contactMailAddress;
	}
	public void setContactMailAddress(String contactMailAddress) {
		this.contactMailAddress = contactMailAddress;
	}
	
	
	
}
