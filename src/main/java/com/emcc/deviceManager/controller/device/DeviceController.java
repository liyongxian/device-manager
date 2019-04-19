package com.emcc.deviceManager.controller.device;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emcc.deviceManager.param.device.AddDeviceParam;
import com.emcc.deviceManager.param.device.Device;
import com.emcc.deviceManager.param.device.DeviceQueryParam;
import com.emcc.deviceManager.paramcheck.device.CheckDevice;
import com.emcc.deviceManager.paramcheck.user.CheckUser;
import com.emcc.deviceManager.response.BaseResponse;
import com.emcc.deviceManager.response.device.DeviceResponse;
import com.emcc.deviceManager.response.device.DevicesResponse;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.device.DeviceService;

@Api(tags = { "设备接口" })
@RestController
@RequestMapping(value = "/device")
public class DeviceController {
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private CheckUser checkUser;
	@Autowired
	private CheckDevice checkDevice;
	@ApiOperation(value = "查询所有的设备模型", notes = "查询所有的设备模型", produces = "application/json")
	@RequestMapping(value = "/queryModel/{tenantId}", method = RequestMethod.GET)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public BaseResponse getAllModel(
			@ApiParam(value = "租户id") 
			@PathVariable("tenantId") String tenantId) {
		BaseResponse response=new BaseResponse();		
		try{
			NodeRespones nodeRespones=deviceService.queryAllModel(tenantId);
			response.setResult(nodeRespones.getResult());
			response.setSuccess(true);
			response.setMessage("查询成功");
		}catch(Exception e){
			response.setMessage("查询异常"+e);
			response.setSuccess(false);
		}
		return response;
	}
	@ApiOperation(value = "查询设备列表", notes = "查询设备列表", produces = "application/json")
	@RequestMapping(value = "/queryDevice/{tenantId}", method = RequestMethod.GET)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public DevicesResponse getAllModel(
			@ApiParam(value = "租户id") 
			@PathVariable("tenantId") String tenantId,
			@ApiParam(value = "产线id") 
			@RequestParam String prolineId,
			@ApiParam(value = "设备类型namespace，此处为模型的namespace") 
			@RequestParam String modelNamespace,
			@ApiParam(value = "设备类型，此处为模型id") 
			@RequestParam String modelId,
			@ApiParam(value = "设备模型名称") 
			@RequestParam String modelName) {
		DevicesResponse response=new DevicesResponse();		
		if(null==modelNamespace||modelNamespace.equals("")){
			modelNamespace="vdma";
		}
		if(null==modelId||modelId.equals("")){
			modelId="1023";
		}
		if(null==modelName||modelName.equals("")){
			modelName="robot";
		}
		if(null==modelNamespace||modelNamespace.equals("")){
			modelNamespace="vdma";
		}
		try{
			List<Device> deviceList=deviceService.queryDevices(tenantId, prolineId, modelNamespace,modelId,modelName);
			response.setDeviceList(deviceList);			
			response.setSuccess(true);
			response.setMessage("查询成功");
		}catch(Exception e){
			response.setMessage("查询异常"+e);
			response.setSuccess(false);
		}
		return response;
	}
	@ApiOperation(value = "查询设备列表", notes = "查询设备列表", produces = "application/json")
	@RequestMapping(value = "/queryDevices/{tenantId}/{userId}", method = RequestMethod.POST)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public DevicesResponse queryDevicenews(
			@ApiParam(value = "租户id") 
			@PathVariable("tenantId") String tenantId,
			@ApiParam(value = "用户id") 
			@PathVariable("userId") String userId,
			@ApiParam(value = "查询条件") 
			@RequestBody DeviceQueryParam queryParam) {
		DevicesResponse response=new DevicesResponse();		
		
		try{
			checkDevice.checkQueryParam(queryParam);
			List<Device> deviceList=deviceService.queryDevicesNews(tenantId, userId,queryParam);
			response.setDeviceList(deviceList);
			response.setSuccess(true);
			response.setMessage("操作成功");
		}catch(Exception e){
			response.setMessage(e.toString());
			response.setSuccess(false);
		}
		return response;
	}
	@ApiOperation(value = "根据设备编号查询单个设备", notes = "根据设备编号查询单个设备", produces = "application/json")
	@RequestMapping(value = "/queryDeviceByUri/{tenantId}", method = RequestMethod.GET)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public DeviceResponse queryDeviceByuri(
			@ApiParam(value = "租户id") 
			@PathVariable("tenantId") String tenantId,
		@ApiParam(value = "设备uri") 
		@RequestParam String deviceUri) {
		DeviceResponse response=new DeviceResponse();	
		Device device=new Device();
		try{
			device=deviceService.queryDeviceByUri(tenantId, deviceUri);
			response.setResult(device);
			response.setSuccess(true);
			response.setMessage("查询成功");
		}catch(Exception e){
			response.setMessage("此设备目前不在设备库，无法添加到用户或查询异常"+e);
			response.setSuccess(false);
		}		
		return response;
	}
	@ApiOperation(value = "新增设备", notes = "根据设备编号新增", produces = "application/json")
	@RequestMapping(value = "/addDevice/{tenantId}/{userId}", method = RequestMethod.POST)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public DeviceResponse addDevice(
			@ApiParam(value = "租户ID") 
			@PathVariable(value="tenantId",required=false) String tenantId,
			@ApiParam(value = "用户ID") 
			@PathVariable("userId") String userId,
			@ApiParam(value = "设备信息") 
			@RequestBody AddDeviceParam device) {
		DeviceResponse response=new DeviceResponse();		
		try{
			//判断用户是否存在，不存在则创建
			checkUser.checkUser(userId);
			deviceService.addDevice(tenantId, userId, device);
			response.setSuccess(true);
			response.setMessage("操作成功");
			response.setResult(device);
		}catch(Exception e){
			response.setMessage(e.toString());
			response.setSuccess(false);
		}		
		return response;
	}
	@ApiOperation(value = "删除设备信息", notes = "删除设备信息", produces = "application/json")
	@RequestMapping(value = "/deleteDevice/{tenantId}", method = RequestMethod.DELETE)
	public BaseResponse deleteDeviceByUri(@ApiParam(value = "租户id") @PathVariable(value = "tenantId") String tenantId,
			@ApiParam(value = "设备编号") @RequestParam(value = "deviceUri") String deviceUri,
			@ApiParam(value = "用户id") 
			@RequestParam(value="userId",required=false) String userId,			
			@ApiParam(value = "产线id") 
			@RequestParam(value="prolineUri",required=false) String prolineUri) {
		BaseResponse response = new BaseResponse();
		if(null==userId){
			userId="";
		}
		if(null==prolineUri){
			prolineUri="";
		}
		try {
			deviceService.deleteDevice(tenantId, deviceUri,userId,prolineUri);
			response.setSuccess(true);
			response.setMessage("删除成功");
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("删除设备失败或删除设备异常" + e);
		}
		return response;
	}

}
