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
public final class CoReference {
    private String id;
    private Integer count; // the number of words in the co-reference string
    private String words;
    private String score;
    private List<String> indicies;
    private String text; // the text where coreference is found.
    private List<String> classifications;
    
    public CoReference() {};
    
    public CoReference(Integer count, String score, String[] words) {
        this.count = count;
        this.score = score;
        setWordsArray(words);
    }
    
    public CoReference(Integer count, String score, String[] words, List<String> indicies, String text, List<String> classifications) {
        this.count = count;
        this.score = score;
        setWordsArray(words);
        this.indicies = indicies;
        this.text = text;
        this.classifications = classifications;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getIndicies() {
        return indicies;
    }

    public void setIndicies(List<String> indicies) {
        this.indicies = indicies;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
    
    public void setWordsArray(String[] wordArray) {
        for(int i = 0; i < wordArray.length; i++) {
            if (words == null) {
                words = wordArray[i];
            } else {
                words = words + " " + wordArray[i];
            }
        }
    }

    public List<String> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<String> classifications) {
        this.classifications = classifications;
    }
    
}
