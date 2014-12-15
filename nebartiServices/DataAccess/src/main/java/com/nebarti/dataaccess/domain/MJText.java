/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
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
 *
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mjtext")
@XmlRootElement
public class MJText {
    @XmlAttribute
    private String id;
    private String createdDate = "";
    private String updatedDate = "";
    private Boolean validated = false;
    private Boolean useInModel = false;
    private String text = "";
    private String latitude = "";
    private String longitude = "";
    private String classification = "";
    private String gender = "";
    private String age = "";
    
    public static final Logger logger = Logger.getLogger(MJText.class.getName());

    public MJText() {}; // needed for JAXB

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
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

    public Boolean getUseInModel() {
        return useInModel;
    }

    public void setUseInModel(Boolean useInModel) {
        this.useInModel = useInModel;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String date) {
        this.createdDate = date;
    }
    
    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String date) {
        this.updatedDate = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MJText other = (MJText) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "MJText{" + "id=" + id + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", validated=" + validated + ", text=" + text + ", latitude=" + latitude + ", longitude=" + longitude + ", classification=" + classification + '}';
    }
    
}
