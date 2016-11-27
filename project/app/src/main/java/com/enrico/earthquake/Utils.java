package com.enrico.earthquake;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
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

    //convert color to hex string
    static String getColorValues(int color) {

        return String.format("#%06X", (0xFFFFFF & color));

    }

    //apply color to home text views
    static void applyTextColor(TextView title, TextView hint, int color) {

        title.setText(getColorValues(color));
        title.setTextColor(getComplementaryColor(color));

        int opaqueColor = (getComplementaryColor(color) & 0x00FFFFFF) | 0x80000000;

        hint.setTextColor(opaqueColor);

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
    private static void changeTitleColor(final Toolbar toolbar, final int color) {

        toolbar.post(new Runnable() {

            @Override
            public void run() {
                toolbar.setTitleTextColor(color);

            }
        });

    }

    //method to change the fab color
    private static void changeFabColor(final FloatingActionButton fab, final int color) {

        fab.post(new Runnable() {

            @Override
            public void run() {
                fab.setBackgroundTintList(ColorStateList.valueOf(color));
                fab.setColorFilter(getComplementaryColor(color));
            }
        });
    }

    //method to change the toolbar's overflow icon
    private static void changeOverflowIcon(final Toolbar toolbar, final Drawable icon) {

        toolbar.post(new Runnable() {

            @Override
            public void run() {
                toolbar.setOverflowIcon(icon);
            }
        });
    }

    //method to change the toolbar's navigation icon
    private static void changeNavigationIcon(final Toolbar toolbar, final Drawable icon) {

        toolbar.post(new Runnable() {

            @Override
            public void run() {
                toolbar.setNavigationIcon(icon);
            }
        });
    }

    //method to apply light status bar
    private static void applyLightIcons(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {

            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }

    //determine if solid color is light or dark to apply proper colors to toolbar and statusbar
    static boolean isColorDark(final Toolbar toolbar, final FloatingActionButton fab, final Activity activity, final Context context, final int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;

        if (darkness < 0.5) {

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    applyLightIcons(activity);

                    //change toolbar shit
                    changeOverflowIcon(toolbar, ContextCompat.getDrawable(activity, R.drawable.ic_dots_dark));
                    changeNavigationIcon(toolbar, ContextCompat.getDrawable(activity, R.drawable.ic_close));
                    changeTitleColor(toolbar, ContextCompat.getColor(context, android.R.color.secondary_text_light));

                    //change fab color
                    changeFabColor(fab, Color.BLACK);
                }
            });

        } else {

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    //change toolbar shit
                    changeOverflowIcon(toolbar, ContextCompat.getDrawable(activity, R.drawable.ic_dots));
                    changeNavigationIcon(toolbar, ContextCompat.getDrawable(activity, R.drawable.ic_close_dark));
                    changeTitleColor(toolbar, ContextCompat.getColor(context, android.R.color.white));

                    //change fab color
                    changeFabColor(fab, Color.WHITE);
                }
            });
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