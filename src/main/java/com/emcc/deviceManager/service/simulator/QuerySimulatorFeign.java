package com.emcc.deviceManager.service.simulator;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.fallback.QuerySimulatorFeignFallback;

@Service
@FeignClient(name = "QuerySimulatorFeign", value = "QuerySimulatorFeign", fallback = QuerySimulatorFeignFallback.class, url = "${model-server}")
public interface QuerySimulatorFeign {
	@PostMapping(value = "/opcua/queryDetailByRootUri")
	public NodeRespones queryDetailByRootUri(@RequestParam(value = "uri") String uri,
			@RequestParam(required = false, value = "extendParentProperties") boolean extendParentProperties);
}
