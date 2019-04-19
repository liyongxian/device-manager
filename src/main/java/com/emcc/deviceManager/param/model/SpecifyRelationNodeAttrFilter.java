package com.emcc.deviceManager.param.model;

import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * 用于过滤具有指定关系的节点的属性
 *
 * @author 马永振
 * @date 2018年08月29日
 */
@ApiModel(value = "指定关系指定节点的信息")
public class SpecifyRelationNodeAttrFilter {
//    @ApiModelProperty(value = "目标节点URI")
//    private String targetUri;
    @ApiModelProperty(value = "关系的URI")
    private  String relationUri;
    @ApiModelProperty(value = "关系的方向")
    private  Boolean forward;

    @ApiModelProperty(value = "目标节点的属性,过滤指定属性等于指定值的目标节点,如{\"uri\":\"/0/45\"}")
    private Map<String,String> targetNodeAttrFilter;


    public String getRelationUri() {
        return relationUri;
    }

    public void setRelationUri(String relationUri) {
        this.relationUri = relationUri;
    }

    public Boolean getForward() {
        return forward;
    }

    public void setForward(Boolean forward) {
        this.forward = forward;
    }

    public Map<String, String> getTargetNodeAttrFilter() {
        return targetNodeAttrFilter;
    }

    public void setTargetNodeAttrFilter(Map<String, String> targetNodeAttrFilter) {
        this.targetNodeAttrFilter = targetNodeAttrFilter;
    }

    @Override
    public String toString() {
        return "SpecifyRelationNodeAttrFilter [relationUri=" + relationUri
                + ", forward=" + forward + ", targetNodeAttrFilter=" + targetNodeAttrFilter + "]";
    }
}
