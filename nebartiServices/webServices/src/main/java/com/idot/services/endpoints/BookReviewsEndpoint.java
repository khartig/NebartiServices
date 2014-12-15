/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.services.endpoints;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.lm.TokenizedLM;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.StopTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.ScoredObject;
import com.nebarti.dataaccess.dao.BookReviewDao;
import com.nebarti.dataaccess.domain.Book;
import com.nebarti.dataaccess.domain.BookReview;
import com.nebarti.dataaccess.domain.CoReference;
import com.nebarti.dataaccess.domain.NamedEntity;
import com.nebarti.dataaccess.domain.WordCount;
//import com.idot.utilities.ner.Dictionaries;
//import com.idot.utilities.ner.Dictionary;
//import com.idot.utilities.wordfrequency.StopWords;
//import com.idot.utilities.wordfrequency.WordCounter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * 
 */
@Path("/bookreviews")
public class BookReviewsEndpoint {

    private static final BookReviewDao bookReviewDao = new BookReviewDao();
//    private static final StopWords stopWords = new StopWords();
//    private static final Dictionaries dictionaries = new Dictionaries();
    private static final double CHUNK_SCORE = 1.0;
    private static final ExactDictionaryChunker dictionaryChunker;
    private static final Pattern stopWordsPattern;

    static {
        MapDictionary<String> dictionary = new MapDictionary<String>();
//        for (Dictionary d : dictionaries.getDictionaryCollection()) {
//            for (String value : d.getValues()) {
//                dictionary.addEntry(new DictionaryEntry<String>(value, d.getType().toString(), CHUNK_SCORE));
//            }
//        }

        dictionaryChunker = new ExactDictionaryChunker(
                dictionary,
                IndoEuropeanTokenizerFactory.INSTANCE,
                false, true); // find all occurances, case sensitive
        dictionaryChunker.setReturnAllMatches(false);
        
        StringBuilder pattern = new StringBuilder();
        pattern.append("\\b(?:");
//        for (String stopWord : stopWords.getwords()) {
//            if(stopWord.equals("?")) {stopWord = "\\?";}
//            pattern.append(stopWord).append("|");
//        }
        pattern.deleteCharAt(pattern.length()-1); // remove last vertical bar
        pattern.append(")\\b\\s*");

        stopWordsPattern = Pattern.compile(pattern.toString(), Pattern.CASE_INSENSITIVE);
    }
    
    @Context
    private UriInfo uriInfo;
    public static final Logger logger = Logger.getLogger(BookReviewsEndpoint.class.getName());

