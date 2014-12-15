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
@XmlType(name = "bookreview", propOrder = {
    "sets",
    "voice",
    "name",
    "date",
    "text",
    "bookRank",
    "author",
    "avgReview",
    "bestSellerPosition",
    "edition",
    "helpful",
    "helpfulPercent",
    "listDate",
    "listName",
    "numberReview",
    "pages",
    "publisher",
    "reviewTitle",
    "reviewerName",
    "reviewDate",
    "reviewLocation",
    "reviewRating",
    "title",
    "total",
    "classification"
})
@XmlRootElement
public class BookReview { 
    @XmlAttribute
    private String id;
    private String sets;
    private String voice;
    private String name;
    private String date;
    private String text;
    private String bookRank;
    private String author;
    private String avgReview;
    private String bestSellerPosition;
    private String edition;
    private String helpful;
    private String helpfulPercent;
    private String listDate;
    private String listName;
    private String numberReview;
    private String pages;
    private String publisher;
    private String reviewTitle;
    private String reviewerName;
    private String reviewDate;
    private String reviewLocation;
    private String reviewRating;
    private String title;
    private String total;
    private String classification;
    public static final Logger logger = Logger.getLogger(BookReview.class.getName());

    public BookReview() {}; // needed for JAXB
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAvgReview() {
        return avgReview;
    }

    public void setAvgReview(String avgReview) {
        this.avgReview = avgReview;
    }

    public String getBestSellerPosition() {
        return bestSellerPosition;
    }

    public void setBestSellerPosition(String bestSellarPosition) {
        this.bestSellerPosition = bestSellarPosition;
    }

    public String getBookRank() {
        return bookRank;
    }

    public void setBookRank(String bookRank) {
        this.bookRank = bookRank;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getHelpful() {
        return helpful;
    }

    public void setHelpful(String helpful) {
        this.helpful = helpful;
    }

    public String getHelpfulPercent() {
        return helpfulPercent;
    }

    public void setHelpfulPercent(String helpfulPercent) {
        this.helpfulPercent = helpfulPercent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getListDate() {
        return listDate;
    }

    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberReview() {
        return numberReview;
    }

    public void setNumberReview(String numberReview) {
        this.numberReview = numberReview;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT 
     * represented by this Date object.
     * 
     * @return 
     */
    public String getTime() {
        Date d = null;
        long t = 0;
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
            d = (Date) formatter.parse(reviewDate);
        } catch (ParseException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage(), e);
        }
        if (d != null) {
            t = d.getTime();
        }
        return Long.toString(t);
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        if (reviewDate.length() < 9) {
            reviewDate = "0" + reviewDate;
        }
        this.reviewDate = reviewDate;
    }

    public String getReviewLocation() {
        return reviewLocation;
    }

    public void setReviewLocation(String reviewLocation) {
        this.reviewLocation = reviewLocation;
    }

    public String getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = correctString(reviewTitle);
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = correctString(title);
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    private String correctString(String string) {
        return string.replaceAll("\\s+", "_").replaceAll(":", "").replaceAll("'", "").replaceAll("/", "");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BookReview other = (BookReview) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.name)) {
            return false;
        }
        if ((this.date == null) ? (other.date != null) : !this.date.equals(other.date)) {
            return false;
        }
        if ((this.author == null) ? (other.author != null) : !this.author.equals(other.author)) {
            return false;
        }
        if ((this.publisher == null) ? (other.publisher != null) : !this.publisher.equals(other.publisher)) {
            return false;
        }
        if ((this.reviewTitle == null) ? (other.reviewTitle != null) : !this.reviewTitle.equals(other.reviewTitle)) {
            return false;
        }
        if ((this.reviewDate == null) ? (other.reviewDate != null) : !this.reviewDate.equals(other.reviewDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 97 * hash + (this.date != null ? this.date.hashCode() : 0);
        hash = 97 * hash + (this.author != null ? this.author.hashCode() : 0);
        hash = 97 * hash + (this.publisher != null ? this.publisher.hashCode() : 0);
        hash = 97 * hash + (this.reviewTitle != null ? this.reviewTitle.hashCode() : 0);
        hash = 97 * hash + (this.reviewDate != null ? this.reviewDate.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "BookReview{" + "id=" + id + ", sets=" + sets + ", voice=" + voice + 
                ", name=" + name + ", date=" + date + ", text=" + text + ", bookRank=" + bookRank +
                ", author=" + author + ", avgReview=" + avgReview + 
                ", bestSellarPosition=" + bestSellerPosition + ", edition=" + edition +
                ", helpful=" + helpful + ", helpfulPercent=" + helpfulPercent +
                ", listDate=" + listDate + ", listName=" + listName +
                ", numberReview=" + numberReview + ", pages=" + pages +
                ", publisher=" + publisher + ", reviewTitle=" + reviewTitle +
                ", reviewerName=" + reviewerName + ", reviewDate=" + reviewDate +
                ", reviewLocation=" + reviewLocation + ", reviewRating=" + reviewRating +
                ", title=" + title + ", total=" + total + ", classification=" + classification + '}';
    }
 
}
