package com.example.aquib.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.aquib.newsapp.db.Contract;
import com.example.aquib.newsapp.db.DBHelper;
import com.example.aquib.newsapp.db.DBUtils;
import com.example.aquib.newsapp.model.NewsModel;
import com.example.aquib.newsapp.newsScheduler.Scheduler;
import com.example.aquib.newsapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Void>>, NewsAdapter.ItemClickListener{
    //private TextView mNewsTextView;
    static final String TAG = "mainactivity";
    private ProgressBar mNewsSpinningPB;
    private RecyclerView mRecylcerView;
    private NewsAdapter mNewsAdapter;
    private SQLiteDatabase db;
    private Cursor cursor;
    private static final int NEWS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mNewsTextView = (TextView) findViewById(R.id.news_api_response);
        mRecylcerView = (RecyclerView) findViewById(R.id.recyclerview_news);
        mNewsSpinningPB = (ProgressBar) findViewById(R.id.news_spinning_pb);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecylcerView.setLayoutManager(layoutManager);

        mRecylcerView.setHasFixedSize(true);
        //Scheduler.scheduleRefresh(this);

        //checking if the app was installed before, if not , loading data into db using network methods
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = prefs.getBoolean("isfirst", true);

        if (isFirst) {
            load();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
        }

        //loads data onto the recyclerview from the database
        db = new DBHelper(this).getReadableDatabase();
        cursor = DBUtils.getAll(db);
        mNewsAdapter = new NewsAdapter(cursor, this);
        mRecylcerView.setAdapter(mNewsAdapter);
    }
    //load method for first time app installation
    public void load() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();
        if (itemThatWasClicked == R.id.news_search) {
            //mNewsTextView.setText("");
            //getResponseFromUrl();
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<Void>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Void>>(this) {
            ArrayList<NewsModel> newsModelResult = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mNewsSpinningPB.setVisibility(View.VISIBLE);

            }
            //performs the actual loading operation. Calls the url builder and parser methods similar to the doInBackground method for AsyncTask
            @Override
            public ArrayList<Void> loadInBackground() {
                URL targetUrl = NetworkUtils.buildUrl();
                try{
                    String newsSearchResults = NetworkUtils.getResponseFromHttpUrl(targetUrl);
                    newsModelResult = NetworkUtils.parseJSON(newsSearchResults);
                    db = new DBHelper(this.getContext()).getWritableDatabase();
                    DBUtils.updateNews(db, newsModelResult);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    //gets executed once loader is done, gets all items from the db
    // and notifies the attached observers that the underlying data has been changed
    @Override
    public void onLoadFinished(Loader<ArrayList<Void>> loader, ArrayList<Void> data) {
        mNewsSpinningPB.setVisibility(View.INVISIBLE);
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DBUtils.getAll(db);
        mNewsAdapter = new NewsAdapter(cursor, this);
        mRecylcerView.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Void>> loader) {

    }

    @Override
    public void onItemClick(Cursor cursor, int clickedItemIndex) {
        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_NEWS_URL));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Scheduler.scheduleRefresh(MainActivity.this);
    }
}
