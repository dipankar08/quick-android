package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.co.dipankar.quickandorid.R;
import in.co.dipankar.quickandorid.utils.DLog;

public class SegmentedControl extends LinearLayout {

    private static final Object TAG ="SegmentedControl" ;
    private List<CharSequence> options;

    public SegmentedControl(Context context) {
        super(context);
        init(context, null);
    }

    public SegmentedControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SegmentedControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SegmentedControl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray attributesArray = getContext().obtainStyledAttributes( attrs, R.styleable.SegmentedControl, 0, 0 );

        try {
            CharSequence[] optionsArray = attributesArray.getTextArray( R.styleable.SegmentedControl_options );
            options = Arrays.asList( optionsArray );
        }
        catch (Resources.NotFoundException e) {
            DLog.d(e.getMessage() );
        }
        finally {
            attributesArray.recycle();
        }
    }

}
