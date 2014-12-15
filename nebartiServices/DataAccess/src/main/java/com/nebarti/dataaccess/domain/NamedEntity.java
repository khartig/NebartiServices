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
public final class NamedEntity {
    private String id;
    private String type;
    private String score;
    private String phrase;
    private Integer frequency;
    private List<String> classifications;
    
    public NamedEntity() {};
    
    public NamedEntity(String type, String score, String phrase, Integer frequency, List<String> classifications) {
        this.type = type;
        this.score = score;
        this.phrase = phrase;
        this.frequency = frequency;
        this.classifications = classifications;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<String> classifications) {
        this.classifications = classifications;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NamedEntity other = (NamedEntity) obj;
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        if ((this.score == null) ? (other.score != null) : !this.score.equals(other.score)) {
            return false;
        }
        if ((this.phrase == null) ? (other.phrase != null) : !this.phrase.equals(other.phrase)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 97 * hash + (this.score != null ? this.score.hashCode() : 0);
        hash = 97 * hash + (this.phrase != null ? this.phrase.hashCode() : 0);
        return hash;
    }

}
