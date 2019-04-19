package com.emcc.deviceManager.service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import util.JacksonJsonUtil;

import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.model.AddNodesItem;
import com.emcc.deviceManager.param.model.DepthAndRelationQueryParam;
import com.emcc.deviceManager.param.model.DepthSpecifyParam;
import com.emcc.deviceManager.param.model.SpecifyRelationNodeAttrFilter;
import com.emcc.deviceManager.response.model.BaseResponseWithTotalCount;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.rest.RestTemplateService;

@Service
public class OpcuaService {
	@Value("${model-server}")
    private String host;
	private static final Logger logger = LoggerFactory.getLogger(OpcuaService.class);	
	@Autowired
	private RestTemplateService restTemplateService;
	public NodeRespones addNodesOld(String namespace,String requestJson)throws Exception,CheckDefineException{
		NodeRespones response=new NodeRespones();
		//新增节点接口
		String url=host+"/opcua/addNodes?namespace="+namespace;
		response=restTemplateService.post(url, requestJson);
		if(response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)){
			throw new CheckDefineException("model servie Exception when excut : "+url+"param:"+requestJson+response.getMsg());
    	}
        return response;
	}
	public NodeRespones addNodes(String namespace,String requestJson)throws Exception,CheckDefineException{
		NodeRespones response=new NodeRespones();
		//新增节点接口
		String url=host+"/opcua/v2/addNodes?namespace="+namespace+"&extendParentProperties=false";
		response=restTemplateService.post(url, requestJson);
		if(response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)){
			throw new CheckDefineException("model servie Exception when excut : "+url+"param:"+requestJson+response.getMsg());
    	}
        return response;
	}
	
	public BaseResponseWithTotalCount getTreeNodesByDepth(String namespace,String requestJson)throws Exception,CheckDefineException{
		BaseResponseWithTotalCount response=new BaseResponseWithTotalCount();
		//新增节点接口
		String url=host+"/opcua/queryTreeNode/depth/"+namespace;
		response=restTemplateService.getTreeNodesByDepth(url, requestJson);
		if(response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)){
			throw new CheckDefineException("model servie Exception when excut : "+url+"param:"+requestJson+response.getMsg());
    	}
        return response;
	}
	public NodeRespones deleteTreeByUri(String namespace, String id, String requestJson)
			throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		String url = host + "/opcua/deleteTree?uri=/" + namespace + "/" + id;
		response = restTemplateService.post(url, requestJson);
		if (response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)) {
			throw new CheckDefineException(
					"model servie Exception when excut : " + url + "param:" + requestJson + response.getMsg());
		}
		return response;
	}
	public NodeRespones deleteTreeByUri(String uri,String requestJson)
			throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		String url = host + "/opcua/deleteTree?uri="+uri;
		response = restTemplateService.post(url, requestJson);
		if (response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)) {
			throw new CheckDefineException(
					"model servie Exception when excut : " + url + "param:" + requestJson + response.getMsg());
		}
		return response;
	}
	
	public NodeRespones queryModelDetail(String uri,boolean extendParentProperties,String requestJson)throws Exception,CheckDefineException{
		NodeRespones response=new NodeRespones();
		//新增节点接口
		String url=host+"/opcua/queryDetailByRootUri?uri="+uri+"&extendParentProperties="+extendParentProperties;
		response=restTemplateService.get(url, requestJson);
		if(response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)){
			throw new CheckDefineException("model servie Exception when excut : "+url+"param:"+requestJson+response.getMsg());
    	}
        return response;
	}
}
