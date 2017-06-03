package com.frainz.easytwt;
import com.google.gson.JsonElement;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.Call;

class MyTwitterApiClient extends TwitterApiClient {
    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    /**
     * Provide CustomService with defined endpoints
     */
    public CustomService getCustomService() {
        return getService(CustomService.class);
    }
}

// example users/show service endpoint
interface CustomService {

    @GET("https://api.twitter.com/1.1/users/show.json?screen_name=twitterdev")
    Call<Tweet> show(@Query("user_id") long id);

    @POST("/1.1/lists/create.json")
    void createList(@Query("name") String name, @Query("mode") String mode, Callback<JsonElement> cb);
}