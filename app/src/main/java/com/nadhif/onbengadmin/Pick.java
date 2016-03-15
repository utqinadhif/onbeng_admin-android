package com.nadhif.onbengadmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Pick extends AppCompatActivity implements View.OnClickListener {
    ImageButton pick;
    TextView latlng, status;
    Vibrator vibrator;
    ExGps exgps;
    boolean start, issound, isvibrate;
    Location lc;
    Uri uri;
    Ringtone ringtone;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Pick Location");

        intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name != null) {
            getSupportActionBar().setSubtitle("For " + name);
        }

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        latlng = (TextView) findViewById(R.id.latlng);
        status = (TextView) findViewById(R.id.status);
        pick = (ImageButton) findViewById(R.id.pick);
        pick.setOnClickListener(this);
        pick.setClickable(false);

        start = true;

        exgps = new ExGps(this);

        try {
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            ringtone = RingtoneManager.getRingtone(this, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sound = Helper.getSP(this, "sound");
        if (sound != null) {
            if (sound.equals("1")) {
                issound = true;
            }
        }

        String vibrate = Helper.getSP(this, "vibrate");
        if (vibrate != null) {
            if (vibrate.equals("1")) {
                isvibrate = true;
            }
        }
    }

    public void cekGps() {
        exgps.getLocation();
        if (!exgps.canGetLocation()) {
            exgps.showSettingsAlert();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.cekGps();
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
    public void onBackPressed() {
        exgps.stopUsingGPS();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pick:
                exgps.stopUsingGPS();
                Intent rIntent = new Intent();
                rIntent.putExtra("lat", String.valueOf(lc.getLatitude()));
                rIntent.putExtra("lng", String.valueOf(lc.getLongitude()));
                setResult(Activity.RESULT_OK, rIntent);
                finish();
                break;
            default:
                break;

        }
    }

    protected void setText(double lt, double lg) {
        latlng.setText(String.valueOf(lt) + "," + String.valueOf(lg));
    }

    private class ExGps extends Gps {
        Context context;

        public ExGps(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public void onLocationChanged(Location location) {
            Helper.checkLogin(context);
            lc = location;
            if (isvibrate) {
                vibrator.vibrate(100);
            }
            if (issound) {
                ringtone.play();
            }
            setText(location.getLatitude(), location.getLongitude());
            if (start) {
                pick.setImageResource(R.drawable.image);
                pick.setClickable(true);
                status.setText("Pick Now");
                start = false;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == 2) {
                exgps.useGPS();
            } else {
                exgps.useNetwork();
                exgps.useGPS();
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            exgps.getLocation();
        }
    }
}
