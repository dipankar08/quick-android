package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import in.co.dipankar.quickandorid.R;

public class SegmentedControl extends LinearLayout {
    private int mSegmentCount;
    private List<SegmentedButton> buttons;

    private Callback mCallback;
    public interface Callback{
        void onClicked(int id);
    }
    public void setCallback(Callback callback){
        mCallback = callback;
    }

    public SegmentedControl(Context context) {
        super(context);
        init();
    }

    public SegmentedControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attrsSet(context, attrs);
    }

    public SegmentedControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attrsSet(context, attrs);
    }

    private void init() {
        buttons = new ArrayList<>();

        this.setGravity(Gravity.CENTER | Gravity.TOP);

        for (int i = 0; i < this.getChildCount(); i++) {
            SegmentedButton segmentedButton;
            segmentedButton = (SegmentedButton) this.getChildAt(i);
            segmentedButton.init();
            if (i == 0) {
                segmentedButton.setPosition(SegmentedButton.Position.left);
            }
            else if(i == this.getChildCount() - 1) {
                segmentedButton.setPosition(SegmentedButton.Position.right);
            }
            else{
                segmentedButton.setPosition(SegmentedButton.Position.middle);
            }
            buttons.add(segmentedButton);
            final int finalI = i;
            segmentedButton.setCallback(new SegmentedButton.Callback() {
                @Override
                public void onClicked() {
                    if(mCallback != null){
                        mCallback.onClicked(finalI);
                    }
                }
            });

        }
        buttons.get(0).setSelected(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    protected  void setOthersUnselected(SegmentedButton activeButton){
        for(SegmentedButton segmentedButton : buttons){
            if(activeButton != segmentedButton){
                segmentedButton.setSelected(false);
            }
        }
    }

    private void attrsSet(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SegmentedControl,
                0, 0);

        try {
            mSegmentCount = a.getInt(R.styleable.SegmentedControl_segmentCount, 2);
        } finally {
            a.recycle();
        }
    }
}