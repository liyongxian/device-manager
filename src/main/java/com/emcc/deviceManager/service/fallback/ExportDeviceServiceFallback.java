package com.emcc.deviceManager.service.fallback;

import com.emcc.deviceManager.response.device.ExportDeviceService;

import feign.Response;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExportDeviceServiceFallback implements ExportDeviceService {

	@Override
	public Response exportModelsToUaXmlFile(List<String> uriList) {
		System.out.println("==============");
		return null;
	}
}
