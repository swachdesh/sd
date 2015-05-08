/**
 * Created by swachtest on 20/04/15.
 */

package com.swach.listener;

import com.swach.db.SwachDBManager;
import com.swach.dto.TwitterData;
import com.swach.dto.LatitudeLongitude;

import com.swach.utils.LocationUtils;
import com.swach.validator.TwitterHashTagValidator;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.HashSet;

public class TwitterSwachListener {


    private TwitterStream twitterStream = null;
   // private Twitter twitter = null;

    public TwitterSwachListener() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(System.getenv("OAuthConsumerKey"))
                .setOAuthConsumerSecret(System.getenv("OAuthConsumerSecret"))
                .setOAuthAccessToken(System.getenv("OAuthAccessToken"))
                .setOAuthAccessTokenSecret(System.getenv("OAuthAccessTokenSecret"));
        Configuration conf = cb.build();
        System.out.println("Creating Twitter Stream ");
        /*if (this.twitter == null)
         this.twitter = new TwitterFactory(conf).getInstance();
*/

        //TwitterStream twitterStream = new TwitterStreamFactory().getSingleton();
        if(this.twitterStream  == null ){

            this.twitterStream = new TwitterStreamFactory(conf).getInstance();
            //  twitterStream.
        }
    }


  /*  public static void main(String[] args) {
         new TwitterSwachListener().main();
    }*/


    public void start()  {
        System.out.println("Entering main method");


        StatusListener listener = new StatusListener() {

            @Override
            public void onException(Exception arg0) {
                arg0.printStackTrace();

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {
                System.out.println("Deletion occured "+arg0.getStatusId());

            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                System.out.println(arg0);
                System.out.println(arg1);

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {
                System.out.println("stallWarning  "+stallWarning.getMessage());

            }

            @Override
            public void onStatus(Status status) {
                System.out.print("On Status  method Entered");
                User user = status.getUser();

                // gets Username
                String username = status.getUser().getScreenName();

                // user location
                String profileLocation = user.getLocation();

                System.out.println(username);
                System.out.println(profileLocation);

                // place
                Place place = status.getPlace();
                if(place != null ) {
                    String placeCountry = place.getCountry();
                    String placeStreetAddress = place.getStreetAddress();
                    String placeName = place.getName();
                    String placeId = place.getId();
                    String placeFullName = place.getFullName();
                    String placeUrl = place.getURL();
                    String placeCountryCode = place.getCountryCode();
                    System.out.println("Place_Country: " + placeCountry);
                    System.out.println("Place_Name: " + placeName);
                    System.out.println("Place_FullName: " + placeFullName);
                    System.out.println("Place_ID: " + placeId);
                    System.out.println("Place_StreetAddress: " + placeStreetAddress);
                    System.out.println("Place_URL: " + placeUrl);
                    System.out.println("Place_CountryId: " + placeCountryCode);

                }
                // tweet id
                long tweetId = status.getId();
                // tweet content

                String content = status.getText();


                // get tweet hash tags
                HashtagEntity hashTags[] = status.getHashtagEntities();
                // geo-location
                GeoLocation geoLocation = status.getGeoLocation();

                System.out.println("geoLocation  "+geoLocation);
               /*  double latitude = geoLocation.getLatitude();
                double longitude = geoLocation.getLongitude();*/
                 /*   String userLocation = null;
                    try {
                        userLocation = getUserLocation(String.valueOf(latitude), String.valueOf(longitude));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                System.out.println("UserName: " + username);
                System.out.println("profileLocation: " + profileLocation);
                System.out.println("TweetId: " + tweetId);
                System.out.println("Tweet: " + content);
                HashSet<String> hashTagContainer = new HashSet<String>();
                for (HashtagEntity hashTag : hashTags) {
                    if(getSwachTags().toLowerCase().contains(hashTag.getText().toLowerCase()))
                      hashTagContainer.add(hashTag.getText().toLowerCase());
                }
                System.out.println("HashTags: " + hashTagContainer);


                TwitterHashTagValidator twitterHashTagFormatValidator = new TwitterHashTagValidator(status, content,hashTagContainer,username).validate();
                System.out.println("twitterHashTagFormatValidator.is()    "+twitterHashTagFormatValidator.is());
                if (!twitterHashTagFormatValidator.is()) {
                    System.out.println("insertIntoDatabase: ");
                    try {
                        LatitudeLongitude latlong =  LocationUtils.getLatLong(twitterHashTagFormatValidator.getPlace(), twitterHashTagFormatValidator.getLocation());


                        TwitterData twitterData = new TwitterData();
                        twitterData.setTweetId(Long.toString(tweetId));
                        twitterData.setTweetText(content);
                        twitterData.setUsername(username);
                        twitterData.setLocation( latlong.getLocation());
                        twitterData.setLatitude((geoLocation == null) ? latlong.getLatitude() : geoLocation.getLatitude());
                        twitterData.setLongitude((geoLocation == null) ? latlong.getLongitude() : geoLocation.getLongitude());
                        twitterData.setDirtynessMeter(Integer.parseInt(twitterHashTagFormatValidator.getDirtinessMeter()));
                        SwachDBManager.insertIntoDatabase(twitterData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onTrackLimitationNotice(int arg0) {
                // TODO Auto-generated method stub

            }

        };
        FilterQuery fq = new FilterQuery();
         String keywords[] = getSwachTags().split(",");

        // {"#sbharatdesh","#swachdesh"};

        fq.track(keywords);

        this.twitterStream.addListener(listener);
        this.twitterStream.filter(fq);
    }

    public void stop() {
        this.twitterStream.cleanUp();
        this.twitterStream.shutdown();
    }


     private String getSwachTags(){
        return System.getenv("swachtags");
     }
}