package com.example.aquib.newsapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.aquib.newsapp.model.NewsModel;

import java.util.ArrayList;

/**
 * Created by aquib on 7/27/17.
 */

public class DBUtils {
    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(Contract.TABLE_ARTICLES.TABLE_NAME, null, null,
                null, null, null,
                Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED + " DESC");
        return cursor;
    }
    public static void updateNews(SQLiteDatabase db, ArrayList<NewsModel> newsItems) {
        db.beginTransaction();
        deleteAllItems(db);
        try {
            for (NewsModel newsItem : newsItems) {
                ContentValues cv = new ContentValues();
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE,
                        newsItem.getTitle());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION,
                        newsItem.getDescription());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED,
                        newsItem.getPublishedAt());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_IMAGE_URL,
                        newsItem.getImageUrl());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_NEWS_URL,
                        newsItem.getUrl());
                db.insert(Contract.TABLE_ARTICLES.TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    private static void deleteAllItems(SQLiteDatabase db) {
        db.delete(Contract.TABLE_ARTICLES.TABLE_NAME, null, null);
    }
}
