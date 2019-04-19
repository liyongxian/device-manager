package com.emcc.deviceManager.response.proline;

import com.emcc.deviceManager.param.proline.Proline;
import com.emcc.deviceManager.response.BaseResponse;

public class CheckProlineResponse extends BaseResponse {
	private Proline proline;

	public Proline getProline() {
		return proline;
	}

	public void setProline(Proline proline) {
		this.proline = proline;
	}
	
}
