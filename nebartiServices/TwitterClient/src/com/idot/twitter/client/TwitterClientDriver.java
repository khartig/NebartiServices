/*
 * 
 */
package com.idot.twitter.client;

import com.idot.twitter.textprocessing.TweetPreprocessor;
import com.idot.twitter.textprocessing.CleanseText;
import com.idot.classifiers.PolarityHierarchical;
import com.idot.geocoding.Geocoder;
import com.idot.sentiment.controllers.SearchStringJpaController;
import com.idot.sentiment.controllers.TweetJpaController;
import com.idot.sentiment.entities.SearchString;
import com.idot.sentiment.entities.Tweet;
import com.idot.twitter.FilterParameterFetcher;
import com.idot.twitter.TwitterClient;
import com.idot.twitter.UndelimitedTwitterStreamProcessor;
import com.idot.utilities.Properties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

;

class TwitterClientDriver {

    EntityManagerFactory emf;
    EntityManager em;
    public PolarityHierarchical polarityHierarcical;
    public static final Logger logger = Logger.getLogger(TwitterClientDriver.class.getName());

    public TwitterClientDriver() {
        emf = Persistence.createEntityManagerFactory("CreateDBPU");
        em = emf.createEntityManager();
    }

    public PolarityHierarchical getPolarityHierarchical() {
        try {
            if (polarityHierarcical == null) {
                polarityHierarcical = new PolarityHierarchical();
            }
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return polarityHierarcical;
    }

    public void startClient(
            Collection<String> trackKeywords,
            FilterParameterFetcher filterParameterFetcher,
            Collection<String> credentials) {

        SearchString searchString = null; // there can be only one per process right now
        SearchStringJpaController searchStringController = new SearchStringJpaController(em.getTransaction(), emf);
        String stringToSearch = trackKeywords.iterator().next().toLowerCase();
        
        // look for an existing search string record
        List<SearchString> searchStrings = searchStringController.findSearchStringEntities();
        for (SearchString ss : searchStrings) {
            if (ss.getSearchString().matches(stringToSearch)) {
                searchString = ss;
                continue;
            }
        }
        
        // create new search string record if existing one wasn't found
        if (searchString == null) {
            searchString = new SearchString();
            searchString.setSearchString(stringToSearch);
            searchStringController.create(searchString);
        }
        
        StorageTwitterStreamProcessor storageTwitterStreamProcessor = new StorageTwitterStreamProcessor(stringToSearch);
        storageTwitterStreamProcessor.setClassifier(polarityHierarcical);

        new TwitterClient(
                filterParameterFetcher,
                storageTwitterStreamProcessor,
                "https://stream.twitter.com/1/statuses/filter.json",
                200,
                10,
                credentials,
                60 * 1000L).execute();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println(
                    "Usage: TwitterClientDriver username:password ... [-f twitter_id ...] [-t keyword]");
            System.exit(1);
        }

        Collection<String> credentials = new ArrayList<String>();
        Collection<String> followIds = null;
        Collection<String> trackKeywords = null;

        Collection<String> list = credentials;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-f")) {
                followIds = new ArrayList<String>();
                list = followIds;
            } else if (arg.equals("-t")) {
                trackKeywords = new ArrayList<String>();
                list = trackKeywords;
            } else {
                list.add(arg);
            }
        }

        final Collection<String> finalFollowIds = followIds;
        final Collection<String> finalTrackKeywords = trackKeywords;

        FilterParameterFetcher filterParameterFetcher =
                new FilterParameterFetcher() {

                    @Override
                    public Collection<String> getFollowIds() {
                        return finalFollowIds;
                    }

                    @Override
                    public Collection<String> getTrackKeywords() {
                        return finalTrackKeywords;
                    }
                };

        TwitterClientDriver driver = new TwitterClientDriver();
        PolarityHierarchical classifier = driver.getPolarityHierarchical();

        driver.startClient(trackKeywords, filterParameterFetcher, credentials);

    }

    /**
     * TwitterStreamProcessor that uses org.json.* to process the
     * stream and just prints out each tweet.  This isn't an endorsement
     * of any techniques, just an example.  In real life the tweet would
     * likely be put into some kind of queue system.
     */
    private class StorageTwitterStreamProcessor extends UndelimitedTwitterStreamProcessor {

        SearchString searchString;
        SearchStringJpaController searchStringController = new SearchStringJpaController(em.getTransaction(), emf);
        TweetJpaController tweetController = new TweetJpaController(em.getTransaction(), emf);
        PolarityHierarchical classifier;

        public StorageTwitterStreamProcessor(String stringSearched) {
            List<SearchString> searchStrings = searchStringController.findSearchStringEntities();
            for (SearchString string : searchStrings) {
                if (string.getSearchString().matches(stringSearched)) {
                    searchString = string;
                }
            }
        }

        public void setClassifier(PolarityHierarchical classifier) {
            this.classifier = classifier;
        }

        @Override
        public void processTwitterStream(InputStream is, String credentials, Set<String> ids)
                throws InterruptedException, IOException {

            JSONTokener jsonTokener = new JSONTokener(new InputStreamReader(is, "UTF-8"));
            while (true) {
                try {
                    saveTweetData(jsonTokener);
                } catch (JSONException ex) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    logger.info(sb.toString());

                    throw new IOException(
                            "Got JSONException while processing Twitter stream: " + ex.getMessage());
                }
            }
        }

        private void saveTweetData(JSONTokener jsonTokener) throws JSONException {
            JSONObject jsonObject = new JSONObject(jsonTokener);

//            logger.info("Got Tweet = " + jsonObject.toString(4));

            // Cleanse the tweet string of control chars.
            String tweetString = CleanseText.cleanse(jsonObject.getString("text"));

            Tweet tweet = new Tweet();
            tweet.setTweet(tweetString);

            String geoString = getGeoPositionString(jsonObject);
            tweet.setGeolocation(geoString);

            // change text to more readable form and remove unnecessary content
            String processedTweet = TweetPreprocessor.prepare(tweetString);

            // ToDo: open this once and update as needed
            //List<Tweet> tweets = tweetController.findTweetEntities();
            SearchString ss = searchStringController.findSearchString(searchString.getId());
            Set<Tweet> tweets = ss.getTweets();

            String sentiment = classifier.evaluate(tweetString).toString();
            tweet.setSentiment(sentiment);
            tweet.setTweetedAt(getDate(jsonObject.getString("created_at")));

            logger.log(Level.INFO, tweet.toString());
//            tweets.add(tweet);

//            Set<Tweet> tweets = new HashSet<Tweet>();
            tweets.add(tweet);

            //searchString.setTweets(new HashSet<Tweet>(tweets));
//            searchString.setTweets(tweets);

            try {
                searchStringController.edit(ss);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            saveClassifiedTweetForEvaluation(sentiment, processedTweet);
        }

        /**
         * Write categorized tweet to file for evaluation and verification. 
         */
        private void saveClassifiedTweetForEvaluation(String sentiment, String processedTweet) {
            String filename = searchString.getSearchString().replace(" ", "_");

            // rather not open this file every time.
            try {
                BufferedWriter tweetFile = new BufferedWriter(new FileWriter(Properties.getProperty("classified.data.dir") + "/" + filename + ".txt", true));
                tweetFile.write(sentiment + "\t" + processedTweet + "\n");
                tweetFile.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        private String getGeoPositionString(JSONObject jsonObject) {
            String geoString = "0.0, 0.0";
            JSONObject geoParameters;

            try {
                logger.log(Level.INFO, jsonObject.toString(4));
                geoParameters = jsonObject.getJSONObject("geo");
                logger.log(Level.INFO, "\nGEO String = {0}\n", geoParameters.toString());

                geoString = geoParameters.getJSONArray("coordinates").toString().replace("[", "").replace("]", "");
                logger.log(Level.INFO, "\nParsed GEO String = {0}\n", geoString);
            } catch (JSONException e) {
                // geo was probably null try getting the user location
                JSONObject userLocation;
                try {
                    userLocation = jsonObject.getJSONObject("user");
                    String userLocationString = userLocation.getString("location");
                    logger.log(Level.INFO, "\nParsed LOCATION String = {0}\n", userLocationString);

                    if (!"".equals(userLocationString)) {
                        geoString = Geocoder.getLatLon(userLocationString);
                    }
                } catch (JSONException ex) {
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                }

            }

            return geoString;
        }

        private Date getDate(String dateString) {
            Date tweetDate = Calendar.getInstance().getTime();
            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
            try {
                tweetDate = df.parse(dateString);
            } catch (ParseException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            return tweetDate;
        }
    }
}