package com.emcc.deviceManager.service.proline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.JacksonJsonUtil;

import com.alibaba.fastjson.JSON;
import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.common.OpcReference;
import com.emcc.deviceManager.common.ProlineConstans;
import com.emcc.deviceManager.common.Tools;
import com.emcc.deviceManager.common.UserConstans;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.device.BaseNode;
import com.emcc.deviceManager.param.model.AccessNodeParam;
import com.emcc.deviceManager.param.model.AddNodesItem;
import com.emcc.deviceManager.param.model.DepthAndRelationQueryParam;
import com.emcc.deviceManager.param.model.DepthSpecifyParam;
import com.emcc.deviceManager.param.model.PatchJson;
import com.emcc.deviceManager.param.proline.EditProline;
import com.emcc.deviceManager.param.proline.NewProline;
import com.emcc.deviceManager.param.proline.Proline;
import com.emcc.deviceManager.param.proline.ProlineModel;
import com.emcc.deviceManager.response.model.BaseResponseWithTotalCount;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.common.GenerateModelParam;
import com.emcc.deviceManager.service.model.OpcuaService;
import com.emcc.deviceManager.service.model.RelationService;
import com.emcc.deviceManager.service.model.V2NodeCrudService;
import com.emcc.deviceManager.service.model.V2QueryService;

@Service
public class ProlineService {
		
	private static final Logger logger = LoggerFactory.getLogger(ProlineService.class);
	@Autowired
    private OpcuaService opcuaService;
	@Autowired
    private V2QueryService v2QueryService;
	@Autowired
    private V2NodeCrudService nodeCrudService;
	@Autowired
    private RelationService relationService;
	@Autowired
    private GenerateModelParam generateModelParam;
	
	 public String  addProlineold(String namespace,NewProline proline)throws Exception{
	    	NodeRespones response=new NodeRespones();
	    	//建立于接口相同的参数
	    	List<AddNodesItem> addNodesItemList=new ArrayList<AddNodesItem>();
	    	AddNodesItem addNodeItem=new AddNodesItem();
	    	//上级节点的id    	
	    	if(null==proline.getParentUri()||proline.getParentUri().equals("")){
	    		addNodeItem.setParentNodeUri(ProlineConstans.PROLINE_ROOT_URI);			
			}else{
				addNodeItem.setParentNodeUri(proline.getParentUri());
			}    	
	    	//扩展属性
	    	Map<String, Object> nodeAttributes=new HashMap<String, Object>();
	    	String description=proline.getDescription();
	    	nodeAttributes.put(ProlineConstans.MODEL_DESCRIPTION, description);
	    	nodeAttributes.put(ProlineConstans.MODEL_DISPLAYNAME, proline.getName());
	    	addNodeItem.setNodeAttributes(nodeAttributes);
	    	//nodeclass类型，此处为对象类型
	    	addNodeItem.setNodeClass(OpcReference.OBJECT);    	
	    	//与上级的关系，层级关系
	    	addNodeItem.setReferenceTypeId(OpcReference.HAS_ORGANIZE);
	    	//所关联产线模型的id
	    	addNodeItem.setTypeDefinition(ProlineConstans.MODEL_PROLINE_ID);
	    	
	    	addNodesItemList.add(addNodeItem);    	
	    	String requestJson = JacksonJsonUtil.toJson(addNodesItemList);
	    	try{
		    	response=opcuaService.addNodes(namespace, requestJson);
		    	String prolinUri=response.getResult().toString();
		         logger.info("response is {}",response);
		    	return prolinUri;
	    	}catch(Exception e){
	    		throw e;
	    	}
	    }
	
