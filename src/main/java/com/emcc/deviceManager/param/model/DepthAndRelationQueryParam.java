package com.emcc.deviceManager.param.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 深度搜索请求参数，包括id、深度和关系等等
 *
 * @author 马永振
 * @date 2018年08月28日
 */
public class DepthAndRelationQueryParam{

    @ApiModelProperty(value = "节点id")
    private String id;

    @ApiModelProperty(value = "要浏览的关系URI")
    private List<String> relations;

    @ApiModelProperty(value = "要返回的属性，为空则返回全体")
    private List<String> fields;

    @ApiModelProperty(value = "查询方向，true为前向，false为后向")
    private boolean forward;

    @ApiModelProperty(value = "用于过滤与指定节点具有指定的关系，指定节点通过属性键值对来指定，如{\"uri\":\"/0/45\"}，多个过滤参数之间是and关系")
    private List<SpecifyRelationNodeAttrFilter> specifyRelationNodeAttrFilters;

    @ApiModelProperty(value = "搜索深度，默认为1")
    private Integer depth = 1;


    @ApiModelProperty(value = "是否包含子关系定义，默认为false")
    private boolean includeSubtype = false;

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public List<SpecifyRelationNodeAttrFilter> getSpecifyRelationNodeAttrFilters() {
        return specifyRelationNodeAttrFilters;
    }

    public void setSpecifyRelationNodeAttrFilters(List<SpecifyRelationNodeAttrFilter> specifyRelationNodeAttrFilters) {
        this.specifyRelationNodeAttrFilters = specifyRelationNodeAttrFilters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getRelations() {
        return relations;
    }

    public void setRelations(List<String> relations) {
        this.relations = relations;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public boolean isIncludeSubtype() {
        return includeSubtype;
    }

    public void setIncludeSubtype(boolean includeSubtype) {
        this.includeSubtype = includeSubtype;
    }

    @Override
    public String toString() {
        return "DepthAndRelationQueryParam [id=" + id + ", relations=" + relations + ", depth=" + depth
                + ", fields=" + fields + ", forward=" + forward + ", includeSubtype="+includeSubtype
                +",specifyRelationNodeAttrFilters="+ specifyRelationNodeAttrFilters + "]";
    }
}
