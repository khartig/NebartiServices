package com.idot.classifiers;

import com.aliasi.util.Files;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.JointClassifierEvaluator;

import com.aliasi.lm.NGramProcessLM;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SubjectivityBasic {
    File polarityDir;
    String[] categoryStrings = new String[] { "plot", "quote" };
    List<String> categories = new ArrayList<String>();
    DynamicLMClassifier<NGramProcessLM> classifier;
    public static final Logger logger = Logger.getLogger(SubjectivityBasic.class.getName());

    SubjectivityBasic(String subjectivityDirPath) {
        polarityDir = new File(subjectivityDirPath);
        logger.log(Level.INFO, "\nData Directory={0}", polarityDir);
        categories = Arrays.asList(categoryStrings);
        int nGram = 8;
        classifier = DynamicLMClassifier.createNGramProcess(categoryStrings, nGram);
    }

    void run() throws ClassNotFoundException, IOException {
        train();
        evaluate();
    }

    void train() throws IOException {
        int numTrainingChars = 0;
        logger.log(Level.INFO, "\nTraining.");
        for (String category : categories) {
            Classification classification = new Classification(category);
            File file = new File(polarityDir, category + ".tok.gt9.5000");
            String data = Files.readFromFile(file,"ISO-8859-1");
            String[] sentences = data.split("\n");
            logger.log(Level.INFO, "# Sentences {0}={1}", new Object[]{category, sentences.length});
            
            int numTraining = (sentences.length * 9) / 10;
            for (int j = 0; j < numTraining; ++j) {
                String sentence = sentences[j];
                numTrainingChars += sentence.length();
                Classified<CharSequence> classified = new Classified<CharSequence>(sentence, classification);
                classifier.handle(classified);
            }
        }
        
        logger.log(Level.INFO, "\nCompiling.\n  Model file=subjectivity.model");
        FileOutputStream fileOut = new FileOutputStream("subjectivity.model");
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        classifier.compileTo(objOut);
        objOut.close();

        logger.log(Level.INFO, "  # Training Cases={0}", 9000);
        logger.log(Level.INFO, "  # Training Chars={0}", numTrainingChars);
    }

    void evaluate() throws IOException {
        // classifier hasn't been compiled, so it'll be slower
        boolean storeInputs = false;
        JointClassifierEvaluator<CharSequence> evaluator
            = new JointClassifierEvaluator<CharSequence>(classifier, categoryStrings, storeInputs);
        logger.log(Level.INFO, "\nEvaluating.");
        for (String category : categories) {
            Classification classification = new Classification(category);
            File file = new File(polarityDir, category + ".tok.gt9.5000");
            String data = Files.readFromFile(file,"ISO-8859-1");
            
            String[] sentences = data.split("\n");
            int numTraining = (sentences.length * 9) / 10;
            for (int j = numTraining; j < sentences.length; ++j) {
                Classified<CharSequence> classified
                    = new Classified<CharSequence>(sentences[j], classification);
                evaluator.handle(classified);
            }
        }
        logger.log(Level.INFO, "\n{0}", evaluator.toString());
    }
    
    public String evaluate(String string) {
        Classification classification = classifier.classify(string);
        return classification.bestCategory();
    }

    public static void main(String[] args) {
        SubjectivityBasic subjectivityBasic = new SubjectivityBasic("./data/movies/rotten_imdb");
        try {
//            subjectivityBasic.run();
            subjectivityBasic.train();
            logger.log(Level.INFO, subjectivityBasic.evaluate("caesar is a transformed monkey who plots his rise to poser."));
        } catch (Throwable t) {
            logger.log(Level.SEVERE, t.getLocalizedMessage(), t);
        }
    }

}

