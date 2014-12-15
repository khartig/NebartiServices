/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idot.sentiment.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author khartig
 */
@Entity
public class Tweet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String tweet;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tweetedAt;
    private String geolocation;
    private String sentiment;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Date getTweetedAt() {
        return tweetedAt;
    }

    public void setTweetedAt(Date tweetedAt) {
        this.tweetedAt = tweetedAt;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tweet other = (Tweet) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.tweet == null) ? (other.tweet != null) : !this.tweet.equals(other.tweet)) {
            return false;
        }
        if (this.tweetedAt != other.tweetedAt && (this.tweetedAt == null || !this.tweetedAt.equals(other.tweetedAt))) {
            return false;
        }
        if ((this.geolocation == null) ? (other.geolocation != null) : !this.geolocation.equals(other.geolocation)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Tweet{" + "id=" + id + ", tweet=" + tweet + ", tweetedAt=" + tweetedAt + ", geolocation=" + geolocation + ", sentiment=" + sentiment + '}';
    }

}
