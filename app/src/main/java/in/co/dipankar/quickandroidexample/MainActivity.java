package in.co.dipankar.quickandroidexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.co.dipankar.quickandorid.utils.DLog;
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
    }
}
