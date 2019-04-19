package com.emcc.deviceManager.service.simulator;

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
import com.emcc.deviceManager.common.DeviceConstans;
import com.emcc.deviceManager.common.OpcReference;
import com.emcc.deviceManager.common.SimulatorConstans;
import com.emcc.deviceManager.common.Tools;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.device.BaseNode;
import com.emcc.deviceManager.param.model.AccessNodeParam;
import com.emcc.deviceManager.param.model.AddNodesItem;
import com.emcc.deviceManager.param.model.DepthAndRelationQueryParam;
import com.emcc.deviceManager.param.model.DepthSpecifyParam;
import com.emcc.deviceManager.param.model.ExtendInfo;
import com.emcc.deviceManager.param.model.PatchJson;
import com.emcc.deviceManager.param.proline.ProlineModel;
import com.emcc.deviceManager.param.simulator.Simulator;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.common.GenerateModelParam;
import com.emcc.deviceManager.service.common.ModelApp;
import com.emcc.deviceManager.service.model.OpcuaService;
import com.emcc.deviceManager.service.model.RelationService;
import com.emcc.deviceManager.service.model.V2NodeCrudService;
import com.emcc.deviceManager.service.model.V2QueryService;
import com.emcc.deviceManager.service.user.UserService;

@Service
public class SimulatorService {
	private static final Logger logger = LoggerFactory.getLogger(SimulatorService.class);
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
	@Autowired
    private ModelApp modelApp;
	public String addSimulator(Simulator simulator)throws Exception,CheckDefineException{    	
    	NodeRespones response=new NodeRespones();
    	String namespace=SimulatorConstans.SIMULATOR_NAMESPACE; 
    	//建立于接口相同的参数
    	List<AddNodesItem> addNodesItemList=new ArrayList<AddNodesItem>();
    	//扩展属性
    	Map<String, Object> nodeAttributes=new HashMap<String, Object>();
    	nodeAttributes.put(Constans.NODE_DESCRIPTION, simulator.getDescription());
    	nodeAttributes.put(Constans.NODE_DISPLAYNAME, simulator.getName());
    	AddNodesItem addNodeItem=generateModelParam.generateAddNodesItem(simulator.getName(), SimulatorConstans.SIMULATOR_ROOT_URI, 
    			OpcReference.OBJECT, OpcReference.HAS_ORGANIZE,  simulator.getModelUri(),nodeAttributes);
    	addNodesItemList.add(addNodeItem);    	
    	String requestJson = JacksonJsonUtil.toJson(addNodesItemList);
    	String simulatorUri="";
    	try{
	    	response=opcuaService.addNodes(namespace, requestJson);	    	
	    	String jsonString = JSON.toJSONString(response.getResult());  
	    	List<String> deviceList = JSON.parseArray(jsonString,String.class);  
	     	if(null!=deviceList&&deviceList.size()>0){
	     		simulatorUri=(String) deviceList.get(0);
	     	}     
	    	 String rootUri=this.addDeviceRootInfo(namespace, simulatorUri);
	    	 List<ExtendInfo> extendInfoList=this.querySimulatorExtendInfos(rootUri);
    		if(null!=extendInfoList&&extendInfoList.size()>0){
	        	//建立基本信息节点
    			for(int i=0;i<extendInfoList.size();i++){
    				if(extendInfoList.get(i).getBrowseName().equals("ID")){
    						extendInfoList.get(i).setValue(Tools.getIdByUri(simulatorUri));
    				}
    				else if(extendInfoList.get(i).getBrowseName().equals("Submitter")){
    					if(null!=simulator.getSubmitter()&&!simulator.getSubmitter().equals(""))
    						extendInfoList.get(i).setValue(simulator.getSubmitter());
    				}
    				else if(extendInfoList.get(i).getBrowseName().equals("Duration")){
    					if(null!=simulator.getDuration()&&!simulator.getDuration().equals(""))
    						extendInfoList.get(i).setValue(simulator.getDuration());
    				}
    				else if(extendInfoList.get(i).getBrowseName().equals("CollectionDate")){
    					if(null!=simulator.getCollectionDate()&&!simulator.getCollectionDate().equals(""))
    						extendInfoList.get(i).setValue(simulator.getCollectionDate());
    				}	 
    				else if(extendInfoList.get(i).getBrowseName().equals("UploadTime")){
    					if(0!=simulator.getUploadTime())
    						extendInfoList.get(i).setValue(String.valueOf(simulator.getUploadTime()));
    					
    				}	 
    			}
    			this.addSimulatorInfo(namespace, extendInfoList);
    		}
    	}catch(Exception e){
    		throw e;
    	}
    	return simulatorUri;
    }
	 /*
     * 新增基本信息跟节点
     */
    @SuppressWarnings("unchecked")
	public String addDeviceRootInfo(String namespace,String simulUri)throws Exception,CheckDefineException{ 
    	NodeRespones response=new NodeRespones();
    	//新增基本信息跟节点建立于接口相同的参数  	
    	
    	List<AddNodesItem> addNodesItemList=new ArrayList<AddNodesItem>();
    	//扩展属性
    	Map<String, Object> nodeAttributes=new HashMap<String, Object>();
    	AddNodesItem addNodeItem=generateModelParam.generateAddNodesItem(Constans.BASE_INFO, simulUri, 
    			OpcReference.OBJECT, OpcReference.HAS_COMPONENT,  SimulatorConstans.SIMULATOR_EXTEND_MODEL_URI,nodeAttributes);
    	addNodesItemList.add(addNodeItem);    	
    	String requestJson = JacksonJsonUtil.toJson(addNodesItemList);
    	try{
    		response=opcuaService.addNodes(namespace, requestJson);
    		if(response.getCode().equals(Constans.MODEL_RESPONSE_SUCCESS)){
    			List<String> rooturiList=(List<String>) response.getResult();
    			if(null!=rooturiList&&rooturiList.size()>0){
    				String rootUri=rooturiList.get(0);
    				return rootUri;
    			}
    		}
    	}catch(Exception e){
    		throw e;
    	}
		return "";    
    }
    @SuppressWarnings("unused")
  	public void addSimulatorInfo(String namespace,List<ExtendInfo> extendInfoList)throws Exception,CheckDefineException{    	
      	NodeRespones response=new NodeRespones();
      	//新增基本信息跟节点建立于接口相同的参数
      	    	
      	for(int i=0;i<extendInfoList.size();i++){
      		if(null!=extendInfoList.get(i).getValue()&&!extendInfoList.get(i).getValue().equals("")){
  	    		List<PatchJson> addNodesItemList=new ArrayList<PatchJson>();
  	    		String id=Tools.getIdByUri(extendInfoList.get(i).getUri());
  	    		PatchJson patchJson=new PatchJson();
  	    		patchJson.setOp(Constans.PATCH_OP_REPLACE);
  	    		patchJson.setPath("/Value");
  	    		if(extendInfoList.get(i).getBrowseName().equals("IsDefault"))  
  	    			patchJson.setValue(Boolean.valueOf((extendInfoList.get(i).getValue())));
  	    		else
  	    			patchJson.setValue((extendInfoList.get(i).getValue()));
  	    		addNodesItemList.add(patchJson);
  	    		String requestJson = JacksonJsonUtil.toJson(addNodesItemList);
  		    	try{
  		    		response=nodeCrudService.editNodePatch(requestJson, namespace, id);
  		    	}catch(Exception e){
  		    		throw e;
  		    	}
      		}
  	    }
      }
    /*
     * 查询设备的基本信息列表
     */
    public List<ExtendInfo> querySimulatorExtendInfos(String uri)throws Exception,CheckDefineException{   
    	String id=Tools.getIdByUri(uri);
    	String namespace=Tools.getNamespaceByUri(uri);    	
    	//构造查询条件
    	List<String> fields=new ArrayList<String>();
    	fields.add("BrowseName");
    	fields.add("uri");
    	fields.add("Value");
    	List<String> relations=new ArrayList<String>();
    	relations.add(OpcReference.HAS_COMPONENT);
    	List<DepthSpecifyParam> specifyList=new ArrayList<DepthSpecifyParam>();      	
         //查询上级一个节点--配置查询条件
    	DepthAndRelationQueryParam depthAndRelationQueryParam=generateModelParam.generateDepth(namespace, 1, true, id, fields, relations, specifyList);
 		String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);     		
		
