package in.co.dipankar.quickandorid.utils;
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

}

