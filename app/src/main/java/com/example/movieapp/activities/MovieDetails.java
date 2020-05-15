package com.example.movieapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.database.MovieProvider;
import com.example.movieapp.database.MovieTableHelper;

public class MovieDetails extends AppCompatActivity {
    TextView titolo,descrizione;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        long id = getIntent().getLongExtra("id",0);
        titolo = findViewById(R.id.movieTitleText);
        descrizione = findViewById(R.id.movieDescriptionText);
        imageView = findViewById(R.id.imageViewDescription);
        getData(id);
    }
    public void getData(long id) {
        Cursor cursor = getContentResolver().query(MovieProvider.MOVIES_URI,null, MovieTableHelper._ID + " = " + id,null,null );
        cursor.moveToNext();
        if(cursor.getString(cursor.getColumnIndex(MovieTableHelper.TITLE)).equals("")){
            titolo.setText(R.string.Title);
        }else {
            titolo.setText(cursor.getString(cursor.getColumnIndex(MovieTableHelper.TITLE)));
        }

        if(cursor.getString(cursor.getColumnIndex(MovieTableHelper.DESCRIPTION)).equals("")){
            descrizione.setText(R.string.Description);
        }else {
            descrizione.setText(cursor.getString(cursor.getColumnIndex(MovieTableHelper.DESCRIPTION)));
        }

        if(cursor.getString(cursor.getColumnIndex(MovieTableHelper.IMAGE_DETAILS)).equals("null")){
            Glide.with(this).load(R.drawable.broken_image_24px).into(imageView);
        }else {
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + cursor.getString(cursor.getColumnIndex(MovieTableHelper.IMAGE_DETAILS))).into(imageView);
        }
        }

}
