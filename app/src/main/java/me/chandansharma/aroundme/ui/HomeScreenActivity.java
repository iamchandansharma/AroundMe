package me.chandansharma.aroundme.ui;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.adapter.HomeScreenItemListAdapter;
import me.chandansharma.aroundme.utils.PlaceDetailProvider;

public class HomeScreenActivity extends AppCompatActivity {

    //View Reference Variable
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private HomeScreenItemListAdapter mHomeScreenItemListAdapter;
    private String[] itemString;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        //set the drawerListener
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.app_name);
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.location_favourite_icon:
                        startActivity(new Intent(HomeScreenActivity.this, FavouritePlaceListActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.share_icon:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout AroundMe Application");
                        startActivity(Intent.createChooser(shareIntent, "Share App.."));
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.feedback_icon:
                        Intent mailToIntent = new Intent(Intent.ACTION_SEND);
                        mailToIntent.setData(Uri.parse("mailto:"));
                        mailToIntent.setType("text/plain");
                        mailToIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"css08740@gmail.com"});
                        startActivity(Intent.createChooser(mailToIntent, "Send Mail.."));
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.about_icon:
                        Dialog aboutDialog = new Dialog(HomeScreenActivity.this, R.style.AboutDialog);
                        aboutDialog.setTitle(getString(R.string.about));
                        aboutDialog.setContentView(R.layout.about_dialog);
                        aboutDialog.show();
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

        itemString = PlaceDetailProvider.popularPlaceTagName;
        mHomeScreenItemListAdapter = new HomeScreenItemListAdapter(this, itemString);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mGridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(36);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setAdapter(mHomeScreenItemListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate menu to add items to action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.removeItem(R.id.share_icon);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, PlaceSearchResultActivity.class)));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


