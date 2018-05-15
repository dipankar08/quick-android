package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import in.co.dipankar.quickandorid.R;


public class SegmentedButton extends android.support.v7.widget.AppCompatButton {
    private SegmentedControl placeholder;
    private Position position;
    private static final String blueColorString = "#4172ff";
    private static final String whiteColorString = "#ffffff";


    private boolean selected =false;
    private Callback mCallback;
    public interface Callback{
        void onClicked();
    }
    public void setCallback(Callback callback){
        mCallback = callback;
    }


    public SegmentedButton(Context context) {
        super(context);
    }

    public SegmentedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SegmentedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init() {
        placeholder = (SegmentedControl) this.getParent();
        position = Position.middle;
        setUnselectedLayout();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(true);
                if(mCallback != null) {
                    mCallback.onClicked();
                }
            }
        });
    }

    public void setSelected(boolean selected){
        this.selected = selected;
        if(selected){
            setSelectedLayout();
            placeholder.setOthersUnselected(this);
        }
        else{
            setUnselectedLayout();
        }

    }

    private void setSelectedLayout(){
        switch (position){
            case left:
                this.setBackgroundResource(R.drawable.selected_left);
                break;
            case middle:
                this.setBackgroundResource(R.drawable.selected_middle);
                break;
            case right:
                this.setBackgroundResource(R.drawable.selected_right);
                break;
        }
        this.setTextColor(Color.parseColor(whiteColorString));
    }

    private void setUnselectedLayout(){
        switch (position){
            case left:
                this.setBackgroundResource(R.drawable.unselected_left);
                break;
            case middle:
                this.setBackgroundResource(R.drawable.unselected_middle);
                break;
            case right:
                this.setBackgroundResource(R.drawable.unselected_right);
                break;
        }
        this.setTextColor(Color.parseColor(blueColorString));
    }

    protected void setPosition(Position position){
        this.position = position;
    }

    protected enum Position{
        left,
        middle,
        right
    }
}
