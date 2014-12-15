/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.wordcount;

import com.nebarti.dataaccess.domain.WordCount;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.comparators.ReverseComparator;

public final class WordCounter {

    private static final Comparator<Map.Entry<String, MutableInteger>> SORT_BY_FREQUENCY_ASCENDING =
            new ComparatorFrequency();
    private static final Comparator<Map.Entry<String, MutableInteger>> SORT_BY_FREQUENCY_DESCENDING =
            new ReverseComparator(new ComparatorFrequency());
    private static final Comparator<Map.Entry<String, MutableInteger>> SORT_ALPHABETICALLY_ASCENDING =
            new ComparatorAlphabetic();
    private static final Comparator<Map.Entry<String, MutableInteger>> SORT_ALPHABETICALLY_DESCENDING =
            new ReverseComparator(new ComparatorAlphabetic());

    public enum SortOrder {

        ALPHABETICALLY_ASCENDING, ALPHABETICALLY_DESCENDING, BY_FREQUENCY_ASCENDING, BY_FREQUENCY_DESCENDING
    }

    private Set<String> ignoreWords;   // Words to ignore.
    private Map<String, MutableInteger> wordFrequency; // Words -> frequency
    int totalWords;    // Total source words.
    public static final Logger logger = Logger.getLogger(WordCountSummarizer.class.getName());

    public WordCounter() {
        ignoreWords = new HashSet<String>();
        wordFrequency = new HashMap<String, MutableInteger>();
        totalWords = 0;
    }
    
    public WordCounter(File wordFile, File ignoreFile) throws IOException {
        this();
        this.ignore(ignoreFile);
        this.countWords(wordFile);
    }
    
    public WordCounter(String wordString, String ignoreString) {
        this();
        this.ignore(ignoreString);
        this.countWords(wordString);
    }

    /**
     *  Reads file of words to ignore. Ignore words are added to a Set.
     * 
     * @param ignoreFile File of words to ignore.
     */
    public void ignore(File ignoreFile) throws IOException {
        Scanner ignoreScanner = new Scanner(ignoreFile);
//        ignoreScanner.useDelimiter("[^A-Za-z]+");
        ignoreScanner.useDelimiter("(?!')[^A-Za-z]+");

        while (ignoreScanner.hasNext()) {
            ignoreWords.add(ignoreScanner.next());
        }
        ignoreScanner.close();  // Close underlying file.
    }

    /**
     * Takes String of words to ignore. Ignore words are added to a Set.
     * 
     * @param ignore String of words to ignore.
     */
    public void ignore(String ignoreString) {
        Scanner ignoreScanner = new Scanner(ignoreString);
//        ignoreScanner.useDelimiter("(?!')[^A-Za-z]+");
        ignoreScanner.useDelimiter("(?!')[^A-Za-z]+");

        while (ignoreScanner.hasNext()) {
            ignoreWords.add(ignoreScanner.next());
        }
    }

    /** Record the frequency of words in the source file.
     *  May be called more than once.
     *  IOException is passed to caller, who might know what to do with it.
     * 
     * @param File of words to process.
     */
    public void countWords(File sourceFile) throws IOException {
        Scanner wordScanner = new Scanner(sourceFile);
//        wordScanner.useDelimiter("(?!')[^A-Za-z]+");
        wordScanner.useDelimiter("(?!')[^A-Za-z]+");
        addWordToMap(wordScanner);
        wordScanner.close();  // Close underlying file.
    }

    /** 
     *  Record the frequency of words in a String.
     *  May be called more than once.
     * 
     *  @param String of words to process.
     */
    public void countWords(String source) {
        Scanner wordScanner = new Scanner(source);
//        wordScanner.useDelimiter("[^A-Za-z]+");
        wordScanner.useDelimiter("(?!')[^A-Za-z]+");
        addWordToMap(wordScanner);
    }
    
    private void addWordToMap(Scanner wordScanner) {
        while (wordScanner.hasNext()) {
            String word = wordScanner.next();
            totalWords++;
            
            word = word.toLowerCase();

            //... Add word if not in map, otherwise increment count.
            if (!ignoreWords.contains(word)) {
                MutableInteger count = wordFrequency.get(word);
                if (count == null) {    // Create new entry with count of 1.
                    wordFrequency.put(word, new MutableInteger(1));
                } else {                // Increment existing count by 1.
                    count.increment();
                }
            }
        }
    }    

    /**
     * Get the calculated Map of words with count.
     * 
     * @return Map<String, Integer> the word map
     */
    public Map<String, Integer> getWordFrequencies(SortOrder sortBy) {
        ArrayList<Map.Entry<String, MutableInteger>> entries =
                new ArrayList<Map.Entry<String, MutableInteger>>(wordFrequency.entrySet());
        if (sortBy == SortOrder.ALPHABETICALLY_ASCENDING) {
            Collections.sort(entries, SORT_ALPHABETICALLY_ASCENDING);
        } else {
            Collections.sort(entries, SORT_BY_FREQUENCY_DESCENDING);
        }
        
        Map<String, Integer> wordFrequencies = new HashMap<String, Integer>();
        ValueComparator bvc =  new ValueComparator(wordFrequencies);
        TreeMap<String, Integer> sorted_map = new TreeMap(bvc);
        
        for (Map.Entry<String, MutableInteger> entry : entries) {
            wordFrequencies.put(entry.getKey(), entry.getValue().getInteger());
        }
        
        sorted_map.putAll(wordFrequencies);

        return sorted_map;
    }
    
