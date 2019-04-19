package com.emcc.deviceManager.response.device;

import com.emcc.deviceManager.param.model.RelationV1;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.service.device.DeviceFeignConfiguration;
import com.emcc.deviceManager.service.fallback.DeviceFeignFallback;
import feign.Param;
import feign.RequestLine;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Service
@FeignClient(name="deviceexport",  fallback = DeviceFeignFallback.class, configuration = DeviceFeignConfiguration.class,path="model")
public interface DeviceExportFeignInterface {

//    @ApiOperation(value = "导出节点", notes = "导出节点")
//    @RequestLine("POST")
//    public NodeRespones exportNodesToJson(URI url, @RequestBody List<String> nodeList);
//
//    @ApiOperation(value = "查询对象或类型详情", notes = "查询对象或类型详情")
//    @RequestLine("POST")
//    public NodeRespones queryDetailByRootUri(URI url, @RequestParam("uri") String uri,  boolean extendParent);
//
//    @ApiOperation(value = "导入节点", notes = "导入节点")
//    @RequestLine("POST")
//    public NodeRespones importModelFromJson(URI url, @RequestBody List<Map<String, Object>> nodeAttrWithRelationsList);
//
//    @ApiOperation(value = "添加关系", notes = "查询对象或类型详情")
//    @RequestLine("POST")
//    public NodeRespones addRelation(URI url, @PathVariable(value = "namespace") @RequestParam("namespace") String namespace,
//                                    @RequestBody RelationV1[] relations,
//                                    @RequestParam("transaction") boolean transaction);
//
//    @ApiOperation(value="添加关系",  notes="查询对象或类型详情")
//    @RequestLine("POST")
//    public NodeRespones addRelation(@PathVariable(value = "namespace")@RequestParam("namespace") String namespace,
//                                    @RequestBody RelationV1[] relations,
//                                    @RequestParam("transaction") Boolean transaction);


    @ApiOperation(value="导出节点", notes="导出节点")
    @RequestLine("POST /opcua/exportNodesToJson")
    public NodeRespones exportNodesToJson(@RequestBody List<String> nodeList);
    @ApiOperation(value="导入节点", notes="导入节点")
    @RequestLine("POST /opcua/importFromJson")
    public NodeRespones importModelFromJson( @RequestBody List<Map<String, Object>> nodeAttrWithRelationsList);

    @ApiOperation(value="查询对象或类型详情", notes="查询对象或类型详情")
    @RequestLine("POST /opcua/queryDetailByRootUri?uri={uri}&extendParentProperties={extendParent}")
    public NodeRespones queryDetailByRootUri(@Param("uri") String uri, @Param("extendParent") Boolean extendParent );

    @ApiOperation(value="添加关系",  notes="查询对象或类型详情")
    @RequestLine("POST /v2/model/relationofnodes/{namespace}?transaction={transaction}")
    public NodeRespones addRelation( @Param("namespace") String namespace,
                                    @RequestBody RelationV1[] relations,
                                    @Param("transaction") Boolean transaction);

}
