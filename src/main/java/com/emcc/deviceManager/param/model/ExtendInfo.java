package com.emcc.deviceManager.param.model;

public class ExtendInfo {
	private String  UserAccessLevel;
	private String  Description;
	private String  uri;
	private String  AccessLevel;
	private String  WriteMask;
	private int  ValueRank;
	private String  NodeClass;
	private int  MinimumSamplingInterval;
	private String  BrowseName;
	private String  DisplayName;
	private String  Value;
	private String  ArrayDimensions;
	private boolean  Historizing;
	private String  UserWriteMask;
	private String  DataType;
	public String getUserAccessLevel() {
		return UserAccessLevel;
	}
	public void setUserAccessLevel(String userAccessLevel) {
		UserAccessLevel = userAccessLevel;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getAccessLevel() {
		return AccessLevel;
	}
	public void setAccessLevel(String accessLevel) {
		AccessLevel = accessLevel;
	}
	public String getWriteMask() {
		return WriteMask;
	}
	public void setWriteMask(String writeMask) {
		WriteMask = writeMask;
	}
	public int getValueRank() {
		return ValueRank;
	}
	public void setValueRank(int valueRank) {
		ValueRank = valueRank;
	}
	public String getNodeClass() {
		return NodeClass;
	}
	public void setNodeClass(String nodeClass) {
		NodeClass = nodeClass;
	}
	public int getMinimumSamplingInterval() {
		return MinimumSamplingInterval;
	}
	public void setMinimumSamplingInterval(int minimumSamplingInterval) {
		MinimumSamplingInterval = minimumSamplingInterval;
	}
	public String getBrowseName() {
		return BrowseName;
	}
	public void setBrowseName(String browseName) {
		BrowseName = browseName;
	}
	public String getDisplayName() {
		return DisplayName;
	}
	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getArrayDimensions() {
		return ArrayDimensions;
	}
	public void setArrayDimensions(String arrayDimensions) {
		ArrayDimensions = arrayDimensions;
	}
	public boolean isHistorizing() {
		return Historizing;
	}
	public void setHistorizing(boolean historizing) {
		Historizing = historizing;
	}
	public String getUserWriteMask() {
		return UserWriteMask;
	}
	public void setUserWriteMask(String userWriteMask) {
		UserWriteMask = userWriteMask;
	}
	public String getDataType() {
		return DataType;
	}
	public void setDataType(String dataType) {
		DataType = dataType;
	}
	
}
