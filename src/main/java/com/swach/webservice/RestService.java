package com.swach.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.swach.db.SwachDBManager;
import com.swach.dto.TwitterData;

import com.google.gson.Gson;

/**
 * Created by krraje on 01/05/15.
 */
@Path("/twitter")
public class RestService {



        @GET
        @Path("/gettweet")
        @Produces("application/json")
        public String getTweetData(@QueryParam("param") String msg)
        {
            String tweets = null;
            try
            {
                List<TwitterData> tweetData = SwachDBManager.getTweetsData();

                Gson gson = new Gson();
                tweets = gson.toJson(tweetData);
                System.out.println("tweets from rest API  "+tweets);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Exception Error"); //Console
            }
            return tweets;
        }


}
