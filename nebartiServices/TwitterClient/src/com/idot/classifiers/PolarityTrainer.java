/*
 * 
 */
package com.idot.classifiers;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author khartig
 */
public class PolarityTrainer {

    File polarityDir;
    File modelFile;
    String[] categories;
    DynamicLMClassifier<NGramProcessLM> polarityClassifier;
    public static final Logger logger = Logger.getLogger(PolarityTrainer.class.getName());

    public PolarityTrainer(String rootDirPath) {
        polarityDir = new File(rootDirPath + "/data/movies/review_polarity/txt_sentoken");
        modelFile = new File(rootDirPath + "/models/polarity.model");

        categories = polarityDir.list();
        int nGram = 8;
        polarityClassifier = DynamicLMClassifier.createNGramProcess(categories, nGram);
    }

    public void train() throws IOException {
        int numTrainingCases = 0;
        int numTrainingChars = 0;
        logger.log(Level.INFO, "\nTraining polarity data....");
        for (int i = 0; i < categories.length; ++i) {
            String category = categories[i];
            Classification classification = new Classification(category);
            File file = new File(polarityDir, categories[i]);
            File[] trainFiles = file.listFiles();
            
            for (int j = 0; j < trainFiles.length; ++j) {
                File trainFile = trainFiles[j];
                ++numTrainingCases;
                String review = Files.readFromFile(trainFile, "ISO-8859-1");
                numTrainingChars += review.length();
                Classified<CharSequence> classified = new Classified<CharSequence>(review, classification);
                polarityClassifier.handle(classified);
            }
        }

        logger.log(Level.INFO, "  # Training Cases={0}", numTrainingCases);
        logger.log(Level.INFO, "  # Training Chars={0}", numTrainingChars);

        // if you want to write the polarity model out for future use, 
        // uncomment the following line
        FileOutputStream fileOut = new FileOutputStream(modelFile);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        polarityClassifier.compileTo(objOut);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PolarityTrainer trainer = new PolarityTrainer("/Users/khartig/projects/Idot/TwitterClient");
        try {
            trainer.train();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
}
