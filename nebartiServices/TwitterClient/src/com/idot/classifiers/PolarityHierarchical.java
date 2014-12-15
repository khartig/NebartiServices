package com.idot.classifiers;

import com.aliasi.classify.Classification;
import com.aliasi.classify.JointClassifier;
import com.aliasi.classify.ConditionalClassification;

import com.aliasi.util.BoundedPriorityQueue;
import com.aliasi.util.ScoredObject;

import com.idot.utilities.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PolarityHierarchical {

    File polarityDir;

    JointClassifier<CharSequence> polarityClassifier;
    JointClassifier<CharSequence> subjectivityClassifier;
    public static final Logger logger = Logger.getLogger(PolarityHierarchical.class.getName());

    public PolarityHierarchical() throws ClassNotFoundException, IOException {        
        // load models
        polarityClassifier = readModelFile(new File(Properties.getProperty("model.dir") + "/models/polarity.model"));
        subjectivityClassifier = readModelFile(new File(Properties.getProperty("model.dir") + "/models/subjectivity.model"));
    }

    private JointClassifier<CharSequence> readModelFile(File modelFile) {
        JointClassifier<CharSequence> classifier = null;
        FileInputStream fileIn = null;
        ObjectInputStream objIn = null;
        
        try {
            logger.log(Level.INFO, "\nReading Compiled Model from file={0}", modelFile);
            
            fileIn = new FileInputStream(modelFile);
            objIn = new ObjectInputStream(fileIn);
            classifier = (JointClassifier<CharSequence>) objIn.readObject();
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            try {
                if (fileIn != null) {
                    fileIn.close();
                }
                
                if (objIn != null) {
                    objIn.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
            return classifier;
        }
    }

    public enum Sentiment { POSITIVE, NEGATIVE, NEUTRAL }

    public Sentiment evaluate(String sentence) {
        Sentiment sentiment = Sentiment.NEUTRAL;
        boolean storeInstances = false;
        Classification classification;

        if (isSubjective(sentence)) {
            classification = polarityClassifier.classify(sentence);
//            logger.info("Best category = " + classification.bestCategory());
            if (classification.bestCategory().matches("pos")) {
                sentiment = Sentiment.POSITIVE;
            } else if (classification.bestCategory().matches("neg")) {
                sentiment = Sentiment.NEGATIVE;
            } else if (classification.bestCategory().matches("neutral")) {
                sentiment = Sentiment.NEUTRAL;
            }
        }

        return sentiment;
    }

    public Boolean isSubjective(String sentence) {
        Boolean subjective = Boolean.FALSE;

        ConditionalClassification subjClassification = (ConditionalClassification) subjectivityClassifier.classify(sentence);

        double subjProb;
        if (subjClassification.category(0).equals("quote")) {
            subjProb = subjClassification.conditionalProbability(0);
        } else {
            subjProb = subjClassification.conditionalProbability(1);
        }

        if (subjProb > 0.5) {
            subjective = Boolean.TRUE;
        }

        return subjective;
    }

    String subjectiveSentences(String review) {
        String[] sentences = review.split("\n");
        BoundedPriorityQueue<ScoredObject<String>> pQueue = new BoundedPriorityQueue<ScoredObject<String>>(ScoredObject.comparator(), MAX_SENTS);
        for (int i = 0; i < sentences.length; ++i) {
            String sentence = sentences[i];
            ConditionalClassification subjClassification = (ConditionalClassification) subjectivityClassifier.classify(sentences[i]);
            double subjProb;
            if (subjClassification.category(0).equals("quote")) {
                subjProb = subjClassification.conditionalProbability(0);
            } else {
                subjProb = subjClassification.conditionalProbability(1);
            }
            pQueue.offer(new ScoredObject<String>(sentence, subjProb));
        }
        StringBuilder reviewBuf = new StringBuilder();
        Iterator<ScoredObject<String>> it = pQueue.iterator();
        for (int i = 0; it.hasNext(); ++i) {
            ScoredObject<String> so = it.next();
            if (so.score() < .5 && i >= MIN_SENTS) {
                break;
            }
            reviewBuf.append(so.getObject()).append("\n");
        }
        String result = reviewBuf.toString().trim();
        return result;
    }
    static int MIN_SENTS = 5;
    static int MAX_SENTS = 25;

    public static void main(String[] args) {
        try {
            PolarityHierarchical polarityHierarchical = new PolarityHierarchical();
            
            String[] tweets = 
                {
                     "Planet of the apes was decent .",
                     "yall gona have the best lookin babies on planet of the apes .)",
                     "Rise of Planet of The Apes has a... happy ending? Um, you know they kill/enslave us all, right? .",
                     "Yall . . I love Planet Of The Apes , I can't stop saying 'Ceasar is home' ?? lol .",
                     "hey i went to see rise of the planet of the apes and it was awesome .",
                     "Mekanism helps Planet of the Apes hit #1 .",
                     "I watched planet of the apes Twice today! .",
                     "You talked shit on Planet of the Apes and you didn't even see it! It was good! hahaha .",
                     "one of these bitches poppin up on my tl look like planet of the apes, bitch u dislike me but youll never look like me, step yo shit up! .",
                     "Planet of the apes was an interesting movie! ."
                };

            for (String tweet : tweets) {
                logger.log(Level.INFO, "sentiment, tweet = {0} {1}", new Object[]{polarityHierarchical.evaluate(tweet), tweet});
            }
            
        } catch (Throwable t) {
            logger.log(Level.SEVERE, t.getLocalizedMessage(), t);
        }
    }
}