    class ValueComparator implements Comparator {
        Map base;

        public ValueComparator(Map base) {
            this.base = base;
        }

        @Override
        public int compare(Object a, Object b) {

            if ((Integer) base.get(a) < (Integer) base.get(b)) {
                return 1;
            } else if ((Integer) base.get(a) == (Integer) base.get(b)) {
                return 0;
            } else {
                return -1;
            }
        }
    }    
    
    /**
     * Get the calculated Map of words with counts.
     * 
     * @return List<WordCount> the word map
     */
    public List<WordCount> getWordCounts(SortOrder sortBy) {
        ArrayList<Map.Entry<String, MutableInteger>> entries =
                new ArrayList<Map.Entry<String, MutableInteger>>(wordFrequency.entrySet());
        
        switch (sortBy) {
            case ALPHABETICALLY_ASCENDING:
                Collections.sort(entries, SORT_ALPHABETICALLY_ASCENDING);
                break;
            case ALPHABETICALLY_DESCENDING:
                Collections.sort(entries, SORT_BY_FREQUENCY_DESCENDING);
                break;
            case BY_FREQUENCY_ASCENDING:
                Collections.sort(entries, SORT_BY_FREQUENCY_ASCENDING);
                break;
            case BY_FREQUENCY_DESCENDING:
                Collections.sort(entries, SORT_BY_FREQUENCY_DESCENDING);
                break;
            default:
                logger.log(Level.WARNING, "Sort type unknown:{0}", sortBy);
                break;        
        }
        
        List<WordCount> wordCounts = new ArrayList<WordCount>();
        
        for (Map.Entry<String, MutableInteger> entry : entries) {
            wordCounts.add(new WordCount(entry.getKey(), entry.getValue().getInteger()));
        }
        
        return wordCounts;
    }

    /** 
     * Returns number of words in all source file(s).
     * 
     * @return Total number of words processed in all source files.
     */
    public int getWordCount() {
        return totalWords;
    }

    /** 
     *  Returns the number of unique, non-ignored words, in the source file(s).
     *  This number should be used to for the size of the arrays that are
     *  passed to getWordFrequency.
     * 
     * @return Number of unique non-ignored source words.
     */
    public int getEntryCount() {
        return wordFrequency.size();
    }

    /** 
     * Stores words and their corresponding frequencies in parallel array lists
     * parameters.  The frequencies are sorted from low to high.
     * 
     * @param words Unique words that were found in the source file(s).
     * @param counts Frequency of words at corresponding index in words array.
     */
    public void getWordFrequency(ArrayList<String> out_words, ArrayList<Integer> out_counts) {
        //... Put in ArrayList so sort entries by frequency
        ArrayList<Map.Entry<String, MutableInteger>> entries =
                new ArrayList<Map.Entry<String, MutableInteger>>(wordFrequency.entrySet());
        Collections.sort(entries, new ComparatorFrequency());

        //... Add word and frequency to parallel output ArrayLists.
        for (Map.Entry<String, MutableInteger> ent : entries) {
            out_words.add(ent.getKey());
            out_counts.add(ent.getValue().getValue());
        }
    }

    /** 
     * Return array of unique words, in the order specified.
     * 
     * @return An array of the words in the currently selected order.
     */
    public String[] getWords(SortOrder sortBy) {
        String[] result = new String[wordFrequency.size()];
        ArrayList<Map.Entry<String, MutableInteger>> entries =
                new ArrayList<Map.Entry<String, MutableInteger>>(wordFrequency.entrySet());
        if (sortBy == SortOrder.ALPHABETICALLY_ASCENDING) {
            Collections.sort(entries, SORT_ALPHABETICALLY_ASCENDING);
        } else {
            Collections.sort(entries, SORT_BY_FREQUENCY_ASCENDING);
        }

        //... Add words to the String array.
        int i = 0;
        for (Map.Entry<String, MutableInteger> ent : entries) {
            result[i++] = ent.getKey();
        }
        return result;
    }

    /** 
     * Return array of frequencies, in the order specified.
     * 
     * @return An array of the frequencies in the specified order.
     */
    public int[] getFrequencies(SortOrder sortBy) {
        int[] result = new int[wordFrequency.size()];
        ArrayList<Map.Entry<String, MutableInteger>> entries =
                new ArrayList<Map.Entry<String, MutableInteger>>(wordFrequency.entrySet());
        if (sortBy == SortOrder.ALPHABETICALLY_ASCENDING) {
            Collections.sort(entries, SORT_ALPHABETICALLY_ASCENDING);
        } else {
            Collections.sort(entries, SORT_BY_FREQUENCY_ASCENDING);
        }

        //... Add words to the String array.
        int i = 0;
        for (Map.Entry<String, MutableInteger> ent : entries) {
            result[i++] = ent.getValue().getValue();
        }
        return result;
    }
}
