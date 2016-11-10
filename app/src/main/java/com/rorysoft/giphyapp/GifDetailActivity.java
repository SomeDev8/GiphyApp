package com.rorysoft.giphyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;


// The passed image path is put into string object and then rendered using the Glide library code

public class GifDetailActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_detail);
        imageView = (ImageView) findViewById(R.id.image_view);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("Image");

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(imagePath).into(imageViewTarget);

        Log.d("GifDetailActivity", "The path in detail activity is: " + imagePath);

    }
}
