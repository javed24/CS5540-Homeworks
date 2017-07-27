package com.example.aquib.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aquib.newsapp.db.Contract;
import com.example.aquib.newsapp.model.NewsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aquib on 6/25/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemHolder> {
    private static final String TAG = "NewsAdapter";
    //private String [] mNewsData;
    private ArrayList<NewsModel> mNewsData;
    ItemClickListener listener;
    //adding cursor
    private Cursor cursor;
    public NewsAdapter(Cursor cursor, ItemClickListener listener){
        this.cursor = cursor;
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(Cursor cursor, int clickedItemIndex);
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

    //private String imageString = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_IMAGE_URL));
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
       // getThumbnail(imageString, holder.image_view,holder.image_view.getContext());
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
    //adding thumbnail for picasso
    private void getThumbnail(String imageUrl, ImageView imageView, Context context){
        Picasso.with(context).load(imageUrl).resize(200, 250).into(imageView);
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        String article_title;
        String desc;
        String url;
        String publish_time;
        long id;
        TextView title;
        //TextView url;
        TextView description;
        TextView publishedAt;
        private ImageView image_view;
        private String imageString;

        ItemHolder(View view){
            super(view);
            title = (TextView)view.findViewById(R.id.news_title);
            //url = (TextView)view.findViewById(R.id.news_url);
            description = (TextView) view.findViewById(R.id.news_desc);
            publishedAt = (TextView) view.findViewById(R.id.news_time);
            image_view = (ImageView) view.findViewById(R.id.image_view);
            view.setOnClickListener(this);
        }

//        public void bind(int pos){
//            NewsModel news = mNewsData.get(pos);
//            title.setText(news.getTitle());
//            //url.setText(news.getUrl());
//            description.setText(news.getDescription());
//            publishedAt.setText(news.getPublishedAt());
//        }
        public void bind (ItemHolder holder,int position){
            cursor.moveToPosition(position);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_ARTICLES._ID));
            article_title=cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE));
            desc=cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION));
            url=cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_NEWS_URL));
            publish_time=cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED));

            //test getImage
            imageString = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_IMAGE_URL));
            Log.d(TAG, ">>>>Image URl: "+ imageString);
            getThumbnail(imageString, holder.image_view,holder.image_view.getContext());

            title.setText(article_title);
            description.setText(desc);
            publishedAt.setText(publish_time);

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(cursor, pos);
        }
    }
//    public void setNewsData(ArrayList<NewsModel> newsData){
//        mNewsData = newsData;
//        /*Notifies the attached observers that the underlying data has been changed
//        *and any View reflecting the data set should refresh itself.*/
//        notifyDataSetChanged();
//    }
}
