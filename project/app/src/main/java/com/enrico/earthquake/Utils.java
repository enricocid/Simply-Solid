package com.enrico.earthquake;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

class Utils {

    //set the solid color
    static void setWallpaper(Activity activity, WallpaperManager myWallpaperManager, int color) {

        try {

            myWallpaperManager.clear();
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;

            Bitmap bmp = Bitmap.createBitmap(1, 1, conf);

            bmp.eraseColor(color);

            myWallpaperManager.setBitmap(bmp);

        } catch (IOException e) {

            e.printStackTrace();
        }

        activity.recreate();
    }

    static void getColorValues(TextView textView, int color) {

        String hexColor = String.format("#%06X", (0xFFFFFF & color));

        textView.setText(hexColor);

        textView.setTextColor(getComplementaryColor(color));

    }

    //function to invert color value
    private static int getComplementaryColor(int colorToInvert) {

        float r = Color.red(colorToInvert);
        float g = Color.green(colorToInvert);
        float b = Color.blue(colorToInvert);

        float newr = 255 - r;
        float newg = 255 - g;
        float newb = 255 - b;

        int red = Math.round(newr);

        int green = Math.round(newg);

        int blue = Math.round(newb);

        return android.graphics.Color.argb(255, red, green, blue);
    }

    //method to change the toolbar's title color
    private static void changeTitleColor(Toolbar toolbar, int color) {

        toolbar.setTitleTextColor(color);

    }

    //method to change the toolbar's overflow icon
    private static void changeOverflowIcon(Toolbar toolbar, Drawable icon) {

        toolbar.setOverflowIcon(icon);

    }

    //method to apply light status bar
    private static void applyLightIcons(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {

            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }

    //determine if solid color is light or dark to apply proper colors to toolbar and statusbar
    static boolean isColorDark(Toolbar toolbar, Activity activity, Context context, int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;

        if (darkness < 0.5) {

            applyLightIcons(activity);

            changeTitleColor(toolbar, ContextCompat.getColor(context, android.R.color.secondary_text_light));

            changeOverflowIcon(toolbar, ContextCompat.getDrawable(activity, R.drawable.ic_dots_dark));

        } else {

            changeTitleColor(toolbar, ContextCompat.getColor(context, android.R.color.white));

            changeOverflowIcon(toolbar, ContextCompat.getDrawable(activity, R.drawable.ic_dots));

        }
        return true;
    }

    //save one color to shared preferences
    static void sendColor(Activity activity, Integer color) {

        SharedPreferences prefs;

        prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        prefs.edit()
                .clear()
                .apply();

        prefs.edit()
                .putString("color", Integer.toString(color))
                .apply();
    }

    //method to retrive the saved color
    static int retrieveColor(Context context, Activity activity) {

        SharedPreferences prefs;

        prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        String value = prefs.getString("color", Integer.toString(ContextCompat.getColor(context, R.color.colorPrimary)));

        return Integer.parseInt(value);
    }

    //show about dialog
    static void showAbout(AppCompatActivity activity) {

        AboutDialog.show(activity);

    }
}