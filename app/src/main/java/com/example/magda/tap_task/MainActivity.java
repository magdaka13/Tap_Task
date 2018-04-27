package com.example.magda.tap_task;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity  {

    NetworkInfo info;
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String ITEM_QUERY_URL = "query";

    //    The Loader takes in a bundle
    Bundle sourceBundle = new Bundle();

    private RecyclerView mRecyclerView;
    private static final int ID_LOADER = 445;
    private MyAdapter mAdapter;
    private  ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView =  (RecyclerView)findViewById(R.id.recyclerview_items);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                TextView mItemTextView ;

                for (int i=0;i<mRecyclerView.getChildCount();i++)
                {
                    View v=mRecyclerView.getChildAt(i);
                    mRecyclerView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    mItemTextView=(TextView)mRecyclerView.getChildViewHolder(v).itemView.findViewById(R.id.tv_text);
                    mItemTextView.setTextColor(Color.BLACK);

                }



                View v=mRecyclerView.getChildAt(position);

                if (v!=null) {
                    v.setBackgroundColor(getResources().getColor(R.color.touched_color));
                    mItemTextView = (TextView) mRecyclerView.getChildViewHolder(v).itemView.findViewById(R.id.tv_text);
                    mItemTextView.setTextColor(Color.WHITE);

                    Intent i = new Intent(MainActivity.this, ItemActivity.class);
                    i.putExtra("Item_id", mItemTextView.getText().toString());
                    Log.e("MainActivity", "send text=" + mItemTextView.getText().toString());
                    startActivity(i);
                }
            }

            @Override
            public void onFocus(View view, int position) {
                TextView mItemTextView ;

                for (int i=0;i<mRecyclerView.getChildCount();i++)
                {
                    View v=mRecyclerView.getChildAt(i);
                    mRecyclerView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    mItemTextView=(TextView)mRecyclerView.getChildViewHolder(v).itemView.findViewById(R.id.tv_text);
                    mItemTextView.setTextColor(Color.BLACK);

                }

                View v=mRecyclerView.getChildAt(position);
                mRecyclerView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.focused_color));
                mItemTextView=(TextView)mRecyclerView.getChildViewHolder(v).itemView.findViewById(R.id.tv_text);
                mItemTextView.setTextColor(Color.WHITE);

            }

            @Override
            public void onLongClick(View view, int position) {
                TextView mItemTextView ;

                for (int i=0;i<mRecyclerView.getChildCount();i++)
                {
                    View v=mRecyclerView.getChildAt(i);
                    mRecyclerView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    mItemTextView=(TextView)mRecyclerView.getChildViewHolder(v).itemView.findViewById(R.id.tv_text);
                    mItemTextView.setTextColor(Color.BLACK);

                }

                View v=mRecyclerView.getChildAt(position);
                mRecyclerView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.selected_color));
                mItemTextView=(TextView)mRecyclerView.getChildViewHolder(v).itemView.findViewById(R.id.tv_text);
                mItemTextView.setTextColor(Color.WHITE);

                //Toast.makeText(MainActivity.this, "Long press on position :"+position,
                  //      Toast.LENGTH_LONG).show();
            }
        }));

        getSupportLoaderManager().initLoader(ID_LOADER, sourceBundle, new ItemsLoader());
        restartLoader();


    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        try {
            info = cm.getActiveNetworkInfo();
        }catch(Exception e)
        {
            Log.e(TAG,"Cannot retrieve ActiveNetworkInfo"+e.getMessage());
        }

        return info != null && info.isConnectedOrConnecting();
    }

    private int anyRandomInt(Random random) {
        return random.nextInt();
    }

    private void restartLoader() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    URL ItemUrl = NetworkUtils.buildUrlItems();
                    sourceBundle.putString(ITEM_QUERY_URL, ItemUrl.toString());

                    Random random = new Random();
                    int uniqueId = anyRandomInt(random); //Generates a new ID for each loader call;


                    LoaderManager loaderManager = getSupportLoaderManager();

                    if (loaderManager.getLoader(ID_LOADER) == null) {
                        loaderManager.initLoader(uniqueId, sourceBundle, new ItemsLoader());
                    } else {
                        loaderManager.restartLoader(ID_LOADER, sourceBundle, new
                                ItemsLoader());
                    }
                }catch (Exception e)
                {
                    Log.e(TAG,"Cannot set URL for items"+e.getMessage());
                }
            }
        }, 5000);
    }

    private void showErrorScreen(){
        Toast.makeText(this,"error loading data",Toast.LENGTH_LONG).show();
    }



    public class ItemsLoader implements LoaderManager.LoaderCallbacks<ArrayList<Item>> {
        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<ArrayList<Item>> onCreateLoader(int id, final Bundle args) {
            if (isConnected()){
                mRecyclerView.setVisibility(View.VISIBLE);
                return new AsyncTaskLoader<ArrayList<Item>>(getApplicationContext()) {
                    ArrayList<Item> mItemsData;

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        if (mItemsData != null){
                            deliverResult(mItemsData);
                        }else{
                            forceLoad();
                        }
                    }

                    @Override
                    public ArrayList<Item> loadInBackground() {
                            ArrayList<Item> items1 = NetworkUtils.parseJSON();
                            return items1;
                    }

                    public void deliverResult(ArrayList<Item> data) {
                        mItemsData = data;
                        super.deliverResult(data);
                    }
                };
            }else{
                showErrorScreen();
                return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Item>> loader, ArrayList<Item> data) {
            if (null == data) {
                showErrorScreen();
            } else {
                if (items != null) {
                    items.clear();
                    items.addAll(data);
                    mAdapter = new MyAdapter(items);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    mAdapter.notifyDataSetChanged();
                } else {
                    items = data;
                    mAdapter = new MyAdapter(items);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    mAdapter.notifyDataSetChanged();

                }
                Log.i( "  this is the data", data.toString());
// Array of objects shows in the log
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Item>> loader) {
            loader.forceLoad();
        }

    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
        public void onFocus(View view,int position);
    }
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
