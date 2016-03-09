package com.nadhif.onbengadmin;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swiper;
    ProgressBar progressBar;

    ArrayList<Data> datas = new ArrayList<>();
    ContentValues cv;
    int pageCurrent = -1;
    int pageFetch = -1;
    int pageTotal = 0;
    boolean refresh = false;
    boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.list_order_recylerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter(datas, recyclerView, this);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);

        swiper = (SwipeRefreshLayout) findViewById(R.id.list_order_swipe);
        swiper.setSize(SwipeRefreshLayout.DEFAULT);
        swiper.setColorSchemeResources(R.color.white);
        swiper.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorAccent));
        swiper.setOnRefreshListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progress_spinner);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        cv = new ContentValues();
        cv.put("beo", "038");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menu.findItem(R.id.sound).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sound:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                break;
            case R.id.vibrate:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                break;
            case R.id.exit:
                finish();
                System.exit(0);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(getApplicationContext(), Form.class));
                break;
            default:
                break;
        }
    }

    private void loadData() {
        pageCurrent++;
        int p = pageCurrent + 1;
        new json(this, "http://nadhif.pe.hu/json/list_item/" + p, cv).execute();
    }

    @Override
    public void onRefresh() {
        refresh = true;
        pageCurrent = -1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        if (pageFetch == pageCurrent && pageCurrent < pageTotal) {
            loadData();
        }
        adapter.setLoaded();
    }

    private class json extends Curl {
        public json(Context context, String url, ContentValues cv) {
            super(context, url, cv);
        }

        @Override
        protected void onPreExecute() {
            if (swiper.getVisibility() == View.GONE) {
                swiper.setVisibility(View.VISIBLE);
            }
            swiper.setRefreshing(true);
            if (first) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getString("ok").equals("1")) {
                    progressBar.setVisibility(View.GONE);
                    if (refresh || first) {
                        datas.clear();
                        adapter.notifyDataSetChanged();
                        refresh = false;
                        first = false;
                    }
                    JSONObject result = json.getJSONObject("result");
                    JSONArray college = result.getJSONArray("list");
                    if (college.length() > 0) {
//                       no_data.setVisibility(View.GONE);
                        for (int i = 0; i < college.length(); i++) {
                            JSONObject c = college.getJSONObject(i);
                            datas.add(new Data(
                                            c.getString("id").toString(),
                                            c.getString("name").toString(),
                                            c.getString("company").toString(),
                                            c.getString("contact").toString(),
                                            c.getString("email").toString(),
                                            c.getString("location").toString(),
                                            c.getString("price").toString(),
                                            c.getString("lat").toString(),
                                            c.getString("lng").toString()
                                    )
                            );
                            adapter.notifyItemInserted(datas.size());
                        }
                        pageTotal = result.getInt("total_page") - 1;
                        pageFetch = pageCurrent;
                        swiper.setRefreshing(false);
                    } else {
//                       no_data.setVisibility(View.VISIBLE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
