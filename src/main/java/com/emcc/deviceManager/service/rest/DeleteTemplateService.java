package com.emcc.deviceManager.service.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.response.model.BaseResponseWithTotalCount;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.model.OpcuaService;

@Service
public class DeleteTemplateService {
	private static final Logger logger = LoggerFactory.getLogger(OpcuaService.class);
	@Autowired
	private RestTemplate restTemplate;

	public NodeRespones post(String url, String requestJson) throws Exception {
		NodeRespones response = new NodeRespones();
		HttpEntity<String> entity = this.getHttpEntity(requestJson);
		try {
			logger.info("remote invoke model interface,url is {}" + url + "json：" + requestJson);
			response = restTemplate.postForObject(url, entity, NodeRespones.class);
			logger.info("response is {}", response);
		} catch (Exception e) {
			logger.error(e.toString());
			response.setMsg("model interface " + url + "error:" + e);
			response.setCode(Constans.MODEL_RESPONSE_FAIL);
			throw e;
		}
		return response;
	}

	public NodeRespones delete(String url, String requestJson) throws Exception {
		NodeRespones response = new NodeRespones();
		HttpEntity<String> entity = this.getHttpEntity(requestJson);
		try {
			logger.info("remote invoke model interface,url is {}" + url + "json：" + requestJson);
			ResponseEntity<NodeRespones> response1 = (ResponseEntity<NodeRespones>) restTemplate.exchange(url,
					HttpMethod.DELETE, entity, NodeRespones.class);
			logger.info("response is {}", response1);
		} catch (Exception e) {
			logger.error(e.toString());
			response.setMsg("model interface " + url + "error:" + e);
			response.setCode(Constans.MODEL_RESPONSE_FAIL);
			throw e;
		}
		return response;
	}

	public NodeRespones deleteDeviceByUri(String url, String requestJson) throws Exception {
		NodeRespones response = new NodeRespones();
		HttpEntity<String> entity = this.getHttpEntity(requestJson);
		try {
			logger.info("remote invoke model interface,url is {}" + url + "json：" + requestJson);
			ResponseEntity<NodeRespones> response1 = (ResponseEntity<NodeRespones>) restTemplate.exchange(url,
					HttpMethod.POST, entity, NodeRespones.class);
			logger.info("response is {}", response1);
		} catch (Exception e) {
			logger.error(e.toString());
			response.setMsg("model interface " + url + "error:" + e);
			response.setCode(Constans.MODEL_RESPONSE_FAIL);
			throw e;
		}
		return response;
	}

	public BaseResponseWithTotalCount getTreeNodesByDepth(String url, String requestJson) throws Exception {
		BaseResponseWithTotalCount response = new BaseResponseWithTotalCount();
		HttpEntity<String> entity = this.getHttpEntity(requestJson);
		try {
			logger.info("remote invoke model interface,url is {}" + url + "json：" + requestJson);
			response = restTemplate.postForObject(url, entity, BaseResponseWithTotalCount.class);
			logger.info("response is {}", response);
		} catch (Exception e) {
			logger.error(e.toString());
			response.setMsg("model interface " + url + "error:" + e);
			response.setCode(Constans.MODEL_RESPONSE_FAIL);
			throw e;
		}
		return response;
	}

	private HttpEntity<String> getHttpEntity(String requestJson) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		return entity;
	}
}
