/*
 * Nebarti
 * Copyright © 2013 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Base class representing classified text.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "classifiedtext")
@XmlRootElement
public class ClassifiedText {
    @XmlAttribute
    private String id;
    private String modelName;
    private String createdDate = "";
    private String updatedDate = "";
    private Boolean validated = false;
    private Boolean useInModel = false;
    private String text = "";
    private String latitude = "";
    private String longitude = "";
    private String classification = "";
    private String score = "";
    private String gender = "";
    private String age = "";
    private String dataSource;
    
    public static final Logger logger = Logger.getLogger(ClassifiedText.class.getName());

    public ClassifiedText() {}; // needed for JAXB
    
    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT 
     * represented by the created Date object.
     * 
     * @return 
     */
    public String getTime(String date) {
        Date d = null;
        long t = 0;
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
            d = (Date) formatter.parse(date);
        } catch (ParseException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage(), e);
        }
        if (d != null) {
            t = d.getTime();
        }
        return Long.toString(t);
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getUseInModel() {
        return useInModel;
    }

    public void setUseInModel(Boolean useInModel) {
        this.useInModel = useInModel;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClassifiedText other = (ClassifiedText) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ClassifiedText{" + "id=" + id + ", modelName=" + modelName + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", validated=" + validated + ", useInModel=" + useInModel + ", text=" + text + ", latitude=" + latitude + ", longitude=" + longitude + ", classification=" + classification + ", gender=" + gender + ", age=" + age + ", dataSource=" + dataSource + '}';
    }
    
}
