package com.emcc.deviceManager.service.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import util.JacksonJsonUtil;

import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.model.RelationV1;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.rest.RestTemplateService;
@Service
public class RelationService {
	@Value("${model-server}")
    private String host;
	private static final Logger logger = LoggerFactory.getLogger(OpcuaService.class);	
	@Autowired
	private RestTemplateService restTemplateService;
	public NodeRespones addRelation(String namespace,String requestJson)throws Exception,CheckDefineException{
		NodeRespones response=new NodeRespones();
		namespace=Constans.NAMESPACE_SYSTEM;
		//新增节点接口
		String url=host+"/v2/model/relationofnodes/"+namespace+"?transaction=true";
		response=restTemplateService.post(url, requestJson);
		if(response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)){
			throw new CheckDefineException("model servie Exception when excut : "+url+"param:"+requestJson+response.getMsg());
    	}
        return response;
	}
	public NodeRespones deleteRelation(String namespace,String requestJson)throws Exception,CheckDefineException{
		NodeRespones response=new NodeRespones();
		namespace=Constans.NAMESPACE_SYSTEM;
		//新增节点接口
		String url=host+"/v2/model/relation/"+namespace+"?transaction=true";
		response=restTemplateService.delete(url, requestJson);
		if(response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)){
			throw new CheckDefineException("model servie Exception when excut : "+url+"param:"+requestJson+response.getMsg());
    	}
        return response;
	}
	 /*
     * 修改关系
     */
    public NodeRespones editRelation(String namespace,String source,String targert,String relationName,String opt)throws Exception,CheckDefineException{  
    	NodeRespones response=new NodeRespones();
    	RelationV1 relation=new RelationV1();
    	List<RelationV1> relationList=new ArrayList<RelationV1>();
    	relation.setRelation(relationName);
    	relation.setSource(source);
    	relation.setTarget(targert);
    	relationList.add(relation);
		String requestRelationJson = JacksonJsonUtil.toJson(relationList);
		if(opt.equals(Constans.PATCH_OP_ADD)){
			this.addRelation(namespace, requestRelationJson);
		}else{
			this.deleteRelation(namespace, requestRelationJson);
		}
    	return response;
    }
}
