package com.emcc.deviceManager.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.model.AddNodesItem;
import com.emcc.deviceManager.param.model.DepthAndRelationQueryParam;
import com.emcc.deviceManager.param.model.DepthSpecifyParam;
import com.emcc.deviceManager.param.model.PatchJson;
import com.emcc.deviceManager.param.model.SpecifyRelationNodeAttrFilter;

@Service
public class GenerateModelParam {
	public AddNodesItem generateAddNodesItem(String browseName,String parentNodeUri,
			String nodeClass,String referenceTypeId,String typeDefinition,Map<String, Object> nodeAttributes){
		AddNodesItem addNodesItem=new AddNodesItem();
		addNodesItem.setNodeAttributes(nodeAttributes);
		addNodesItem.setNodeClass(nodeClass);
		addNodesItem.setParentNodeUri(parentNodeUri);
		addNodesItem.setReferenceTypeId(referenceTypeId);
		addNodesItem.setTypeDefinition(typeDefinition);
		return addNodesItem;
	}
	public DepthAndRelationQueryParam generateDepth(String namespace,int depth,boolean forward,String id,List<String> fields,List<String> relations,List<DepthSpecifyParam> specifyList)throws CheckDefineException{
		DepthAndRelationQueryParam depthQueryParam=new DepthAndRelationQueryParam();
    	depthQueryParam.setDepth(depth);
    	depthQueryParam.setForward(forward);
    	depthQueryParam.setId(id);
    	
    	if(null!=fields&&fields.size()>0){
    		depthQueryParam.setFields(fields);
    	}
    	if(null!=relations&&relations.size()>0){
    		depthQueryParam.setRelations(relations);
    	}else{
    		throw new CheckDefineException("query relatoion can not null");
    	}
    	
    	//查询指向model为产线的部分
    	List<SpecifyRelationNodeAttrFilter> specifyRelationNodeAttrFilterList=new ArrayList<SpecifyRelationNodeAttrFilter>();        
    	
    	if(null!=specifyList&&specifyList.size()>0){
	    	for(int i=0;i<specifyList.size();i++){
	    		DepthSpecifyParam specify=specifyList.get(i);
		    	SpecifyRelationNodeAttrFilter specifyRelationNodeAttrFilter=new SpecifyRelationNodeAttrFilter();
		    	specifyRelationNodeAttrFilter.setForward(specify.isForward());
		    	specifyRelationNodeAttrFilter.setRelationUri(specify.getRelation());
		    	Map<String,String> map=new HashMap<String,String>();
		    	map.put("uri",specify.getUri() );
		    	specifyRelationNodeAttrFilter.setTargetNodeAttrFilter(map);
		    	specifyRelationNodeAttrFilterList.add(specifyRelationNodeAttrFilter);
		    	depthQueryParam.setSpecifyRelationNodeAttrFilters(specifyRelationNodeAttrFilterList);
	    	}
    	}  
		return depthQueryParam;
	}
	public PatchJson generatePatchJson(String op,String path,
			String value){
		PatchJson patchJson=new PatchJson();
		patchJson.setOp(op);
		patchJson.setPath("/"+path);
		patchJson.setValue(value);
		return patchJson;
	}
	
}