    public String  addProline(String namespace,String userId,NewProline proline)throws Exception{
    	NodeRespones response=new NodeRespones();
    	String userUri=Tools.generateUri(Constans.NAMESPACE_SYSTEM,userId);
    	//建立于接口相同的参数
    	List<AddNodesItem> addNodesItemList=new ArrayList<AddNodesItem>();
    	AddNodesItem addNodeItem=new AddNodesItem();
    	//上级节点的id    	
    	if(null==proline.getParentUri()||proline.getParentUri().equals("")){
    		addNodeItem.setParentNodeUri(ProlineConstans.PROLINE_ROOT_URI);			
		}else{
			addNodeItem.setParentNodeUri(proline.getParentUri());
		}    	
    	//扩展属性
    	Map<String, Object> nodeAttributes=new HashMap<String, Object>();
    	String description=proline.getDescription();
    	nodeAttributes.put(ProlineConstans.MODEL_DESCRIPTION, description);
    	nodeAttributes.put(ProlineConstans.MODEL_DISPLAYNAME, proline.getName());
    	addNodeItem.setNodeAttributes(nodeAttributes);
    	//nodeclass类型，此处为对象类型
    	addNodeItem.setNodeClass(OpcReference.OBJECT);    	
    	//与上级的关系，层级关系
    	addNodeItem.setReferenceTypeId(OpcReference.HAS_ORGANIZE);
    	//所关联产线模型的id
    	addNodeItem.setTypeDefinition(ProlineConstans.MODEL_PROLINE_ID);
    	
    	addNodesItemList.add(addNodeItem);    	
    	String requestJson = JacksonJsonUtil.toJson(addNodesItemList);
    	try{
	    	response=opcuaService.addNodes(namespace, requestJson);
	    	String jsonString = JSON.toJSONString(response.getResult());  
	    	List<String> deviceList = JSON.parseArray(jsonString,String.class);  
	    	String prolinUri="";
	     	if(null!=deviceList&&deviceList.size()>0){
	     		prolinUri=(String) deviceList.get(0);
	     	}        
	    	relationService.editRelation(namespace, prolinUri, userUri, UserConstans.USER_RELATION, Constans.PATCH_OP_ADD);
	        logger.info("response is {}",response);
	    	return prolinUri;
    	}catch(Exception e){
    		throw e;
    	}
    }
    /*
     * 根据uri查询产线
     */
    public Proline queryProlineByUri(String namespace,String id)throws Exception{
    	Proline proline=new Proline();
    	NodeRespones response=new NodeRespones();
    	//建立于接口查询的参数
    	AccessNodeParam accessNodeParam=new AccessNodeParam();
    	//初始化model接口参数
    	List<String> fields=new ArrayList<>();
    	fields.add("");
    	accessNodeParam.setFields(fields);
    	List<String> ids=new ArrayList<>();
    	ids.add(id);
    	accessNodeParam.setIds(ids);    	
    	String requestJson = JacksonJsonUtil.toJson(accessNodeParam);   
    	try{
	    	response = v2QueryService.queryByUri(requestJson,namespace, id);
	    	String jsonString = JSON.toJSONString(response.getResult());  
	        System.out.println("jsonString:" + jsonString);  
	        // JSON串转用户对象列表  
	       // ProlineModel group2 = JSON.parseObject(jsonString2, ProlineModel.class);  
	        List<ProlineModel> prolineList = JSON.parseArray(jsonString, ProlineModel.class); 
	        if(null!=prolineList&&prolineList.size()>0){
		        proline.setDescription(prolineList.get(0).getDescription());
		        proline.setId(id);
		        proline.setName(prolineList.get(0).getBrowseName());  
		        //构造查询上级的查询条件
		        BaseNode baseNode=this.queryRelationNodes(prolineList.get(0).getUri(), OpcReference.HAS_ORGANIZE, false);
		         //填充上级节点信息    
		        if(null!=baseNode&&null!=baseNode.getUri()&&null!=baseNode.getBrowseName()){
		         proline.setParentUri(baseNode.getUri());
		         proline.setParentName(baseNode.getBrowseName());
		        }
		         return proline;
	        }
    	}catch(Exception e){
    		throw e;
    	}
    	return null;
    }
    
 
    /*
     * 编辑产线
     */
    public boolean editPatchProline(String namespace,EditProline proline)throws CheckDefineException, Exception{
    	String id=proline.getId();
    	String uri=Tools.generateUri(namespace, id);
    	NodeRespones response=new NodeRespones();  	
    	List<PatchJson> patchList=new ArrayList<PatchJson>();
    	//修改名称
    	PatchJson patchName=new PatchJson();
    	patchName.setOp(Constans.PATCH_OP_REPLACE);
    	patchName.setPath("/DisplayName");
    	patchName.setValue(proline.getName());
    	patchList.add(patchName);   	
    	
    	PatchJson name=new PatchJson();
    	name.setOp(Constans.PATCH_OP_REPLACE);
    	name.setPath("/name");
    	name.setValue(proline.getName());
    	patchList.add(name);
    	
    	//修改描述
    	PatchJson patchDecription=new PatchJson();
    	patchDecription.setOp(Constans.PATCH_OP_REPLACE);
    	patchDecription.setPath("/Description");
    	patchDecription.setValue(proline.getDescription());
    	patchList.add(patchDecription);
    	String requestJson = JacksonJsonUtil.toJson(patchList);
    	try{
    		response=nodeCrudService.editNodePatch(requestJson, namespace, id);
    		if(null!=proline.getParentUri()&&!proline.getParentUri().equals("")){
        		//构造查询上级的查询条件
    			BaseNode baseNode=this.queryRelationNodes(uri, OpcReference.HAS_ORGANIZE, false);
    	       
    	         //判断是否上级节点没有变            
    	        if(!proline.getParentUri().equals(baseNode.getUri())){
    	        	//删除原有关系
		        	relationService.editRelation(namespace,  baseNode.getUri(),uri, OpcReference.HAS_ORGANIZE, Constans.PATCH_OP_MOVE);
		    		//新增关系
		    		relationService.editRelation(namespace, proline.getParentUri(), uri, OpcReference.HAS_ORGANIZE, Constans.PATCH_OP_ADD);
    	        }
        	}
    	}catch(Exception e){
    		throw e;
    	}    	
        if(response.getCode().equals(Constans.MODEL_RESPONSE_SUCCESS)){
        	return true;
        }else{
        	return false;
        }
    }
   /* 
     * 查询整个产线树
     */
    public NodeRespones queryProlineByNamespaceold(String namespace)throws Exception{    	
    	NodeRespones response=new NodeRespones();
    	namespace=ProlineConstans.PROLINE_ROOT_NAMESPACE;
    	
    	//初始化查询条件
    	List<String> fields=new ArrayList<String>();
    	fields.add("uri");
    	fields.add("Description");
    	List<String> relations=new ArrayList<String>();
    	relations.add(OpcReference.HAS_ORGANIZE);
    	List<DepthSpecifyParam> specifyList=new ArrayList<DepthSpecifyParam>();  
    	//查询指向model为产线的部分
    	DepthSpecifyParam depthSpecifyParam=new DepthSpecifyParam();
    	depthSpecifyParam.setForward(true);
    	depthSpecifyParam.setRelation(OpcReference.HAS_TYPE_DEFINITION);
    	depthSpecifyParam.setUri(ProlineConstans.MODEL_PROLINE_ID);
    	specifyList.add(depthSpecifyParam);
         //查询上级一个节点--配置查询条件
     	try{
     		DepthAndRelationQueryParam depthAndRelationQueryParam=generateModelParam.generateDepth(namespace, 5, true, ProlineConstans.PROLINE_ROOT_ID, fields, relations, specifyList);
     		String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);     		
    		
     		BaseResponseWithTotalCount responseTree= opcuaService.getTreeNodesByDepth(namespace, requestJson);
	     	if(null!=responseTree){
	        	response.setResult(responseTree.getResult());
	        }
     	}catch(Exception e){
     		throw e;
     	}        
    	return response;
    }
    /*
     * 查询整个产线树
     */
    public NodeRespones queryProlineByNamespace(String namespace,String userId)throws Exception{    	
    	NodeRespones response=new NodeRespones();
    	String userUri=Tools.generateUri(Constans.NAMESPACE_SYSTEM, userId);
    	namespace=ProlineConstans.PROLINE_ROOT_NAMESPACE;
    	
    	//初始化查询条件
    	List<String> fields=new ArrayList<String>();
    	fields.add("uri");
    	fields.add("Description");
    	List<String> relations=new ArrayList<String>();
    	relations.add(OpcReference.HAS_ORGANIZE);
    	List<DepthSpecifyParam> specifyList=new ArrayList<DepthSpecifyParam>();  
    	//查询指向model为产线的部分
    	DepthSpecifyParam depthSpecifyParam=new DepthSpecifyParam();
    	depthSpecifyParam.setForward(true);
    	depthSpecifyParam.setRelation(OpcReference.HAS_TYPE_DEFINITION);
    	depthSpecifyParam.setUri(ProlineConstans.MODEL_PROLINE_ID);
    	specifyList.add(depthSpecifyParam);
    	//增加指向用户的条件  
    	DepthSpecifyParam userSpecifyParam=new DepthSpecifyParam();
    	userSpecifyParam.setForward(true);
    	userSpecifyParam.setRelation(UserConstans.USER_RELATION);
    	userSpecifyParam.setUri(userUri);
    	specifyList.add(userSpecifyParam);    	
    	
         //查询上级一个节点--配置查询条件
     	try{
     		DepthAndRelationQueryParam depthAndRelationQueryParam=generateModelParam.generateDepth(namespace, 5, true, ProlineConstans.PROLINE_ROOT_ID, fields, relations, specifyList);
     		String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);     		
    		
	     	BaseResponseWithTotalCount responseTree= opcuaService.getTreeNodesByDepth(namespace, requestJson);
	     	if(null!=responseTree){
	        	response.setResult(responseTree.getResult());
	        }
     	}catch(Exception e){
     		throw e;
     	}        
    	return response;
    }
    /*
	 * 删除产线,如果产线下有下级节点则不删除 先把id看做namespace 查询的namespace:
	 * haier:http://model.hollysys.com/haier/
	 */
	public void deleteProline(String namespace, String id) throws Exception,CheckDefineException {
		// 1.查询是否存在下级节点,构造参数		
		String uri=Tools.generateUri(namespace, id);
		
		try{
			BaseNode baseNode=this.queryRelationNodes(uri, OpcReference.HAS_ORGANIZE, true);			
			if (null==baseNode||null==baseNode.getUri()||baseNode.getUri().equals("")) {
				// 2.删除产线,在模型服务内删除
				String[] nodesDelete = { id };
				String deleteRequestJson = JacksonJsonUtil.toJson(nodesDelete);
				NodeRespones deleteNodeRespones = nodeCrudService.delete(deleteRequestJson, namespace);	
			}else{
				throw new CheckDefineException("There is a lower level entity in the proline. Do not delete it.");			    
			}
		}catch(Exception e){
			throw e;
		}
	}
	/*
	 * 查询上级或下级关系
	 */
	private BaseNode queryRelationNodes(String deviceUri,String relation,boolean forward)throws Exception,CheckDefineException{
    	String namespace=Tools.getNamespaceByUri(deviceUri);
    	String id=Tools.getIdByUri(deviceUri);
    	List<BaseNode> baseNodes=new ArrayList<BaseNode>();
    	 //构造查询上级的查询条件
    	BaseNode baseNode=new BaseNode();    	
    	//构造查询条件
    	List<String> fields=new ArrayList<String>();
    	fields.add("uri");
    	fields.add("BrowseName");
    	List<String> relations=new ArrayList<String>();
    	relations.add(relation);
    	List<DepthSpecifyParam> specifyList=new ArrayList<DepthSpecifyParam>();     	
        //查询上级一个节点--配置查询条件
    	try{
    		DepthAndRelationQueryParam depthAndRelationQueryParam=generateModelParam.generateDepth(namespace,  1, forward, id, fields, relations, specifyList);
     		String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);     		
    		
	    	NodeRespones responseModel=v2QueryService.depth(requestJson, namespace, 1);
	    	String jsonString = JSON.toJSONString(responseModel.getResult());  
	    	baseNodes = JSON.parseArray(jsonString, BaseNode.class); 
	    	if(null!=baseNodes&&baseNodes.size()>0){
	    		return baseNodes.get(0);
	    	}
    	}catch(Exception e){
    		throw e;
    	}
		return baseNode;
    }

}
