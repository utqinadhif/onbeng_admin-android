package com.nadhif.onbengadmin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

public class Form extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {
    ScrollView scroll_form;
    Button saveNow;
    EditText name, company, contact, email, location, price, latlng;
    Intent intent, intents;
    ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scroll_form = (ScrollView) findViewById(R.id.scroll_form);
        scroll_form.setOnClickListener(this);

        name = (EditText) findViewById(R.id.name);
        company = (EditText) findViewById(R.id.company);
        contact = (EditText) findViewById(R.id.contact);
        email = (EditText) findViewById(R.id.email);
        location = (EditText) findViewById(R.id.location);
        price = (EditText) findViewById(R.id.price);


        latlng = (EditText) findViewById(R.id.latlng);
        latlng.setOnFocusChangeListener(this);
        latlng.setOnClickListener(this);

        saveNow = (Button) findViewById(R.id.saveNow);
        saveNow.setOnClickListener(this);

        cv = new ContentValues();
        cv.put("token", Helper.getSP(this, "key"));

        intent = getIntent();
        String id = intent.getStringExtra("id_marker");
        if (id != null) {
            getSupportActionBar().setTitle("Edit data");
            getSupportActionBar().setSubtitle(id);
            name.setText(intent.getStringExtra("name"));
            name.setTag(intent.getStringExtra("id_marker"));
            company.setText(intent.getStringExtra("company"));
            contact.setText(intent.getStringExtra("contact"));
            email.setText(intent.getStringExtra("email"));
            location.setText(intent.getStringExtra("location"));
            price.setText(intent.getStringExtra("price"));
            latlng.setText(intent.getStringExtra("lat") + ", " + intent.getStringExtra("lng"));
            saveNow.setText("Update Now");
            saveNow.setTag(1);
        } else {
            getSupportActionBar().setTitle("Add New Data");
            saveNow.setText("Save Now");
            saveNow.setTag(2);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                latlng.setText(data.getStringExtra("lat") + ", " + data.getStringExtra("lng"));
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == latlng && hasFocus) {
            addData();
        }
    }

    private void addData() {
        Helper.hideSoftKeyboard(this);
        intents = new Intent(this, Pick.class);
        intents.putExtra("name", intent.getStringExtra("name"));
        startActivityForResult(intents, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.latlng:
                addData();
                break;
            case R.id.saveNow:
                Helper.hideSoftKeyboard(this);
                cv.put("name", name.getText().toString());
                cv.put("company", company.getText().toString());
                cv.put("contact", contact.getText().toString());
                cv.put("email", email.getText().toString());
                cv.put("location", location.getText().toString());
                cv.put("price", price.getText().toString());
                cv.put("latlng", "(" + latlng.getText().toString() + ")");
                String p = String.valueOf(saveNow.getTag());
                String add;
                if (p.equals("1")) {
                    // update
                    cv.put("id_marker", name.getTag().toString());
                    add = "updateMarker";
                } else {
                    // save
                    cv.put("id_marker", Helper.nownow());
                    add = "saveMarker";
                }
                new Post(this, Helper.url + "marker/" + add, cv).execute();
                break;
            default:
                break;
        }
    }

    private class Post extends Curl {

        public Post(Context context, String url, ContentValues cv) {
            super(context, url, cv);
        }

        @Override
        protected void onPostExecute(String s) {
            pg.dismiss();
            finish();
        }
    }
}
