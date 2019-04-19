package com.emcc.deviceManager.param.model;

public class RelationV1 {
	private String source;
	private String target;
	private String relation;
	private Object relationValues;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public Object getRelationValues() {
		return relationValues;
	}
	public void setRelationValues(Object relationValues) {
		this.relationValues = relationValues;
	}
	
}
