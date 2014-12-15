/*
 * Nebarti
 * Copyright © 2012-2013 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * 
 */
@XmlRootElement
public class WordCount {   
    private String id;
    private String word;
    private Integer frequency;
    private List<String> classifications;
    
    public WordCount() {};
    
    public WordCount(String word, Integer frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<String> classifications) {
        this.classifications = classifications;
    }
    
}
