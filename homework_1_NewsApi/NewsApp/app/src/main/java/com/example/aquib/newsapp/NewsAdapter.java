package com.example.aquib.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aquib.newsapp.model.NewsModel;

import java.util.ArrayList;

/**
 * Created by aquib on 6/25/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    //private String [] mNewsData;
    private ArrayList<NewsModel> mNewsData;
    public NewsAdapter(){

    }
    class NewsAdapterViewHolder extends RecyclerView.ViewHolder{

        public final TextView mNewsTextView;

        public NewsAdapterViewHolder (View view){
            super(view);
            mNewsTextView = (TextView) view.findViewById(R.id.news_app_data);
        }
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.news_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder newsAdapterViewHolder, int position) {
        newsAdapterViewHolder.mNewsTextView.setText(String.valueOf(mNewsData.indexOf(position)));
    }

    @Override
    public int getItemCount() {
        if(mNewsData == null){
            return 0;
        }
        return mNewsData.size();
    }
    public void setNewsData(ArrayList<NewsModel> newsData){
        mNewsData = newsData;
        /*Notifies the attached observers that the underlying data has been changed
        *and any View reflecting the data set should refresh itself.*/
        notifyDataSetChanged();
    }
}
