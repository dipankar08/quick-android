package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.co.dipankar.quickandorid.R;

public class PinSecurityView extends RelativeLayout{

    @Nullable  private Callback mCallback;
    private Context mContext;
    private View mRootView;

    private View mPinSetupHolder;
    private TextView mPinSetupErrorText;
    private EditText mPinInput;
    private EditText mPinReInput;
    private Button mPinSetupButton;

    private View mPinVerifyHolder;
    private TextView mPinVerifyText;
    private EditText mPinVerifyInput;
    private Button mPinVerifyButton;

    private static String PIN_PREF_KEY = "PIN_PREF_KEY";
    private static String PIN_STRING_KEY = "PIN_STRING_KEY";



    public interface Callback {
        void onPinSetupSuccess();
        void onPinSetupError();
        void onPinVerifySuccess();
        void onPinVerifyError();
    }

    public PinSecurityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public PinSecurityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PinSecurityView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        final LayoutInflater mInflater = LayoutInflater.from(context);
        mRootView = mInflater.inflate(R.layout.view_pin_security, this, true);
        this.setVisibility(GONE);
        mPinSetupHolder = (View) mRootView.findViewById(R.id.pin_setup_holder);
        mPinSetupErrorText = mRootView.findViewById(R.id.pin_setup_error_text);
        mPinInput = mRootView.findViewById(R.id.pin_setup_input);
        mPinReInput = mRootView.findViewById(R.id.pin_setup_reinput);
        mPinSetupButton = mRootView.findViewById(R.id.pin_setup_button);

        mPinVerifyHolder = (View) mRootView.findViewById(R.id.pin_verify_holder);
        mPinVerifyText = mRootView.findViewById(R.id.pin_verify_error_text);
        mPinVerifyInput = mRootView.findViewById(R.id.pin_verify_input);
        mPinVerifyButton = mRootView.findViewById(R.id.pin_verify_button);


        mPinSetupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                savePin(mPinReInput.getText().toString());
                if(mCallback!= null) {
                    mCallback.onPinSetupSuccess();
                }
            }
        });

        mPinReInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String firstPin = mPinInput.getText().toString();
                String secondPin = mPinReInput.getText().toString();
                if(!firstPin.equals(secondPin)){
                    mPinSetupErrorText.setText("Pin mismatch");
                    mPinSetupErrorText.setVisibility(VISIBLE);
                    mPinSetupButton.setEnabled(false);

                } else{
                    mPinSetupErrorText.setVisibility(GONE);
                    mPinSetupButton.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPinVerifyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPin(mPinReInput.getText().toString());
            }
        });
        showPinScreen();
    }

    private void savePin(String pin) {
        SharedPreferences prefs = mContext.getSharedPreferences(PIN_PREF_KEY, Context.MODE_PRIVATE);
        prefs.edit().putString(PIN_STRING_KEY,pin).apply();
    }
    private void verifyPin(String pin) {
        SharedPreferences prefs = mContext.getSharedPreferences(PIN_PREF_KEY, Context.MODE_PRIVATE);
        String savedPin = prefs.getString(PIN_STRING_KEY,"");
        if(savedPin!= null && pin.equals(savedPin)){
            if(mCallback!= null) {
                mCallback.onPinVerifySuccess();
            }
        } else{
            mPinVerifyText.setText("Wrong Pin. Please retry!");
            if(mCallback != null) {
                mCallback.onPinVerifyError();
            }
        }
    }

    public void showPinScreen(){
        SharedPreferences prefs = mContext.getSharedPreferences(PIN_PREF_KEY, Context.MODE_PRIVATE);
        String savedPin = prefs.getString(PIN_STRING_KEY,null);
        if(savedPin == null){
            showPinSetupScreen();
        } else{
            showPinVerifyScreen();
        }
    }
    private void showPinSetupScreen(){
        mPinSetupHolder.setVisibility(VISIBLE);
        mPinVerifyHolder.setVisibility(GONE);
        this.setVisibility(VISIBLE);
    }

    private void showPinVerifyScreen(){
        mPinSetupHolder.setVisibility(GONE);
        mPinVerifyHolder.setVisibility(VISIBLE);
        this.setVisibility(VISIBLE);
    }

}
