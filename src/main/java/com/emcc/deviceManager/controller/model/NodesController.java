package com.emcc.deviceManager.controller.model;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



import com.emcc.deviceManager.param.device.QueryTreeParam;
import com.emcc.deviceManager.param.model.PatchJson;
import com.emcc.deviceManager.paramcheck.model.CheckModel;
import com.emcc.deviceManager.response.BaseResponse;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.modelquery.ModelService;

@Api(tags = { "通用操作接口" })
@RestController
@RequestMapping(value = "/common/crud")
public class NodesController {
	@Autowired
	private ModelService modelService;
	@Autowired
	private CheckModel checkModel;
	
	@ApiOperation(value = "查询指定初始节点与关系的树结构", notes = "查询指定初始节点与关系的树结构", produces = "application/json")
	@RequestMapping(value = "/edit/{namespace}/{id}", method = RequestMethod.PATCH)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public BaseResponse editPatch( @ApiParam(value = "对象所属集合") @PathVariable("namespace") String namespace,
            @ApiParam(value = "对象的ID") @PathVariable("id") String id,
            @ApiParam(value = "需要修改的单个属性及操作，详情见接口文档") @RequestBody List<PatchJson> patchJson) {
		BaseResponse response=new BaseResponse();		
		try{
			NodeRespones nodeRespones=modelService.editPatch(namespace,id,patchJson);
			response.setResult(nodeRespones.getResult());
			response.setSuccess(true);
			response.setMessage("操作成功");
		}catch(Exception e){
			response.setMessage("操作异常"+e);
			response.setSuccess(false);
		}
		return response;
	}

}
