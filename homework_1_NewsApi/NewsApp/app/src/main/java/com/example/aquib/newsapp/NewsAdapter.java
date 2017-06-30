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

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemHolder> {
    //private String [] mNewsData;
    private ArrayList<NewsModel> mNewsData;
    ItemClickListener listener;
    public NewsAdapter(ArrayList<NewsModel> data, ItemClickListener listener){
        this.mNewsData = data;
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int clickedItemIndex);
    }

//    public NewsAdapter(){
//
//    }

//    class NewsAdapterViewHolder extends RecyclerView.ViewHolder{
//
//        public final TextView mNewsTextView;
//
//        public NewsAdapterViewHolder (View view){
//            super(view);
//            mNewsTextView = (TextView) view.findViewById(R.id.news_app_data);
//        }
//    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.news_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ItemHolder(view);
    }

//    @Override
//    public void onBindViewHolder(NewsAdapterViewHolder newsAdapterViewHolder, int position) {
//        //newsAdapterViewHolder.mNewsTextView.setText(String.valueOf(mNewsData.indexOf(position)));
//        newsAdapterViewHolder.bind(position);
//    }


    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mNewsData == null){
            return 0;
        }
        return mNewsData.size();
    }
    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView url;
        TextView description;
        TextView publishedAt;

        ItemHolder(View view){
            super(view);
            title = (TextView)view.findViewById(R.id.news_title);
            url = (TextView)view.findViewById(R.id.news_url);
            description = (TextView) view.findViewById(R.id.news_desc);
            publishedAt = (TextView) view.findViewById(R.id.news_time);
            view.setOnClickListener(this);
        }

        public void bind(int pos){
            NewsModel news = mNewsData.get(pos);
            title.setText(news.getTitle());
            //url.setText(news.getUrl());
            description.setText(news.getDescription());
            publishedAt.setText(news.getPublishedAt());
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        }
    }
//    public void setNewsData(ArrayList<NewsModel> newsData){
//        mNewsData = newsData;
//        /*Notifies the attached observers that the underlying data has been changed
//        *and any View reflecting the data set should refresh itself.*/
//        notifyDataSetChanged();
//    }
}
