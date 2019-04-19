package com.emcc.deviceManager.response.device;

import java.util.List;

import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.emcc.deviceManager.service.fallback.ExportDeviceServiceFallback;

import feign.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Service
@FeignClient(name = "ExportDevice", value = "ExportDevice", fallback = ExportDeviceServiceFallback.class, url = "${model-server}")
public interface ExportDeviceService {
	/**
	 * 导出产线数据
	 *
	 * @param
	 * @param
	 * @return
	 */

	@PostMapping(value = "/opcua/exportModelsToUaXmlFile")
	@ApiOperation(value = "根据指定uri路径获取文件流信息", notes = "根据uri路径集合获取文件流信息,返回值可以从响应中获取XML文件流", produces = "application/json")
	public Response exportModelsToUaXmlFile(
			@ApiParam(value = "uri路径集合,参数示例:[\"61\"]或[\"61\",\"85\"]") @RequestBody List<String> uriList);
}
