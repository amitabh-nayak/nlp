package com.fileprocessing.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SentenceComponent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sentence;
	private List<String> words;
	
	private List<NounEntity> nounList;
	
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public List<String> getWords() {
		return words;
	}
	public void setWords(List<String> list) {
		this.words = list;
	}
	public List<NounEntity> getNounList() {
		return nounList;
	}
	public void setNounList(List<NounEntity> nounList) {
		this.nounList = nounList;
	}
	@Override
	public String toString() {
		return "SentenceComponent [sentence=" + sentence + ", words=" + words
				+ ", nounList=" + nounList + "]";
	}
	
	

}
