package com.emcc.deviceManager.service.modelquery;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.JacksonJsonUtil;

import com.emcc.deviceManager.common.DeviceConstans;
import com.emcc.deviceManager.common.OpcReference;
import com.emcc.deviceManager.common.Tools;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.device.DeviceQueryParam;
import com.emcc.deviceManager.param.device.QueryTreeParam;
import com.emcc.deviceManager.param.model.DepthAndRelationQueryParam;
import com.emcc.deviceManager.param.model.DepthSpecifyParam;
import com.emcc.deviceManager.param.model.PatchJson;
import com.emcc.deviceManager.response.model.BaseResponseWithTotalCount;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.common.GenerateModelParam;
import com.emcc.deviceManager.service.device.DeviceService;
import com.emcc.deviceManager.service.model.OpcuaService;
import com.emcc.deviceManager.service.model.RelationService;
import com.emcc.deviceManager.service.model.V2NodeCrudService;
import com.emcc.deviceManager.service.model.V2QueryService;
import com.emcc.deviceManager.service.user.UserService;

@Service
public class ModelService {
	private static final Logger logger = LoggerFactory.getLogger(ModelService.class);
	@Autowired
    private OpcuaService opcuaService;
	@Autowired
    private V2QueryService v2QueryService;
	@Autowired
    private V2NodeCrudService nodeCrudService;
	@Autowired
    private RelationService relationService;
	@Autowired
    private UserService userService;
	@Autowired
    private GenerateModelParam generateModelParam;
	 /*
     * 查询所有模型
     */
    public NodeRespones queryTree(QueryTreeParam queryParam)throws Exception,CheckDefineException{    	
    	NodeRespones response=new NodeRespones();
    	
    	String namespace=Tools.getNamespaceByUri(queryParam.getUri());
    	String id=Tools.getIdByUri(queryParam.getUri());
    	//初始化查询条件
    	List<String> fields=new ArrayList<String>();
    	fields.add("uri");
    	List<String> relations=new ArrayList<String>();
    	relations.add(OpcReference.HAS_ORGANIZE);
    	List<DepthSpecifyParam> specifyList=new ArrayList<DepthSpecifyParam>();    	
    	
     	try{
     		DepthAndRelationQueryParam depthAndRelationQueryParam=generateModelParam.generateDepth(namespace,5, true, id, fields, relations, specifyList);
     		String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);
     		BaseResponseWithTotalCount responseTree=opcuaService.getTreeNodesByDepth(namespace, requestJson);
	         response.setResult(responseTree.getResult());
     	}catch(Exception e){
     		throw e;
     	}
        
    	return response;
    }
    
    public NodeRespones  queryModelDetail(String uri,boolean extendParentProperties)throws Exception,CheckDefineException{
    	NodeRespones response=new NodeRespones();
    	try{
    		String requestJson="";
    		response=opcuaService.queryModelDetail(uri, extendParentProperties, requestJson);
    		return response;
    	}catch(Exception e){
    		throw e;
    		
    	}
    	
    }
    public NodeRespones  editPatch(String namespace,String id,List<PatchJson> patchJson)throws Exception,CheckDefineException{
    	NodeRespones response=new NodeRespones();
    	try{
    		String requestJson=JacksonJsonUtil.toJson(patchJson);
    		response=nodeCrudService.editNodePatch(requestJson, namespace, id);
    		return response;
    	}catch(Exception e){
    		throw e;
    		
    	}
    }
}
