package eu.ways4.newsmaniac;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import eu.ways4.newsmaniac.R;

import java.util.Objects;

public class AppUtils {


    public static String selCountry;

    public static void setCountry(String sCountry) {
        selCountry = sCountry;
    }

    public static String getCountry() {
        if (selCountry == null){
            return "de";
        }
        else return selCountry;
    }

    public static void setToolbarTitle(String title, AppCompatActivity context) {
        Objects.requireNonNull(context.getSupportActionBar()).setTitle(title);
    }

    public static void shareNewsTitle(Context context, Activity activity, String title) {
        context.startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText(title)
                .getIntent(), context.getString((R.string.news_headline))));
    }
}
