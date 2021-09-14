package com.ex1.assignment2m;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ex1.assignment2m.MainActivity.MyAdapter;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static androidx.recyclerview.widget.RecyclerView.*;

public class DetailsResult extends AppCompatActivity {
//    int movieId;

    final OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_result);

        ImageView postView = findViewById(R.id.poster1);
        ImageView coverView = findViewById(R.id.cover1);
        TextView titleView = findViewById(R.id.title1);
        TextView description = findViewById(R.id.description);
//        TextView value = findViewById(R.id.rating1);
        RatingBar ratingBar = findViewById(R.id.ratBar);
        TextView release = findViewById(R.id.releaseDate);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int movieId = getIntent().getIntExtra("movie_id", 0);


        String response = null;
        String response1 = null;

        try {
            response = run("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=3fa9058382669f72dcb18fb405b7a831");
            response1 = run("https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=3fa9058382669f72dcb18fb405b7a831");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Test  " + movieId);

        MovieDetails mr = new Gson().fromJson(response, MovieDetails.class);

        Author md = new Gson().fromJson(response1,Author.class);
        RecyclerView castRecyclerView = findViewById(R.id.cast);
        castRecyclerView.setLayoutManager(layoutManager);
        castRecyclerView.setAdapter(new CastAdapter(md.getCast()));

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + mr.getPosterPath())
                .into(postView);
//        value.rating.setText(String.valueOf(mr.getPosterPath().getVoteAverage()));
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + mr.getBackdropPath())
                .into(coverView);

        titleView.setText(mr.getTitle());
        description.setText(mr.getOverview());
        ratingBar.setRating((float) mr.getVoteAverage() / 2);
        release.setText("Release Date"+mr.getReleaseDate());

    }
    public class CastAdapter extends RecyclerView.Adapter<CastHolder> {
        List<Cast> castList;

        public CastAdapter(List<Cast> cast) {
            this.castList = cast;

        }

        @NotNull
        public CastHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            View mItemView = LayoutInflater.from(DetailsResult.this)
                    .inflate(R.layout.single_view, parent, false);
            return new CastHolder(mItemView);

        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull DetailsResult.CastHolder holder, int position) {
            Cast cast = castList.get(position);

            holder.castText.setText(cast.getName());
            if (cast.getProfilePath() != null)
                Glide.with(DetailsResult.this).load("https://image.tmdb.org/t/p/w500" + cast.getProfilePath())
                        .into(holder.castImg);

        }

        @Override
        public int getItemCount() { return castList.size(); }
    }
            public class CastHolder extends ViewHolder {
            ImageView castImg;
            TextView castText;

            public CastHolder(View itemView) {
                super(itemView);
                castImg = itemView.findViewById(R.id.castPic);
                castText = itemView.findViewById(R.id.castName);
            }

        }

}


