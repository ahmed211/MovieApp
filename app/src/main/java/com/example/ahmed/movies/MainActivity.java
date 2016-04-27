package com.example.ahmed.movies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
public class MainActivity extends AppCompatActivity {


    boolean tab;
    android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FrameLayout panel=(FrameLayout)findViewById(R.id.detailFragment);

        if(null==panel)//edit
        {
            fragmentManager.beginTransaction().addToBackStack("").replace(R.id.mainlayout, new MainActivityFragment()).commit();
            tab=false;
        }

        else
            tab=true;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setData(String poster_path , String overview,String title,String backdrop_path,
                      String vote_average,String release_date, String id)
    {
        MoviesDetailFragment moviesDetailFragment =new MoviesDetailFragment();

        Bundle extra = new Bundle();
        extra.putString("description" , overview);
        extra.putString("date", release_date);
        extra.putString("title", title);
        extra.putString("background", backdrop_path);
        extra.putString("vote", vote_average);
        extra.putString("id", id);
        extra.putString("poster", poster_path);

        moviesDetailFragment.setArguments(extra);


        if(true==tab)
            fragmentManager.beginTransaction().addToBackStack("").replace(R.id.detailFragment, moviesDetailFragment).commit();
        else
            fragmentManager.beginTransaction().addToBackStack("").replace(R.id.mainlayout, moviesDetailFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (R.id.action_settings == id) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
