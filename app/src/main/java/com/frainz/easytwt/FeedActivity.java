package com.frainz.easytwt;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce = false;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        startService(new Intent(getBaseContext(),TwtrService.class));

        sharedPreferences = getSharedPreferences("com.frainz.easytwt",MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        View header = navigationView.getHeaderView(0);
//        TextView text_name = (TextView) header.findViewById(R.id.nav_name);
//        TextView text_username = (TextView) header.findViewById(R.id.nav_username);
//        String username = sharedPreferences.getString("username", "irshadpi");
//        text_username.setText("@"+username);
//        text_name.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
        loadFeed(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        View view = findViewById(R.id.content_frame);
        Snackbar.make(view, "Loading Tweets", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        if (id == R.id.nav_camera) {
            loadFeed(0);

        } else if (id == R.id.nav_gallery) {
            loadFeed(1);
        } else if (id == R.id.nav_slideshow) {
            final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                    .getActiveSession();
            final Intent intent = new ComposerActivity.Builder(FeedActivity.this)
                    .session(session)
                    .text("Enter your tweet")
                    .hashtags("#frainz")
                    .createIntent();
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Snackbar.make(view, "Searching..", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            //searchFeed("hello");
            editHost();
        } else if (id == R.id.nav_share) {
            Snackbar.make(view, "TODO share", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (id == R.id.nav_send) {
            Snackbar.make(view, "TODO Send", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }


        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void  loadFeed(int type) {
        String username = sharedPreferences.getString("username", "irshadshalu");
        String s_user_id = sharedPreferences.getString("user_id", "917206495");
        Long user_id = Long.parseLong(s_user_id);
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();

        Call<List<Tweet>> call;
        if(type == 0)
            call = statusesService.homeTimeline(20, null, null, null, null, null, null);
        else
            call = statusesService.userTimeline(user_id, username, 20, null, null, null, null,null, null);
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                //Do something with result

                List<Tweet> tweetlist = (List<Tweet>) result.response.body();
                ArrayList<String> tweets = new ArrayList<String>();
                ArrayList<String> users = new ArrayList<String>();
                ArrayList<String> favourites = new ArrayList<String>();
                ArrayList<String> retweetcounts= new ArrayList<String>();

                for (Tweet i : tweetlist) {
                    Log.wtf("rrr", i.text + " " + i.user + " " + i.retweetCount + "\n");
                    User user = i.user;
                    tweets.add(i.text);
                    users.add(user.screenName);
                    favourites.add(Integer.toString(i.favoriteCount));
                    retweetcounts.add(Integer.toString(i.retweetCount));

                }

                ArrayList<ArrayList<String>> group = new ArrayList<>();
                group.add(tweets);
                group.add(users);
                group.add(favourites);
                group.add(retweetcounts);

                Fragment fragment = new FeedFragment(group);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();

            }

            public void failure(TwitterException exception) {
                //Do something on failure
                Log.wtf("irs", exception.toString());
                View view = findViewById(R.id.content_frame);
                Snackbar.make(view, "Network failure", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void  searchFeed(String keyword) {
        String username = sharedPreferences.getString("username", "irshadshalu");
        String s_user_id = sharedPreferences.getString("user_id", "917206495");
        Long user_id = Long.parseLong(s_user_id);
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        SearchService searchService = twitterApiClient.getSearchService();
        Call<Search> call;
        call = searchService.tweets(keyword,null,null,null,null,20,null,null,null,null);
        call.enqueue(new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                //Do something with result

                Search searchList = (Search) result.response.body();
                List<Tweet> tweetlist = (List<Tweet>) searchList.tweets;

                ArrayList<String> tweets = new ArrayList<String>();
                ArrayList<String> users = new ArrayList<String>();
                ArrayList<String> favourites = new ArrayList<String>();
                ArrayList<String> retweetcounts= new ArrayList<String>();

                for (Tweet i : tweetlist) {
                    Log.wtf("rrr", i.text + " " + i.user + " " + i.retweetCount + "\n");
                    User user = i.user;
                    tweets.add(i.text);
                    users.add(user.screenName);
                    favourites.add(Integer.toString(i.favoriteCount));
                    retweetcounts.add(Integer.toString(i.retweetCount));

                }

                ArrayList<ArrayList<String>> group = new ArrayList<>();
                group.add(tweets);
                group.add(users);
                group.add(favourites);
                group.add(retweetcounts);

                Fragment fragment = new FeedFragment(group);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();

            }

            public void failure(TwitterException exception) {
                //Do something on failure
                Log.wtf("irs", exception.toString());
                View view = findViewById(R.id.content_frame);
                Snackbar.make(view, "Network failure", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    public void editHost(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.et_host);
        dialogBuilder.setTitle("Search for #hashtag");
        dialogBuilder.setMessage(" ");
        dialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                searchFeed(edt.getText().toString());

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                //super.onBackPressed();
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click the back button again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}
