package com.emcc.deviceManager.paramcheck.proline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.emcc.deviceManager.controller.QueryController;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.proline.EditProline;
import com.emcc.deviceManager.param.proline.NewProline;
@Service
public class CheckProline {
	private static final Logger logger = LoggerFactory.getLogger(CheckProline.class);
	public boolean  checkAddProline (NewProline proline)throws CheckDefineException{
		boolean result=false;
		if(null==proline){
			result=false;
			throw new CheckDefineException("proline Object cannot null");	
		}else if(null==proline.getName()||proline.getName().equals("")){
			result=false;
			throw new CheckDefineException("proline name Object cannot null");	
		}
		return result;
	}
	public boolean  checkeditProline (EditProline proline)throws CheckDefineException{
		boolean result=false;
		if(null==proline){
			result=false;
			throw new CheckDefineException("proline Object cannot null");	
		}else if(null==proline.getId()||proline.getId().equals("")){
			result=false;
			throw new CheckDefineException("proline Id cannot null");	
		}
		else if(null==proline.getName()||proline.getName().equals("")){
			result=false;
			throw new CheckDefineException("proline name cannot null");	
		}
		return result;
	}
}
