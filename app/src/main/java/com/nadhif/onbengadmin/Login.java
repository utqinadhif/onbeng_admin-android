package com.nadhif.onbengadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by nadhif on 12/03/2016.
 */
public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText u, p;
    Button l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        u = (EditText) findViewById(R.id.u);
        p = (EditText) findViewById(R.id.p);
        l = (Button) findViewById(R.id.l);
        l.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.l:
                Helper.setSP(this, "key", "U4gaYcqow6tHSCfqAu5O6x8CHz3Cy2XqwmfVpRMszOsgfTWAgczHEAhsuQJgXLXCzYMUwDlrxVSULQqzh1KvmQ==");
                startActivity(new Intent(this, Home.class));
                this.finish();
                break;
            default:
                break;
        }
    }
}