     	NodeRespones responseTree=v2QueryService.depth(requestJson, namespace, 20);
     	String jsonString = JSON.toJSONString(responseTree.getResult()); 
     	List<ExtendInfo> simulatorExtendInfoList=new ArrayList<ExtendInfo>();
     	try{
     		simulatorExtendInfoList = JSON.parseArray(jsonString, ExtendInfo.class); 
     	}catch(Exception e){
     		throw e;
     	}
     	return simulatorExtendInfoList;
    }
    public void editSimulator(Simulator simulator)throws Exception,CheckDefineException{    	
    	NodeRespones response=new NodeRespones();
    	String namespace=SimulatorConstans.SIMULATOR_NAMESPACE; 
    	String uri=Tools.generateUri(namespace, simulator.getId());
    	List<PatchJson> patchList=new ArrayList<PatchJson>();
    	if(Tools.checkStringNull(simulator.getName())){
	    	PatchJson patchName=generateModelParam.generatePatchJson(Constans.PATCH_OP_REPLACE, 
	    			"DisplayName", simulator.getName());	    	
	    	patchList.add(patchName);    
	    	
	    	PatchJson name=generateModelParam.generatePatchJson(Constans.PATCH_OP_REPLACE, 
	    			"name", simulator.getName());	 
	    	patchList.add(name);
    	}
    	
    	//修改描述
    	if(Tools.checkStringNull(simulator.getDescription())){
	    	PatchJson patchDecription=generateModelParam.generatePatchJson(Constans.PATCH_OP_REPLACE, 
	    			"Description", simulator.getDescription());
	    	patchList.add(patchDecription);
	    }
    	String requestJson = JacksonJsonUtil.toJson(patchList);
    	try{
    		response=nodeCrudService.editNodePatch(requestJson, SimulatorConstans.SIMULATOR_NAMESPACE, simulator.getId());
    		if(Tools.checkStringNull(simulator.getCollectionDate())||Tools.checkStringNull(simulator.getDuration())
    				||Tools.checkStringNull(simulator.getSubmitter())){
        		//构造查询上级的查询条件
    			String rootUri=this.querySimulatorExtendInfoRootUri(uri) ;
    			List<ExtendInfo> extendInfoList=this.querySimulatorExtendInfos(rootUri);
    	         //判断是否上级节点没有变  
    			if(Tools.checkListNull(extendInfoList)){
	    			for(int i=0;i<extendInfoList.size();i++){
	    				if(extendInfoList.get(i).getBrowseName().equals("Submitter")){
			    	        if(Tools.checkStringNull(simulator.getSubmitter())){
			    	        	//删除原有关系
			    	        	extendInfoList.get(i).setValue(simulator.getSubmitter());
			    	        }
	    				}else if(extendInfoList.get(i).getBrowseName().equals("Duration")){
			    	        if(Tools.checkStringNull(simulator.getDuration())){
			    	        	//删除原有关系
			    	        	extendInfoList.get(i).setValue(simulator.getDuration());
			    	        }
	    				}else if(extendInfoList.get(i).getBrowseName().equals("CollectionDate")){
			    	        if(Tools.checkStringNull(simulator.getCollectionDate())){
			    	        	//删除原有关系
			    	        	extendInfoList.get(i).setValue(simulator.getCollectionDate());
			    	        }
	    				}
	    				else if(extendInfoList.get(i).getBrowseName().equals("UploadTime")){
	    					if(0!=simulator.getUploadTime())
	    						extendInfoList.get(i).setValue(String.valueOf(simulator.getUploadTime()));
	    					
	    				}	
	    			}
	    			this.addSimulatorInfo(namespace, extendInfoList);	    			
    			} 
    		}
    	}catch(Exception e){
    		throw e;
    	}  
    }
    /*
     * 查询设备的基本信息的跟节点
     */
    @SuppressWarnings("rawtypes")
	public String querySimulatorExtendInfoRootUri(String uri) throws Exception,CheckDefineException{   
    	String id=Tools.getIdByUri(uri);
    	String namespace=Tools.getNamespaceByUri(uri);    	
    	//构造查询条件
    	List<String> fields=new ArrayList<String>();
    	fields.add("uri");
    	List<String> relations=new ArrayList<String>();
    	relations.add(OpcReference.HAS_COMPONENT);
    	List<DepthSpecifyParam> specifyList=new ArrayList<DepthSpecifyParam>();  
    	DepthSpecifyParam depthSpecifyParam=new DepthSpecifyParam();
    	depthSpecifyParam.setForward(true);
    	depthSpecifyParam.setRelation(OpcReference.HAS_TYPE_DEFINITION);
    	depthSpecifyParam.setUri(SimulatorConstans.SIMULATOR_EXTEND_MODEL_URI);
    	specifyList.add(depthSpecifyParam);
    	try{
    		DepthAndRelationQueryParam depthAndRelationQueryParam=generateModelParam.generateDepth(namespace,  1, true, id, fields, relations, specifyList);
     		String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);     		
    		
	    	NodeRespones responseTree= v2QueryService.depth(requestJson, namespace, 20);
	 		String jsonString = JSON.toJSONString(responseTree.getResult());  
	     	List<Map> deviceList = JSON.parseArray(jsonString, Map.class);  
     		if(null!=deviceList&&deviceList.size()>0){
	     		String extendInfouri=(String) deviceList.get(0).get("uri");
	     		return extendInfouri;
	     	}        
    	}catch(Exception e){
    		throw e;
    	}
    	return "";
    }
    public void setDefault(String oldId,String defaultId)throws Exception,CheckDefineException{
    	String namespace=SimulatorConstans.SIMULATOR_NAMESPACE; 
    	String oldUri=Tools.generateUri(namespace, oldId);
    	String defaultUri=Tools.generateUri(namespace, defaultId);
    	List<ExtendInfo> extendList=new ArrayList<ExtendInfo>();
    	try{
	    	if(Tools.checkStringNull(oldId)){
	    		String oldRootUri=this.querySimulatorExtendInfoRootUri(oldUri) ;
	    		List<ExtendInfo> extendInfoList=this.querySimulatorExtendInfos(oldRootUri);
	    		if(Tools.checkListNull(extendInfoList)){
	    			for(int i=0;i<extendInfoList.size();i++){
	    				if(extendInfoList.get(i).getBrowseName().equals("IsDefault")){		    	        	//删除原有关系
			    	        extendInfoList.get(i).setValue("false");
			    	        extendList.add(extendInfoList.get(i));
	    				}
	    			}	    			
	    		}
	    	}
	    	String defaultRootUri=this.querySimulatorExtendInfoRootUri(defaultUri) ;
			List<ExtendInfo> extendInfoList=this.querySimulatorExtendInfos(defaultRootUri);
			if(Tools.checkListNull(extendInfoList)){
				for(int i=0;i<extendInfoList.size();i++){
					if(extendInfoList.get(i).getBrowseName().equals("IsDefault")){		    	        	//删除原有关系
		    	        extendInfoList.get(i).setValue("true");
		    	        extendList.add(extendInfoList.get(i));
					}
				}
			}    	
		    this.addSimulatorInfo(namespace, extendList);	  		
    	}catch(Exception e){
    		throw e;
    	}  
    }
	public Simulator getSimulator(String id)throws Exception,CheckDefineException{
		Simulator simulator=new Simulator();
		String uri=Tools.generateUri(SimulatorConstans.SIMULATOR_NAMESPACE, id);
		NodeRespones response=new NodeRespones();
    	//建立于接口查询的参数
    	AccessNodeParam accessNodeParam=new AccessNodeParam();
    	//初始化model接口参数
    	List<String> fields=new ArrayList<>();
    	fields.add("uri");
    	fields.add("name");
    	fields.add("Description");
    	accessNodeParam.setFields(fields);
    	List<String> ids=new ArrayList<>();
    	ids.add(id);
    	accessNodeParam.setIds(ids);    	
    	String requestJson = JacksonJsonUtil.toJson(accessNodeParam);   
    	try{
	    	response = v2QueryService.queryByUri(requestJson,SimulatorConstans.SIMULATOR_NAMESPACE, id);
	    	List<BaseNode> model=modelApp.queryDeviceRelation(uri, OpcReference.HAS_TYPE_DEFINITION, true);
	    	String jsonString = JSON.toJSONString(response.getResult());  
	        System.out.println("jsonString:" + jsonString);
	        List<Simulator> simulatorList = JSON.parseArray(jsonString, Simulator.class); 
	        if(null!=simulatorList&&simulatorList.size()>0){
	        	if(Tools.checkListNull(model)){
	        		simulator.setModelName(model.get(0).getBrowseName());
	        		simulator.setModelUri(model.get(0).getUri());
	        	}
	        	simulator.setDescription(simulatorList.get(0).getDescription());
	        	simulator.setId(id);
	        	simulator.setName(simulatorList.get(0).getName());  
	        	
		        //构造查询上级的查询条件
		        String extendIfoRootUri=this.querySimulatorExtendInfoRootUri(uri);
		        if(Tools.checkStringNull(extendIfoRootUri)){
			        List<ExtendInfo> extendInfoList=this.querySimulatorExtendInfos(extendIfoRootUri);
			         //填充上级节点信息    
			        if(Tools.checkListNull(extendInfoList)){
			        	for(int i=0;i<extendInfoList.size();i++){
					        if(extendInfoList.get(i).getBrowseName().equals("ID")){
					        	simulator.setId(extendInfoList.get(i).getValue());
					        }else  if(extendInfoList.get(i).getBrowseName().equals("Submitter")){
					        	simulator.setSubmitter(extendInfoList.get(i).getValue());
					        }else  if(extendInfoList.get(i).getBrowseName().equals("Duration")){
					        	simulator.setDuration(extendInfoList.get(i).getValue());
					        }else  if(extendInfoList.get(i).getBrowseName().equals("CollectionDate")){
					        	simulator.setCollectionDate(extendInfoList.get(i).getValue());
					        }else  if(extendInfoList.get(i).getBrowseName().equals("IsDefault")){
					        	simulator.setDefault(Boolean.valueOf(extendInfoList.get(i).getValue()));
					        }else if(extendInfoList.get(i).getBrowseName().equals("UploadTime")&&!extendInfoList.get(i).getValue().equals("")){
					        	simulator.setUploadTime(Long.valueOf(extendInfoList.get(i).getValue()));		    					
		    				}else if(extendInfoList.get(i).getBrowseName().equals("UploadTime")&&extendInfoList.get(i).getValue().equals("")){
		    					simulator.setUploadTime(0);	
		    				}
			        	}
			        }
		        }
	        }
    	}catch(Exception e){
    		throw e;
    	}
    	 return simulator;
	}
	/*
	 * 根据模型查询列表
	 */
	public List<Simulator> querySimulators(String modelNamespace, String modelId,String modelName)
			throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		String modelUri = Tools.generateUri(modelNamespace, modelId);		
		List<Simulator> simulatorInfoList = new ArrayList<>();
		List<String> fields = new ArrayList<>();
		fields.add("name");
		fields.add("uri");
		fields.add("Description");
		List<String> relations = new ArrayList<>();
		relations.add(OpcReference.HAS_ORGANIZE);
		// 构造模型参数
		List<DepthSpecifyParam> specifyList = new ArrayList<>();
		DepthSpecifyParam depthSpecifyParam = new DepthSpecifyParam();
		depthSpecifyParam.setForward(true);
		depthSpecifyParam.setRelation(OpcReference.HAS_TYPE_DEFINITION);
		depthSpecifyParam.setUri(modelUri);
		specifyList.add(depthSpecifyParam);
		try {
			// 查询仿真数据信息
			DepthAndRelationQueryParam depthparam=generateModelParam.generateDepth(Constans.NAMESPACE_SYSTEM, 1,
					true, Tools.getIdByUri(SimulatorConstans.SIMULATOR_ROOT_URI), fields, relations, specifyList);
			String requestJson = JacksonJsonUtil.toJson(depthparam);
			response = v2QueryService.depth(requestJson, Constans.NAMESPACE_SYSTEM, 10);
			String jsonString = JSON.toJSONString(response.getResult());
			List<Map> simulatorUriList = JSON.parseArray(jsonString, Map.class);
			if (null != simulatorUriList && simulatorUriList.size() > 0) {
				for (int i=0;i<simulatorUriList.size();i++) {
					String name = (String) simulatorUriList.get(i).get("name");
					String uri = (String) simulatorUriList.get(i).get("uri");
					String description = (String) simulatorUriList.get(i).get("Description");
					Simulator simulator = new Simulator();
					simulator.setDescription(description);
					simulator.setName(name);
					// 查询仿真数据所属模型信息
					simulator.setModelUri(modelUri);
					simulator.setModelName(modelName);
					// 查询仿真数据的extendInfo					
					 //构造查询上级的查询条件
			        String extendIfoRootUri=this.querySimulatorExtendInfoRootUri(uri);
			        if(Tools.checkStringNull(extendIfoRootUri)){
				        List<ExtendInfo> extendInfoList=this.querySimulatorExtendInfos(extendIfoRootUri);
				         //填充上级节点信息    
				        if(Tools.checkListNull(extendInfoList)){
				        	for(int j=0;j<extendInfoList.size();j++){
				        		if(extendInfoList.get(j).getBrowseName().equals("ID")){
						        	simulator.setId(extendInfoList.get(j).getValue());
						        }else  if(extendInfoList.get(j).getBrowseName().equals("Submitter")){
						        	simulator.setSubmitter(extendInfoList.get(j).getValue());
						        }else  if(extendInfoList.get(j).getBrowseName().equals("Duration")){
						        	simulator.setDuration(extendInfoList.get(j).getValue());
						        }else  if(extendInfoList.get(j).getBrowseName().equals("CollectionDate")){
						        	simulator.setCollectionDate(extendInfoList.get(j).getValue());
						        }else  if(extendInfoList.get(j).getBrowseName().equals("IsDefault")){
						        	simulator.setDefault(Boolean.valueOf(extendInfoList.get(j).getValue()));
						        }else if(extendInfoList.get(j).getBrowseName().equals("UploadTime")&&!extendInfoList.get(j).getValue().equals("")){
						        	simulator.setUploadTime(Long.valueOf(extendInfoList.get(j).getValue()));		    					
			    				}else if(extendInfoList.get(j).getBrowseName().equals("UploadTime")&&extendInfoList.get(j).getValue().equals("")){
			    					simulator.setUploadTime(0);	
			    				}
				        	}
				        }
			        }					
					simulatorInfoList.add(simulator);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return simulatorInfoList;
	}
	public  void deleteSimulator(String id)throws Exception, CheckDefineException{
		try{
			opcuaService.deleteTreeByUri(SimulatorConstans.SIMULATOR_NAMESPACE, id, "");
		}catch(Exception e){
			throw e;
		}
	}
}
