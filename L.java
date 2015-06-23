package com.ebookfrenzy.threadactivity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Pinchas Frieder on 6/22/2015.
 */
public class L {
    public static void m(String message) {
        Log.d("pinchas", message);
    }

    public static void s(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}