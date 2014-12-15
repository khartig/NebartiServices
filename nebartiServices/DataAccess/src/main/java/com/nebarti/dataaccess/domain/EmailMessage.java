/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Base class representing an email message.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emailmessage")
@XmlRootElement
public class EmailMessage {
    @XmlAttribute
    private String id;
    private String name;
    private String emailAddress;
    private String phone;
    private String feedbackType;
    private String message;
    
    public EmailMessage() {}; // needed for JAXB

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmailMessage other = (EmailMessage) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.emailAddress == null) ? (other.emailAddress != null) : !this.emailAddress.equals(other.emailAddress)) {
            return false;
        }
        if ((this.phone == null) ? (other.phone != null) : !this.phone.equals(other.phone)) {
            return false;
        }
        if ((this.feedbackType == null) ? (other.feedbackType != null) : !this.feedbackType.equals(other.feedbackType)) {
            return false;
        }
        if ((this.message == null) ? (other.message != null) : !this.message.equals(other.message)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 83 * hash + (this.emailAddress != null ? this.emailAddress.hashCode() : 0);
        hash = 83 * hash + (this.phone != null ? this.phone.hashCode() : 0);
        hash = 83 * hash + (this.feedbackType != null ? this.feedbackType.hashCode() : 0);
        hash = 83 * hash + (this.message != null ? this.message.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "EmailMessage{" + "id=" + id + ", name=" + name + ", emailAddress=" + emailAddress + ", phone=" + phone + ", feedbackType=" + feedbackType + ", message=" + message + '}';
    }
    
}
