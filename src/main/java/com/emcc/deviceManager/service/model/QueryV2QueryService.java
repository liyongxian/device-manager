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
import org.springframework.web.client.RestTemplate;

import util.JacksonJsonUtil;

import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.model.DepthAndRelationQueryParam;
import com.emcc.deviceManager.param.model.DepthSpecifyParam;
import com.emcc.deviceManager.param.model.SpecifyRelationNodeAttrFilter;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.rest.RestTemplateService;

@Service
public class QueryV2QueryService {
	@Value("${model-server}")
	private String host;
	private static final Logger logger = LoggerFactory.getLogger(OpcuaService.class);
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private RestTemplateService restTemplateService;

	/*
	 * 深度查询
	 */
	public NodeRespones depth(String namespace, int maxSize, int depth, boolean forward, String id, List<String> fields,
			List<String> relations, List<DepthSpecifyParam> specifyList) throws Exception, CheckDefineException {
		NodeRespones response = new NodeRespones();
		// 采用深度查询，查询上级节点
		DepthAndRelationQueryParam depthQueryParam = new DepthAndRelationQueryParam();
		depthQueryParam.setDepth(depth);
		depthQueryParam.setForward(forward);
		depthQueryParam.setId(id);
		if (null != fields && fields.size() > 0) {
			depthQueryParam.setFields(fields);
		}
		if (null != relations && relations.size() > 0) {
			depthQueryParam.setRelations(relations);
		} else {
			throw new CheckDefineException("query relatoion can not null");
		}


		if (null != specifyList && specifyList.size() > 0) {
			List<SpecifyRelationNodeAttrFilter> specifyRelationNodeAttrFilterList = new ArrayList<SpecifyRelationNodeAttrFilter>();
			for (int i = 0; i < specifyList.size(); i++) {
				DepthSpecifyParam specify = specifyList.get(i);
				SpecifyRelationNodeAttrFilter specifyRelationNodeAttrFilter = new SpecifyRelationNodeAttrFilter();
				specifyRelationNodeAttrFilter.setForward(specify.isForward());
				specifyRelationNodeAttrFilter.setRelationUri(specify.getRelation());
				Map<String, String> map = new HashMap<>();
				map.put("uri", specify.getUri());
				specifyRelationNodeAttrFilter.setTargetNodeAttrFilter(map);
				specifyRelationNodeAttrFilterList.add(specifyRelationNodeAttrFilter);
				depthQueryParam.setSpecifyRelationNodeAttrFilters(specifyRelationNodeAttrFilterList);
			}
		}
		String requestJson = JacksonJsonUtil.toJson(depthQueryParam);
		String url = host + "/v2/model/query/depth/" + namespace + "?filter=&maxSize=" + maxSize;
		response = restTemplateService.post(url, requestJson);
		if (response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)) {
			throw new CheckDefineException(
					"model servie Exception when excut : " + url + "param:" + requestJson + response.getMsg());
		}
		return response;
	}

}
