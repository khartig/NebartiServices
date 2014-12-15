/*
 * 
 */
package com.idot.twitter.textprocessing;

/**
 *
 * @author khartig
 */
public class CleanseText {

    public static String cleanse(String string) {
        return CleanseText.replaceDoubleQuotes(
                CleanseText.removeControlCharacters(string));
    }

    private static String removeControlCharacters(String string) {
        return string.replaceAll("\\p{Cntrl}", "");
    }

    private static String replaceDoubleQuotes(String string) {
        return string.replaceAll("\"", "'");
    }
}
