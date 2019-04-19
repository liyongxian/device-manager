package com.emcc.deviceManager.controller;

import io.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@Api(tags = { "查询" })
@RestController
@RequestMapping(value = "/test")
public class QueryController {
	 @Autowired
	    private RestTemplate restTemplate;
private static final Logger logger = LoggerFactory.getLogger(QueryController.class);
/*	@ApiOperation(value = "查询namespace", notes = "查询所有的namespace", produces = "application/json")
	@RequestMapping(value = "/test", method = { RequestMethod.GET})
	public @ResponseBody  QueryResponse query() {
		QueryResponse response =new QueryResponse();
		try {			
			System.out.println("这只是一个测试");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		response.setMsg("这只是一个测试");

		return response;
	}*/
	/*@ApiOperation(value = "通过条件查询节点", notes = "查找符合条件的节点", produces = "application/json")
	@RequestMapping(value = "/node/{namespace}", method = RequestMethod.POST)
	@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public NodeQueryResponse getNodesByCondition(
			@ApiParam(value = "命名空间") 
				@PathVariable("namespace") String namespace,@ApiParam(value = "过滤条件，支持针对属性的，大于(>)、大于等于(>=)、小于(<)、小于等于(<=)、等于(=)、不等于(!=)、范围(..)、包含(like)、或(|)、且(:)、括号(())") 
			@RequestParam(value = "filter", required = false) String filter,
		@ApiParam(value = "查询参数") 
			@RequestBody NodeQueryParam nodeQueryParam) {
		
		List<HashMap<String, Object>> nodes=new  ArrayList<>();
		
		HashMap<String, Object> node1=new HashMap<String, Object>();
		HashMap<String, Object> node2=new HashMap<String, Object>();
		
		
		node2.put("nodeQueryParam", nodeQueryParam);
		
		
		nodes.add(node2);
		logger.debug("begin to execute queryNode, collection=" + namespace);
		QueryResponse response = new QueryResponse();
		 String requestJson = JacksonJsonUtil.toJson(nodeQueryParam);
		String url="http://localhost:8065/model/v2/model/query/node/"+namespace+"?filter=";
        logger.info("remote invoke findNamespece,url is {}",url);
       
        logger.info("requestJson is {}",requestJson);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        NodeQueryResponse modelResponsev2 = restTemplate.postForObject(url,entity, NodeQueryResponse.class);
       

		logger.debug("execute queryNode finished, response=" + response);
		return modelResponsev2;
	}*/
	
}
