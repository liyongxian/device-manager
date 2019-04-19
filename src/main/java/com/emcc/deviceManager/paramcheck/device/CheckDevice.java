package com.emcc.deviceManager.paramcheck.device;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.device.BaseNode;
import com.emcc.deviceManager.param.device.DeviceQueryParam;
@Service
public class CheckDevice {
	public static boolean CheckExportDevicePath(String path) {
		String replacePath = path;
		if (path.indexOf("//") != -1) {
			replacePath = path.replaceAll("//", "\\\\");
			return matchPathRegex(replacePath);
		}
		if (path.indexOf("/") != -1) {
			replacePath = path.replaceAll("/", "\\\\");
			return matchPathRegex(replacePath);
		}
		return matchPathRegex(replacePath);
	}

	private static boolean matchPathRegex(String replacePath) {
		String regex = "[a-zA-Z]:(((\\\\(?! )[^/:*?<>\\\"\"|\\\\]+)+\\\\?)|(\\\\)?)\\s*";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regex);
		// Matcher matcher = pattern.matcher(path);
		Matcher matcher = pattern.matcher(replacePath);
		// 字符串是否与正则表达式相匹配
		return matcher.matches();
	}
	public void checkQueryParam(DeviceQueryParam deviceQueryParam)throws CheckDefineException {
		if(null==deviceQueryParam){
			throw new CheckDefineException("query param is cannot null!");	
		}
		/*else if(null==deviceQueryParam.getModels()||deviceQueryParam.getModels().size()<=0){
			throw new CheckDefineException("query param models is cannot null!");	
		}
		else if(null!=deviceQueryParam.getModels()&&deviceQueryParam.getModels().size()>0){
			for(int i=0;i<deviceQueryParam.getModels().size();i++){
				if(null==deviceQueryParam.getModels().get(i).getUri()){
					throw new CheckDefineException("query param models id is cannot null!");
				}
				if(null==deviceQueryParam.getModels().get(i).getBrowseName()||deviceQueryParam.getModels().get(i).getBrowseName().equals("")){
					throw new CheckDefineException("query param models name is cannot null!");
				}				
			}
		}*/
		else if(null==deviceQueryParam.getProline()){
			throw new CheckDefineException("query param proline is cannot null!");	
		}
		else if(null==deviceQueryParam.getProline().getUri()){
			throw new CheckDefineException("query param proline id is cannot null!");	
		}
		else if(null==deviceQueryParam.getProline().getBrowseName()){
			throw new CheckDefineException("query param proline name is cannot null!");	
		}
		
	}
}
