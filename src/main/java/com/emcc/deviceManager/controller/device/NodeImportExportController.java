package com.emcc.deviceManager.controller.device;

import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.common.Tools;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.device.NodeImportExportService;
import feign.codec.Decoder;
import feign.codec.Encoder;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.emcc.deviceManager.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api(tags = "模型或设备的相关节点导入导出xml")
@RestController
@RequestMapping(value = "/device")
public class NodeImportExportController {

    private NodeImportExportService nodeImportExportService;
    @Autowired
    private Encoder encoder;
    @Autowired
    private Decoder decoder;

//	@ApiOperation(value = "基于uri导出节点为xml文件", notes = "基于uri导出节点为xml文件", produces = "application/json")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name="tenantId", value="租户Id", paramType = "path"),
//			@ApiImplicitParam(name="rootUri", value="模型或设备uri, 如 /vdma/1023", paramType = "query")
//	})
//	@PostMapping(value = "/exportNodes/{tenantId}")
//	public BaseResponse exportNodesToUaXmlFileByUri(
//			@PathVariable String tenantId,
//			String rootUri, HttpServletResponse httpServletResponse) {
//		BaseResponse baseResponse = new BaseResponse();
//
//		try {
//			if(!Tools.checkUriFormat(rootUri)) {
//				baseResponse.setErrorCode(Constans.MODEL_RESPONSE_FAIL);
//			}
//		} catch (CheckDefineException e) {
//			baseResponse.setErrorCode(Constans.MODEL_RESPONSE_FAIL);
//			baseResponse.setMessage(e.getClass()+":"+e.getMessage());
//			e.printStackTrace();
//			return baseResponse;
//		}
//		List<String> uriList = nodeImportExportService.queryNodesByRootUri(tenantId, rootUri, baseResponse);
//		if(!baseResponse.isSuccess()) {
//			return baseResponse;
//		}
//
//		//调用导出接口
//		nodeImportExportService.exportNodesToUAXML(tenantId, uriList, baseResponse, httpServletResponse);
//		if(!baseResponse.isSuccess()) {
//			return baseResponse;
//		}
//
//		baseResponse.setResult(httpServletResponse);
//		baseResponse.setSuccess(true);
//		baseResponse.setErrorCode(Constans.MODEL_RESPONSE_SUCCESS);
//		baseResponse.setMessage("Export successfully.");
//		return  baseResponse;
//
//	}
//
//	@ApiOperation(value = "导入xml文件里的节点", notes = "导入xml文件里的节点",  produces = "application/json")
//	@RequestMapping(value = "/importNodes/{tenantId}", headers ="content-type=multipart/form-data", method = RequestMethod.POST)
//	public BaseResponse importNodesByUAXml(@ApiParam(value = "租户ID")@PathVariable String tenantId,
//										   @ApiParam(value = "导入的OPCUA标准文件")@RequestParam MultipartFile file) {
//
//		BaseResponse baseResponse = new BaseResponse();
////		String name  = file.getOriginalFilename();
//		if(!file.getOriginalFilename().endsWith(".xml")){
//			baseResponse.setMessage("import OPC UA Model failed, file name should be end with 'xml'.");
//			baseResponse.setErrorCode(Constans.MODEL_RESPONSE_FAIL);
//			return baseResponse;
//		}
//
//		baseResponse = nodeImportExportService.importNodesByUAXML(tenantId, file);
//		return baseResponse;
//	}
//
//	@ApiOperation(value ="给指定的Node添加关系", notes="给指定的Node添加关系",   produces = "application/json")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name="sourceUri", value = "起点Node的Uri, 如 /vdma/1023",paramType = "query"),
//			@ApiImplicitParam(name="targetUri", value = "终点Node的Uri, 如 /vdma/1024",paramType = "query"),
//			@ApiImplicitParam(name="relationUri", value = "添加关系的Uri, 如 /0/45",paramType = "query"),
//	})
//	@RequestMapping(value = "/addRelation/{tenantId}", method = RequestMethod.POST)
//	public BaseResponse addRelations(@ApiParam(value = "租户ID") @PathVariable String tenantId,
//									 String sourceUri,
//									 String targetUri,
//									 String relationUri) {
//
//		BaseResponse baseResponse = new BaseResponse();
//		try {
//			if(!Tools.checkUriFormat(sourceUri)) {
//				baseResponse.setErrorCode(Constans.MODEL_RESPONSE_FAIL);
//			}
//			if(!Tools.checkUriFormat(targetUri)) {
//				baseResponse.setErrorCode(Constans.MODEL_RESPONSE_FAIL);
//			}
//			if(!Tools.checkUriFormat(relationUri)) {
//				baseResponse.setErrorCode(Constans.MODEL_RESPONSE_FAIL);
//			}
//		} catch (CheckDefineException e) {
//			baseResponse.setErrorCode(Constans.MODEL_RESPONSE_FAIL);
//			baseResponse.setMessage(e.getClass()+":"+e.getMessage());
//			e.printStackTrace();
//			return  baseResponse;
//		}
//
//		baseResponse = nodeImportExportService.addRelation(tenantId, sourceUri, targetUri, relationUri);
//
//		return baseResponse;
//	}
//
	@ApiOperation(value ="从指定的源租户导出设备/模型节点并导入指定目标租户的目标节点", notes="从指定的源租户导出设备/模型节点并导入指定目标租户的目标节点",   produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name="sourceTenant", value = "导出模型库URL中的租户ID, 如 hollysys",paramType = "query", required = true),
			@ApiImplicitParam(name="sourceProject", value = "导出模型库URL中的项目ID, 如default",paramType = "query", required = true),
			@ApiImplicitParam(name="targetTenant", value = "导入模型库URL中的租户ID, 如 hollysys",paramType = "query", required = true),
			@ApiImplicitParam(name="targetProject", value = "导出模型库URL中的项目ID, 如 haierrobot",paramType = "query", required = true),
			@ApiImplicitParam(name="sourceNodeUri", value = "导出模型库的节点的Uri模型, 如 /vdma/1023",paramType = "query", required = true),
			@ApiImplicitParam(name="targetNodeUri", value = "将上述导出的模型内容导入下挂到该节点，输入节点的Uri, 如 /vdma/1024",paramType = "query", required = true),
			@ApiImplicitParam(name="relationUri", value = "添加sourceNodeUri和targetNodeUri关系的Uri, 如 /0/45",paramType = "query", required = true),
	})
	@RequestMapping(value = "/exportandimport", method = RequestMethod.POST)
	public BaseResponse nodeImportFromSourceToTarget(
			@RequestParam("sourceTenant") String sourceTenant,
			@RequestParam("sourceProject") String sourceProject,
			@RequestParam("targetTenant") String targetTenant,
			@RequestParam("targetProject") String targetProject,
			@RequestParam("sourceNodeUri") String sourceNodeUri,
			@RequestParam("targetNodeUri") String targetNodeUri,
			@RequestParam("relationUri") String relationUri)
	{
		BaseResponse baseResponse = new BaseResponse();

		String sourceModelURL = "http://model-srv."+sourceTenant+"-"+sourceProject+".svc.cluster.local";
		String targetModelURL = "http://model-srv."+targetTenant+"-"+targetProject+".svc.cluster.local";

//		String sourceModelURL="http://192.168.66.131:8065/model";
//		String targetModelURL="http://127.0.0.1:8069/model";
		nodeImportExportService = new NodeImportExportService(decoder, encoder, sourceModelURL, targetModelURL);


		try {
			if(!Tools.checkUriFormat(sourceNodeUri) || !Tools.checkUriFormat(targetNodeUri)) {
				baseResponse.setErrorCode(Constans.MODEL_RESPONSE_FAIL);
			}
		} catch (CheckDefineException e) {
			baseResponse.setErrorCode(Constans.MODEL_RESPONSE_FAIL);
			baseResponse.setMessage(e.getClass()+":"+e.getMessage());
			e.printStackTrace();
			return baseResponse;
		}
		//调用查询节点详细信息接口
		List<String> uriList = nodeImportExportService.queryNodesByRootUri(sourceModelURL, sourceNodeUri, baseResponse);
		if(!baseResponse.isSuccess()) {
			return baseResponse;
		}

		//调用导出接口
		List<Map<String, Object>> nodeList =  nodeImportExportService.exportNodesToJson(sourceModelURL, uriList, baseResponse);
		if(!baseResponse.isSuccess()) {
			return baseResponse;
		}

		//调用导入接口
		baseResponse = nodeImportExportService.importNodesByJson(targetModelURL, nodeList);
		if(!baseResponse.isSuccess()) {
			return baseResponse;
		}

		//调用添加关系接口
		baseResponse = nodeImportExportService.addRelation(targetModelURL, sourceNodeUri, targetNodeUri, relationUri);

		return baseResponse;
	}
}
