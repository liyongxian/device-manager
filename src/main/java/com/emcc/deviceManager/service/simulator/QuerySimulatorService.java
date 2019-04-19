package com.emcc.deviceManager.service.simulator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.JacksonJsonUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.common.OpcReference;
import com.emcc.deviceManager.common.SimulatorConstans;
import com.emcc.deviceManager.common.Tools;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.device.ExtendInfo;
import com.emcc.deviceManager.param.model.DepthAndRelationQueryParam;
import com.emcc.deviceManager.param.model.DepthSpecifyParam;
import com.emcc.deviceManager.param.model.NodeDetail;
import com.emcc.deviceManager.param.simulator.Simulator;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.common.GenerateModelParam;
import com.emcc.deviceManager.service.model.V2QueryService;

@Service
@SuppressWarnings("rawtypes")
public class QuerySimulatorService {
	@Autowired
	private GenerateModelParam generateModelParam;
	@Autowired
	private V2QueryService v2QueryService;
	@Autowired
	private QuerySimulatorFeign querySimulatorFeign;

	public List<Simulator> querySimulator(String modelNamespace, String modelId, String modelName)
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
			DepthAndRelationQueryParam depthparam = generateModelParam.generateDepth(Constans.NAMESPACE_SYSTEM, 1, true,
					Tools.getIdByUri(SimulatorConstans.SIMULATOR_ROOT_URI), fields, relations, specifyList);
			String requestJson = JacksonJsonUtil.toJson(depthparam);
			response = v2QueryService.depth(requestJson, Constans.NAMESPACE_SYSTEM, 20);
			String jsonString = JSON.toJSONString(response.getResult());
			List<Map> simulatorUriList = JSON.parseArray(jsonString, Map.class);
			if (null != simulatorUriList && simulatorUriList.size() > 0) {
				for (Map map : simulatorUriList) {
					Simulator simulator = new Simulator();
					String name = (String) map.get("name");
					String uri = (String) map.get("uri");
					String description = (String) map.get("Description");
					// 查询仿真数据的extendInfo
					List<ExtendInfo> extendInfoList = this.querySimulatorEntendInfo(uri);
					if (Tools.checkListNull(extendInfoList)) {
						for (int j = 0; j < extendInfoList.size(); j++) {
							if (extendInfoList.get(j).getBrowseName().equals("ID")) {
								simulator.setId(extendInfoList.get(j).getValue());
							} else if (extendInfoList.get(j).getBrowseName().equals("Submitter")) {
								simulator.setSubmitter(extendInfoList.get(j).getValue());
							} else if (extendInfoList.get(j).getBrowseName().equals("Duration")) {
								simulator.setDuration(extendInfoList.get(j).getValue());
							} else if (extendInfoList.get(j).getBrowseName().equals("CollectionDate")) {
								simulator.setCollectionDate(extendInfoList.get(j).getValue());
							} else if (extendInfoList.get(j).getBrowseName().equals("IsDefault")) {
								simulator.setDefault(Boolean.valueOf(extendInfoList.get(j).getValue()));
							}
						}
					}
					simulator.setDescription(description);
					simulator.setName(name);
					// 添加仿真数据所属模型信息
					simulator.setModelUri(modelUri);
					simulator.setModelName(modelName);
					simulatorInfoList.add(simulator);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return simulatorInfoList;
	}

	@SuppressWarnings("unchecked")
	// 获取仿真数据
	private List<ExtendInfo> querySimulatorEntendInfo(String uri) throws Exception, CheckDefineException {
		Set<String> relationNodeUriSet = new HashSet<>();
		Set<String> baseInfoUrisSet = new HashSet<>();
		List<ExtendInfo> extendInfoList = new ArrayList<>();
		try {
			NodeRespones nodeRespones = querySimulatorFeign.queryDetailByRootUri(uri, false);
			// 获取所有节点的查询结果,为Map集合,key是uri,value为NodeDetail对象
			Map<String, Object> resultMap = (Map<String, Object>) nodeRespones.getResult();
			for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
				if (Objects.equals("root", entry.getKey())) {
					// 获取uri为"root"的节点详情
					Object nodeObject = entry.getValue();
					NodeDetail nodeDetail = JSONObject.parseObject(JSONObject.toJSONString(nodeObject),
							NodeDetail.class);
					Map<String, Set<String>> relationNodeUris = nodeDetail.getRelationNodeUris();
					relationNodeUriSet = relationNodeUris.get("/0/47");
				}
			}
			// 获取BaseInfo的Uri
			for (String relationNodeUri : relationNodeUriSet) {
				Object nodeObject = resultMap.get(relationNodeUri);
				NodeDetail nodeDetail = JSONObject.parseObject(JSONObject.toJSONString(nodeObject), NodeDetail.class);
				Map<String, Object> properties = nodeDetail.getProperties();
				if (Constans.BASE_INFO.equals(properties.get("BrowseName"))) {
					Map<String, Set<String>> relationNodeUris = nodeDetail.getRelationNodeUris();
					baseInfoUrisSet = relationNodeUris.get("/0/47");
				}
			}
			// 获取所有extendInfo对象,封装到List集合
			for (String baseInfoUri : baseInfoUrisSet) {
				Object nodeObject = resultMap.get(baseInfoUri);
				NodeDetail nodeDetail = JSONObject.parseObject(JSONObject.toJSONString(nodeObject), NodeDetail.class);
				Map<String, Object> properties = nodeDetail.getProperties();
				ExtendInfo extendInfo = JSONObject.parseObject(JSONObject.toJSONString(properties), ExtendInfo.class);
				extendInfoList.add(extendInfo);
			}
		} catch (Exception e) {
			throw e;
		}
		return extendInfoList;
	}
}
