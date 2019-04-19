package com.emcc.deviceManager.controller.device;

import com.emcc.deviceManager.param.model.ExportDeviceParam;
import com.emcc.deviceManager.paramcheck.device.CheckDevice;
import com.emcc.deviceManager.response.BaseResponse;
import com.emcc.deviceManager.response.device.ExportDeviceService;

import feign.Response;
import feign.Response.Body;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Api(tags = { "导出设备信息为xml" })
@RestController
@RequestMapping("/device")
public class ExportDeviceController {
	private static final Logger logger = LoggerFactory.getLogger(ExportDeviceController.class);
	@Autowired
	private ExportDeviceService exportDeviceService;

	@ApiOperation(value = "导出设备信息", produces = "application/json")
	@PostMapping("/exportDevice/{namespace}")
	public BaseResponse exportDevice(
			@ApiParam(value = "设备类型namespace,参数示例:0") @PathVariable("namespace") String namespace,
			@ApiParam(value = "设备导出参数exportDeviceParam,参数示例:{\"idList\": [\"61\",\"85\"],\"path\": \"d:\\\\\\work\"}") @RequestBody ExportDeviceParam exportDeviceParam) {
		BaseResponse response = new BaseResponse();
		// 获取参数
		String path = exportDeviceParam.getPath();
		List<String> idList = exportDeviceParam.getIdList();
		// 文件路径校验
		if (!CheckDevice.CheckExportDevicePath(path)) {
			response.setSuccess(false);
			response.setMessage("请输入正确的文件路径!");
			return response;
		}
		// 参数校验
		if (null != idList && idList.size() > 0) {

			List<String> uriList = new ArrayList<>();
			for (String id : idList) {
				// 构造参数
				uriList.add("/" + namespace + "/" + id);
			}
			this.operExportDeviceInfo(response, path, uriList);
		} else {
			response.setSuccess(false);
			response.setMessage("产线id不能为空");
			return response;
		}
		return response;
	}

	private BaseResponse operExportDeviceInfo(BaseResponse response, String path, List<String> uriList) {
		Response resp = exportDeviceService.exportModelsToUaXmlFile(uriList);
		logger.info("Exported device id:" + uriList);
		// 判断断路器执行状态,执行则resp返回null
		if (null == resp) {
			response.setSuccess(false);
			response.setMessage("Net error");
			return response;
		}
		// 获取文件名
		Map<String, Collection<String>> headers = resp.headers();
		Collection<String> contentDispositionCollection = headers.get("content-disposition");
		if (null == contentDispositionCollection) {
			response.setSuccess(false);
			response.setMessage("未查询到相关数据");
			return response;
		}
		String fileName = null;
		for (String string : contentDispositionCollection) {
			int index = string.indexOf("=") + 1;
			fileName = string.substring(index);
		}
		// 获取响应体输入流信息,并写入文件"path+fileName"中
		Body body = resp.body();
		if (null != body && body.length() != null && body.length() > 0) {

			File fileDir = new File(path);
			if (!fileDir.exists()) {
				fileDir.setWritable(true);
				fileDir.mkdirs();
			}
			File file = new File(path, fileName);
			try (InputStream in = body.asInputStream(); FileOutputStream out = new FileOutputStream(file);) {
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = in.read(b)) != -1) {
					out.write(b, 0, len);
				}
				out.flush();
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		} else {
			response.setSuccess(false);
			response.setMessage("未查询到相关数据");
			return response;
		}
		logger.info("Path of exported device file:" + path);
		response.setSuccess(true);
		return response;
	}

}
