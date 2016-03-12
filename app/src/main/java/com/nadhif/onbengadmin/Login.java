package com.nadhif.onbengadmin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nadhif on 12/03/2016.
 */
public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText u, p;
    Button l;
    ContentValues cv;
    ProgressBar progressBar;
    TextView err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        u = (EditText) findViewById(R.id.u);
        p = (EditText) findViewById(R.id.p);
        l = (Button) findViewById(R.id.l);
        l.setOnClickListener(this);

        cv = new ContentValues();

        progressBar = (ProgressBar) findViewById(R.id.pb);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        err = (TextView) findViewById(R.id.err);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.l:
                cv.put("user", u.getText().toString());
                cv.put("pass", p.getText().toString());
                new Login_(this, Helper.url + "main/login/json", cv).execute();
                break;
            default:
                break;
        }
    }

    private class Login_ extends Curl {
        Context context;

        public Login_(Context context, String url, ContentValues cv) {
            super(context, url, cv);
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            err.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getString("ok").equals("1")) {
                    Helper.setSP((Activity) context, "key", json.getString("token"));
                    Helper.setSP((Activity) context, "session", (int) Helper.times() + 180000);
                    startActivity(new Intent(context, Home.class));
                    finish();
                } else {
                    err.setText(json.getString("msg"));
                    err.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
