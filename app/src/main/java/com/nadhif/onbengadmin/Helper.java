package com.nadhif.onbengadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nadhif on 28/02/2016.
 */
public class Helper {
    public static String url = "http://nadhif.pe.hu/";
//    public static String url = "http://192.168.0.101/onbeng/";

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void log(String s) {
        Log.d("--nadhif", s);
    }

    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static String nownow() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    }

    public static void setSP(Activity activity, String key, String content) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("general", Context.MODE_PRIVATE).edit();
        editor.putString(key, content);
        editor.commit();
    }

    public static void setSP(Activity activity, String key, int content) {
        setSP(activity, key, String.valueOf(content));
    }

    public static String getSP(Activity activity, String key) {
        SharedPreferences sp = activity.getSharedPreferences("general", Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static long times() {
        return new Date().getTime();
    }

    public static void returnExit(final Context context) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirmation")
                .setMessage("Are you sure to exit from application?")
                .setPositiveButton("Yes, I'm Sure", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Stop the activity
                        ((Activity) context).finish();
                        System.exit(0);
                    }

                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }
}
