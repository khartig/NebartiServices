/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import java.util.Collection;
import java.util.HashSet;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Defines the attributes of a classifier
 */
@XmlType(name = "classifier")
@XmlRootElement
public class Classifier {
    String id;
    String name;
    String visibleName;
    String category;
    String classificationSet;
    String model;
    String filter;
    Boolean dataFeedSwitch;    
    String owner;
    Collection<String> dataSources = new HashSet<String>();

    public Classifier() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClassificationSet() {
        return classificationSet;
    }

    public void setClassificationSet(String classificationSet) {
        this.classificationSet = classificationSet;
    }

    public Boolean getDataFeedSwitch() {
        return dataFeedSwitch;
    }

    public void setDataFeedSwitch(Boolean dataFeedSwitch) {
        this.dataFeedSwitch = dataFeedSwitch;
    }

    public Collection<String> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Collection<String> dataSources) {
        this.dataSources = dataSources;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVisibleName() {
        return visibleName;
    }

    public void setVisibleName(String visibleName) {
        this.visibleName = visibleName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Classifier other = (Classifier) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
}
