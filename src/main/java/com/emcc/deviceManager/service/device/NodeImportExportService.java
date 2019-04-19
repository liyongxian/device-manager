package com.emcc.deviceManager.service.device;

import com.alibaba.fastjson.JSONArray;
import com.emcc.deviceManager.param.model.RelationV1;
import com.emcc.deviceManager.response.device.DeviceExportFeignInterface;
import com.emcc.deviceManager.service.fallback.DeviceFeignFallback;
import feign.Feign;
import feign.Target;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.emcc.deviceManager.response.BaseResponse;
import org.apache.commons.lang3.StringUtils;

import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;
import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.response.model.NodeRespones;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Import(FeignClientsConfiguration.class)
public class NodeImportExportService {


    private DeviceExportFeignInterface  deviceExportFeignInterface;
    private DeviceExportFeignInterface  deviceImportFeignInterface;
    private String sourceModelURL;
    private String targetModelURL;

    public NodeImportExportService(Decoder decoder, Encoder encoder, String sourceModelURL, String targetModelURL) {
        this.sourceModelURL = sourceModelURL;
        this.targetModelURL = targetModelURL;
        deviceExportFeignInterface = Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .requestInterceptor( new BasicAuthRequestInterceptor("export","export"))
//                .target(Target.EmptyTarget.create(DeviceExportFeignInterface.class));
                .target(DeviceExportFeignInterface.class,this.sourceModelURL);
        deviceImportFeignInterface = Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .requestInterceptor( new BasicAuthRequestInterceptor("import","import"))
                .target(DeviceExportFeignInterface.class, this.targetModelURL);
    }

    /**
     * 导出指定模型或设备下的所有节点
     * @param url 访问的模型库
     * @param rootUri 模型或设备的uri
     * @param baseResponse 返回状态
     * @return 返回传入模型下HasComponent和HasProperty对应关系的uri列表
     */
    public List<String> queryNodesByRootUri(String url, String rootUri, BaseResponse baseResponse) {

        this.sourceModelURL = url;
        List<String> uriList = new ArrayList<>();
        NodeRespones nodeRespones = null;
        nodeRespones = deviceExportFeignInterface.queryDetailByRootUri(rootUri, false);
        //根据接口返回状态设置返回值
        baseResponse.setMessage(nodeRespones.getMsg());
        baseResponse.setErrorCode(nodeRespones.getCode());
        if(StringUtils.equals(Constans.MODEL_RESPONSE_SUCCESS, nodeRespones.getCode())) {
            baseResponse.setSuccess(true);
            baseResponse.setResult(nodeRespones.getResult());
        }
        if(!baseResponse.isSuccess()) {
            return uriList;
        }
        String nodeJson = JSON.toJSONString(baseResponse.getResult());
        JSONObject nodesObject =  JSON.parseObject(nodeJson);

        //如果正常，则取出找到Node的uri返回
        Iterator<String> iterator= nodesObject.keySet().iterator();
        while(iterator.hasNext()) {
            String key = iterator.next();
            JSONObject  value = (JSONObject) nodesObject.get(key);
            String nodeUri  = value.getString("uri");
            uriList.add(nodeUri);
        }

        return uriList;
    }

    /**
     * 导出传入设备uri至xml
     * @param url 访问的模型库
     * @param uriList 需要导出Node的uri列表
     * @param baseResponse 内含导出文件流
     */
    public List<Map<String,Object>> exportNodesToJson(String url, List<String> uriList, BaseResponse baseResponse) {

        this.sourceModelURL=url;
        NodeRespones nodeRespones = null;
        List<Map<String, Object>> nodeList = new ArrayList<>();
        nodeRespones = deviceExportFeignInterface.exportNodesToJson(uriList);
//        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
//        Response.Body body = response.body();
//
//        //封装httpServletResponse
//        httpServletResponse.reset();
////        httpServletResponse.setContentType("application/octet-stream");
//        httpServletResponse.addHeader("Content-Disposition",
//                "attachment; filename=" +dateFormat.format(new Date())+".xml");
//
//        try {
//            InputStream in  = body.asInputStream();
//            OutputStream out ;
//            out = httpServletResponse.getOutputStream();
//
//            byte[] buffer = new byte[1024];
//            int len=0;
//            while((len=in.read(buffer) )!= -1) {
//                out.write(buffer,0,len);
//            }
//            out.flush();
//            in.close();
//            out.close();
//
//        } catch (IOException e) {
//            baseResponse.setSuccess(false);
//            baseResponse.setMessage(e.getClass().getName()+":"+e.getMessage());
//            baseResponse.setErrorCode(Constans.MODEL_RESPONSE_FAIL);
//        }

        baseResponse.setMessage(nodeRespones.getMsg());
        baseResponse.setErrorCode(nodeRespones.getCode());
        if(StringUtils.equals(Constans.MODEL_RESPONSE_SUCCESS, nodeRespones.getCode())) {
            baseResponse.setSuccess(true);
            baseResponse.setResult(nodeRespones.getResult());
            nodeList = (List<Map<String, Object>>) baseResponse.getResult();
            return nodeList;
        }
        if(!baseResponse.isSuccess()) {
            return null;
        }

        return nodeList;

    }

    /**
     * 调用OPCUA模型的import接口
     * @param url 访问的模型库
     * @param nodeList 带有Nodes信息的集合
     */
    public BaseResponse importNodesByJson(String url, List<Map<String, Object>> nodeList) {

        this.targetModelURL=url;
        BaseResponse baseResponse = new BaseResponse() ;
        NodeRespones nodeRespones = null;
        nodeRespones = deviceImportFeignInterface.importModelFromJson(nodeList);
        //根据接口返回状态设置返回值
        baseResponse.setMessage(nodeRespones.getMsg());
        baseResponse.setErrorCode(nodeRespones.getCode());
        if(StringUtils.equals(Constans.MODEL_RESPONSE_SUCCESS, nodeRespones.getCode())) {
            baseResponse.setSuccess(true);
        }

        return baseResponse;
    }

    /**
     * 给指定节点添加关系
     * @param url 访问的模型库
     * @param sourceUri 起点Node的uri
     * @param targetUri 终点Node的uri
     * @param relationUri  新建关系的uri
     * @return 执行结果状态
     */
    public BaseResponse addRelation(String url, String sourceUri, String targetUri, String relationUri) {

        this.targetModelURL=url;
        BaseResponse baseResponse = new BaseResponse();
        RelationV1 relation = new RelationV1();
        relation.setSource(sourceUri);
        relation.setTarget(targetUri);
        relation.setRelation(relationUri);
        RelationV1[] relations = new RelationV1[1];
        relations[0] = relation;
        NodeRespones nodeRespones = null;
        nodeRespones = deviceImportFeignInterface.addRelation("vdma", relations, true);

        //根据接口返回状态设置返回值
        baseResponse.setMessage(nodeRespones.getMsg());
        baseResponse.setErrorCode(nodeRespones.getCode());
        if(StringUtils.equals(Constans.MODEL_RESPONSE_SUCCESS, nodeRespones.getCode())) {
            baseResponse.setSuccess(true);
        }

        return baseResponse;
    }



}
