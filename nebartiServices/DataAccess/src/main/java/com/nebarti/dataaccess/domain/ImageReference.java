/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import java.util.List;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Defines reference information to an image for a URL found in a web page.
 */
@XmlType(name = "imagereference")
@XmlRootElement
public class ImageReference {
    String id;
    String urlString;
    List<String> classifications;
    public static final Logger logger = Logger.getLogger(ImageReference.class.getName());
    
    public ImageReference() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
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
        final ImageReference other = (ImageReference) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.urlString == null) ? (other.urlString != null) : !this.urlString.equals(other.urlString)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 41 * hash + (this.urlString != null ? this.urlString.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ImageReference{" + "id=" + id + ", urlString=" + urlString + ", classifications=" + classifications + '}';
    }

}
