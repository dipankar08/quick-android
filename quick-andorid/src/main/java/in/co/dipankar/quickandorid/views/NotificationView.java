package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
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

public class NotificationView extends RelativeLayout {

    @Nullable
    private Callback mCallback;
    private AnswerCallback mAnswerCallback;
    private AlertCallback mAlertCallback;

    private TextView mTextView;
    private Button mAccept, mReject;
    private View mButtonPanel;
    private Handler mHandler;
    public interface Callback {
        void onOpen();
        void onClose();
    }
    public interface AnswerCallback{
        void onAccept();
        void onReject();
    }

    public interface AlertCallback{
        void onOK();
    }

    public NotificationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NotificationView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mHandler=new Handler();
        final LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.view_notification, this, true);
        mTextView = findViewById(R.id.text);
        mAccept = findViewById(R.id.accept);
        mReject = findViewById(R.id.reject);
        mButtonPanel = findViewById(R.id.buttonPanel);
        mAccept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerCallback != null){
                    mAnswerCallback.onAccept();
                }
                if(mAlertCallback != null){
                    mAlertCallback.onOK();
                }
                hide();
            }
        });
        mReject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerCallback != null){
                    mAnswerCallback.onReject();
                }
                hide();
            }
        });
        mButtonPanel.setVisibility(GONE);
        this.setVisibility(GONE);
    }

    public void showSuccess(String msg){
        showSuccess(msg, null, 10);
    }
    public void showSuccess(String msg, Callback callback, int autoCloseinSec){
        mCallback = callback;
        mTextView.setText(msg);
        mTextView.setBackgroundColor(Color.parseColor("#1e4f18"));
        mButtonPanel.setVisibility(GONE);
        this.setVisibility(VISIBLE);
        if(mCallback != null){
            mCallback.onOpen();
        }
        autoHide(autoCloseinSec);
    }

    public void showInfo(String msg){
        showInfo(msg, null, 10);
    }
    public void showInfo(String msg, Callback callback, int autoCloseinSec){
        mCallback = callback;
        mTextView.setText(msg);
        mTextView.setBackgroundColor(Color.parseColor("#005c73"));
        mButtonPanel.setVisibility(GONE);
        this.setVisibility(VISIBLE);
        if(mCallback != null){
            mCallback.onOpen();
        }
        autoHide(autoCloseinSec);
    }
    public void showError(String msg){
        showError(msg, null, 10);
    }
    public void showError(String msg, Callback callback, int autoCloseinSec){
        mCallback = callback;
        mTextView.setText(msg);
        mTextView.setBackgroundColor(Color.parseColor("#a30000"));
        mButtonPanel.setVisibility(GONE);
        this.setVisibility(VISIBLE);
        if(mCallback != null){
            mCallback.onOpen();
        }
        autoHide(autoCloseinSec);
    }

    public void ask(String question, AnswerCallback answerCallback) {
        ask(question, answerCallback, "Accept","Reject");
    }
    public void ask(String question, AnswerCallback answerCallback, String accText, String rejText){
        mAnswerCallback = answerCallback;
        mAccept.setText(accText);
        mReject.setText(rejText);
        mReject.setVisibility(VISIBLE);
        mTextView.setText(question);
        mTextView.setBackgroundColor(Color.parseColor("#1f0066"));
        mButtonPanel.setVisibility(VISIBLE);
        this.setVisibility(VISIBLE);
        if(mCallback != null){
            mCallback.onOpen();
        }
    }


    public void showAlert(String question, AlertCallback answerCallback) {
        showAlert(question, answerCallback, "OK");
    }

    public void showAlert(String question, AlertCallback answerCallback, String accText){
        mAlertCallback = answerCallback;
        mAccept.setText(accText);
        mReject.setVisibility(GONE);
        mTextView.setText(question);
        mTextView.setBackgroundColor(Color.parseColor("#1f0066"));
        mButtonPanel.setVisibility(VISIBLE);
        this.setVisibility(VISIBLE);
        if(mCallback != null){
            mCallback.onOpen();
        }
    }

    public void updateText(String msg){
        mTextView.setText(msg);
    }
    
    private void autoHide(int autoCloseinSec){
        if(autoCloseinSec > 0){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hide();
                }
            }, autoCloseinSec * 1000);
        }
    }
    public void hide(){
        mButtonPanel.setVisibility(GONE);
        this.setVisibility(GONE);
        if(mCallback != null){
            mCallback.onClose();
        }
    }
}
