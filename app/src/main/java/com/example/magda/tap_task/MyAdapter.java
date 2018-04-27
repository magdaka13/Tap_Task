package com.example.magda.tap_task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
    public static class ItemsHolder extends RecyclerView.ViewHolder{
        private TextView mItemTextView;
        private ImageView mItemImageView;

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
        //Log.e("Myadapter->onBindHolder","Position="+position);
        holder.mItemTextView.setText(mItems.get(position).getmText());
        Uri u=Uri.parse(mItems.get(position).getmImage()).buildUpon().build();

        new DownloadImageTask(holder.mItemImageView).execute(mItems.get(position).getmImage());
    }

    @Override
    public int getItemCount() {
      //  Log.e("Myadapter->getItemcount","Ile="+mItems.size());
        return mItems.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}