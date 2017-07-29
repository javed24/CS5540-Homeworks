package com.example.aquib.newsapp.newsScheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.aquib.newsapp.db.DBHelper;
import com.example.aquib.newsapp.db.DBUtils;
import com.example.aquib.newsapp.model.NewsModel;
import com.example.aquib.newsapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aquib on 7/27/17.
 */

public class ServiceNews extends com.firebase.jobdispatcher.JobService {

    private static final String TAG = "ServiceNews";

    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
            new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    updateDatabase(ServiceNews.this);
                    return null;
                }
            }.execute();
        //checking if update after a certain interval works
        Toast.makeText(ServiceNews.this, "Updated from source", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        return false;
    }

    //updates the database by calling the API again
    public static void updateDatabase(Context context) {
        try {
            URL targetUrl = NetworkUtils.buildUrl();
            String newsSearchResults = NetworkUtils.getResponseFromHttpUrl(targetUrl);
            if (newsSearchResults != null && !newsSearchResults.isEmpty()) {
                ArrayList<NewsModel> newsItems = NetworkUtils.parseJSON(newsSearchResults);
                SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
                DBUtils.updateNews(db, newsItems);
                Log.d(TAG, "executed update after a minute");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
