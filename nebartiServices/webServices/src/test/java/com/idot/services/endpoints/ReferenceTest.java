/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.services.endpoints;

import com.idot.utilities.TitleExtractor;
import com.mongodb.BasicDBObjectBuilder;
import com.nebarti.dataaccess.dao.GenericDao;
import com.nebarti.dataaccess.domain.ClassifiedText;
import com.nebarti.dataaccess.domain.Reference;
import com.nebarti.dataaccess.domain.comparators.ReferenceComparator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 */
public class ReferenceTest {

    BasicDBObjectBuilder builder;
    GenericDao genericDao;
    String modelName = "total_recall";
    List<ClassifiedText> documents;
    
    List<String> spamTitles = new ArrayList<String>();

    public static final Logger logger = Logger.getLogger(ReferenceTest.class.getName());

    @BeforeClass
    public void setUp() {
//        builder = BasicDBObjectBuilder.start();
//        genericDao = new GenericDao(modelName, "sentiment");
//        documents = genericDao.findDocuments(modelName, genericDao.getCount(), 0, builder);
//        genericDao.close();
//        
//        spamTitles.add("GetGlue");
//        spamTitles.add("Twitter /");
//        spamTitles.add("cashadvancepaydayloans");
//        spamTitles.add("bit.ly");
//        spamTitles.add("AdF.ly");
        
    }

//    @Test
//    public void getReferencesTest() {
//        HashMap<String, Reference> references = new HashMap<String, Reference>();
//        ReferenceComparator comparator =  new ReferenceComparator(references);
//        TreeMap<String, Reference> sorted_map = new TreeMap(comparator);
//        
//        List<String> urlStrings = new ArrayList<String>();
//        for (ClassifiedText document : documents) {
//            for (String urlString : extractLinks(document.getText())) {
//                urlStrings.add(urlString);
//            }
//        }
//
//        int num = 0;
//        Reference reference;
//        for (String urlString : urlStrings) {
//            logger.info("processing " + ++num + " of " + urlStrings.size() + " urls");
//            try {
//                String title = TitleExtractor.getPageTitle(urlString);
//                if (title != null && !title.equals("") && !spam(title)) {
//                    if (references.containsKey(title)) {
//                        reference = references.get(title);
//                        reference.setCount(reference.getCount() + 1);
//                    } else {
//                        reference = new Reference();
//                        reference.setShortURLString(urlString); // may have to check if url is really shortened
//                        reference.setTitle(title);
//                        reference.setCount(1);
//                    }
//
//                    references.put(reference.getTitle(), reference);
//                }
//            } catch (IOException ex) {
//                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
//            }
//
//        }
//        
//        sorted_map.putAll(references);
//        
//        logger.info("exiting");
//    }

    private List<String> extractLinks(String text) {
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
    
    private boolean spam(String title) {
        for (String spamString : spamTitles) {
            if (title.contains(spamString)) {
                return true;
            }
        }
        
        return false;
    }

    @AfterClass
    public void cleanUp() {
        // code that will be invoked after this test ends
    }

}
