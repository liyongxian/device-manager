package com.emcc.deviceManager.service.device;

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
import com.emcc.deviceManager.common.Tools;
import com.emcc.deviceManager.common.UserConstans;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.device.AddDeviceParam;
import com.emcc.deviceManager.param.device.BaseNode;
import com.emcc.deviceManager.param.device.Device;
import com.emcc.deviceManager.param.device.DeviceQueryParam;
import com.emcc.deviceManager.param.model.AccessNodeParam;
import com.emcc.deviceManager.param.model.AddNodesItem;
import com.emcc.deviceManager.param.model.DepthAndRelationQueryParam;
import com.emcc.deviceManager.param.model.DepthSpecifyParam;
import com.emcc.deviceManager.param.model.ExtendInfo;
import com.emcc.deviceManager.param.model.NodeBase;
import com.emcc.deviceManager.param.model.PatchJson;
import com.emcc.deviceManager.param.model.QueryRelation;
import com.emcc.deviceManager.param.model.RelationV1;
import com.emcc.deviceManager.response.model.BaseResponseWithTotalCount;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.common.GenerateModelParam;
import com.emcc.deviceManager.service.model.OpcuaService;
import com.emcc.deviceManager.service.model.RelationService;
import com.emcc.deviceManager.service.model.V2NodeCrudService;
import com.emcc.deviceManager.service.model.V2QueryService;
import com.emcc.deviceManager.service.proline.ProlineService;
import com.emcc.deviceManager.service.user.UserService;

