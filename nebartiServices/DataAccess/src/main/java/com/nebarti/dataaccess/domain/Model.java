/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents model attributes
 */
@XmlType(name = "model")
@XmlRootElement
public class Model {
    String id;
    String name;
    String visibleName;
    String category;
    String classifier;
    String owner;
    String creationDate;
    String updatedDate;
    
    public Model() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String cretionDate) {
        this.creationDate = cretionDate;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public String toString() {
        return "Model{" + "id=" + id + ", name=" + name + ", visibleName=" + visibleName + ", category=" + category + ", classifier=" + classifier + ", owner=" + owner + ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + '}';
    }
    
}
