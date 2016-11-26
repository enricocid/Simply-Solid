package com.enrico.earthquake;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.CircleView;
import com.afollestad.materialdialogs.color.ColorChooserDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    private static final int REQUEST_CODE = 1;

    //android's Wallpaper Manager
    WallpaperManager myWallpaperManager;

    //color to save to storage
    int colortosave;

    //dynamic TextView
    TextView DynamicText;
    TextView Hint;

    //menu items
    Menu menu;

    //zoom in animation
    Animation zoomIn;
    Animation blink;

    //toolbar
    Toolbar toolbar;

    //floating action button
    FloatingActionButton fab;

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

        //provide back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

        //fab button
        fab = (FloatingActionButton) findViewById(R.id.fab);

        //request permission to save pngs to storage on MM
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

            }
        }

        //on click retrieve the color and save as png
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    colortosave = Utils.retrieveColor(getBaseContext(), MainActivity.this);

                    //retrieve display dimensions
                    int dheight;

                    int dwidth;

                    WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

                    //retrieve display specifications
                    DisplayMetrics d = new DisplayMetrics();
                    window.getDefaultDisplay().getMetrics(d);

                    dwidth = d.widthPixels;
                    dheight = d.heightPixels;

                    //create a bitmap according to the size of the display
                    Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                    Bitmap resizedBitmap = Bitmap.createBitmap(dwidth, dheight, conf);
                    resizedBitmap.eraseColor(colortosave);

                    //this is the name of the image
                    String hexColor = Utils.getColorValues(colortosave);

                    //save image method
                    savebitmap(resizedBitmap, hexColor, view);

                } catch (IOException e) {

                    e.printStackTrace();
                }

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

        DynamicText = (TextView) findViewById(R.id.title);

        DynamicText.post(new Runnable() {
            @Override
            public void run() {
                //set the animation on TextView
                zoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.zoom_in);

                DynamicText.setAnimation(zoomIn);
            }
        });

        Hint = (TextView) findViewById(R.id.hint);

        Hint.post(new Runnable() {
            @Override
            public void run() {
                //set the animation on TextView
                blink = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);

                Hint.setAnimation(blink);
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

    public boolean dir_exists(String dir_path) {
        boolean ret = false;
        File dir = new File(dir_path);
        if (dir.exists() && dir.isDirectory())
            ret = true;
        return ret;
    }

    public File savebitmap(Bitmap bmp, String color, View view) throws IOException {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        //100% quality png
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        //save to Simply Solid folder
        String dir_path = Environment.getExternalStorageDirectory() + File.separator + "SimplySolid";

        //create the directory if it doesn't exists
        if (!dir_exists(dir_path)) {
            File directory = new File(dir_path);
            directory.mkdirs();
        }

        //the name of the image is the hex code
        File f = new File(dir_path

                + File.separator + color + ".png");

        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();

        Snackbar.make(view, color + getString(R.string.saved), Snackbar.LENGTH_LONG)
                .show();

        //refresh media store database
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }

        return f;
    }

    //change colors on app resume
    private void updateColorOnResume() {

        setColor(Utils.retrieveColor(getBaseContext(), this));

        Utils.applyTextColor(DynamicText, Hint, Utils.retrieveColor(getBaseContext(), this));

        Utils.isColorDark(toolbar, fab, this, getBaseContext(), Utils.retrieveColor(getBaseContext(), this));
    }

    //do shit on color selected
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int color) {

        myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        setColor(color);

        Utils.setWallpaper(this, myWallpaperManager, color);

        Utils.sendColor(MainActivity.this, color);

    }

    //close app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //on resume activity do shit
    @Override
    public void onResume() {
        super.onResume();
        updateColorOnResume();

    }
}

