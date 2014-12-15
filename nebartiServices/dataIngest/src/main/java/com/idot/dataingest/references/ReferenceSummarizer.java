/*
 * Nebarti
 * Copyright © 2012. All rights reserved.
 */
package com.idot.dataingest.references;

import com.mongodb.BasicDBObjectBuilder;
import com.nebarti.dataaccess.dao.ClassifierDao;
import com.nebarti.dataaccess.dao.GenericDao;
import com.nebarti.dataaccess.domain.ClassifiedText;
import com.nebarti.dataaccess.domain.Classifier;
import com.nebarti.dataaccess.domain.ImageReference;
import com.nebarti.dataaccess.domain.Reference;
import com.nebarti.dataaccess.domain.comparators.ReferenceComparator;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 */
public class ReferenceSummarizer {
    private static final List<String> spamTitles = new ArrayList<String>();
    public static final Logger logger = Logger.getLogger(ReferenceSummarizer.class.getName());
    
    static {
        spamTitles.add("GetGlue");
        spamTitles.add("Twitter /");
        spamTitles.add("cashadvancepaydayloans");
        spamTitles.add("bit.ly");
        spamTitles.add("AdF.ly");
        spamTitles.add("dlvr.it");
    }
    
    public static void saveReferences(String modelName, Integer limit) {
        ClassifierDao classifierDao = new ClassifierDao();
        Classifier classifier = classifierDao.getClassifierByModelName(modelName);

        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();

        // extract urls from text
        Map<String, String> urlStrings = new HashMap<String, String>();
        List<ClassifiedText> documents = GenericDao.findDocuments(modelName, "sentiment", modelName, limit, 0, builder);
        
        for (ClassifiedText document : documents) {
            for (String urlString : extractLinks(document.getText())) {
                if (!urlStrings.containsKey(urlString)) {
                    urlStrings.put(urlString, document.getClassification());
                }
            }
        }

        int num = 0;
        Reference reference;
        HashMap<String, Reference> references = new HashMap<String, Reference>();
        ReferenceComparator comparator = new ReferenceComparator(references);
        TreeMap<String, Reference> sorted_map = new TreeMap(comparator);
        List<ImageReference> images = new ArrayList<ImageReference>();

        for (String urlString : urlStrings.keySet()) {
            if ((++num % 10) == 0) {
                logger.log(Level.INFO, "processing {0} of {1} urls for model = {2}", new Object[]{num, urlStrings.size(), modelName});
            }

            try {
                Document document = Jsoup.connect(urlString).get();

                // extract image references from document
                List<ImageReference> imageReferences = extractImages(document, classifier, urlStrings.get(urlString));
                for (ImageReference imageReference : imageReferences) {
                    if (!images.contains(imageReference)) {
                        images.add(imageReference);
                    }
                }

                // extract url references from document (web page)
                // urlStrings.get(urlString) = the classification associated with the url reference
                String title = document.title();
                if (title != null && !title.equals("") && !spam(title)) {
                    if (references.containsKey(title)) {
                        reference = references.get(title);
                        reference.setCount(reference.getCount() + 1);
                        
                        // check if classification associated with url exists, if not add it
                        List<String> classifications = reference.getClassifications();
                        if (!classifications.contains(urlStrings.get(urlString))) {
                            classifications.add(urlStrings.get(urlString));
                            reference.setClassifications(classifications);
                        }
                    } else {
                        reference = new Reference();
                        reference.setShortURLString(urlString); // may have to check if url is really shortened
                        reference.setTitle(title);
                        reference.setCount(1);
                        
                        List<String> classifications = new ArrayList<String>();
                        classifications.add(urlStrings.get(urlString));
                        reference.setClassifications(classifications);
                    }
                    if (reference.getUrlString() != null) {
                        references.put(reference.getTitle(), reference);
                    }
                }
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getLocalizedMessage());
            } catch (UnsupportedCharsetException uce) {
                logger.log(Level.WARNING, uce.getLocalizedMessage());
            } catch (IllegalArgumentException iae) {
                logger.log(Level.WARNING, iae.getLocalizedMessage());
            } catch (StringIndexOutOfBoundsException sioobe) {
                logger.log(Level.WARNING, "urlString = {0}", urlString);
                logger.log(Level.WARNING, sioobe.getLocalizedMessage(), sioobe);
            }
        }

        sorted_map.putAll(references);

        GenericDao.setReferences(modelName, "references", new ArrayList<Reference>(sorted_map.values()));
        GenericDao.setImages(modelName, "images", images);
    }
    
        /**
     * Extract all links from the body for easy retrieval
     */
    private static List<String> extractLinks(String text) {
        ArrayList<String> links = new ArrayList<String>();

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            links.add(urlStr);
        }
        return links;
    }
    
    private static List<ImageReference> extractImages(Document document, Classifier classifier, String classification) {
        List<ImageReference> images = new ArrayList<ImageReference>();

        for (Element imageElement : document.select("img")) {
            ImageReference imageReference = new ImageReference();
            String absUrl = imageElement.absUrl("src");
            if (isValidUrl(absUrl, classifier.getFilter())) {
                imageReference.setUrlString(imageElement.absUrl("src"));
                
                List<String> classifications = new ArrayList<String>();
                classifications.add(classification);
                imageReference.setClassifications(classifications);
                images.add(imageReference);
            }
        }

        return images;
    }
    
    private static Boolean isValidUrl(String url, String filter) {
        filter = filter.replaceAll("\"", ""); // there may be quotes to define multi-word strings to search
        String[] tokens = filter.split(" ");
        List<String> tokenList = Arrays.asList(tokens);

        for (String token : tokenList) {
            if (url.contains(token)) {
                if (containsExtension(url)
                        && (!url.contains("ads")
                        || !url.contains("adf")
                        || !url.contains("facebook")
                        || !url.contains("twitter")
                        || !url.contains("twimg")
                        || !url.contains("dlvr")
                        || !url.contains("aspx")
                        || !url.contains("ammoland"))) {
                    return true;
                }
            }
        }

        return false;
    }
    
    private static Boolean containsExtension(String url) {
        Boolean contains = false;
        if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".gif")) {
            contains = true;
        }
        return contains;
    }
    
    private static boolean spam(String title) {
        for (String spamString : spamTitles) {
            if (title.contains(spamString)) {
                return true;
            }
        }

        return false;
    }
}