    /** 
     * Save a review to the proper collection.
     * 
     * @param review 
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response saveBookReview(BookReview review) {
        bookReviewDao.save(review);
        URI uri = uriInfo.getAbsolutePathBuilder().path(review.getId().toString()).build();
        return Response.created(uri).build();
    }

    /**
     * List all books which have reviews.
     * 
     * @return 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> listBooks() {
        List<Book> bookList = new ArrayList<Book>();
        Set<String> bookNames = bookReviewDao.listBooks();

        for (String bookName : bookNames) {
            if (!bookName.equals("system.indexes")) { //exclude the mongo index collection
                Book book = new Book();
                book.setTitle(bookName.replaceAll("_", " "));
                bookList.add(book);
            }
        }

        return bookList;
    }

    @GET
    @Path("/{book_title}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookReview> getReviews(@PathParam("book_title") String title) {
        List<BookReview> reviews = bookReviewDao.findByTitle(title);
        return reviews;
    }

//    @GET
//    @Path("/wordcount/{book_title}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<WordCount> getWordCount(@PathParam("book_title") String title) {
//        List<WordCount> wordCounts = new ArrayList<WordCount>();
//
//        List<BookReview> reviews = bookReviewDao.findByTitle(title);
//        if (!reviews.isEmpty()) {
//            WordCounter wordCounter = new WordCounter();
//            wordCounter.ignore(stopWords.toString());
//
//            for (BookReview review : reviews) {
//                wordCounter.countWords(review.getText());
//            }
//
//            wordCounts = wordCounter.getWordCounts(WordCounter.SortOrder.BY_FREQUENCY_DESCENDING);
//        }
//
//        return wordCounts;
//    }

    @GET
    @Path("/coreferences/{book_title}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CoReference> getCoreferences(
            @PathParam("book_title") String title,
            @DefaultValue("2") @QueryParam("count") Integer wordCount) {

        if (wordCount < 2) {
            wordCount = 2;
        } else if (wordCount > 3) {
            wordCount = 3;
        }

        int NGRAM_REPORTING_LENGTH = wordCount;
        int NGRAM;
        if (wordCount == 2) {
            NGRAM = 3;
        } else {
            NGRAM = 5;
        }

        int MIN_COUNT = 5;
        int MAX_COUNT = 100;

        List<BookReview> reviews = bookReviewDao.findByTitle(title);
        TokenizerFactory ietf = IndoEuropeanTokenizerFactory.INSTANCE;
        TokenizerFactory lctf = new LowerCaseTokenizerFactory(ietf, Locale.ENGLISH);
//        TokenizerFactory tf = new StopTokenizerFactory(lctf, stopWords.getwords());

//        TokenizedLM model = new TokenizedLM(tf, NGRAM);

//        for (BookReview review : reviews) {
//            String text = review.getText().replaceAll("'", "").replaceAll("\\-", "").replaceAll("\\*", "");
//            text = text.replaceAll("\\<.*?\\>", "");
//            model.handle(text);
//        }
//
//        model.sequenceCounter().prune(3);
//        SortedSet<ScoredObject<String[]>> collocations = model.collocationSet(NGRAM_REPORTING_LENGTH, MIN_COUNT, MAX_COUNT);
//
//        List<CoReference> coreferences = new ArrayList<CoReference>();
//
//        for (ScoredObject<String[]> collocation : collocations) {
//            Set<String> indicies = getDocumentIndicies(reviews, collocation.getObject());
//            coreferences.add(new CoReference(wordCount, Double.toString(collocation.score()), collocation.getObject(), indicies, null, new ArrayList<String>()));
//        }

//        return coreferences;
        return null;
    }
    
    private Set<String> getDocumentIndicies(List<BookReview> reviews, String[] wordArray) {
        Set<String> indicies = new HashSet<String>();
        String phrase = getPhrase(wordArray);
        
        Matcher matcher;
        for (BookReview review : reviews) {
            matcher = stopWordsPattern.matcher(review.getText());
            String cleanedReview = matcher.replaceAll("");
            if (cleanedReview.contains(phrase)) {
                indicies.add(review.getId());
            }
        }
        
        return indicies;
    }
    
    private String getPhrase(String[] wordArray) {
        String words = null;
        for(int i = 0; i < wordArray.length; i++) {
            if (words == null) {
                words = wordArray[i];
            } else {
                words = words + " " + wordArray[i];
            }
        }
        return words;
    }
    
    @GET
    @Path("/entities/{book_title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<NamedEntity> getNamedEntities(@PathParam("book_title") String title) {
        List<BookReview> reviews = bookReviewDao.findByTitle(title);
        StringBuilder stringBuilder = new StringBuilder();
                
        for (BookReview review : reviews) {
            stringBuilder.append(review.getText());
        }
        
        Set<NamedEntity> namedEntities = new HashSet<NamedEntity>();
        Chunking chunking =  dictionaryChunker.chunk(stringBuilder.toString());
        for (Chunk chunk : chunking.chunkSet()) {
            namedEntities.add(
                    new NamedEntity(
                            chunk.type(), 
                            Double.toString(chunk.score()), 
                            stringBuilder.substring(chunk.start(), chunk.end()),
                            1, new ArrayList<String>())
                    );
        }
        
        return namedEntities;
        
    }
}
