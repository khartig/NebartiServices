/*
 * Nebarti
 * Copyright © 2013 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used to hold data to populate one table row in a jQuery DataTables 
 * managed table.
 */
@XmlRootElement
public class DataTableRecord {
    String id;
    Boolean validated;
    String classification;
    String score;
    String text;
    Boolean useInModel;
    String date;
    String latitude;
    String longitude;
    
    public DataTableRecord() {}; // needed for JAXB

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
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

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public Boolean getUseInModel() {
        return useInModel;
    }

    public void setUseInModel(Boolean useInModel) {
        this.useInModel = useInModel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
}
