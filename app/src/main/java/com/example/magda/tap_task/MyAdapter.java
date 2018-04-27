package com.example.magda.tap_task;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class MyAdapter extends  RecyclerView.Adapter<MyAdapter.ItemsHolder> {

    //    Variable used to reference the model
    private ArrayList<Item> mItems = new ArrayList<>();

    //    NewsHolder class that extends the ViewHolder
    public class ItemsHolder extends RecyclerView.ViewHolder {
        public TextView mItemTextView;
        public ImageView mItemImageView;

        //   Constructor to set the views
        public ItemsHolder(View itemView){
            super(itemView);
            mItemTextView = (TextView) itemView.findViewById(R.id.tv_text);
            mItemImageView = (ImageView) itemView.findViewById(R.id.tv_image);
        }

    }

    //    Constructor to set the adapter
    public MyAdapter(ArrayList<Item> items){
        mItems = items;
    }

    @Override
    public ItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,
                        parent, false);
        return new ItemsHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsHolder holder, int position) {
        holder.mItemTextView.setText(mItems.get(position).getmText());
        new DownloadImageTask(holder.mItemImageView).execute(mItems.get(position).getmImage());


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


}