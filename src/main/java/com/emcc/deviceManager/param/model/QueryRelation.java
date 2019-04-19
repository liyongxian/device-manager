package com.emcc.deviceManager.param.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 查询关系时使用
 *
 * @author 马永振
 * @date 2018年08月13日
 */
public class QueryRelation {
    @ApiModelProperty(value = "起始节点uri")
    private String sourceNodeUri;
    @ApiModelProperty(value = "目标节点uri")
    private String targetNodeUri;
    @ApiModelProperty(value = "关系的uri")
    private String relation;
    @ApiModelProperty(value = "是否是前向关联，可选参数")
    private Boolean forward;

    public String getSourceNodeUri() {
        return sourceNodeUri;
    }

    public void setSourceNodeUri(String sourceNodeUri) {
        this.sourceNodeUri = sourceNodeUri;
    }

    public String getTargetNodeUri() {
        return targetNodeUri;
    }

    public void setTargetNodeUri(String targetNodeUri) {
        this.targetNodeUri = targetNodeUri;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Boolean getForward() {
        return forward;
    }

    public void setForward(Boolean forward) {
        this.forward = forward;
    }

    @Override
    public String toString() {
        return "QueryRelation{" +
                "sourceNodeUri='" + sourceNodeUri + '\'' +
                ", targetNodeUri='" + targetNodeUri + '\'' +
                ", forward='" + forward + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}

