package com.example.movieapp.database;

import android.media.Image;
import android.provider.BaseColumns;

public class MovieTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "movies";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE_LIST = "image_list";
    public static final String IMAGE_DETAILS = "image_details";
    public static final String RATING = "rating";
    public static final String FAVOURITE = "favourite";
    public static final String API_ID = "api_id";



    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            DESCRIPTION + " TEXT , " +
            TITLE + " TEXT , " +
            IMAGE_LIST + " TEXT , " +
            RATING + " TEXT , " +
            FAVOURITE + " INT , " +
            IMAGE_DETAILS + " TEXT , " +
            API_ID + " TEXT ) ;";
}
