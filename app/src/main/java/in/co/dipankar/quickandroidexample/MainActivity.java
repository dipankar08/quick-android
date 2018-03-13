package in.co.dipankar.quickandroidexample;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.co.dipankar.quickandorid.utils.DLog;
import in.co.dipankar.quickandorid.utils.RuntimePermissionUtils;
import in.co.dipankar.quickandorid.utils.SharedPrefsUtil;
import in.co.dipankar.quickandorid.views.MultiStateImageButton;
import in.co.dipankar.quickandorid.views.StateImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StateImageButton state1 = findViewById(R.id.state1);


        state1.setCallBack(new StateImageButton.Callback() {
            @Override
            public void click(boolean newstate) {
                DLog.d("New State"+newstate);
            }
        });


        MultiStateImageButton state2 = findViewById(R.id.state2);
        state2.setCallBack(new MultiStateImageButton.Callback() {
            @Override
            public void click(int newstate) {
                DLog.d("New State"+newstate);
            }
        });
        testSF();
        testRP();
    }

    private void testRP() {
        RuntimePermissionUtils.getInstance().init(this);
        RuntimePermissionUtils.getInstance().askPermission(new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, new RuntimePermissionUtils.CallBack() {
            @Override
            public void onSuccess() {
                DLog.d("Test Run Permission Accepted");
            }

            @Override
            public void onFail() {
                DLog.d("Test Run Permission Denied");
            }
        });
    }

    private void testSF() {
        SharedPrefsUtil.getInstance().init(this);
        SharedPrefsUtil.getInstance().setBoolean("bool",true);
        SharedPrefsUtil.getInstance().setString("str","Dipankar");
        SharedPrefsUtil.getInstance().setInt("int",10);
        DLog.d("Test Bool:"+(SharedPrefsUtil.getInstance().getBoolean("bool",false) == true));
        DLog.d("Test Str:"+(SharedPrefsUtil.getInstance().getString("str","").equals("Dipankar")));
        DLog.d("Test Bool:"+(SharedPrefsUtil.getInstance().getInt("int",-1) == 10));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        RuntimePermissionUtils.getInstance().onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

}
