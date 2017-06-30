package com.example.aquib.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.aquib.newsapp.model.NewsModel;
import com.example.aquib.newsapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //private TextView mNewsTextView;
    static final String TAG = "mainactivity";
    private ProgressBar mNewsSpinningPB;
    private RecyclerView mRecylcerView;
    private NewsAdapter mNewsAdapter;

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
        //mNewsAdapter = new NewsAdapter();
        mRecylcerView.setAdapter(mNewsAdapter);
        //getResponseFromUrl();
    }

    private void getResponseFromUrl() {
        URL newsSearchUrl = NetworkUtils.buildUrl();
        new getNewsResponse().execute(newsSearchUrl);
    }

    //public class getNewsResponse extends AsyncTask<URL, Void, String> {
    public class getNewsResponse extends AsyncTask<URL, Void, ArrayList<NewsModel>> {

        String query;
        getNewsResponse(){

        }
        getNewsResponse(String s) {
            query = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mNewsSpinningPB.setVisibility(View.VISIBLE);
            //mNewsTextView.setVisibility(View.VISIBLE);
            mRecylcerView.setVisibility(View.VISIBLE);
        }

        protected ArrayList<NewsModel> doInBackground(URL... params) {
            URL targetUrl = params[0];
            String newsSearchResults = null;
            ArrayList<NewsModel> newsModelResult = null;
            try {
                newsSearchResults = NetworkUtils.getResponseFromHttpUrl(targetUrl);
                newsModelResult = NetworkUtils.parseJSON(newsSearchResults);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // return newsSearchResults;
            return newsModelResult;
        }

//        @Override
//        protected void onPostExecute(String newsSearchResults) {
//            mNewsSpinningPB.setVisibility(View.INVISIBLE);
//            if (newsSearchResults != null && !newsSearchResults.equals("")) {
//                mNewsTextView.setText(newsSearchResults);
//                //mNewsTextView.setVisibility(View.INVISIBLE);
//            } else {
//                mNewsTextView.setText("Error, try again.");
//            }
//        }

        @Override
        protected void onPostExecute(final ArrayList<NewsModel> data) {
            super.onPostExecute(data);
            mNewsSpinningPB.setVisibility(View.INVISIBLE);
            if (data != null) {
                NewsAdapter adapter = new NewsAdapter(data, new NewsAdapter.ItemClickListener(){
                    @Override
                    public void onItemClick(int clickedItemIndex) {
                        String url = data.get(clickedItemIndex).getUrl();
                        Log.d(TAG, String.format("Url %s", url));
                        openWebPage(url);
                    }
                });
                //  mNewsAdapter.setNewsData(datum.getTitle());
                mRecylcerView.setAdapter(adapter);
            }
        }
        public void openWebPage(String url) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemThatWasClicked = item.getItemId();
//        if (itemThatWasClicked == R.id.news_search) {
//            //mNewsTextView.setText("");
//            getResponseFromUrl();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();
        if (itemThatWasClicked == R.id.news_search) {
            //mNewsTextView.setText("");
            getResponseFromUrl();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
