package com.example.aquib.newsapp.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.aquib.newsapp.model.NewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by aquib on 6/17/17.
 */

// https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=46656d79737843f3bf6aaa24b3311e77
//sample URL: https://api.github.com/search/repositories?q=javed24&sort=stars

public class NetworkUtils {
    private static final String BASE_URL =
            "https://newsapi.org/v1/articles";

    private static final String source = "the-next-web";
    private static final String sortBy = "latest";
    private static final String apiKey = "46656d79737843f3bf6aaa24b3311e77";

    private static final String PARAM_QUERY = "source";
    private static final String SORT = "sortBy";
    private static final String API_KEY = "apiKey";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, source)
                .appendQueryParameter(SORT, sortBy)
                .appendQueryParameter(API_KEY, apiKey)
                .build();
        URL url = null;
        try {
            String urlString = builtUri.toString();
            Log.d(TAG, "Generated URL: " + urlString);
            url = new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    //parsing the JSON response
    public static ArrayList<NewsModel> parseJSON(String json) throws JSONException {
        ArrayList<NewsModel> result = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray articles = main.getJSONArray("articles");

        for(int i = 0; i < articles.length(); i++){
            JSONObject article = articles.getJSONObject(i);
            String title = article.getString("title");
            String description = article.getString("description");
            String url = article.getString("url");
            String publishedAt = article.getString("publishedAt");
            String imageUrl = article.getString("urlToImage");
            String author = article.getString("author");
           // String url = article.getString("html_url");
            NewsModel newsModel = new NewsModel(title, description, url, publishedAt, imageUrl, author);
            result.add(newsModel);
        }
        return result;
    }
    //end parse
}
