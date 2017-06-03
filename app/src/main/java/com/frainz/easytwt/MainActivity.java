package com.frainz.easytwt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

public class MainActivity extends AppCompatActivity {
    private TwitterLoginButton loginButton;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        sharedPreferences = this.getSharedPreferences("com.frainz.easytwt",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("logged",false)){
            Intent feedActivityIntent = new Intent(MainActivity.this,FeedActivity.class);
            startActivity(feedActivityIntent);
            finish();
        }
        setContentView(R.layout.activity_main);

        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();

                String token = authToken.token;
                String secret = authToken.secret;
                sharedPreferences.edit().putString("app_token",token).apply();
                sharedPreferences.edit().putString("app_secret",secret).apply();
                sharedPreferences.edit().putString("user_id",Long.toString(session.getUserId())).apply();
                sharedPreferences.edit().putString("username",session.getUserName()).apply();
                sharedPreferences.edit().putBoolean("logged",true).apply();

                Intent feedActivityIntent = new Intent(MainActivity.this,FeedActivity.class);
                startActivity(feedActivityIntent);

            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
