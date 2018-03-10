package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import in.co.dipankar.quickandorid.R;

public class StateImageButton extends android.support.v7.widget.AppCompatImageButton implements View.OnClickListener{


    private Drawable enableBack, disableBack;
    boolean mViewEnable = true;
    public interface Callback{
        void click( boolean newstate);
    }

    private Callback mCallback;


    public StateImageButton(Context context) {
        super(context);
        init(context, null);
    }

    public StateImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StateImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mViewEnable = true;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateImageButton, 0, 0);
        try {
            enableBack = a.getDrawable(R.styleable.StateImageButton_enableBackground);
            disableBack = a.getDrawable(R.styleable.StateImageButton_disableBackground);
            mViewEnable = a.getBoolean(R.styleable.StateImageButton_is_enable, true);
        } finally {
            a.recycle();
        }
        setViewEnabled(mViewEnable);
        setOnClickListener(this);
    }

    public void setViewEnabled(boolean enabled) {
        if (enabled) {
            if (enableBack != null) {
                this.setImageDrawable(enableBack);
            }
        } else {
            if (disableBack != null) {
                this.setImageDrawable(disableBack);
            }
        }
        mViewEnable = enabled;
    }

    public boolean isViewEnabled() {
        return mViewEnable;
    }

    public void setCallBack(Callback callback){
        mCallback = callback;
    }

    @Override
    public void onClick(View v) {
        mViewEnable = !mViewEnable;
        setViewEnabled(mViewEnable);
        if(mCallback != null){
            mCallback.click(mViewEnable);
        }
    }
}
