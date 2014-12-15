/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * 
 */
@XmlType(name = "classifications")
@XmlRootElement
public class ClassificationSet {
    String id;
    String name;
    String visibleName;
    Collection<String> classifications = new HashSet<String>();
    
    public ClassificationSet() {};

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

    public String getVisibleName() {
        return visibleName;
    }

    public void setVisibleName(String visibleName) {
        this.visibleName = visibleName;
    }

    public Collection<String> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<String> classifications) {
        this.classifications = classifications;
    }
    
    public void addClassification(String classification) {
        classifications.add(classification);
    }
}
