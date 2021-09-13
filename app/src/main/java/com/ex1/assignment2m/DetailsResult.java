package com.ex1.assignment2m;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
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
        TextView description    = findViewById(R.id.description);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView castRecyclerView = findViewById(R.id.cast);
        castRecyclerView.setLayoutManager(layoutManager);
        castRecyclerView.setHasFixedSize(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int movieId =  getIntent().getIntExtra("movie_id", 0);


        String response = null;
        String response1 = null;

        try {
            response = run("https://api.themoviedb.org/3/movie/"+movieId+"?api_key=3fa9058382669f72dcb18fb405b7a831");
            response1 = run("https://api.themoviedb.org/3/movie/482373/credits?api_key=3fa9058382669f72dcb18fb405b7a831");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Test  "+movieId);

        MovieDetails mr = new Gson().fromJson(response, MovieDetails.class);
//        RecyclerView countryRecyclerView = findViewById(R.id.detailResult);
//        countryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        countryRecyclerView.setAdapter(new MainActivity.MyAdapter(mr.getBackdropPath()));

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+mr.getPosterPath())
                .into(postView);
//        holder.rating.setText(String.valueOf(mr.getPosterPath().getVoteAverage()));
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+mr.getBackdropPath())
                .into(coverView);
        titleView.setText(mr.getTitle());
        description.setText(mr.getOverview());
//        Toast.makeText(this, "Movie id "+response, Toast.LENGTH_LONG).show();


    }
    public class MyAdapters extends RecyclerView.Adapter<MainActivity.MyViewHolder> {

        List<Cast> castList;

        public MyAdapters (List<Cast> castList) {
            this.castList =castList;
        }

        @Override
        public MainActivity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View mItemView = LayoutInflater.from(DetailsResult.this)
                    .inflate(R.layout.single_view, parent, false);
            return new MainActivity.MyViewHolder(mItemView);
        }

        @Override
        public void onBindViewHolder( MainActivity.MyViewHolder holder, int position) {

//            Result movie= movieList.get(position);


            holder.castText.setText(castList.get(position).getName());

            //use glide library to display the image
            Glide.with(DetailsResult.this)
                    .load("https://image.tmdb.org/t/p/w500"+castList.get(position).getProfilePath())
                    .into(holder.castImg);
//            holder.rating.setText(String.valueOf(castList.get(position).getOriginalName()));
//            holder.cardView.setOnClickListener(new View.OnClickListener() {

//                public void onClick(View v) {
//                    Intent intent = new Intent(MainActivity.this, DetailsResult.class);
//                    intent.putExtra("movie_id", movieList.get(position).getId());
//                    startActivity(intent);
//             Intent intent = new Intent(MainActivity.this, MovieDetails.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("poster", movie.getId());
                }
//            });
//        }


        @Override
        public int getItemCount() {
            return castList.size();
        }

    }

}