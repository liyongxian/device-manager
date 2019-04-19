package com.emcc.deviceManager.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.UnexpectedTypeException;






import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;

import com.emcc.deviceManager.exception.CheckDefineException;

public class Tools {
	 /**
     * 从uri获取namespace
     * @param uri
     * @author yqy
     * @return
     */
	 public static String getNamespaceByUri(String uri) {
	        int i = uri.indexOf("/", 2);
	        if (i != -1) {
	            String namespace = uri.substring(1,i);
	            return namespace;
	        } else {
	            return null;
	        }
	    }
	 /**
	     * 从uri获取id
	     * @param uri
	     * @author yqy
	     * @return
	     */
		 public static String getIdByUri(String uri) {
		        int i = uri.indexOf("/", 2);
		        if (i != -1) {
		            String id = uri.substring(i + 1);
		            return id;
		        } else {
		            return null;
		        }
		    }
	

    /**
     * 强大而健壮的uri格式验证方法
     * @param uri
     * @author zhh
     * @return
     */
    public static boolean checkUriFormat(String uri)throws CheckDefineException {
        if(StringUtils.isBlank(uri)){
        	throw new CheckDefineException("请使用符合要求的uri，正确格式为/.../..");
        }
        if(!StringUtils.startsWith(uri,"/")){
        	throw new CheckDefineException("请使用符合要求的uri，正确格式为/.../..");
        }
        if(StringUtils.endsWith(uri, "/")){
        	throw new CheckDefineException("请使用符合要求的uri，正确格式为/.../..");
        }
        if(StringUtils.countMatches(uri, "/") != 2){
        	throw new CheckDefineException("请使用符合要求的uri，正确格式为/.../..");
        }
        uri = uri.substring(1,uri.length());
        String[] arr = uri.trim().split("/");
        for(String s : arr){
            if(StringUtils.isBlank(s)){
            	throw new CheckDefineException("请使用符合要求的uri，正确格式为/.../..");
            }
        }
        return true;
    }
    /*
     * 构造uri
     */
    public static String generateUri(String collection, String id) {
        String uri = "";
        if (null != id) {
            uri = "/" + collection + "/" + id;
        } else {
            uri = "/" + collection;
        }
        return uri;
    }
    /*
     * 检验字符串是否为空
     */
    public static boolean checkStringNull(String v) {
       if(null!=v&&!v.equals("")){
    	   return true;
       } else{
    	   return false;
       }       
    }
    /*
     * 检验字符串是否为空
     */
    public static boolean checkListNull(List v) {
       if(null!=v&&v.size()>=0){
    	   return true;
       } else{
    	   return false;
       }       
    }

}
