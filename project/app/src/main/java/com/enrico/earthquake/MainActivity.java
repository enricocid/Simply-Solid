package com.enrico.earthquake;

import android.app.WallpaperManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.CircleView;
import com.afollestad.materialdialogs.color.ColorChooserDialog;


public class MainActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    //android's Wallpaper Manager
    WallpaperManager myWallpaperManager;

    //dynamic TextView
    TextView DynamicText;

    //menu items
    Menu menu;

    //zoom in animation
    Animation zoomIn;

    //toolbar

    Toolbar toolbar;

    //create the toolbar's menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        getMenuInflater().inflate(R.menu.activity_main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //set the view
        setContentView(R.layout.home);

        //initialize the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //set the toolbar
        setSupportActionBar(toolbar);

        //set the menu's toolbar click listener
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        final int mItemId = item.getItemId();

                        //share button using share intent
                        switch (mItemId) {

                            //about button
                            case R.id.about:

                                //show about dialog
                                Utils.showAbout(MainActivity.this);

                                break;
                        }

                        return false;
                    }
                });

        //set click to the view
        View myview = findViewById(R.id.activity_main);

        myview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openColorChooser();

            }
        });

        DynamicText = (TextView) findViewById(R.id.textview);

        DynamicText.post(new Runnable() {
            @Override
            public void run() {
                //set the animation on TextView
                zoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.zoom_in);

                DynamicText.setAnimation(zoomIn);
            }
        });

    }

    //open material color picker
    private void openColorChooser() {

        new ColorChooserDialog.Builder(this, R.string.color_palette)
                .allowUserColorInputAlpha(false)
                .titleSub(R.string.colors)
                .accentMode(true)
                .doneButton(R.string.md_done_label)
                .cancelButton(R.string.md_cancel_label)
                .backButton(R.string.md_back_label)
                .dynamicButtonColor(true)
                .show();
    }

    //change view colors
    private void setColor(final int color) {

        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {


                if (getSupportActionBar() != null) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
                    getWindow().setNavigationBarColor(color);
                    getWindow().setBackgroundDrawable(new ColorDrawable(color));
                }
            }
        });

    }

    //change colors on app resume
    private void updateColorOnResume() {

        setColor(Utils.retrieveColor(getBaseContext(), this));

        Utils.getColorValues(DynamicText, Utils.retrieveColor(getBaseContext(), this));

        Utils.isColorDark(toolbar, this, getBaseContext(), Utils.retrieveColor(getBaseContext(), this));
    }

    //do shit on color selected
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int color) {

        myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        setColor(color);

        Utils.setWallpaper(this, myWallpaperManager, color);

        Utils.sendColor(MainActivity.this, color);

    }

    //on resume activity do shit
    @Override
    public void onResume() {
        super.onResume();
        updateColorOnResume();

    }
}

