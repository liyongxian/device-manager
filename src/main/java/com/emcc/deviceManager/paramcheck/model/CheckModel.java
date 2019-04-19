package com.emcc.deviceManager.paramcheck.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.emcc.deviceManager.common.Tools;
import com.emcc.deviceManager.exception.CheckDefineException;
@Service
public class CheckModel {
	private static final Logger logger = LoggerFactory.getLogger(CheckModel.class);
	public void  checkModel (String uri)throws CheckDefineException{
		if(null!=uri){
			boolean result=Tools.checkUriFormat(uri);
			if(!result)
				throw new CheckDefineException("uri format is error");	
		}else{
			throw new CheckDefineException("uri format is not null");	
		}
	}
}
