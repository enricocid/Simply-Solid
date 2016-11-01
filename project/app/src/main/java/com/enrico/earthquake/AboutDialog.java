package com.enrico.earthquake;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;

import com.afollestad.materialdialogs.MaterialDialog;


public class AboutDialog extends DialogFragment {

    Spanned result;

    public static void show(AppCompatActivity context) {
        AboutDialog dialog = new AboutDialog();
        dialog.show(context.getSupportFragmentManager(), "[ABOUT_DIALOG]");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= 24) {
            result = Html.fromHtml(getString(R.string.about_body), Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(getString(R.string.about_body));
        }

        return new MaterialDialog.Builder(getActivity())
                .title(R.string.about)
                .positiveText(R.string.dismiss)
                .content(result)
                .contentLineSpacing(1.6f)
                .build();
    }
}
