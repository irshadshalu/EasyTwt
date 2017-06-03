package com.frainz.easytwt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.MyViewHolder> {
  private Context mContext;
  private List<String> mTexts;
  private Context context;
  ArrayList<String> mUsers;
  ArrayList<String> mFavs;
  ArrayList<String> mReTweets;

  public FeedRecyclerAdapter(Context context, ArrayList<ArrayList<String>> group) {
    mContext = context;
    this.mTexts = group.get(0);
    this.mUsers = group.get(1);
    this.mFavs = group.get(2);
    this.mReTweets = group.get(3);
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
        mContext).inflate(R.layout.item_feed, parent, false));
    return holder;
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, final int position) {

    holder.tweet.setText(mTexts.get(position));
      holder.user.setText("@"+mUsers.get(position) );
      holder.retweet.setText(mReTweets.get(position) + " retweets");
      holder.fav.setText(mFavs.get(position) + " likes");

//    holder.root.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        final Intent intent =  new Intent(context, TweetActivit.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("mno",mNos.get(position));
//      }
//    });
  }

  @Override
  public int getItemCount() {
    return mTexts.size();
  }

  class MyViewHolder extends RecyclerView.ViewHolder {
    LinearLayout root;
    View views;
    TextView tweet,user,fav,retweet;

    public MyViewHolder(View view) {
      super(view);
      context = itemView.getContext();
      root = (LinearLayout) view.findViewById(R.id.rootView);
      views =  view.findViewById(R.id.stroke);
      tweet = (TextView) view.findViewById(R.id.text_tweet);
      user = (TextView) view.findViewById(R.id.text_username);
      fav = (TextView) view.findViewById(R.id.text_likes);
      retweet = (TextView) view.findViewById(R.id.text_retweets);
    }
  }
}