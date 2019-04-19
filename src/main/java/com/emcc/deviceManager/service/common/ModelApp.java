package com.emcc.deviceManager.service.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.JacksonJsonUtil;

import com.alibaba.fastjson.JSON;
import com.emcc.deviceManager.common.Tools;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.device.BaseNode;
import com.emcc.deviceManager.param.model.DepthAndRelationQueryParam;
import com.emcc.deviceManager.param.model.DepthSpecifyParam;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.model.V2QueryService;
@Service
public class ModelApp {
	@Autowired
	private V2QueryService v2QueryService;
	@Autowired
    private GenerateModelParam generateModelParam;
	 /*
     * 查询设备的上下级关系
     */
	public List<BaseNode> queryDeviceRelation(String deviceUri,String relation,boolean forward)throws Exception,CheckDefineException{
    	String namespace=Tools.getNamespaceByUri(deviceUri);
    	String id=Tools.getIdByUri(deviceUri);
    	List<BaseNode> baseNodes=new ArrayList<BaseNode>();
    	 //构造查询条件
    	List<String> fields=new ArrayList<String>();
    	fields.add("uri");
    	fields.add("BrowseName");
    	List<String> relations=new ArrayList<String>();
    	relations.add(relation);
    	List<DepthSpecifyParam> specifyList=new ArrayList<DepthSpecifyParam>();     	
        //查询上级一个节点--配置查询条件
    	try{
    		DepthAndRelationQueryParam depthAndRelationQueryParam=generateModelParam.generateDepth(namespace, 1, true, id, fields, relations, specifyList);
     		String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);     		
    		
	    	NodeRespones responseModel=v2QueryService.depth(requestJson, namespace, 1);
	    	String jsonString = JSON.toJSONString(responseModel.getResult());  
	    	baseNodes = JSON.parseArray(jsonString, BaseNode.class); 
	    	if(null!=baseNodes&&baseNodes.size()>0){
	    		return baseNodes;
	    	}
    	}catch(Exception e){
    		throw e;
    	}
		return baseNodes;
    }
}
