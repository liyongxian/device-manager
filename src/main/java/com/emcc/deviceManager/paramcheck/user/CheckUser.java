package com.emcc.deviceManager.paramcheck.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emcc.deviceManager.response.device.DeviceResponse;
import com.emcc.deviceManager.service.user.UserService;

@Service
public class CheckUser {
	@Autowired
	private UserService userService;
	public boolean checkUser(String id)throws Exception{
		try{
			boolean result=userService.queryUserByUri(id);
			if(result){
				return result;
			}else{
				userService.addUser(id);
			}
		}catch(Exception e){
			throw e;
		}
		return false;
	}
}
