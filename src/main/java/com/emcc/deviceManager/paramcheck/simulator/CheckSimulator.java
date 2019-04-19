package com.emcc.deviceManager.paramcheck.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.simulator.Simulator;
@Service
public class CheckSimulator {
	private static final Logger logger = LoggerFactory.getLogger(CheckSimulator.class);
	public void  checkAddSimulator(Simulator simulator)throws CheckDefineException{
		if(null==simulator){
			throw new CheckDefineException("proline Object cannot null");	
		}else if(null==simulator.getName()||simulator.getName().equals("")){
			throw new CheckDefineException("simulator name Object cannot null");	
		}else if(null==simulator.getCollectionDate()||simulator.getCollectionDate().equals("")){
			throw new CheckDefineException("simulator CollectionDate Object cannot null");	
		}else if(null==simulator.getDuration()||simulator.getDuration().equals("")){
			throw new CheckDefineException("simulator Duration Object cannot null");	
		}else if(null==simulator.getModelUri()||simulator.getModelUri().equals("")){
			throw new CheckDefineException("simulator ModelUri Object cannot null");	
		}
	}
	public void  checkEditSimulator(Simulator simulator)throws CheckDefineException{
		if(null==simulator){
			throw new CheckDefineException("proline Object cannot null");	
		}else if(null==simulator.getId()||simulator.getId().equals("")){
			throw new CheckDefineException("simulator id Object cannot null");	
		}
	}
}
