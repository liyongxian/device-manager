package com.emcc.deviceManager.param.model;

import java.util.Map;
import java.util.Set;

/**
 * @Author: zhanghh
 * @Date: 2018/8/2 11:00
 * @Description: 描述Object 或 Object Type的详情
 */
public class NodeDetail {
    /**
     * node（Object或Object Type)的uri
     */
    private String uri;
    /**
     * node的属性（uri、BrowseName、NodeClass、DisplayName等）
     */
    private Map<String,Object> properties;
    /**
     * 与node相关联的对象的uri的集合
     * Map的key为关系名，value(List<String>）为该关系下，所有对象的uri集合
     */
    private Map<String, Set<String>> relationNodeUris;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Set<String>> getRelationNodeUris() {
        return relationNodeUris;
    }

    public void setRelationNodeUris(Map<String, Set<String>> relationNodeUris) {
        this.relationNodeUris = relationNodeUris;
    }

    @Override
    public String toString() {
        return "NodeDetail{" +
                "uri='" + uri + '\'' +
                ", properties=" + properties +
                ", relationNodeUris=" + relationNodeUris +
                '}';
    }
}
