package com.fileprocessing.domain;

import java.io.Serializable;

public class NounEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String entityName;
	private String sentenceFragment;
	private int position;
	
	public NounEntity(String entityName, String sentenceFragment, int position) {
		this.entityName = entityName;
		this.sentenceFragment = sentenceFragment;
		this.position = position;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getSentenceFragment() {
		return sentenceFragment;
	}
	public void setSentenceFragment(String sentenceFragment) {
		this.sentenceFragment = sentenceFragment;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	@Override
	public String toString() {
		return "NounEntity [entityName=" + entityName + ", sentenceFragment="
				+ sentenceFragment + ", position=" + position + "]";
	}
	
	
}
