package udacity.hamid.picassogridviewproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import udacity.hamid.picassogridviewproject.data.MovieContract;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Product> arrayList;
    private GridView gridView;
    private CustomListAdapter adapter;
    private TextView tvPopularAndRated;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayList = new ArrayList<>();
        gridView = (GridView) findViewById(R.id.gridView);
        tvPopularAndRated = (TextView) findViewById(R.id.tv_popular_and_topRated);


        topRated();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Product product = (Product) gridView.getItemAtPosition(position);
                String image = product.getImage().toString();

                String title = product.getTitle();
                String releaseDate = product.getReleaseDate();
                String voteAvg = product.getVoteAvg();
                String plotSynopsis = product.getPlotSynopsis();
                int id = product.getId();

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("k_image", image);
                intent.putExtra("k_title", title);
                intent.putExtra("k_releaseDate", releaseDate);
                intent.putExtra("k_voteAvg", voteAvg);
                intent.putExtra("k_plotSynopsis", plotSynopsis);
                intent.putExtra("k_id", id);
                startActivity(intent);

            }
        });
    }

    private void topRated() {
        tvPopularAndRated.setText("Top Rated");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://api.themoviedb.org/3/movie/top_rated?api_key=89f8031fa012853efc9498472830528d");
            }
        });
        gridUpdate();
    }

    private void popular() {
        tvPopularAndRated.setText("Popular");

        adapter.clear();
        adapter.notifyDataSetChanged();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("https://api.themoviedb.org/3/movie/popular?api_key=89f8031fa012853efc9498472830528d");
            }
        });
        gridUpdate();
    }

    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return readURL(strings[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject productObject = jsonArray.getJSONObject(i);
                    String poster_path = productObject.getString("poster_path");
                    String complete_url = "https://image.tmdb.org/t/p/w185/" + poster_path;
                    arrayList.add(new Product(
                            complete_url,
                            productObject.getString("original_title"),
                            productObject.getString("release_date"),
                            productObject.getString("vote_average"),
                            productObject.getString("overview"),
                            productObject.getInt("id")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            gridUpdate();
        }
    }

    private void gridUpdate() {
        adapter = new CustomListAdapter
                (MainActivity.this, R.layout.custom_list_layout, arrayList);
        gridView.setAdapter(adapter);

    }

    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theUrl);

            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_top_rated) {
            popular();
        }
        if (id == R.id.action_mostPopular) {
            adapter.clear();
            adapter.notifyDataSetChanged();
            topRated();
        }
        if (id == R.id.action_fav) {

            Intent launchFavIntent = new Intent(this, FavActivity.class);
            startActivity(launchFavIntent);
        }
        return super.onOptionsItemSelected(item);

    }
}
