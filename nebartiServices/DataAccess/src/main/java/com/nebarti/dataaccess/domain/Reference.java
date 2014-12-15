/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Defines the reference information for a URL found in text being analyzed.
 * Information includes:
 *     URL - shortened or un-shortened or both
 *     IMG - links to images found in the URL
 */
@XmlType(name = "reference")
@XmlRootElement
public class Reference {
    String id;
    String shortURLString;
    String urlString;
    String title;
    Integer count;
    List<String> classifications;
    public static final Logger logger = Logger.getLogger(Reference.class.getName());

    public Reference() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getShortURLString() {
        return shortURLString;
    }

    public void setShortURLString(String shortURLString) {
        this.shortURLString = shortURLString;
        this.urlString = expandShortURL(shortURLString);
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<String> classifications) {
        this.classifications = classifications;
    }

    /**
     * Expand a shortened url. If the url passed in is from Twitter, it 
     * could be a shortening of and already shortened url.
     * 
     * @param address
     * @return
     * @throws IOException 
     */
    private String expandShortURL(String address) {
        String expandedURL = null;
        try {
            URL url = new URL(address);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.connect();
            expandedURL = connection.getHeaderField("Location");
            connection.getInputStream().close();

        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return expandedURL;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reference other = (Reference) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 73 * hash + (this.title != null ? this.title.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Reference{" + "id=" + id + ", shortURLString=" + shortURLString + ", urlString=" + urlString + ", title=" + title + ", count=" + count + ", classifications=" + classifications + '}';
    }

}
