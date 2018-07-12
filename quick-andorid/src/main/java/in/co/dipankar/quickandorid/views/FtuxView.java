package in.co.dipankar.quickandorid.views;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.co.dipankar.quickandorid.R;

public class FtuxView extends RelativeLayout {


    private ImageView mLogo;
    private TextView mAppName;
    private TextView mAppSubtitle;
    private TextView mVersion;

    public FtuxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }
    public FtuxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }
    public FtuxView(Context context) {
        super(context);
        initView(context, null);
    }
    private void initView(Context context, AttributeSet attrs) {
        final LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.view_ftux, this, true);

        mLogo = (ImageView) findViewById(R.id.logo);
        mAppName = (TextView) findViewById(R.id.app_name);
        mAppSubtitle = (TextView) findViewById(R.id.status);
        mVersion = (TextView) findViewById(R.id.version);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FtuxView, 0, 0);
        try {
            Drawable a1;
            a1 = a.getDrawable(R.styleable.FtuxView_logo);
            if (a1 != null) {
                mLogo.setImageDrawable(a1);
            }

            String s1 = a.getString(R.styleable.FtuxView_app_name);
            if (s1 != null) {
                mAppName.setText(s1);
            }

            String s2 = a.getString(R.styleable.FtuxView_status);
            if (s2 != null) {
                mAppSubtitle.setText(s2);
            }
        } catch (Exception e){

        }

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            int ver = pInfo.versionCode;
            mVersion.setText("V-"+version+" ("+ver+")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void setStatus(String status){
        mAppSubtitle.setText(status);
    }
}
