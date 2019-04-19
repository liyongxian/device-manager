package com.emcc.deviceManager.param.model;

public class Relation {
	private String linkedNode;
	private String relation;
	private boolean forward;//0:前向关联，1:后向关联，2:双向关联
	public String getLinkedNode() {
		return linkedNode;
	}
	public void setLinkedNode(String linkedNode) {
		this.linkedNode = linkedNode;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public boolean isForward() {
		return forward;
	}
	public void setForward(boolean forward) {
		this.forward = forward;
	}
	
}
