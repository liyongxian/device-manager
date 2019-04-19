package com.emcc.deviceManager.controller.device;

import io.minio.MinioClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.emcc.deviceManager.response.BaseResponse;

@Controller
@Api(tags = { "图片操作接口" })
@RestController
@RequestMapping(value = "/image")
public class ImageController {
	@Autowired
	MinioClient minioClient;
	@Value("${minio-buckets}")
    private String buckets;
	@ApiOperation(value = "上传图片", notes = "上传图片", produces = "application/json")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public  BaseResponse uploadImg(@RequestParam("file")MultipartFile file,
            HttpServletRequest request) {
		BaseResponse response=new BaseResponse();		
		try{			
			InputStream inputStream = file.getInputStream();		
			String bucketName=buckets;
			 String objectName = file.getOriginalFilename();
			 String contentType=file.getContentType();
			 minioClient.putObject(bucketName, objectName, inputStream, contentType);
			String url1 = minioClient.getObjectUrl(bucketName, objectName);
			response.setMessage("上传成功");
			response.setSuccess(true);
			response.setResult(url1);
		}catch(Exception e){
			response.setMessage("产生异常"+e);
			response.setSuccess(false);
		}
		return response;
	}
}
