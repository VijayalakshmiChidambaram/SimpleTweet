package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {

    //Query tweet items in the Database
    @Query("SELECT Tweet.body AS tweet_body, Tweet.createdAt AS tweet_createdAt, Tweet.id AS tweet_id, User.* " +
            "FROM Tweet INNER JOIN User ON Tweet.userId = User.id ORDER BY Tweet.createdAt DESC LIMIT 5")
    //This will return SQL Query on Tweet Table, which is Auto generated (Have used @Entity)
    List<TweetWithUser> recentItems();

    //Insert item once network is back
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //...represents Variable number of arguments- any number of tweets as an array
    void insertModel(Tweet... tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);
}
