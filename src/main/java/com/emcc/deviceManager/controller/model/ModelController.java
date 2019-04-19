package com.emcc.deviceManager.controller.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emcc.deviceManager.param.device.DeviceQueryParam;
import com.emcc.deviceManager.param.device.QueryTreeParam;
import com.emcc.deviceManager.paramcheck.model.CheckModel;
import com.emcc.deviceManager.response.BaseResponse;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.modelquery.ModelService;

@Api(tags = { "通用查询接口" })
@RestController
@RequestMapping(value = "/common/query")
public class ModelController {
	@Autowired
	private ModelService modelService;
	@Autowired
	private CheckModel checkModel;
	@ApiOperation(value = "查询指定初始节点与关系的树结构", notes = "查询指定初始节点与关系的树结构", produces = "application/json")
	@RequestMapping(value = "/queryModel", method = RequestMethod.POST)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public BaseResponse getAllModel(
			@ApiParam(value = "查询条件") 
			@RequestBody QueryTreeParam queryParam) {
		BaseResponse response=new BaseResponse();		
		try{
			NodeRespones nodeRespones=modelService.queryTree(queryParam);
			response.setResult(nodeRespones.getResult());
			response.setSuccess(true);
			response.setMessage("查询成功");
		}catch(Exception e){
			response.setMessage("查询异常"+e);
			response.setSuccess(false);
		}
		return response;
	}
	@ApiOperation(value = "查询模型详情", notes = "查询指定初始节点与关系的树结构", produces = "application/json")
	@RequestMapping(value = "/queryModelDetail", method = RequestMethod.GET)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public BaseResponse queryModelDetail(
			@ApiParam(value = "uri") @RequestParam String uri,
            @ApiParam(value = "是否继承祖先的变量或组件")
            @RequestParam(required = false) boolean extendParentProperties) {
		BaseResponse response=new BaseResponse();		
		try{
			checkModel.checkModel(uri);
			NodeRespones nodeRespones=modelService.queryModelDetail(uri,extendParentProperties);
			response.setResult(nodeRespones.getResult());
			response.setSuccess(true);
			response.setMessage("查询成功");
		}catch(Exception e){
			response.setMessage("查询异常"+e);
			response.setSuccess(false);
		}
		return response;
	}
}
