package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.AttributeSet;


public class VersionView extends android.support.v7.widget.AppCompatTextView {
    public VersionView(Context context) {
        super(context);
        setVersion();
    }

    public VersionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVersion();
    }

    public VersionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setVersion();
    }

    private void setVersion() {
        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            setText("V" + version + " (Release - " + verCode + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            setText("V-e.0");
        }
    }
}