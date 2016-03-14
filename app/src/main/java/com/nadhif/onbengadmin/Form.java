package com.nadhif.onbengadmin;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class Form extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {
    ScrollView scroll_form;
    Button saveNow;
    EditText name, company, contact, email, location, price, latlng;
    Intent intent, intents;
    ContentValues cv;
    String p;
    int position;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progress_spinner);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        scroll_form = (ScrollView) findViewById(R.id.scroll_form);
        scroll_form.setOnClickListener(this);

        name = (EditText) findViewById(R.id.name);
        company = (EditText) findViewById(R.id.company);
        contact = (EditText) findViewById(R.id.contact);
        email = (EditText) findViewById(R.id.email);
        location = (EditText) findViewById(R.id.location);
        location.setOnClickListener(this);
        location.setOnFocusChangeListener(this);
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
            latlng.setTag(R.string.pick_off, intent.getStringExtra("lat"));
            latlng.setTag(R.string.pick_on, intent.getStringExtra("lng"));
            position = intent.getIntExtra("position", 0);
            saveNow.setText("Update Now");
            saveNow.setTag(1);
        } else {
            getSupportActionBar().setTitle("Add New Data");
            saveNow.setText("Save Now");
            saveNow.setTag(2);
        }

        p = String.valueOf(saveNow.getTag());
    }

    private void searchLocation() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, 2);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    private void addData() {
        Helper.hideSoftKeyboard(this);
        intents = new Intent(this, Pick.class);
        intents.putExtra("name", intent.getStringExtra("name"));
        startActivityForResult(intents, 1);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.delete);
        if (p.equals("1")) {
//            update
            menuItem.setVisible(true);
        } else {
//            save
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Helper.hideSoftKeyboard(this);
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure to delete this data?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                p = "3";
                                cv.put("id_marker", name.getTag().toString());
                                new Post(getApplicationContext(), Helper.url + "marker/deleteMarker", cv).execute();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create()
                        .show();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                latlng.setText(data.getStringExtra("lat") + ", " + data.getStringExtra("lng"));
                latlng.setTag(R.string.pick_off, data.getStringExtra("lat"));
                latlng.setTag(R.string.pick_on, data.getStringExtra("lng"));
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                location.setText(place.getName());
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == latlng && hasFocus) {
            addData();
        } else if (v == location && hasFocus) {
            searchLocation();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.latlng:
                addData();
                break;
            case R.id.location:
                searchLocation();
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
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            if (p.equals("1")) {
                // update
                Home.datas.get(position).setBengkel_name(name.getText().toString());
                Home.datas.get(position).setBengkel_company(company.getText().toString());
                Home.datas.get(position).setContact(contact.getText().toString());
                Home.datas.get(position).setEmail(email.getText().toString());
                Home.datas.get(position).setLocation(location.getText().toString());
                Home.datas.get(position).setPrice_per_km(price.getText().toString());
                Home.datas.get(position).setLat(latlng.getTag(R.string.pick_off).toString());
                Home.datas.get(position).setLng(latlng.getTag(R.string.pick_on).toString());
            } else if (p.equals("2")) {
                // save
                Home.datas.add(0, new Data(
                                Helper.nownow(),
                                name.getText().toString(),
                                company.getText().toString(),
                                contact.getText().toString(),
                                email.getText().toString(),
                                location.getText().toString(),
                                price.getText().toString(),
                                latlng.getTag(R.string.pick_off).toString(),
                                latlng.getTag(R.string.pick_on).toString()
                        )
                );
                Home.adapter.notifyItemInserted(0);
            } else {
                Home.datas.remove(position);
                Home.adapter.notifyItemRemoved(position);
            }
            finish();
        }
    }
}
