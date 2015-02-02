package com.ea7jmf.androidcourse_imagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ea7jmf.androidcourse_imagesearch.ImageResultsAdapter;
import com.ea7jmf.androidcourse_imagesearch.R;
import com.ea7jmf.androidcourse_imagesearch.apis.GoogleImagesAPI;
import com.ea7jmf.androidcourse_imagesearch.fragments.SettingsDialog;
import com.ea7jmf.androidcourse_imagesearch.models.ImageResult;
import com.ea7jmf.androidcourse_imagesearch.thirdparty.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements SettingsDialog.SettingsDialogListener {

    private SearchView searchView;

    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    private RelativeLayout rlLoader;
    private ImageView ivLoaderImage;
    private TextView txtLoaderCaption;
    private Animation animRotate;

    GoogleImagesAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        api = new GoogleImagesAPI();

        setupView();
        setupListeners();

        imageResults = new ArrayList<>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);

        isNetworkAvailable();
    }

    private void setupView() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        rlLoader = (RelativeLayout) findViewById(R.id.rlLoader);
        ivLoaderImage = (ImageView) findViewById(R.id.ivLoaderImage);
        txtLoaderCaption = (TextView) findViewById(R.id.txtLoaderCaption);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
    }

    private void setupListeners() {
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult result = imageResults.get(position);
                i.putExtra("result", result);
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                doSearch(api.getQuery(), api.nextPage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                // Set loading image
                ivLoaderImage.setImageResource(R.drawable.loading);
                ivLoaderImage.setAnimation(animRotate);

                // Modify placeholder text
                txtLoaderCaption.setText(getString(R.string.loader_loading));

                // Swap results to loader view
                if(rlLoader.getVisibility() != FrameLayout.VISIBLE) {
                    rlLoader.setVisibility(FrameLayout.VISIBLE);
                    gvResults.setVisibility(FrameLayout.GONE);
                }

                searchView.clearFocus();

                doSearch(s, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                aImageResults.clear();

                // Set magnifier image
                ivLoaderImage.setImageResource(R.drawable.magnifier);
                ivLoaderImage.setAnimation(null);

                // modify placeholder text in loader
                if (s.isEmpty()) {
                    txtLoaderCaption.setText(getString(R.string.loader_search_images));
                } else {
                    txtLoaderCaption.setText(String.format(getString(R.string.loader_search_images_for), s));
                }

                // Swap results to loader view
                if(rlLoader.getVisibility() != FrameLayout.VISIBLE) {
                    rlLoader.setVisibility(FrameLayout.VISIBLE);
                    gvResults.setVisibility(FrameLayout.GONE);
                }

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            Bundle dialogArgs = new Bundle();
            dialogArgs.putString("size", api.getSize());
            dialogArgs.putString("color", api.getColor());
            dialogArgs.putString("type", api.getType());
            dialogArgs.putString("site", api.getSite());
            SettingsDialog settingsDialog = SettingsDialog.newInstance(dialogArgs);
            settingsDialog.show(fm, "fragment_settings");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void doSearch(String query, int start) {
        gvResults.setClickable(false);

        ResponseHandlerInterface handler = new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                ivLoaderImage.setImageResource(R.drawable.warning);
                txtLoaderCaption.setText(getText(R.string.loader_failure));
                if(gvResults.getVisibility() != FrameLayout.VISIBLE) {
                    rlLoader.setVisibility(FrameLayout.VISIBLE);
                    gvResults.setVisibility(FrameLayout.GONE);
                }

                gvResults.setClickable(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                try {
                    JSONArray jsonArray = response
                            .getJSONObject("responseData")
                            .getJSONArray("results");

                    aImageResults.addAll(ImageResult.fromJSONArray(jsonArray));

                    if(gvResults.getVisibility() != FrameLayout.VISIBLE) {
                        gvResults.setVisibility(FrameLayout.VISIBLE);
                        rlLoader.setVisibility(FrameLayout.GONE);
                    }

                } catch (JSONException e) {
                    txtLoaderCaption.setText(getText(R.string.loader_failure));
                    if(gvResults.getVisibility() != FrameLayout.VISIBLE) {
                        rlLoader.setVisibility(FrameLayout.VISIBLE);
                        gvResults.setVisibility(FrameLayout.GONE);
                    }
                    e.printStackTrace();
                } finally {
                    gvResults.setClickable(false);
                }
            }
        };

        if(isNetworkAvailable()) {
            api.setQuery(query);
            api.setStart(start);
            api.query(handler);
        }
    }

    @Override
    public void onFinishSettingsDialog(Bundle settings) {
        api.setColor(settings.getString("color"));
        api.setType(settings.getString("type"));
        api.setSize(settings.getString("size"));
        api.setSite(settings.getString("site"));
    }

    private Boolean isNetworkAvailable() {
        boolean networkStatus;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        networkStatus = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if(!networkStatus) {
            // No network
            ivLoaderImage.setImageResource(R.drawable.network);
            ivLoaderImage.setAnimation(null);

            // modify placeholder text in loader
            txtLoaderCaption.setText(getString(R.string.loader_no_network));

            if(gvResults.getVisibility() != FrameLayout.VISIBLE) {
                rlLoader.setVisibility(FrameLayout.VISIBLE);
                gvResults.setVisibility(FrameLayout.GONE);
            }
        }

        return networkStatus;
    }
}
