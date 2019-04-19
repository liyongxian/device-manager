package com.emcc.deviceManager.service.model;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.rest.RestTemplateService;

@Service
public class V2NodeCrudService {
	@Value("${model-server}")
	private String host;
	private static final Logger logger = LoggerFactory.getLogger(OpcuaService.class);
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private RestTemplateService restTemplateService;

	public NodeRespones editNode(String requestJson, String namespace, String id) throws Exception ,CheckDefineException{
		NodeRespones response = new NodeRespones();
		String url = host + "/v2/model/nodes/" + namespace + "?transaction=true";
		response = restTemplateService.put(url, requestJson);
		if(response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)){
			throw new CheckDefineException("model servie Exception when excut : "+url+"param:"+requestJson+response.getMsg());
    	}
		return response;
	}

	public NodeRespones editNodePatch(String requestJson, String namespace, String id) throws Exception,CheckDefineException {
		NodeRespones response = new NodeRespones();
		String url = host + "/v2/model/nodes/" + namespace + "/" + id + "?transaction=true";
		response = restTemplateService.patch(url, requestJson);
		if(response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)){
			throw new CheckDefineException("model servie Exception when excut : "+url+"param:"+requestJson+response.getMsg());
    	}
		return response;
	}

	/*
	 * 删除节点
	 */
	public NodeRespones delete(String requestJson, String namespace) throws Exception,CheckDefineException {
		NodeRespones response = new NodeRespones();
		// 删除节点
		String url = host + "/v2/model/nodes/" + namespace + "?transaction=true";
		response = restTemplateService.delete(url, requestJson);
		if(response.getCode().equals(Constans.MODEL_RESPONSE_FAIL)){
			throw new CheckDefineException("model servie Exception when excut : "+url+"param:"+requestJson+response.getMsg());
    	}
		return response;
	}
}
