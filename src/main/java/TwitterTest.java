import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.swach.utils.NewCityEnums;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Arrays;

/**
 * Created by swachtest on 19/04/15.
 */
public class TwitterTest {


    public static boolean contains(String cityName) {

        for (NewCityEnums c : NewCityEnums.values()) {
            if (c.name().equals(cityName)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) throws TwitterException {


        System.out.println(EnumUtils.isValidEnum(NewCityEnums.class, "bangaloe"));
        System.out.println(EnumUtils.getEnum(NewCityEnums.class,"bangalore").getNewNames());

       // System.out.println.(Arrays.asList("#cleanindia,#swachbharat,#swachdesh,#swachbangalore,#swachblr".split(",")));
/*

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey("qRGlDoLkUxzdiX8upB5AGLUA0")
                .setOAuthConsumerSecret("qMfOzp6lNVjjZL7DQnkmhL4aK40WYWt69PxHhrWFYKai9zKcPG")
                .setOAuthAccessToken("3164076691-RQSOBNdFH7Y9X6ntPAPhhB5vA3QafJwl8B5GDPr")
                .setOAuthAccessTokenSecret("D3TlTuyxW1PegtqkrUu3yV8DDp0Rbcdo423HeiCVjzDDz");
        TwitterFactory tf = new TwitterFactory(cb.build());
       Twitter twitter = tf.getInstance();

        Query query = new Query("#swachdesh");
        QueryResult result = twitter.search(query);
        for (Status status : result.getTweets()) {
            System.out.println("@" + status.getUser().getScreenName() + " : " + status.getText() + " : " + status.getGeoLocation());
        }
*/


        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCwc_len-PRWjzBTyO-PUJBFRW7PPdNJoE");
        GeocodingResult[] results = new GeocodingResult[0];
        try {
            results = GeocodingApi.geocode(context,
                    "vijaynagar bangalore").await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(results.length);
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].formattedAddress);
            System.out.println(results[i].geometry.location.toString());
            System.out.println(results[i].geometry.location.toString());


        }

/*
        String filterToken = "#swachdesh";
        String content="#swachdesh jhjh     abc      123 j hj   hj h";
        if(content != null && !"".equalsIgnoreCase(content) && content.contains(filterToken))
        {
            System.out.println(""+content.indexOf("#swachdesh"));
            System.out.println(""+content.length());
            System.out.println("-----"+content.substring(filterToken.length()+1,content.length()));
            content = content.trim().replaceAll(" +", " ");
            System.out.println(content);
            int count = StringUtils.countMatches(content, " ");
            System.out.println(count);
          //  String[] sp = content.split(filterToken);
            String[] sp =   StringUtils.split(content, ' ');

            for (int i = 0; i < sp.length; i++) {
                System.out.println(sp[i]);
            }

            int dirtynessmeter = 0 ;
            if(dirtynessmeter > 5  ||  dirtynessmeter < 1 ){
                System.out.println("Incorrect dirtynessmeter  "+dirtynessmeter);
               // errorOccured = true;
            }

            // make http call.

                *//*    try {
                        String resp = sendGet(sp[1]);
                        StatusUpdate stat = new StatusUpdate(resp);

                        stat.setInReplyToStatusId(status.getId());
                        twitter.updateStatus(stat);
                    }
*//*




        }*/
    }


}
