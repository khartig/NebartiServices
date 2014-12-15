/*
 * 
 */
package com.idot.twitter.textprocessing;

import java.util.logging.Logger;

/**
 *
 * @author khartig
 */
public class TweetPreprocessor {

    public static final Logger logger = Logger.getLogger(TweetPreprocessor.class.getName());

    public static String prepare(String tweetString) {
        return addTerminator(
                 fixSpaces(
                   removeURLs(
                     removeHashes(
                       removeNames(
                         removeRTs(tweetString))))));
    }

    public static String removeHashes(String tweetString) {
        return tweetString.replace("#", "");
    }

    public static String removeURLs(String tweetString) {
        String protocol = "http://";
        if (tweetString.contains(protocol)) {
            int startPos = tweetString.indexOf(protocol);
            if (startPos == -1) {
                return tweetString;
            }
            int endPos = tweetString.indexOf(" ", startPos);
            if (endPos == -1) {
                endPos = tweetString.length();
            }
            String url = tweetString.substring(startPos, endPos);
            tweetString = tweetString.replace(url, "");

            if (tweetString.contains(protocol)) {
                tweetString = removeURLs(tweetString);
            }
        }
        return tweetString;
    }
    
    public static String removeRTs(String tweetString) {
        if (tweetString.contains("RT: ")) {
            tweetString = tweetString.replace("RT: ", "");
        } else if (tweetString.contains("RT ")) {
            tweetString = tweetString.replace("RT ", "");
        }
        
        return tweetString;
    }
    
    public static String fixSpaces(String tweetString) {
        tweetString = tweetString.replaceAll("[\t]+", " ");
        return tweetString.replaceAll("[ ]+", " ").trim();
        
    }
    
    public static String removeNames(String tweetString) {
        if (tweetString.contains("@")) {
            tweetString = tweetString.replaceAll("@[a-zA-Z0-9_]+", "");
        }
        
        return tweetString;
    }

    public static String addTerminator(String tweetString) {
        tweetString = tweetString.trim();
        if (tweetString.endsWith(".")) {
            tweetString = tweetString.substring(0, tweetString.length() - 1);
        }

        return tweetString + " .";
    }
}
