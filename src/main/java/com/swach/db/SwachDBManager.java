package com.swach.db;

import com.swach.dto.TwitterData;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swachtest on 29/04/15.
 */
public class SwachDBManager  {
    public static void insertIntoDatabase(TwitterData twitterData)
            throws IOException {
        Connection connection = null;
        try {
            connection = getConnection();

            Statement stmt = connection.createStatement();
            int res = stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tweets (tweetid text, tweettext text,username text,location text,latitude decimal , longitude decimal,dirtymeter integer  )");
            if(res == 0 ) System.out.println(" Table Exists");
            stmt.close();
            System.out.println(" Inserting value"+twitterData);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO tweets(tweetid,tweettext,username,location,latitude,longitude,dirtymeter) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, twitterData.getTweetId());
            ps.setString(2, twitterData.getTweetText());
            ps.setString(3, twitterData.getUsername());
            ps.setString(4, twitterData.getLocation());
            ps.setDouble(5, twitterData.getLatitude());
            ps.setDouble(6, twitterData.getLongitude());
            ps.setInt(7, twitterData.getDirtynessMeter());
            ps.executeUpdate();
            ps.close();
            //stmt.executeUpdate("INSERT INTO tweets VALUES ('\"+tweetId+\"','\"+tweetText+\"','\"+username+\"','\"+location+\"')\")");

            //s.executeUpdate("INSERT INTO  tweets  VALUE ('"+tweetId+"','"+tweetText+"','"+username+"','"+location+"')")
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) try{connection.close();} catch(SQLException e){}
        }
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        int port = dbUri.getPort();

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static List <TwitterData> getTweetsData()
            throws Exception {
        List<TwitterData> tweetList = new ArrayList<TwitterData>();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            connection = getConnection();

             stmt = connection.createStatement();



             rs = stmt.executeQuery("SELECT tweetid,tweettext,username, location, latitude, longitude, dirtymeter  FROM tweets");



            while (rs.next()) {

                TwitterData twitterData = new TwitterData();
                twitterData.setUsername(rs.getString("username"));
                twitterData.setTweetId(rs.getString("tweetid"));
                twitterData.setTweetText(rs.getString("tweettext"));
                twitterData.setLatitude(rs.getDouble("latitude"));
                twitterData.setLongitude(rs.getDouble("longitude"));
                 String strLoc = rs.getString("location");
                if(strLoc != null && strLoc.contains(",")) {
                    strLoc = strLoc.replace(",", " ");
                }

                twitterData.setLocation(strLoc);
                twitterData.setDirtynessMeter(rs.getInt("dirtymeter"));

                tweetList.add(twitterData);

              //  out += "Read from tweets DB: " + rs.getString("tweettext") +"  Location: "+rs.getString("location") + "  username: "+rs.getString("username") +"\n";

            }

            return tweetList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            rs.close();
            stmt.close();


            if (connection != null) try{connection.close();} catch(SQLException e){}
        }
    }
}
