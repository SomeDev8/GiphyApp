package com.rorysoft.giphyapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.List;

/**
 * Created by Alexander on 11/8/16.
 */

// The adapter class uses the ArrayList from the MainActivity AsyncTask which is passed to the constructor. Each object's image is rendered
// by the Glide library code after the grid_image layout is inflated. Finally, the imagePath is extracted again and passed to the
// detail activity through intent

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.MyViewHolder> {

    private Context mContext;
    private List<Gif> gifs;

    public GifAdapter(Context context, List<Gif> gifs) {
        this.gifs = gifs;
        this.mContext = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_image, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(gifs.get(position));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return gifs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        private MyViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.image_thumbnail);
        }

        private void bind(final Gif gif) {
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
            Glide.with(mContext)
                    .load(gif.getImagePath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(imageViewTarget);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imagePath = gif.getImagePath();
                    Intent intent = new Intent(mContext, GifDetailActivity.class);
                    intent.putExtra("Image", imagePath);
                    mContext.startActivity(intent);
                }
            });
        }
    }


}
