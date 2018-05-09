package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import in.co.dipankar.quickandorid.R;

public class SliderView extends RelativeLayout {



    public interface Callback {
        void onSkip();
        void onNext();
        void onClose();
    }

    public interface Item{
         String getTitle();
         String getSubTitle();
         int getImageId();
        String getBackgroundColor();
    }

    private List<Item> mItemList;
    private Callback mCallback;
    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private TextView[]dotstv;
    private Button btnSkip;
    private Button btnNext;
    private MyPagerAdapter pagerAdapter;
    private Context mContext;

    public SliderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SliderView(Context context) {
        super(context);
        initView(context);
    }

    public void init(List<Item> items, Callback callback) {
        mItemList = items;
        mCallback = callback;
        initItem(items);
    }

    private void initItem(List<Item> items) {
        //layouts = new int[]{R.layout.slider_1,R.layout.slider_2, R.layout.slider_3, R.layout.slider_4};
        pagerAdapter = new MyPagerAdapter(items,mContext);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position == mItemList.size()-1){
                    //LAST PAGE
                    btnNext.setText("START");
                    btnSkip.setVisibility(View.GONE);
                }else {
                    btnNext.setText("NEXT");
                    btnSkip.setVisibility(View.VISIBLE);
                }
                setDotStatus(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);
    }

    private void initView(Context context) {
        mContext = context;
        final LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.view_slider, this, true);

        viewPager = findViewById(R.id.view_pager);
        layoutDot = findViewById(R.id.dotLayout);
        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);

        //When user press skip, start Main Activity
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCallback != null){
                    mCallback.onClose();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage = viewPager.getCurrentItem()+1;
                if(currentPage < mItemList.size()) {
                    //move to next page
                    viewPager.setCurrentItem(currentPage);
                } else {
                    if(mCallback != null){
                        mCallback.onClose();
                    }
                }
            }
        });

    }

    private void setDotStatus(int page){
        layoutDot.removeAllViews();
        dotstv =new TextView[mItemList.size()];
        for (int i = 0; i < dotstv.length; i++) {
            dotstv[i] = new TextView(getContext());
            dotstv[i].setText(Html.fromHtml("&#8226;"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDot.addView(dotstv[i]);
        }
        //Set current dot active
        if(dotstv.length>0){
            dotstv[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private  class MyPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        List<Item> itemList;
        private Context context;

        public MyPagerAdapter(List<Item> items, Context context) {
            this.itemList = items;
            this.context = context;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.view_slider_item, container, false);
            Item item = itemList.get(position);
            ((TextView)v.findViewById(R.id.title)).setText(item.getTitle());
            ((TextView)v.findViewById(R.id.sub_title)).setText(item.getSubTitle());
            ((View)v.findViewById(R.id.container)).setBackgroundColor(Color.parseColor(item.getBackgroundColor()));
            ((ImageView)v.findViewById(R.id.icon)).setImageResource(item.getImageId());
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View)object;
            container.removeView(v);
        }
    }

}
