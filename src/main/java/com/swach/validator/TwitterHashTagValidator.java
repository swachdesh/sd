package com.swach.validator;


import com.swach.dto.LatitudeLongitude;
import com.swach.utils.LocationUtils;
import com.swach.utils.NewCityEnums;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Arrays;
import java.util.Set;


/**
 * Created by swachtest on 26/04/15.
 */
public  class TwitterHashTagValidator {
    private boolean myResult;
    private Status status;
    private String content;
    private  Set<String> hashTags;
    private Twitter twitter;
    private  String username;
    private String[] splitContent;
    private static final   String errorResponse="Correct Syntax is <HASHTAG> <dirtyness-meter(1-5)> <city> <Location1> <location2>";

    public TwitterHashTagValidator(Status status, String content, Set<String> hashTags,String username) {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(System.getenv("OAuthConsumerKey"))
                .setOAuthConsumerSecret(System.getenv("OAuthConsumerSecret"))
                .setOAuthAccessToken(System.getenv("OAuthAccessToken"))
                .setOAuthAccessTokenSecret(System.getenv("OAuthAccessTokenSecret"));
        Configuration conf = cb.build();
        System.out.println("Creating Twitter Stream ");
        if (this.twitter == null)
            this.twitter = new TwitterFactory(conf).getInstance();

        this.status = status;
        this.content = content;
        this.hashTags = hashTags;
        this.username = username;

    }

    public boolean is() {
        System.out.println("myResult  "+myResult);
        return myResult;
    }

    public String getLocation() {  return splitContent[3]+""+splitContent[4]; }
    public String getPlace() {
        return splitContent[2];
    }
    public String getDirtinessMeter() {
        return splitContent[1];
    }
    public String getFilterQuery() {
        return splitContent[0];
    }

    public TwitterHashTagValidator validate() {

        myResult = false;
        String swachTag = null;
        // check no hashtags if more than 1 return.. no need to check content format
        if(hashTags == null || hashTags.size() > 1){
            String resp = "Only one hashtag permitted Correct Syntax is <HASHTAG> <city> <Location> <dirtiness-meter(1-5)>";
            sendErrorTweet(resp);
            return this;

        }else
        {
             swachTag =  hashTags.toArray()[0].toString();
             swachTag = (swachTag != null || swachTag.equalsIgnoreCase("")) ? "#"+swachTag :swachTag;
        }

        System.out.println("content  is "+content);
        if(content != null && !"".equalsIgnoreCase(content) &&
                 hashTags != null &&  content.toLowerCase().contains(swachTag.toLowerCase()))
        {

            content = content.trim().replaceAll(" +", " "); // Remove the extra spaces if any keep only single spaces


            if(StringUtils.countMatches(content," ") != 4) {// check if spaces are  #hashtag 1 mumbai jp  nagar

                // if spaces are more or less than 3 throw error
                String resp = "Incorrect Format."+errorResponse;
                sendErrorTweet(resp);
            } else {


                splitContent =   StringUtils.split(content, ' ');
                System.out.println( "splitContent "+ Arrays.asList(splitContent) );
                if(splitContent[1].startsWith("#") ||splitContent[2].startsWith("#") || splitContent[3].startsWith("#") || splitContent[4].startsWith("#"))
                {
                    String resp = "Incorrect Format"+errorResponse;;
                    sendErrorTweet(resp);
                }
                // check hashtag should be the first word
               else  if(!splitContent[0].equalsIgnoreCase(swachTag))
                {
                    String response = "Incorrect hashtag value."+errorResponse;
                    sendErrorTweet(response);
                }else if(isValidDirtyMeterValue()) {// check for dirtness range and value
                    String response = "Provided Dirty meter value "+splitContent[1] +" not in range valid range is from 1-5.";//sendGet(sp[1]);
                    sendErrorTweet(response);
                }
                 else if(!validateCity(splitContent[2],splitContent[3],splitContent[4])){
                        String response = "Unable to find city "+splitContent[2]+". Please provide a valid city" ;//sendGet(sp[1]);
                        sendErrorTweet(response);

                    }
                    // unable to find location as a user cn specify for example btm2ndstage and rhe value it returns will be btm 2nd stage
                    // which cannot be checked so leaving as of now.

                    /*else  if(validateLocation(splitContent[2])){
                    String response = "Incorrect Format .Dirty meter valid range 1-5. Correct Syntax is: "+filterToken+" <city> <Location> <dirtiness-meter(1-5)>";//sendGet(sp[1]);
                    sendErrorTweet(response);
                }
*/

            }
        }else {
            String resp = "Incorrect Format ."+errorResponse;
            sendErrorTweet(resp);
        }

        return this;
    }

    private boolean isValidDirtyMeterValue() {
        boolean errorOccured = false;
        try {
            System.out.println("splitContent[1]"+splitContent[1]);
           int dirtynessmeter = Integer.parseInt(splitContent[1]);
            System.out.println("dirtynessmeter  "+dirtynessmeter);

            if(dirtynessmeter > 5  ||  dirtynessmeter < 1 ){
                System.out.println("Incorrect dirtynessmeter  "+dirtynessmeter);
                errorOccured = true;
            }

        }catch (NumberFormatException ex){

            System.out.println("NumberFormatException   "+ex.getMessage());
            errorOccured = true;

        }
        return errorOccured;
    }


    private boolean validateCity(String city,String location1,String location2){
        boolean cityFound = false;

        if(EnumUtils.isValidEnum(NewCityEnums.class, city.toLowerCase())){
            city = EnumUtils.getEnum(NewCityEnums.class,city.toLowerCase()).getNewNames();
        }
        LatitudeLongitude latlong = LocationUtils.getLatLong(city,location1+" "+location2);
        if(latlong != null ) {
            System.out.println("latlong  " + latlong);
            System.out.println("latlong lowercase " + latlong.getLocation().toLowerCase());
            System.out.println("latlong lowercase " + city.toLowerCase());
        }

        cityFound =  latlong != null &&  latlong.getLocation().toLowerCase().contains(city.toLowerCase());
        return cityFound;
    }

    private void sendErrorTweet(String response) {

        //StatusUpdate stat = new StatusUpdate(response);
       // stat.setInReplyToStatusId(status.getId());
      //  stat.inReplyToStatusId(status.getId());
        System.out.println("sendErrorTweet   @" + status.getUser().getScreenName()
                +" "+ response);

        try {
           // this.twitter.updateStatus(stat);
            RandomStringUtils.randomAlphanumeric(6);
            twitter.updateStatus("@" + status.getUser().getScreenName()
                    +" ["+RandomStringUtils.randomAlphanumeric(6)+"]"+ response);
            //DirectMessage message = twitter.sendDirectMessage(this.username, response);
            myResult = true;

        } catch (TwitterException e) {
            e.printStackTrace();
        }

    }
}
