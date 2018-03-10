package in.co.dipankar.quickandorid.views;


import android.content.Context;
        import android.content.res.TypedArray;
        import android.graphics.Typeface;
        import android.util.AttributeSet;

import in.co.dipankar.quickandorid.R;

public class CustomFontTextView extends android.support.v7.widget.AppCompatTextView {
    private String mFont;
    private Context mContext;

    public CustomFontTextView(Context context) {
        super(context);
        mContext = context;
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView, 0, 0);

        try {
            //TODO verify if the font is set (chashing if not set)
            mFont = a.getString(R.styleable.CustomFontTextView_customfont);
            if (mFont != null) {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + mFont);
                this.setTypeface(typeface);
            }
        } finally {
            a.recycle();
        }
    }

    public String getFont() {
        return mFont;
    }

    public void setFont(String mFont) {
        this.mFont = mFont;
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + mFont);
        this.setTypeface(typeface);
        invalidate();
        requestLayout();
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }
}
