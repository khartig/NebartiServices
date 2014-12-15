package com.idot.classifiers;

import com.aliasi.util.Files;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;

import com.aliasi.lm.NGramProcessLM;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PolarityBasic {
    File polarityDir;
    List<String> categories = new ArrayList<String>();
    DynamicLMClassifier<NGramProcessLM> classifier;
    public static final Logger logger = Logger.getLogger(PolarityBasic.class.getName());

    PolarityBasic(String polarityDirPath) {
        polarityDir = new File(polarityDirPath);
        categories = Arrays.asList(polarityDir.list());
        logger.log(Level.INFO, "Category directories = {0}", categories.toString());

        int nGram = 8;
        classifier = DynamicLMClassifier.createNGramProcess(polarityDir.list(), nGram);
    }

    void run() throws ClassNotFoundException, IOException {
        train();
        evaluate();
    }

    boolean isTrainingFile(File file) {
        return file.getName().charAt(2) != '9';  // test on fold 9
    }

    void train() throws IOException {
        int numTrainingCases = 0;
        int numTrainingChars = 0;
        List<File> trainingFiles = new ArrayList<File>();

        logger.log(Level.INFO, "Training text files...");
        for (String category : categories) {
            Classification classification = new Classification(category);
            File categoryDir = new File(polarityDir, category);
            trainingFiles = Arrays.asList(categoryDir.listFiles());
            for (File file : trainingFiles) {
                if (isTrainingFile(file)) {
                    ++numTrainingCases;
                    String review = Files.readFromFile(file, "ISO-8859-1");
                    numTrainingChars += review.length();
                    Classified<CharSequence> classified = new Classified<CharSequence>(review, classification);
                    classifier.handle(classified);
                }
            }
        }
        logger.log(Level.INFO, "  # Training Cases={0}", numTrainingCases);
        logger.log(Level.INFO, "  # Training Chars={0}", numTrainingChars);
    }

    void evaluate() throws IOException {
        logger.log(Level.INFO, "\nEvaluating.");
        int numTests = 0;
        int numCorrect = 0;
        List<File> trainingFiles = new ArrayList<File>();

        for (String category : categories) {
            File categoryDir = new File(polarityDir, category);
            trainingFiles = Arrays.asList(categoryDir.listFiles());
            for (File file : trainingFiles) {
                if (!isTrainingFile(file)) {
                    String review = Files.readFromFile(file, "ISO-8859-1");
                    ++numTests;
                    Classification classification = classifier.classify(review);
                    if (classification.bestCategory().equals(category)) {
                        ++numCorrect;
                    }
                }
            }
        }
        
        logger.log(Level.INFO, "  # Test Cases={0}", numTests);
        logger.log(Level.INFO, "  # Correct={0}", numCorrect);
        logger.log(Level.INFO, "  % Correct={0}", ((double) numCorrect) / (double) numTests);
    }
    
    public String evaluate(String string) {
        Classification classification = classifier.classify(string);
        return classification.bestCategory();
    }

    public static void main(String[] args) {
        PolarityBasic polarityBasic = null;
        try {
            polarityBasic = new PolarityBasic("./data/movies/review_polarity/txt_sentoken");
            polarityBasic.train();
            logger.log(Level.INFO, 
                    "Tweet sentiment = {0}", 
                    polarityBasic.evaluate("Planet of the Apes. terrible sad ugly disgusting"));

//            polarityBasic.run();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, t.getLocalizedMessage(), t);
        }
        
    }
}
