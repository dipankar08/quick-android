package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import in.co.dipankar.quickandorid.R;

public class MultiStateImageButton extends android.support.v7.widget.AppCompatImageButton implements View.OnClickListener{

        int  currentState = 0;
    List<Drawable> listBackgroud;
    public interface Callback{
        void click( int newstate);
    }

    private Callback mCallback;
    public MultiStateImageButton(Context context) {
        super(context);
        init(context, null);
    }

    public MultiStateImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MultiStateImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
         currentState = 1;
        listBackgroud = new ArrayList<Drawable>();
        setOnClickListener(this);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MultiStateImageButton, 0, 0);
        try {
            Drawable a1;
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState0);
            if(a1 != null){
                listBackgroud.add(a1);
            }
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState1);
            if(a1 != null){
                listBackgroud.add(a1);
            }
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState2);
            if(a1 != null){
                listBackgroud.add(a1);
            }
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState3);
            if(a1 != null){
                listBackgroud.add(a1);
            }
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState4);
            if(a1 != null){
                listBackgroud.add(a1);
            }
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState5);
            if(a1 != null){
                listBackgroud.add(a1);
            }
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState6);
            if(a1 != null){
                listBackgroud.add(a1);
            }
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState7);
            if(a1 != null){
                listBackgroud.add(a1);
            }
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState8);
            if(a1 != null){
                listBackgroud.add(a1);
            }
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState9);
            if(a1 != null){
                listBackgroud.add(a1);
            }
            a1= a.getDrawable(R.styleable.MultiStateImageButton_backgroundState10);
            if(a1 != null){
                listBackgroud.add(a1);
            }

            currentState = a.getInteger(R.styleable.MultiStateImageButton_current_state, 0);
        } finally {
            a.recycle();
        }
        setState(currentState);
    }

    public void setState(int idx) {
        currentState = idx;
        this.setImageDrawable(listBackgroud.get(currentState));
    }

    public int getState() {
        return currentState;
    }

    public void setCallBack(Callback callback){
        mCallback = callback;
    }

    @Override
    public void onClick(View v) {
        currentState = (currentState+1)%listBackgroud.size();
        setState(currentState);
        if(mCallback != null){
            mCallback.click(currentState);
        }
    }
}
