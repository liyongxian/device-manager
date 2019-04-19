package com.emcc.deviceManager.controller.simulator;

import java.util.List;

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

import com.emcc.deviceManager.param.proline.NewProline;
import com.emcc.deviceManager.param.simulator.Simulator;
import com.emcc.deviceManager.paramcheck.simulator.CheckSimulator;
import com.emcc.deviceManager.response.BaseResponse;
import com.emcc.deviceManager.response.proline.ProlineResponse;
import com.emcc.deviceManager.response.simulator.SimulatorsResponse;
import com.emcc.deviceManager.service.simulator.SimulatorService;

@Api(tags = { "仿真数据接口" })
@RestController
@RequestMapping(value = "/simulator")
public class SimulatorController {
	@Autowired
	private SimulatorService simulatorService;
	@Autowired
	private CheckSimulator checkSimulator;
	@ApiOperation(value = "新增仿真数据", notes = "新增仿真数据", produces = "application/json")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public BaseResponse addSimulator(
			@ApiParam(value = "新增仿真数据参数") 
			@RequestBody Simulator simulator) {
		BaseResponse response=new BaseResponse();		
		//调用仿真数据接口存入数据
		try{
			checkSimulator.checkAddSimulator(simulator);	
			String uri=simulatorService.addSimulator(simulator);
			response.setSuccess(true);
			response.setMessage("操作成功");
			response.setResult(uri);
		}catch(Exception e){
			response.setMessage("操作异常"+e);
			response.setSuccess(false);
		}		
		return response;
	}
	@ApiOperation(value = "修改仿真数据", notes = "修改仿真数据", produces = "application/json")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public BaseResponse editSimulator(
			@ApiParam(value = "新增仿真数据参数") 
			@RequestBody Simulator simulator) {
		BaseResponse response=new BaseResponse();		
		//调用仿真数据接口存入数据
		try{
			checkSimulator.checkEditSimulator(simulator);	
			simulatorService.editSimulator(simulator);
			response.setSuccess(true);
			response.setMessage("操作成功");
		}catch(Exception e){
			response.setMessage("操作异常"+e);
			response.setSuccess(false);
		}		
		return response;
	}
	@ApiOperation(value = "设置默认仿真数据", notes = "设置默认仿真数据", produces = "application/json")
	@RequestMapping(value = "/setDefault/{defaultId}", method = RequestMethod.POST)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public BaseResponse editSimulator(
			@ApiParam(value = "默认值的id") 
			@PathVariable(value = "defaultId") String defaultId,
			@ApiParam(value = "原默认值的id,若目前无默认可为空",required=false) 
			@RequestParam(required=false)String oldId) {
		BaseResponse response=new BaseResponse();		
		//调用仿真数据接口存入数据
		try{			
			simulatorService.setDefault(oldId,defaultId);
			response.setSuccess(true);
			response.setMessage("操作成功");
		}catch(Exception e){
			response.setMessage("操作异常"+e);
			response.setSuccess(false);
		}		
		return response;
	}
	@ApiOperation(value = "获取单个仿真数据", notes = "获取单个仿真数据", produces = "application/json")
	@RequestMapping(value = "/querySimulator/{id}", method = RequestMethod.GET)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public BaseResponse editSimulator(@ApiParam(value = "仿真数据的id") 
	@PathVariable(value = "id") String id) {
		BaseResponse response=new BaseResponse();		
		//调用仿真数据接口存入数据
		try{			
			Simulator simulator=simulatorService.getSimulator(id);
			response.setSuccess(true);
			response.setMessage("操作成功");
			response.setResult(simulator);
		}catch(Exception e){
			response.setMessage("操作异常"+e);
			response.setSuccess(false);
		}		
		return response;
	}

	@ApiOperation(value = "仿真数据查询", notes = "仿真数据查询", produces = "application/json")
	@RequestMapping(value = "/querySimulators/{modelNamespace}/{modelId}/{modelName}", method = RequestMethod.POST)
	public SimulatorsResponse querySimulator(
			@ApiParam(value = "模型namespace") @PathVariable(value = "modelNamespace") String modelNamespace,
			@ApiParam(value = "模型id") @PathVariable(value = "modelId") String modelId,
			@ApiParam(value = "模型name") @PathVariable(value = "modelName") String modelName) {
		SimulatorsResponse response = new SimulatorsResponse();
		try {
			List<Simulator> simulatorList = simulatorService.querySimulators(modelNamespace, modelId,modelName);
			response.setSimulatorList(simulatorList);
			response.setSuccess(true);
			response.setMessage("操作成功");
		} catch (Exception e) {
			response.setMessage(e.toString());
			response.setSuccess(false);
		}
		return response;
	}
	@ApiOperation(value = "删除仿真数据", notes = "删除产线", produces = "application/json")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public BaseResponse deleteProlines(			
			@ApiParam(value = "仿真设备id") @PathVariable(value = "id") String id) {
		BaseResponse response = new BaseResponse();
		try {
			simulatorService.deleteSimulator(id);
			response.setMessage("删除成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setMessage(e.toString());
			response.setSuccess(false);
			return response;
		}
		return response;
	}
}
