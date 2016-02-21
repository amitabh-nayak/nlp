package com.fileprocessing.domain;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileComponent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private ArrayList<SentenceComponent> sentenceList;
	
	public FileComponent() {
		sentenceList = new ArrayList<>();
	}
	public void addSentence(SentenceComponent component){
		sentenceList.add(component);
	}
	public ArrayList<SentenceComponent> getSentenceList() {
		return sentenceList;
	}
	public void setSentenceList(ArrayList<SentenceComponent> sentenceList) {
		this.sentenceList = sentenceList;
	}
	@Override
	public String toString() {
		return "FileComponent [sentenceList=" + sentenceList + "]";
	}

	
}
