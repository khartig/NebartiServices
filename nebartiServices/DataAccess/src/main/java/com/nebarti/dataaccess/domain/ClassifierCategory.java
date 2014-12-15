/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Defines a classifier category.
 */
@XmlType(name = "classifierCategory")
@XmlRootElement
public class ClassifierCategory {
    String id;
    String name;
    String owner;
    String visibleName;
    
    public ClassifierCategory() {
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
    
}
