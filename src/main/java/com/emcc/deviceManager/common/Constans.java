package com.emcc.deviceManager.common;

import org.springframework.beans.factory.annotation.Value;

public class Constans {
	
	
	//租户namespace
	public static String  NAMESPACE="haier";	
	public static String  NAMESPACE_SYSTEM="system";	
	
	public static String MODEL_RESPONSE_SUCCESS="0x00000000";
	
	public static String MODEL_RESPONSE_FAIL="0x80000000";
	
	public static String MODEL_ROOT_URI="/system/5110";
	public static String MODEL_ROOT_NAMESPACE="system";
	public static String MODEL_ROOT_ID="5110";
	
	public static final String PATCH_OP_ADD="add";
	public static final String PATCH_OP_REMOVE="remove";
	public static final String PATCH_OP_MOVE="move";
	public static final String PATCH_OP_COPY="copy";
	public static final String PATCH_OP_REPLACE="replace";
	
	public static String NODE_DESCRIPTION="Description";
	
	public static String NODE_DISPLAYNAME="DisplayName";
	
	
	public static String BASE_INFO="BaseInfo";	
	
}
