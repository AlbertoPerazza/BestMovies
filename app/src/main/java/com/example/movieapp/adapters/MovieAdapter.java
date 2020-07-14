package com.example.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieapp.R;
import com.example.movieapp.activities.MovieDetails;
import com.example.movieapp.database.MovieTableHelper;
import com.example.movieapp.fragments.FavouriteDialogFragment;
import com.example.movieapp.fragments.FavouriteDialogFragmentListener;
import com.example.movieapp.utils.StaticValues;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private FavouriteDialogFragmentListener mListener;

    public MovieAdapter(Context context,Cursor cursor,FavouriteDialogFragmentListener listener){
        mContext = context;
        mCursor = cursor;
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView,imageFavourite;
        public TextView textRating;

            public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView =itemView.findViewById(R.id.imageView);
            imageFavourite = itemView.findViewById(R.id.imageFavourite);
            textRating = itemView.findViewById(R.id.ratingList);
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
        final String title = mCursor.getString(mCursor.getColumnIndex(MovieTableHelper.TITLE));
        final int fav =mCursor.getInt(mCursor.getColumnIndex(MovieTableHelper.FAVOURITE));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mContext;
                String message ;
                if (fav==0){
                    message = "Stai per aggiungere " + title  + " ai preferiti";
                }else{
                    message = "Stai per rimuovere " + title + " dai preferiti";
                }
                FavouriteDialogFragment dialogFragment = new FavouriteDialogFragment("Preferiti",message , id, mListener);
                dialogFragment.show(activity.getSupportFragmentManager(), null);
                return true;
            }
        });
        String image = mCursor.getString(mCursor.getColumnIndex(MovieTableHelper.IMAGE_LIST));
        Glide.with(mContext).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.broken_image_24px).error(R.drawable.broken_image_24px)).
                load("https://image.tmdb.org/t/p/w500/" + image).into(holder.imageView);
        String rating = mCursor.getString(mCursor.getColumnIndex(MovieTableHelper.RATING));
        holder.textRating.setText(rating);
        int imageFav = mCursor.getInt(mCursor.getColumnIndex(MovieTableHelper.FAVOURITE));
        if(imageFav == 1){
            Glide.with(mContext).load(R.drawable.baseline_star).into(holder.imageFavourite);
        }else {
            Glide.with(mContext).load(R.drawable.baseline_star_outline).into(holder.imageFavourite);
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
