package in.co.dipankar.quickandroidexample;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;

public class FtuxActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftux);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(FtuxActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }
}