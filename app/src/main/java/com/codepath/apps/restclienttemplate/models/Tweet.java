package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    //JSON onject convert into Java
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        // Build the tweet
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        //Twitter user should be Java user model, but what we get below is JSON Object, So we create static method User which takes JSON and will return user model
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }

    //Returns a list of tweets from JSON Array
    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        //for loop - Till the length of JSON Array add tweets
        for(int i =0; i<jsonArray.length(); i++) {
            tweets.add(fromJSON(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
