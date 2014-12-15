/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds information about Twitter account Streaming API access.
 *
 */
@XmlRootElement
public final class TwitterAccount {
    private String id;
    private String userID;
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;
    private String creationDate;
    private String updatedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    
    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TwitterAccount other = (TwitterAccount) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.userID == null) ? (other.userID != null) : !this.userID.equals(other.userID)) {
            return false;
        }
        if ((this.consumerKey == null) ? (other.consumerKey != null) : !this.consumerKey.equals(other.consumerKey)) {
            return false;
        }
        if ((this.consumerSecret == null) ? (other.consumerSecret != null) : !this.consumerSecret.equals(other.consumerSecret)) {
            return false;
        }
        if ((this.accessToken == null) ? (other.accessToken != null) : !this.accessToken.equals(other.accessToken)) {
            return false;
        }
        if ((this.accessTokenSecret == null) ? (other.accessTokenSecret != null) : !this.accessTokenSecret.equals(other.accessTokenSecret)) {
            return false;
        }
        if ((this.creationDate == null) ? (other.creationDate != null) : !this.creationDate.equals(other.creationDate)) {
            return false;
        }
        if ((this.updatedDate == null) ? (other.updatedDate != null) : !this.updatedDate.equals(other.updatedDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.userID != null ? this.userID.hashCode() : 0);
        hash = 37 * hash + (this.consumerKey != null ? this.consumerKey.hashCode() : 0);
        hash = 37 * hash + (this.consumerSecret != null ? this.consumerSecret.hashCode() : 0);
        hash = 37 * hash + (this.accessToken != null ? this.accessToken.hashCode() : 0);
        hash = 37 * hash + (this.accessTokenSecret != null ? this.accessTokenSecret.hashCode() : 0);
        hash = 37 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0);
        hash = 37 * hash + (this.updatedDate != null ? this.updatedDate.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "TwitterAccount{" + "id=" + id + ", userID=" + userID + ", consumerKey=" + consumerKey + ", consumerSecret=" + consumerSecret + ", accessToken=" + accessToken + ", accessTokenSecret=" + accessTokenSecret + ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + '}';
    }

}
