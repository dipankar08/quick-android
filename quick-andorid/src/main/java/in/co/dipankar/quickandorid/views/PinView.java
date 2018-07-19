package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import in.co.dipankar.quickandorid.R;

public class PinView extends LinearLayout {
    private static final String STATE_SUPER_CLASS = "STATE_SUPER_CLASS";
    private static final String STATE_PIN_LENGTH = "STATE_PIN_LENGTH";
    private static final String STATE_PIN_STR = "STATE_PIN_STR";
    private List<Button> mKeyButtons;
    private List<ImageView> mPinDots;
    private ViewGroup mPinDotsHolder;
    ImageButton mClear, mBack;
    private int mCurrentLength = 0;

    private Drawable mEmptyDotDrawableId;
    private Drawable mFullDotDrawableId;
    private StringBuilder mCurPinCode;

    public interface Callback{
        void onPinComplete(String pin);
    }
    private View mRootView;
    private @Nullable  Callback mCallback;

    public PinView(Context context) {
        super(context);
        init(context);
    }

    public PinView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        final LayoutInflater mInflater = LayoutInflater.from(context);
        mRootView = mInflater.inflate(R.layout.view_pin_screen, this, true);
        initKeyButtons();
        initPinDots();
        mCurPinCode = new StringBuilder(mPinDots.size());
    }

    private void initKeyButtons() {
            mKeyButtons = new ArrayList<>();
            mKeyButtons.add((Button) findViewById(R.id.key_0));
            mKeyButtons.add((Button) findViewById(R.id.key_1));
            mKeyButtons.add((Button) findViewById(R.id.key_2));
            mKeyButtons.add((Button) findViewById(R.id.key_3));
            mKeyButtons.add((Button) findViewById(R.id.key_4));
            mKeyButtons.add((Button) findViewById(R.id.key_5));
            mKeyButtons.add((Button) findViewById(R.id.key_6));
            mKeyButtons.add((Button) findViewById(R.id.key_7));
            mKeyButtons.add((Button) findViewById(R.id.key_8));
            mKeyButtons.add((Button) findViewById(R.id.key_9));

            for(final View button : mKeyButtons) {
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonPressed(mKeyButtons.indexOf(button));
                    }
                });
            }
            mClear = findViewById(R.id.clear);
            mClear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClear();
                }
            });

            mBack = findViewById(R.id.back);
            mBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBack();
                }
            });
    }

    private void initPinDots() {
        mPinDotsHolder = findViewById(R.id.pin_dot_holder);
        mPinDots = new ArrayList<>();
        mPinDots.add((ImageView) findViewById(R.id.dotImage1));
        mPinDots.add((ImageView) findViewById(R.id.dotImage2));
        mPinDots.add((ImageView) findViewById(R.id.dotImage3));
        mPinDots.add((ImageView) findViewById(R.id.dotImage4));

        mEmptyDotDrawableId = getResources().getDrawable(R.drawable.circle_empty);
        mFullDotDrawableId = getResources().getDrawable(R.drawable.circle_fill);

    }

    public void refreshUI() {
        for (int i = 0; i < mPinDots.size(); i++) {
            if (mCurrentLength - 1 >= i) {
                mPinDots.get(i).setImageDrawable(mFullDotDrawableId);
            } else {
                mPinDots.get(i).setImageDrawable(mEmptyDotDrawableId);
            }
        }
    }

    private void buttonPressed(int i) {
        if(mCurrentLength > mPinDots.size()){
            return;
        }
        mCurrentLength ++;
        mCurPinCode.append(i);
        refreshUI();
        if(mCurrentLength >= 4){
            mCallback.onPinComplete(mCurPinCode.toString());
        }
    }

    private void onBack(){
        if(mCurrentLength <= 0){
            return;
        }
        mCurrentLength --;
        mCurPinCode.deleteCharAt(mCurrentLength);
        refreshUI();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_SUPER_CLASS, super.onSaveInstanceState());
        bundle.putInt(STATE_PIN_LENGTH, mCurrentLength);
        bundle.putString(STATE_PIN_STR, mCurPinCode.toString());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle)state;
            super.onRestoreInstanceState(bundle
                    .getParcelable(STATE_SUPER_CLASS));
            mCurrentLength = bundle.getInt(STATE_PIN_LENGTH);
            mCurPinCode = new StringBuilder(bundle.getString(STATE_PIN_STR));
            refreshUI();
    } else {
      super.onRestoreInstanceState(state);
            }

    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    public void onClear(){
        mCurrentLength = 0;
        mCurPinCode.delete(0, mCurPinCode.length());
        refreshUI();
    }
    public void shakePinBox(){
        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.pin_shake);
        mPinDotsHolder.startAnimation(shake);
    }
}
