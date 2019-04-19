package com.emcc.deviceManager.response.proline;

import com.emcc.deviceManager.param.proline.Proline;
import com.emcc.deviceManager.response.BaseResponse;

public class ProlineResponse extends BaseResponse {
	private String uri;
	private Proline proline;

	public Proline getProline() {
		return proline;
	}

	public void setProline(Proline proline) {
		this.proline = proline;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
