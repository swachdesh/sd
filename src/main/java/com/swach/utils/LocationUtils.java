package com.swach.utils;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.swach.dto.LatitudeLongitude;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Created by swachtest on 05/05/15.
 */
public class LocationUtils {

    public static LatitudeLongitude getLatLong(String place, String locationStr){

        String apiKey=System.getenv("GOOG_ACCESS_KEY");
        GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
        GeocodingResult[] results = new GeocodingResult[0];

        try {
            results = GeocodingApi.geocode(context,
                    place + " " + locationStr).await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LatitudeLongitude latLong = null;
        if(results != null && results.length > 0){
            System.out.println(results[0].geometry.location.toString());
            System.out.println(results[0].formattedAddress);
            latLong = new LatitudeLongitude(results[0].geometry.location.lat,results[0].geometry.location.lng,results[0].formattedAddress);

        }
        return latLong;
    }
}
