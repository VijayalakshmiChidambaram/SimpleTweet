package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    //Toast are not the better way for error handling so can use snackbar instead

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 140;
    TwitterClient client;

    EditText etCompose;
    Button btnTweet;
    TextView count;
    private static final int TWEET_MAX_LENGTH = 280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        count = findViewById(R.id.count);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int counts) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int counts,
                                          int after) {
                // Fires right before text is changing
                int textLength = s.length();
                String lengthAsStr = String.valueOf(textLength);
                count.setText(lengthAsStr+" characters of "+String.valueOf(TWEET_MAX_LENGTH));
                if (textLength>TWEET_MAX_LENGTH){
                    count.setTextColor(Color.RED);
                }
                else{
                    count.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed

            }
        });

        //Set up an on Click Listener
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                //Error handling introduce - if the input is empty or too long
                if (tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Sorry, you tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Sorry, you tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                //When button is clicked , Make an API call to Twitter to publish the tweet

                client.publistTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG,"On success Tweet");
                        try {
                            Tweet tweet = Tweet.fromJSON(json.jsonObject);
                            Log.i(TAG,"Published Tweet says:" + tweet.body);
                            //Prepare data Intent
                            Intent intent = new Intent();
                            //Pass relevant data back as a result
                            intent.putExtra("tweet", Parcels.wrap(tweet));

                            //Activity finished OK, return the result
                            //Set Result Code and bundle data for response
                            setResult(RESULT_OK,intent);
                            //Closes the activity, Pass data to Parent
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "On failure to publish tweet", throwable);
                    }
                });
            }



        });

    }
}