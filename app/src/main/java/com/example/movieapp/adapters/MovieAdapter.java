package com.example.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.activities.MovieDetails;
import com.example.movieapp.database.MovieTableHelper;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public MovieAdapter(Context context,Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView =itemView.findViewById(R.id.imageView);
        }

    }

    public void setmCursor(Cursor mCursor) {
        this.mCursor = mCursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cell_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        final long id = mCursor.getInt(mCursor.getColumnIndex(MovieTableHelper._ID));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetails.class);
                intent.putExtra("id",id);
                mContext.startActivity(intent);
            }
        });
        String image = mCursor.getString(mCursor.getColumnIndex(MovieTableHelper.IMAGE_LIST));
        if(image.equals("null")){
        Glide.with(mContext).load(R.drawable.broken_image_24px).into(holder.imageView);
        }else {
            Glide.with(mContext).load("https://image.tmdb.org/t/p/w500/" + image).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

}
