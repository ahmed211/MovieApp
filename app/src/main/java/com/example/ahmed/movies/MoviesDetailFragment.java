package com.example.ahmed.movies;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;




public class MoviesDetailFragment extends Fragment {

    String description, date, title, poster_path, vote, id, background, fristTrailer;
    Button fav;
    Database myDB;


    public MoviesDetailFragment() {setHasOptionsMenu(true);}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id=getArguments().getString("id");
        TrailerTask trailerTask;
        trailerTask=new TrailerTask("http://api.themoviedb.org/3/movie/"+id+"/videos");
        trailerTask.execute();
        trailerTask=new TrailerTask("http://api.themoviedb.org/3/movie/"+id+"/reviews");
        trailerTask.execute();

        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies_detail, container, false);


        myDB=new Database(getActivity());

        description = getArguments().getString("description");
        date = getArguments().getString("date");
        title = getArguments().getString("title");
        background = getArguments().getString("background");
        vote = getArguments().getString("vote");
        poster_path=getArguments().getString("poster");

        ((TextView) rootView.findViewById(R.id.description)).setText(description);
        ((TextView) rootView.findViewById(R.id.date)).setText(date);
        ((TextView) rootView.findViewById(R.id.title)).setText(title);
        ((TextView) rootView.findViewById(R.id.vote)).setText(vote);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
        Picasso.with(getActivity()).load(background).resize(800, 1000).into(imageView);

        fav=(Button)rootView.findViewById(R.id.favorit);

        AddData();
        return rootView;
    }

    public void AddData()
    {
        fav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean isInserted = myDB.insertData(id.toString(),
                        "http://image.tmdb.org/t/p/w185/" + poster_path.toString(),
                        description.toString(), date.toString(), title.toString(),
                        "http://image.tmdb.org/t/p/w185/" + background.toString(), vote.toString());

                if (true == isInserted) {
                    Toast.makeText(getActivity(), "Added To Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    myDB.deleteData(id);
                    Toast.makeText(getActivity(), "Removed From Favorites", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.shar, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (null != mShareActionProvider)
        {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + fristTrailer);
        return shareIntent;
    }



    public class TrailerTask extends AsyncTask<String, Void, Void>
    {

        ArrayList<String> trailerKey=new ArrayList<>();
        ArrayList<String> review = new ArrayList<>();
        ArrayList<String> author = new ArrayList<>();
        String baseURL;
        public TrailerTask(String baseURL)
        {
            this.baseURL = baseURL;
        }
        private final String LOG_TAG = TrailerTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String movieJsonStr = null;

            try {
                String API_KEY = "?api_key="+BuildConfig.OPEN_MOVIE_API_KEY;
                URL url = new URL(baseURL.concat(API_KEY));

                Log.v(LOG_TAG, url.toString());

                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream=httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(null == inputStream)
                {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line=bufferedReader.readLine())!=null)
                {
                    buffer.append(line + "\n");
                }

                if(0 == buffer.length())
                {
                    return  null;
                }
                movieJsonStr = buffer.toString();
                JSONObject jsonObject=new JSONObject(movieJsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("results");


                if('o' == baseURL.charAt(baseURL.length()-2)) {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject myMovie = jsonArray.getJSONObject(i);

                        String key = myMovie.getString("key");

                        trailerKey.add(key);

                    }
                    fristTrailer=trailerKey.get(0);

                }
                else
                {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject myMovie = jsonArray.getJSONObject(i);

                        String content = myMovie.getString("content");
                        String name=myMovie.getString("author");
                        author.add(name);
                        review.add(content);
                    }
                }
                Log.v(LOG_TAG, " movieJSON " + movieJsonStr);

            }catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (null != httpURLConnection ) {
                    httpURLConnection.disconnect();
                }
                if (null != bufferedReader) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if('o' == baseURL.charAt(baseURL.length()-2))
            {
                int image[] = new int[trailerKey.size()];
                for (int i = 0; i < image.length; i++)
                {
                    image[i] = R.drawable.video512x512;
                }

                ListView listView = (ListView) getActivity().findViewById(R.id.listview_data);

                ViewGroup.LayoutParams params = listView.getLayoutParams();
                listView.setAdapter(new ListImage(getActivity(), trailerKey, image));

                setListViewHeightBasedOnItems(listView);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String key = trailerKey.get(position);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
                    }
                });
            }

            else {

                final  ArrayAdapter<String> reviewList =
                        new ArrayAdapter<String>(
                                getActivity(),
                                R.layout.review_item,
                                R.id.list_item_review,
                                review);
                ListView listView1 = (ListView) getActivity().findViewById(R.id.listview_review);
                listView1.setAdapter(reviewList);
                setListViewHeightBasedOnItems(listView1);

                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        showMessage(author.get(position), reviewList.getItem(position));
                    }
                });


            }

        }

        public void  showMessage(String title, String message)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.show();
        }

        public boolean setListViewHeightBasedOnItems(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (null != listAdapter) {

                int numberOfItems = listAdapter.getCount();
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = listAdapter.getView(itemPos, null, listView);
                    item.measure(0, 0);
                    totalItemsHeight += item.getMeasuredHeight();
                }
                int totalDividersHeight = listView.getDividerHeight() * (numberOfItems -1);
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight;
                listView.setLayoutParams(params);
                listView.requestLayout();

                return true;

            } else {
                return false;
            }

        }
    }


}
