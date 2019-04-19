package com.emcc.deviceManager.service.user;

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
import com.emcc.deviceManager.param.model.AccessNodeParam;
import com.emcc.deviceManager.param.model.AddNodesItem;
import com.emcc.deviceManager.param.model.QueryRelationParam;
import com.emcc.deviceManager.param.model.RelationV1;
import com.emcc.deviceManager.param.proline.NewProline;
import com.emcc.deviceManager.param.proline.Proline;
import com.emcc.deviceManager.param.proline.ProlineModel;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.model.OpcuaService;
import com.emcc.deviceManager.service.model.RelationService;
import com.emcc.deviceManager.service.model.V2NodeCrudService;
import com.emcc.deviceManager.service.model.V2QueryService;
import com.emcc.deviceManager.service.proline.ProlineService;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(ProlineService.class);
	@Autowired
    private OpcuaService opcuaService;
	@Autowired
    private V2QueryService v2QueryService;
	@Autowired
    private RelationService relationService;
	@Autowired
    private V2NodeCrudService nodeCrudService;
	private String namespace=Constans.NAMESPACE_SYSTEM; 
	/*
	 * 新增用户
	 */	
    public String  addUser(String id)throws Exception{
    	NodeRespones response=new NodeRespones();
    	//建立于接口相同的参数
    	List<AddNodesItem> addNodesItemList=new ArrayList<AddNodesItem>();
    	AddNodesItem addNodeItem=new AddNodesItem();
    	//初始化model接口参数
    	addNodeItem.setBrowseName(UserConstans.USER_NAME);
    	//上级节点的id    	
    	addNodeItem.setParentNodeUri(UserConstans.USER_ROOT);	
    	//扩展属性
    	Map<String, Object> nodeAttributes=new HashMap<String, Object>();
    	nodeAttributes.put("id", id);
    	addNodeItem.setNodeAttributes(nodeAttributes);
    	//nodeclass类型，此处为对象类型
    	addNodeItem.setNodeClass(OpcReference.OBJECT);    	
    	//与上级的关系，层级关系
    	addNodeItem.setReferenceTypeId(OpcReference.HAS_ORGANIZE);
    	//所关联产线模型的id
    	addNodeItem.setTypeDefinition(UserConstans.USER_MODEL);
    	
    	addNodesItemList.add(addNodeItem);    	
    	String requestJson = JacksonJsonUtil.toJson(addNodesItemList);
    	String uri="";
    	try{
    		response=opcuaService.addNodes(namespace, requestJson);
    		uri=response.getResult().toString();
    	}catch(Exception e){
    		throw e;
    	}    	        
    	return uri;
    }
    /*
     * 根据id查询用户是否存在
     */
    public boolean queryUserByUri(String id)throws Exception{
    	NodeRespones response=new NodeRespones();
    	String namespace=Constans.NAMESPACE_SYSTEM;
    	boolean result;
    	//建立于接口查询的参数
    	AccessNodeParam accessNodeParam=new AccessNodeParam();
    	//初始化model接口参数
    	List<String> fields=new ArrayList<>();
    	fields.add("uri");
    	accessNodeParam.setFields(fields);
    	List<String> ids=new ArrayList<>();
    	ids.add(id);
    	accessNodeParam.setIds(ids);    	
    	String requestJson = JacksonJsonUtil.toJson(accessNodeParam);    	
    	response = v2QueryService.queryByUri(requestJson,namespace, id);
    	String jsonString = JSON.toJSONString(response.getResult());  
        System.out.println("jsonString:" + jsonString);  
        // JSON串转用户对象列表  
       // ProlineModel group2 = JSON.parseObject(jsonString2, ProlineModel.class);  
        List<ProlineModel> prolineList = JSON.parseArray(jsonString, ProlineModel.class); 
        if(null==prolineList||prolineList.size()<=0){
	        return false;
        }else{
        	return true;
        }
    }
    /*
	 * 建立与用户的关联
	 */	
    public String  addUserRelation(String objectUri,String userId)throws Exception{
    	NodeRespones response=new NodeRespones();
    	String userUri=Tools.generateUri(Constans.NAMESPACE_SYSTEM, userId);
    	List<RelationV1> relations = new ArrayList<RelationV1>();
    	RelationV1 relation=new RelationV1();
    	relation.setRelation(UserConstans.USER_RELATION);
    	relation.setTarget(objectUri);
    	relation.setSource(userUri);
    	//建立于接口相同的参数
    	relations.add(relation);
    	String requestJson = JacksonJsonUtil.toJson(relations);
    	String uri="";
    	try{
    		response=relationService.addRelation(uri, requestJson);
    		uri=response.getResult().toString();
    	}catch(Exception e){
    		throw e;
    	}    	        
    	return uri;
    }
}
