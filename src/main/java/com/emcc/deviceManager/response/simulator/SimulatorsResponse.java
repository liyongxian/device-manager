package com.emcc.deviceManager.response.simulator;

import java.util.List;

import com.emcc.deviceManager.param.simulator.Simulator;
import com.emcc.deviceManager.response.BaseResponse;

public class SimulatorsResponse extends BaseResponse {
	private List<Simulator> simulatorList;

	public List<Simulator> getSimulatorList() {
		return simulatorList;
	}

	public void setSimulatorList(List<Simulator> simulatorList) {
		this.simulatorList = simulatorList;
	}

}
