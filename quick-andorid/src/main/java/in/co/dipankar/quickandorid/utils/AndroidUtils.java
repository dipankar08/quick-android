package in.co.dipankar.quickandorid.utils;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import in.co.dipankar.quickandorid.BuildConfig;

// This utils should be independent of Context
public class AndroidUtils {

    private static AndroidUtils sAndroidUtils;
    public static AndroidUtils Get() {
        if (sAndroidUtils == null) {
            sAndroidUtils = new AndroidUtils();
        }
        return sAndroidUtils;
    }
    public static boolean isDebug() {
        return isDebugInternal();
    }

    private static boolean isDebugInternal() {
        if (BuildConfig.DEBUG) {
            return true;
        } else {
            return false;
        }
    }

    public static void RateIt(Context context){
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context. startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

}

