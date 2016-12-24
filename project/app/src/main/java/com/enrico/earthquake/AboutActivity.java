package com.enrico.earthquake;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    //get all the TextViews
    TextView dev, enricoGit, enricoGplus, credits, ivanGit, ivanGplus, stack, maticons, lucas, appCompat, colorPicker, libraries, appInfo, build, sources;

    //Get all the Stings
    String enricoGitPage, enricoGplusPage, ivanGitPage, ivanGplusPage, stackPage, maticonsPage, lucasPage, appCompatPage, colorPickerPage, version, appGit;

    //method to set clickable links
    @SuppressWarnings("deprecation")
    static Spanned setTextLinks(String htmlText, TextView... textViews) {

        for (TextView links : textViews) {
            links.setMovementMethod(LinkMovementMethod.getInstance());
        }
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);

        } else {
            return Html.fromHtml(htmlText);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        //set the toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //provide back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String colon = ": ";

        //get Titles
        dev = (TextView) findViewById(R.id.dev);

        credits = (TextView) findViewById(R.id.credits);

        libraries = (TextView) findViewById(R.id.libs);

        appInfo = (TextView) findViewById(R.id.app_info);

        //set text options to titles
        setTextUnderline(dev, credits, libraries, appInfo);

        //Developer info
        //git page
        enricoGit = (TextView) findViewById(R.id.enrico_git);

        enricoGitPage = "<a href='https://github.com/enricocid'>github";

        enricoGit.setText(setTextLinks(enricoGitPage, enricoGit));

        //gplus page
        enricoGplus = (TextView) findViewById(R.id.enrico_gplus);

        enricoGplusPage = "<a href='https://plus.google.com/u/0/+EnricoDortenzio/'>google+";

        enricoGplus.setText(setTextLinks(enricoGplusPage, enricoGplus));

        //Credits
        //Ivan

        //git page
        ivanGit = (TextView) findViewById(R.id.ivan_git);

        ivanGitPage = "<a href='https://github.com/ivn888/'>github";

        ivanGit.setText(setTextLinks(ivanGitPage, ivanGit));

        //gplus page
        ivanGplus = (TextView) findViewById(R.id.ivan_gplus);

        ivanGplusPage = "<a href='https://plus.google.com/u/0/+ivandortenzio/'>google+";

        ivanGplus.setText(setTextLinks(ivanGplusPage, ivanGplus));

        //stackoverflow
        stack = (TextView) findViewById(R.id.stack_page);

        stackPage = "<a href='http://stackoverflow.com/'>home";

        stack.setText(setTextLinks(stackPage, stack));

        //materialdesignicons.com
        maticons = (TextView) findViewById(R.id.maticons_page);

        maticonsPage = "<a href='https://materialdesignicons.com/'>home";

        maticons.setText(setTextLinks(maticonsPage, maticons));

        //Lucas Urbas
        lucas = (TextView) findViewById(R.id.lucas_page);

        lucasPage = "<a href='https://medium.com/@lucasurbas/making-android-toolbar-responsive-2627d4e07129/'>responsive toolbar";

        lucas.setText(setTextLinks(lucasPage, lucas));

        //Libraries used
        //appcompat
        appCompat = (TextView) findViewById(R.id.appcompat_page);

        appCompatPage = "<a href='https://developer.android.com/topic/libraries/support-library/features.html#v7-appcompat/'>page";

        appCompat.setText(setTextLinks(appCompatPage, appCompat));

        //color picker by myself medesimo
        colorPicker = (TextView) findViewById(R.id.colorpicker_page);

        colorPickerPage = "<a href='https://github.com/enricocid/Color-picker-library'>github";

        colorPicker.setText(setTextLinks(colorPickerPage, colorPicker));

        //Application info
        //set build version
        version = BuildConfig.VERSION_NAME;

        build = (TextView) findViewById(R.id.version);

        build.setText(getString(R.string.version) + colon + version);

        //set application git page
        sources = (TextView) findViewById(R.id.sources);

        appGit = "<a href='https://github.com/enricocid/Simply-Solid'>github";

        sources.setText(setTextLinks(appGit, sources));

        //set text options
        setTextOptions(enricoGit, enricoGplus, ivanGit, build, sources);
    }

    //set text options
    public void setTextOptions(TextView... textView) {

        for (TextView body : textView) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                body.setElegantTextHeight(true);
            }
        }
    }

    //method to style headlines
    public void setTextUnderline(TextView... textView) {

        for (TextView title : textView) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                title.setElegantTextHeight(true);
                title.setElevation(6);
                title.setFontFeatureSettings("smcp"); //small caps
            }
            title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
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
}
