package com.example.aquib.newsapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aquib.newsapp.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mNewsTextView;
    private ProgressBar mNewsSpinningPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsTextView = (TextView) findViewById(R.id.news_api_response);
        mNewsSpinningPB = (ProgressBar) findViewById(R.id.news_spinning_pb);
        getResponseFromUrl();
    }

    private void getResponseFromUrl() {
        URL newsSearchUrl = NetworkUtils.buildUrl();
        new getNewsResponse().execute(newsSearchUrl);
    }

    public class getNewsResponse extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mNewsSpinningPB.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(URL... params) {
            URL targetUrl = params[0];
            String newsSearchResults = null;
            try {
                newsSearchResults = NetworkUtils.getResponseFromHttpUrl(targetUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return newsSearchResults;
        }

        @Override
        protected void onPostExecute(String newsSearchResults) {
            mNewsSpinningPB.setVisibility(View.INVISIBLE);
            if (newsSearchResults != null && !newsSearchResults.equals("")) {
                mNewsTextView.setText(newsSearchResults);
                mNewsTextView.setVisibility(View.INVISIBLE);
            } else {
                mNewsTextView.setText("Error, try again.");
            }
        }
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
//            Context context = MainActivity.this;
//            String message = "Searching";
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            mNewsTextView.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }
}
