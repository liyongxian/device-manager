package com.emcc.deviceManager.controller.simulator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emcc.deviceManager.param.simulator.Simulator;
import com.emcc.deviceManager.response.simulator.SimulatorsResponse;
import com.emcc.deviceManager.service.simulator.QuerySimulatorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = { "仿真数据接口" })
@RestController
@RequestMapping("/simulator")
public class QuerySimulatorController {
	@Autowired
	private QuerySimulatorService querySimulatorService;

	@ApiOperation(value = "仿真数据查询", notes = "仿真数据查询", produces = "application/json")
	@RequestMapping(value = "/json/querySimulator/{modelNamespace}/{modelId}/{modelName}", method = RequestMethod.POST)
	public SimulatorsResponse querySimulator(
			@ApiParam(value = "模型namespace") @PathVariable(value = "modelNamespace") String modelNamespace,
			@ApiParam(value = "模型id") @PathVariable(value = "modelId") String modelId,
			@ApiParam(value = "模型名称") @PathVariable(value = "modelName") String modelName) {
		SimulatorsResponse response = new SimulatorsResponse();
		try {
			List<Simulator> simulatorList = querySimulatorService.querySimulator(modelNamespace, modelId,modelName);
			response.setSimulatorList(simulatorList);
			response.setSuccess(true);
			response.setMessage("操作成功");
		} catch (Exception e) {
			response.setMessage(e.toString());
			response.setSuccess(false);
		}
		return response;
	}
}
