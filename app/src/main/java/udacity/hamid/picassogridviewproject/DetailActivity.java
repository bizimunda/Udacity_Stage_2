package udacity.hamid.picassogridviewproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import udacity.hamid.picassogridviewproject.data.MovieContract.MovieEntry;

public class DetailActivity extends AppCompatActivity{

    private ImageView imageView;
    private TextView tvTitle, tvReleaseDate, tvVoteAvg, tvPlotSynopsis;
    String title, plotSynopsis, image;
    int id;
    StringBuffer content ;
    ArrayList<String>trailers_list ;
    ListView listView ;
    TextView review;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = (ImageView) findViewById(R.id.iv_moviePoster);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        tvVoteAvg = (TextView) findViewById(R.id.tv_voteAvg);
        tvPlotSynopsis = (TextView) findViewById(R.id.tv_plotSynopsis);
        listView=(ListView)findViewById(R.id.lv_trailer) ;
        review=(TextView)findViewById(R.id.review);
        trailers_list= new ArrayList<String>();


        final Intent intent = getIntent();
        if (intent.getExtras()!=null) {
            image = intent.getStringExtra("k_image");
            imageUri=Uri.parse(image);
            title = intent.getStringExtra("k_title");
            String releaseDate = intent.getStringExtra("k_releaseDate");
            String voteAvg = intent.getStringExtra("k_voteAvg");
             plotSynopsis = intent.getStringExtra("k_plotSynopsis");
            id=intent.getIntExtra("k_id", 0);

            //setting views
            Picasso.with(DetailActivity.this).load(imageUri).into(imageView);
            tvTitle.setText(title);
            tvReleaseDate.setText(releaseDate);
            tvVoteAvg.setText(voteAvg);
            tvPlotSynopsis.setText(plotSynopsis);
            //int size=arrayList.size();

        }

        //executing background threads
        new getTrailers().execute();
        new getReviews().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent youtubeIntent= new Intent();
                youtubeIntent.setData(Uri.parse("https://www.youtube.com/watch?v="+trailers_list.get(position)));
                startActivity(youtubeIntent);
            }
        });


    }

    private void addToFav()
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_IMAGE, imageUri.toString());
        contentValues.put(MovieEntry.COLUMN_TITLE, title);
        contentValues.put(MovieEntry.COLUMN_MOVIE_ID, id);
        contentValues.put(MovieEntry.COLUMN_DESCRIPTION, plotSynopsis);

        Uri uri = getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Something Wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_fav) {
            addToFav();
        }
        return super.onOptionsItemSelected(item);

    }

    /****************************************************************/
    class getReviews extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            HttpURLConnection httpURLConnection = null ;
            BufferedReader bufferedReader =null ;
            InputStream inputStream ;
            StringBuffer stringBuffer=new StringBuffer("") ;
            try {
                URL myurl = new URL("https://api.themoviedb.org/3/movie/"+id+"/reviews?api_key=89f8031fa012853efc9498472830528d");
                httpURLConnection=(HttpURLConnection) myurl.openConnection() ;
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                if(inputStream==null)
                    return null ;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;
                String line=null ;
                while ((line = bufferedReader.readLine())!=null)
                    stringBuffer.append(line+"\n") ;
                String jsn = stringBuffer.toString() ;
                JSONObject jsonObject = new JSONObject(jsn);
                JSONArray result = jsonObject.getJSONArray("results");
                content = new StringBuffer();
                for (int i = 0; i < result.length() ; i++) {
                    JSONObject jsonObject1 = result.getJSONObject(i);
                    content.append( jsonObject1.getString("content")) ;
                }
                return content.toString() ;
            }catch (Exception e)
            {
                e.getStackTrace();
            }
           return "No review";
        }

        @Override
        protected void onPostExecute(String s) {
            review.setText(content.toString());
        }
    }

    /****************************************************************/
    class getTrailers extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            trailers_list.clear();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpURLConnection httpURLConnection = null ;
            BufferedReader bufferedReader =null ;
            InputStream inputStream ;
            StringBuffer stringBuffer=new StringBuffer() ;
            try {
                URL myurl = new URL("https://api.themoviedb.org/3/movie/"+id+"/videos?api_key=89f8031fa012853efc9498472830528d");
                httpURLConnection=(HttpURLConnection) myurl.openConnection() ;
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                if(inputStream==null)
                    return null ;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;
                String line ;
                while ((line = bufferedReader.readLine())!=null)
                    stringBuffer.append(line+"\n") ;
                String jsn = stringBuffer.toString() ;
                JSONObject jsonObject = new JSONObject(jsn);
                JSONArray result = jsonObject.getJSONArray("results");
                for (int i = 0; i < result.length() ; i++) {
                    JSONObject jsonObject1 = result.getJSONObject(i);
                    String key = jsonObject1.getString("key") ;
                    trailers_list.add(key) ;

                }
            }catch (Exception e)
            {
                Log.e(e.getMessage(),"background") ;
            }
            return trailers_list.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            trailers_list.size();
            ArrayList<String> arrayObject= new ArrayList<String>();
            for (int i=0; i<trailers_list.size();i++)
            {
                arrayObject.add("Trailer: "+ i);
            }
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayObject);
            itemsAdapter.getCount();

            //ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,strings) ;
            ViewGroup.LayoutParams L = listView.getLayoutParams();
            L.height = itemsAdapter.getCount()*200;
            listView.setLayoutParams(L);
            listView.requestLayout();
            listView.setAdapter(itemsAdapter);


        }
    }


}
