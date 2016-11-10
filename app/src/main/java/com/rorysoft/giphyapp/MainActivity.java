package com.rorysoft.giphyapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText searchBox;
    private Button searchButton;
    private Gif gif = null;
    private List<Gif> gifs;
    private RecyclerView mRecyclerView;
    private GifAdapter gifAdapter = null;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int resultCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBox = (EditText) findViewById(R.id.search_box);
        searchButton = (Button) findViewById(R.id.search_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        initSearch();
    }

    // Perform search method and input the parameter 100

    public void initSearch() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 100;
                search(count);
            }
        });
    }

    // Extract string from user input and append it to the URL with the count parameter. Then perform execution on AsyncTask

    public void search(int count) {
        String searchQuery = ("http://api.giphy.com/v1/gifs/search?q=" +
                searchBox.getText().toString().replace(" ", "+") + "&api_key=dc6zaTOxFJmzC" + "&limit=" + count);
        Log.d("MainActivity", "Here is the url: " + searchQuery);
        DownloadData downloadData = new DownloadData();
        downloadData.execute(searchQuery);


    }

    // Attempt to load more gifs as the user scrolls down//

//    private void initOnScroll() {
//        mRecyclerView.addOnScrollListener(new InfiniteScrollListener(staggeredGridLayoutManager, 25) {
//            @Override
//            public void onLoadMore(int update) {
//                search(update);
//            }
//        });
//    }

    // AsyncTask for Downloading JSON data, parsing elements, creating arrayList and passing it to adapter in post execution.

    public class DownloadData extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                gifAdapter = new GifAdapter(MainActivity.this, gifs);
                mRecyclerView.setAdapter(gifAdapter);
//                initOnScroll();

            } else {
                Log.d("MainActivity", "The data failed to download, encountered IO Exception");
                Toast.makeText(MainActivity.this, "The data failed to download", Toast.LENGTH_LONG).show();
            }
        }

        // Connect to the URL and pull the input stream of data into the reader for character conversion.
        // JSON data is then created by StringBuilder object and passed method that creates GIF objects and
        // adds them to an ArrayList.

        @Override
        protected Boolean doInBackground(String... params) {
            char[] buffer = new char[500];
            StringBuilder jsonBuilder = new StringBuilder();
            int charsRead;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                charsRead = inputStreamReader.read(buffer);

                while (charsRead >= 0) {

                    jsonBuilder.append(buffer, 0, charsRead);
                    charsRead = inputStreamReader.read(buffer);
                }

                createGifList(jsonBuilder.toString());

                return true;

            } catch (IOException e) {
                Log.d("MainActivity", "Here is the caught exception: " + e);
            }
            return false;
        }

        private void createGifList(String jsonData) {
            try {

                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                JSONObject pagination = jsonObject.getJSONObject("pagination");
                resultCount = pagination.getInt("count");

                gifs = new ArrayList<>();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject subSet = dataArray.getJSONObject(i);
                    JSONObject image = subSet.getJSONObject("images");
                    JSONObject originalImage = image.getJSONObject("original");
                    String imageUrl = originalImage.getString("url");

                    gif = new Gif();
                    gif.setImagePath(imageUrl);
                    gifs.add(gif);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("MainActivity", "Here is the JSON exception: " + e.getMessage());
            }
        }
    }
}