@Service
public class DeviceService {
	private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);
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
	public NodeRespones queryAllModel(String namespace) throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		namespace = Constans.MODEL_ROOT_NAMESPACE;
		// 初始化查询条件
		List<String> fields = new ArrayList<String>();
		fields.add("uri");
		List<String> relations = new ArrayList<String>();
		relations.add(OpcReference.HAS_ORGANIZE);
		List<DepthSpecifyParam> specifyList = new ArrayList<DepthSpecifyParam>();

		try {
			DepthAndRelationQueryParam depthAndRelationQueryParam = generateModelParam.generateDepth(namespace, 5, true,
					Constans.MODEL_ROOT_ID, fields, relations, specifyList);
			String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);
			BaseResponseWithTotalCount responseTree = opcuaService.getTreeNodesByDepth(namespace, requestJson);
			response.setResult(responseTree.getResult());
		} catch (Exception e) {
			throw e;
		}

		return response;
	}

	/*
	 * 根据uri查询设备
	 */
	public Device queryDeviceByUri(String namespace, String deviceUri) throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();

		// 建立于接口查询的参数
		AccessNodeParam accessNodeParam = new AccessNodeParam();
		String id = Tools.getIdByUri(deviceUri);
		String deviceNamespace = Tools.getNamespaceByUri(deviceUri);
		// 初始化查询条件
		List<String> fields = new ArrayList<>();
		fields.add("uri");
		fields.add("BrowseName");
		accessNodeParam.setFields(fields);
		List<String> ids = new ArrayList<>();
		ids.add(id);
		accessNodeParam.setIds(ids);
		String requestJson = JacksonJsonUtil.toJson(accessNodeParam);
		try {
			response = v2QueryService.queryByUri(requestJson, deviceNamespace, id);
			List resultList = (List) response.getResult();
			if (resultList.size() == 0) {
				throw new CheckDefineException("device can not be null!");
			}
			String jsonString = JSON.toJSONString(response.getResult());
			System.out.println("jsonString:" + jsonString);

			List<NodeBase> deviceList = JSON.parseArray(jsonString, NodeBase.class);
			if (null != deviceList && deviceList.size() > 0) {
				Device device = new Device();
				device.setId(Tools.getIdByUri(deviceList.get(0).getUri()));
				device.setName(deviceList.get(0).getBrowseName());
				BaseNode model = this.queryDeviceRelation(deviceList.get(0).getUri(), OpcReference.HAS_TYPE_DEFINITION,
						true);
				device.setModelName(model.getBrowseName());
				device.setModelUri(model.getUri());
				return device;
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	/*
	 * 查询设备列表 不含userId，即将作废
	 */
	public List<Device> queryDevices(String namespace, String prolineId, String modelNamespace, String modelId,
			String modelName) throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		List<Device> deviceInfoList = new ArrayList<Device>();
		List<String> fields = new ArrayList<String>();
		fields.add("uri");
		List<String> relations = new ArrayList<String>();
		relations.add(OpcReference.HAS_ORGANIZE);
		List<DepthSpecifyParam> specifyList = new ArrayList<DepthSpecifyParam>();
		DepthSpecifyParam depthSpecifyParam = new DepthSpecifyParam();
		depthSpecifyParam.setForward(true);
		depthSpecifyParam.setRelation(OpcReference.HAS_TYPE_DEFINITION);
		depthSpecifyParam.setUri(DeviceConstans.DEVICE_ROOT);
		specifyList.add(depthSpecifyParam);
		// 查询设备列表
		try {
			DepthAndRelationQueryParam depthAndRelationQueryParam = generateModelParam.generateDepth(namespace, 1, true,
					prolineId, fields, relations, specifyList);
			String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);
			response = v2QueryService.depth(requestJson, modelNamespace, 20);
			String jsonString = JSON.toJSONString(response.getResult());
			@SuppressWarnings("rawtypes")
			List<Map> deviceUriList = JSON.parseArray(jsonString, Map.class);
			if (null != deviceUriList && deviceUriList.size() > 0) {
				for (int i = 0; i < deviceUriList.size(); i++) {
					String uri = (String) deviceUriList.get(i).get("uri");
					Device device = this.queryDevicesRootExtendInfo(uri);
					device.setId(Tools.getIdByUri(uri));
					device.setModelName(modelName);
					deviceInfoList.add(device);
				}
			}
		} catch (Exception e) {
			throw e;
		}

		return deviceInfoList;
	}

	/*
	 * 查询设备列表
	 */
	@SuppressWarnings("rawtypes")
	public List<Device> queryDevicesNews(String namespace, String userId, DeviceQueryParam queryParam)
			throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		List<Device> deviceInfoList = new ArrayList<Device>();
		namespace = Tools.getNamespaceByUri(queryParam.getProline().getUri());
		String userUri = Tools.generateUri(Constans.NAMESPACE_SYSTEM, userId);

		List<String> fields = new ArrayList<String>();
		fields.add("uri");
		fields.add("Description");
		fields.add("name");
		List<String> relations = new ArrayList<String>();
		relations.add(OpcReference.HAS_ORGANIZE);
		// 构造模型参数
		List<DepthSpecifyParam> specifyList = new ArrayList<DepthSpecifyParam>();
		if (null != queryParam.getModels() && queryParam.getModels().size() >= 0) {
			for (int i = 0; i < queryParam.getModels().size(); i++) {
				String modelUri = queryParam.getModels().get(i).getUri();
				DepthSpecifyParam depthSpecifyParam = new DepthSpecifyParam();
				depthSpecifyParam.setForward(true);
				depthSpecifyParam.setRelation(OpcReference.HAS_TYPE_DEFINITION);
				depthSpecifyParam.setUri(modelUri);
				specifyList.add(depthSpecifyParam);
			}
		}
		// 构造用户参数
		DepthSpecifyParam userSpecifyParam = new DepthSpecifyParam();
		userSpecifyParam.setForward(false);
		userSpecifyParam.setRelation(UserConstans.USER_RELATION);
		userSpecifyParam.setUri(userUri);
		specifyList.add(userSpecifyParam);

		// 查询设备列表
		try {
			String prolineId = Tools.getIdByUri(queryParam.getProline().getUri());
			DepthAndRelationQueryParam depthAndRelationQueryParam = generateModelParam.generateDepth(namespace, 1, true,
					prolineId, fields, relations, specifyList);
			String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);

			response = v2QueryService.depth(requestJson, namespace, 20);
			String jsonString = JSON.toJSONString(response.getResult());
			List<Map> deviceUriList = JSON.parseArray(jsonString, Map.class);
			if (null != deviceUriList && deviceUriList.size() > 0) {
				for (int i = 0; i < deviceUriList.size(); i++) {
					String uri = (String) deviceUriList.get(i).get("uri");
					String description = (String) deviceUriList.get(i).get("Description");
					String name = (String) deviceUriList.get(i).get("name");
					// 查询基本信息跟节点
					Device device = this.queryDevicesRootExtendInfo(uri);
					device.setDescription(description);
					device.setName(name);
					// 此处保存uri
					device.setId(uri);
					device.setProlineName(queryParam.getProline().getBrowseName());
					device.setProlineUri(queryParam.getProline().getUri());
					// 查询设备所属模型信息
					BaseNode model = this.queryDeviceRelation(uri, OpcReference.HAS_TYPE_DEFINITION, true);
					device.setModelName(model.getBrowseName());
					device.setModelUri(model.getUri());
					deviceInfoList.add(device);
				}
			}
		} catch (Exception e) {
			throw e;
		}

		return deviceInfoList;
	}

	/*
	 * 查询设备的基本信息的跟节点
	 */
	@SuppressWarnings("rawtypes")
	public Device queryDevicesRootExtendInfo(String uri) throws Exception, CheckDefineException {
		Device device = new Device();
		String id = Tools.getIdByUri(uri);
		String namespace = Tools.getNamespaceByUri(uri);

		NodeRespones responseTree = new NodeRespones();
		try {
			// 查询下一级跟节点
			String extendInfouri = this.queryDevicesRootExtendInfoUri(uri);
			device = this.queryDevicesExtendInfo(extendInfouri);
		} catch (Exception e) {
			throw e;
		}

		return device;
	}

	/*
	 * 查询设备的基本信息
	 */
	public Device queryDevicesExtendInfo(String uri) throws Exception, CheckDefineException {
		Device device = new Device();
		String id = Tools.getIdByUri(uri);
		String namespace = Tools.getNamespaceByUri(uri);

		// 构造查询条件
		List<String> fields = new ArrayList<String>();
		fields.add("BrowseName");
		fields.add("Value");
		List<String> relations = new ArrayList<String>();
		relations.add(OpcReference.HAS_COMPONENT);
		List<DepthSpecifyParam> specifyList = new ArrayList<DepthSpecifyParam>();
		try {
			DepthAndRelationQueryParam depthAndRelationQueryParam = generateModelParam.generateDepth(namespace, 1, true,
					id, fields, relations, specifyList);
			String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);

			NodeRespones responseTree = v2QueryService.depth(requestJson, namespace, 20);
			String jsonString = JSON.toJSONString(responseTree.getResult());
			device.setId(id);
			List<ExtendInfo> deviceExtendInfoList = JSON.parseArray(jsonString, ExtendInfo.class);
			if (null != deviceExtendInfoList && deviceExtendInfoList.size() > 0) {
				for (int i = 0; i < deviceExtendInfoList.size(); i++) {
					if (deviceExtendInfoList.get(i).getBrowseName().equals("MaintenancePeriod")) {
						String value = deviceExtendInfoList.get(i).getValue();
						device.setMaintenancePeriod(value);
					}
					if (deviceExtendInfoList.get(i).getBrowseName().equals("Address")) {
						String value = deviceExtendInfoList.get(i).getValue();
						device.setAddress(value);
					}
					if (deviceExtendInfoList.get(i).getBrowseName().equals("LastMaintenanceTime")) {
						String value = deviceExtendInfoList.get(i).getValue();
						device.setLastMaintenanceTime(value);
					}
					if (deviceExtendInfoList.get(i).getBrowseName().equals("Manager")) {
						String value = deviceExtendInfoList.get(i).getValue();
						device.setManager(value);
					}
					if (deviceExtendInfoList.get(i).getBrowseName().equals("IconAddress")) {
						String value = deviceExtendInfoList.get(i).getValue();
						device.setIconAddress(value);
					}
					if (deviceExtendInfoList.get(i).getBrowseName().equals("ServiceContact")) {
						String value = deviceExtendInfoList.get(i).getValue();
						device.setServiceContact(value);
					}
					if (deviceExtendInfoList.get(i).getBrowseName().equals("ContactMailAddress")) {
						String value = deviceExtendInfoList.get(i).getValue();
						device.setContactMailAddress(value);
					}
					if (deviceExtendInfoList.get(i).getBrowseName().equals("ContactPhone")) {
						String value = deviceExtendInfoList.get(i).getValue();
						device.setContactPhone(value);
					}

					if (deviceExtendInfoList.get(i).getBrowseName().equals("ServiceProvider")) {
						String value = deviceExtendInfoList.get(i).getValue();
						device.setServiceProvider(value);
					}
				}
			}
			return device;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * 查询设备的基本信息的跟节点
	 */
	@SuppressWarnings("rawtypes")
	public String queryDevicesRootExtendInfoUri(String uri) throws Exception, CheckDefineException {
		String id = Tools.getIdByUri(uri);
		String namespace = Tools.getNamespaceByUri(uri);
		// 构造查询条件
		List<String> fields = new ArrayList<String>();
		fields.add("uri");
		List<String> relations = new ArrayList<String>();
		relations.add(OpcReference.HAS_COMPONENT);
		List<DepthSpecifyParam> specifyList = new ArrayList<DepthSpecifyParam>();
		DepthSpecifyParam depthSpecifyParam = new DepthSpecifyParam();
		depthSpecifyParam.setForward(true);
		depthSpecifyParam.setRelation(OpcReference.HAS_TYPE_DEFINITION);
		depthSpecifyParam.setUri(DeviceConstans.DEVICE_EXTEDNINFO_MODEL);
		specifyList.add(depthSpecifyParam);
		try {
			DepthAndRelationQueryParam depthAndRelationQueryParam = generateModelParam.generateDepth(namespace, 1, true,
					id, fields, relations, specifyList);
			String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);

			NodeRespones responseTree = v2QueryService.depth(requestJson, namespace, 20);
			String jsonString = JSON.toJSONString(responseTree.getResult());
			List<Map> deviceList = JSON.parseArray(jsonString, Map.class);
			if (null != deviceList && deviceList.size() > 0) {
				String extendInfouri = (String) deviceList.get(0).get("uri");
				return extendInfouri;
			}
		} catch (Exception e) {
			throw e;
		}
		return "";
	}

	/*
	 * 查询设备的基本信息列表
	 */
	public List<ExtendInfo> queryDevicesExtendInfos(String uri) throws Exception, CheckDefineException {
		String id = Tools.getIdByUri(uri);
		String namespace = Tools.getNamespaceByUri(uri);

		// 构造查询条件
		List<String> fields = new ArrayList<String>();
		fields.add("BrowseName");
		fields.add("uri");
		List<String> relations = new ArrayList<String>();
		relations.add(OpcReference.HAS_COMPONENT);
		List<DepthSpecifyParam> specifyList = new ArrayList<DepthSpecifyParam>();
		// 查询上级一个节点--配置查询条件
		DepthAndRelationQueryParam depthAndRelationQueryParam = generateModelParam.generateDepth(namespace, 1, true, id,
				fields, relations, specifyList);
		String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);

		NodeRespones responseTree = v2QueryService.depth(requestJson, namespace, 20);
		String jsonString = JSON.toJSONString(responseTree.getResult());
		List<ExtendInfo> deviceExtendInfoList = new ArrayList<ExtendInfo>();
		try {
			deviceExtendInfoList = JSON.parseArray(jsonString, ExtendInfo.class);
		} catch (Exception e) {
			throw e;
		}
		return deviceExtendInfoList;
	}

	/*
	 * 根据网关上传的基本设备id，填充设备信息
	 */
	public String addDevice(String namespace, String userId, AddDeviceParam deviceParam)
			throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		String userUri = Tools.generateUri(Constans.NAMESPACE_SYSTEM, userId);
		String deviceUri = deviceParam.getDeviceUri();
		String prolineUri = deviceParam.getProLineUri();
		Device device = deviceParam.getDevice();
		// 建立设备与用户的关系
		try {
			this.addUserRelation(Constans.NAMESPACE_SYSTEM, deviceUri, userUri);
			// 建立设备与产线的关系
			this.addProlineRelation(Constans.NAMESPACE_SYSTEM, deviceUri, prolineUri);
			// 修改产线本身的描述信息与名称信息
			this.editDevice(Tools.getNamespaceByUri(deviceUri), Tools.getIdByUri(deviceUri), device.getDescription(),
					device.getName());
			// 查询基本信息节点
			String baseInfoUri = this.queryDevicesRootExtendInfoUri(deviceUri);
			String rootUri = "";
			if (null == baseInfoUri || baseInfoUri.equals("")) {
				rootUri = this.addDeviceRootInfo(namespace, deviceUri);
			} else {
				rootUri = baseInfoUri;
			}
			List<ExtendInfo> extendInfoList = this.queryDevicesExtendInfos(rootUri);
			if (null != extendInfoList && extendInfoList.size() > 0) {
				// 建立基本信息节点
				for (int i = 0; i < extendInfoList.size(); i++) {
					if (extendInfoList.get(i).getBrowseName().equals("IconAddress")) {
						if (null != device.getIconAddress() && !device.getIconAddress().equals(""))
							extendInfoList.get(i).setValue(device.getIconAddress());
					} else if (extendInfoList.get(i).getBrowseName().equals("MaintenancePeriod")) {
						if (null != device.getMaintenancePeriod() && !device.getMaintenancePeriod().equals(""))
							extendInfoList.get(i).setValue(device.getMaintenancePeriod());
					} else if (extendInfoList.get(i).getBrowseName().equals("Manager")) {
						if (null != device.getManager() && !device.getManager().equals(""))
							extendInfoList.get(i).setValue(device.getManager());
					} else if (extendInfoList.get(i).getBrowseName().equals("Address")) {
						if (null != device.getAddress() && !device.getAddress().equals(""))
							extendInfoList.get(i).setValue(device.getAddress());
					} else if (extendInfoList.get(i).getBrowseName().equals("ServiceContact")) {
						if (null != device.getServiceContact() && !device.getServiceContact().equals(""))
							extendInfoList.get(i).setValue(device.getServiceContact());
					} else if (extendInfoList.get(i).getBrowseName().equals("ContactMailAddress")) {
						if (null != device.getContactMailAddress() && !device.getContactMailAddress().equals(""))
							extendInfoList.get(i).setValue(device.getContactMailAddress());
					} else if (extendInfoList.get(i).getBrowseName().equals("ServiceProvider")) {
						if (null != device.getServiceProvider() && !device.getServiceProvider().equals(""))
							extendInfoList.get(i).setValue(device.getServiceProvider());
					} else if (extendInfoList.get(i).getBrowseName().equals("ContactPhone")) {
						if (null != device.getContactPhone() && !device.getContactPhone().equals(""))
							extendInfoList.get(i).setValue(device.getContactPhone());
					} else if (extendInfoList.get(i).getBrowseName().equals("DeviceCode")) {
						if (null != device.getId() && !device.getId().equals(""))
							extendInfoList.get(i).setValue(device.getId());
					} else if (extendInfoList.get(i).getBrowseName().equals("LastMaintenanceTime")) {
						if (null != device.getLastMaintenanceTime() && !device.getLastMaintenanceTime().equals(""))
							extendInfoList.get(i).setValue(device.getLastMaintenanceTime());
					}

				}
				this.addDeviceInfo(namespace, extendInfoList);
			}
		} catch (Exception e) {
			throw e;
		}
		logger.info("response is {}", response);
		return "";
	}

	/*
	 * 新增基本信息跟节点
	 */
	@SuppressWarnings("unchecked")
	public String addDeviceRootInfo(String namespace, String deviceUri) throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		// 新增基本信息跟节点建立于接口相同的参数
		List<AddNodesItem> addNodesItemList = new ArrayList<AddNodesItem>();
		AddNodesItem addNodeItem = new AddNodesItem();
		// 上级节点的id
		addNodeItem.setParentNodeUri(deviceUri);
		// 扩展属性
		Map<String, Object> nodeAttributes = new HashMap<String, Object>();
		addNodeItem.setNodeAttributes(nodeAttributes);
		// nodeclass类型，此处为对象类型
		addNodeItem.setNodeClass(OpcReference.OBJECT);
		// 与上级的关系，层级关系
		addNodeItem.setReferenceTypeId(OpcReference.HAS_COMPONENT);
		// 所关联产线模型的id
		addNodeItem.setTypeDefinition(DeviceConstans.DEVICE_EXTEDNINFO_MODEL);

		addNodesItemList.add(addNodeItem);
		String requestJson = JacksonJsonUtil.toJson(addNodesItemList);
		try {
			response = opcuaService.addNodes(namespace, requestJson);
			if (response.getCode().equals(Constans.MODEL_RESPONSE_SUCCESS)) {
				List<String> rooturiList = (List<String>) response.getResult();
				if (null != rooturiList && rooturiList.size() > 0) {
					String rootUri = rooturiList.get(0);
					return rootUri;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return "";
	}

	/*
	 * 根据网关上传的基本设备id，填充设备信息
	 */
	@SuppressWarnings("unused")
	public void addDeviceInfo(String namespace, List<ExtendInfo> extendInfoList)
			throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		// 新增基本信息跟节点建立于接口相同的参数

		for (int i = 0; i < extendInfoList.size(); i++) {
			if (null != extendInfoList.get(i).getValue() && !extendInfoList.get(i).getValue().equals("")) {
				List<PatchJson> addNodesItemList = new ArrayList<PatchJson>();
				String id = Tools.getIdByUri(extendInfoList.get(i).getUri());
				PatchJson patchJson = new PatchJson();
				patchJson.setOp(Constans.PATCH_OP_REPLACE);
				patchJson.setPath("/Value");
				patchJson.setValue(extendInfoList.get(i).getValue());
				addNodesItemList.add(patchJson);
				String requestJson = JacksonJsonUtil.toJson(addNodesItemList);
				try {
					response = nodeCrudService.editNodePatch(requestJson, namespace, id);
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	/*
	 * 建立用户与设备的关系
	 */
	@SuppressWarnings("unchecked")
	public boolean addUserRelation(String namespace, String deviceUri, String userUri)
			throws Exception, CheckDefineException {
		// 查询用户与设备的关系
		QueryRelation userRelation = new QueryRelation();
		List<QueryRelation> userRelationList = new ArrayList<QueryRelation>();
		userRelation.setForward(true);
		userRelation.setRelation(UserConstans.USER_RELATION);
		userRelation.setSourceNodeUri(userUri);
		userRelation.setTargetNodeUri(deviceUri);
		userRelationList.add(userRelation);
		String requestJson = JacksonJsonUtil.toJson(userRelationList);
		// 建立设备与用户的关系
		try {
			NodeRespones response = v2QueryService.whetherHasRelation(requestJson);
			List<Boolean> resultResponse = (List<Boolean>) response.getResult();
			Boolean result = (Boolean) resultResponse.get(0);
			if (!result) {
				response = relationService.editRelation(namespace, userUri, deviceUri, UserConstans.USER_RELATION,
						Constans.PATCH_OP_ADD);
				return true;
			}
		} catch (Exception e) {
			throw e;
		}
		return false;
	}

	/*
	 * 建立设备与产线的关系
	 */
	@SuppressWarnings("rawtypes")
	public Boolean addProlineRelation(String namespace, String deviceUri, String prolineUri)
			throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		// 查询用户与产线的关系
		QueryRelation prolineRelation = new QueryRelation();
		List<QueryRelation> prolineRelationList = new ArrayList<QueryRelation>();
		prolineRelation.setForward(true);
		prolineRelation.setRelation(OpcReference.HAS_ORGANIZE);
		prolineRelation.setSourceNodeUri(prolineUri);
		prolineRelation.setTargetNodeUri(deviceUri);
		prolineRelationList.add(prolineRelation);
		String requestJson = JacksonJsonUtil.toJson(prolineRelationList);
		try {
			response = v2QueryService.whetherHasRelation(requestJson);
			List resultResponse = (List) response.getResult();
			Boolean result = (Boolean) resultResponse.get(0);
			if (!result) {
				// 建立设备与产线的关系
				response = relationService.editRelation(namespace, prolineUri, deviceUri, OpcReference.HAS_ORGANIZE,
						Constans.PATCH_OP_ADD);
				return true;
			}
		} catch (Exception e) {
			throw e;
		}
		return true;

	}

	@SuppressWarnings("unused")
	private void editDevice(String namespace, String id, String description, String name)
			throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		// 新增基本信息跟节点建立于接口相同的参数
		List<PatchJson> addNodesItemList = new ArrayList<PatchJson>();
		PatchJson descriptionJson = new PatchJson();
		descriptionJson.setOp(Constans.PATCH_OP_REPLACE);
		descriptionJson.setPath("/Description");
		descriptionJson.setValue(description);
		addNodesItemList.add(descriptionJson);
		PatchJson nameJson = new PatchJson();
		nameJson.setOp(Constans.PATCH_OP_REPLACE);
		nameJson.setPath("/name");
		nameJson.setValue(name);
		addNodesItemList.add(nameJson);
		String requestJson = JacksonJsonUtil.toJson(addNodesItemList);
		try {
			response = nodeCrudService.editNodePatch(requestJson, namespace, id);
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * 删除设备
	 */
	public void deleteDevice(String namespace, String deviceUri, String userId, String prolineUri)
			throws Exception, CheckDefineException {/*
		String deviceUri = Tools.generateUri(namespace, deviceId);*/
		try {
			List<RelationV1> relationList = new ArrayList<RelationV1>();
			if (null != userId && !userId.equals("")) {
				String userUri = Tools.generateUri(Constans.NAMESPACE_SYSTEM, userId);
				RelationV1 relationUser = new RelationV1();
				relationUser.setRelation(UserConstans.USER_RELATION);
				relationUser.setSource(deviceUri);
				relationUser.setTarget(userUri);
				relationList.add(relationUser);
			}
			if (null != prolineUri && !prolineUri.equals("")) {
				/*String prolineUri = Tools.generateUri(namespace, prolineId);*/
				RelationV1 relationProline = new RelationV1();
				relationProline.setRelation(OpcReference.HAS_ORGANIZE);
				relationProline.setSource(prolineUri);
				relationProline.setTarget(deviceUri);
				relationList.add(relationProline);
			}
			if (null != relationList && relationList.size() > 0) {
				String requestJson = JacksonJsonUtil.toJson(relationList);
				relationService.deleteRelation(namespace, requestJson);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * 查询设备的上一级所属的用户或者产线，或者模型
	 */
	private BaseNode queryDeviceRelation(String deviceUri, String relation, boolean forward)
			throws Exception, CheckDefineException {
		String namespace = Tools.getNamespaceByUri(deviceUri);
		String id = Tools.getIdByUri(deviceUri);
		List<BaseNode> baseNodes = new ArrayList<BaseNode>();
		// 构造查询上级的查询条件
		BaseNode baseNode = new BaseNode();
		// 构造查询条件
		List<String> fields = new ArrayList<String>();
		fields.add("uri");
		fields.add("BrowseName");
		List<String> relations = new ArrayList<String>();
		relations.add(relation);
		List<DepthSpecifyParam> specifyList = new ArrayList<DepthSpecifyParam>();
		// 查询上级一个节点--配置查询条件
		try {
			DepthAndRelationQueryParam depthAndRelationQueryParam = generateModelParam.generateDepth(namespace, 1, true,
					id, fields, relations, specifyList);
			String requestJson = JacksonJsonUtil.toJson(depthAndRelationQueryParam);

			NodeRespones responseModel = v2QueryService.depth(requestJson, namespace, 1);
			String jsonString = JSON.toJSONString(responseModel.getResult());
			baseNodes = JSON.parseArray(jsonString, BaseNode.class);
			if (null != baseNodes && baseNodes.size() > 0) {
				return baseNodes.get(0);
			}
		} catch (Exception e) {
			throw e;
		}
		return baseNode;
	}

}
