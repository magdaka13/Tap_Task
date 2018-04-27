package com.example.magda.tap_task;

import android.net.Uri;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the tap servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TAP_URL =
            "http://dev.tapptic.com/test/json.php";

    public static URL getUrl() {
            return buildUrlItems();

    }

    /**
     * Builds the URL of items.
     *
     * @return The Url to use to query the items.
     */
    public static URL buildUrlItems() {
        Uri itemQueryUri = Uri.parse(TAP_URL).buildUpon()
                .build();

        try {
            URL itemQueryUrl = new URL(itemQueryUri.toString());
            Log.v(TAG, "URL: " + itemQueryUrl);
            return itemQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds the URL of items.
     *
     * @return The Url to use to query for image name
     */
    private static URL buildUrlItemsName(String x) {
        Uri itemQueryUri = Uri.parse(TAP_URL).buildUpon()
                .appendPath("name="+x)
                .build();

        try {
            URL itemQueryUrl = new URL(itemQueryUri.toString());
            Log.v(TAG, "URL: " + itemQueryUrl);
            return itemQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<Item> parseJSON() {
        String resp="";
        ArrayList<Item> p = new ArrayList<Item>();

        URL u=buildUrlItems();

        try
        {
            resp = getResponseFromHttpUrl(u);

                JSONArray ItemsJson = new JSONArray(resp);
            if (ItemsJson!=null)
            {
                for (int i=0;i<ItemsJson.length();i++)
                {
                    JSONObject item=ItemsJson.getJSONObject(i);
                    Item ii=new Item(item.getString("name"),item.getString("image"));
                    p.add(ii);
                    Log.e(TAG,"Parsing name="+ii.getmText()+" image="+ii.getmImage());
                }
            }
/*

                if (ItemJson.has("name")) {
                    int errorCode = ItemJson.getInt("name");

                    switch (errorCode) {
                        case HttpURLConnection.HTTP_OK:
                            Log.e(TAG,"Parsing JSON-HTTP OK");
                            break;
                        case HttpURLConnection.HTTP_NOT_FOUND:
                            Log.e(TAG,"Parsing JSON-HTTP NOT FOUND");
                    /* Location invalid */
//                            return null;
  //                      default:
    //                        Log.e(TAG,"Parsing JSON-SERVER DOWN");
                    /* Server probably down */
        //                    return null;
      //              }
          //      }
/*
                JSONArray jsonItemArray = ItemJson.getJSONArray("");
                if (jsonItemArray!=null)
                {
                    for (int i=0;i<jsonItemArray.length();i++)
                    {
                        JSONObject item=jsonItemArray.getJSONObject(i);
                        Item ii=new Item(item.getString("name"),item.getString("image"));
                        p.add(ii);
                        Log.e(TAG,"Parsing name="+ii.getmText()+" image="+ii.getmImage());
                    }
                }
*/


        }
     catch (Exception e) {
            /* Server probably invalid */
        e.printStackTrace();
        p=null;
    }finally {
            return p;
        }
    }

}