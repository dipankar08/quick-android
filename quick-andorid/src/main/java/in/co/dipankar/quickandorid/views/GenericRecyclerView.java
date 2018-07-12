package in.co.dipankar.quickandorid.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import in.co.dipankar.quickandorid.R;

public class GenericRecyclerView <T , VH extends GenericRecyclerView.BaseHolder> extends RelativeLayout {

    // -- Action Callbacks
    public interface Callback<T> {
        void onItemClick(T item);
        void onViewInItemClicked(T item, int viewID);
    }

    public abstract class BaseHolder extends RecyclerView.ViewHolder {
        public BaseHolder(View itemView) {
            super(itemView);
        }
        public abstract void onBind(T item, Callback listener);
        public abstract BaseHolder newObject();
    }

    private List<T> mItemList;
    private Callback mCallback;

    private RecyclerView mRecyclerView;
    private GenericAdapter<T> adapter;
    Class<VH> mViewHolderClass;
    private Context mContext;
    private int mItemLayoutID;

    public GenericRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public GenericRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public GenericRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public GenericRecyclerView(Context context, Class<VH> mViewHolderClass) {
        super(context);
        initView(context);
    }

    private void initView(final Context context) {
        mContext = context;
        mItemList = new ArrayList<>();
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        View mRootView = mInflater.inflate(R.layout.view_quick_list, this, true);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);


        adapter = new GenericAdapter<T>(context,mItemList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(context).inflate(mItemLayoutID, parent, false);
                try {
                    Constructor<VH> constructor = mViewHolderClass.getConstructor(View.class);
                    return constructor.newInstance(view);
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder1, T val) {
                T userModel = val;
                VH holder = (VH)holder1;
                holder.onBind(userModel, mCallback);
            }
        };
        mRecyclerView.setAdapter(adapter);
    }


    public void addCallback(Callback callback) {
        mCallback = callback;
    }

    private abstract class GenericAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private List<T> items;


        public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);
        public abstract void onBindData(RecyclerView.ViewHolder holder, T val);

        public GenericAdapter(Context context, List<T> items){
            this.context = context;
            this.items = items;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = setViewHolder(parent);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            onBindData(holder,items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItems( ArrayList<T> savedCardItemz){
            items = savedCardItemz;
            this.notifyDataSetChanged();
        }

        public T getItem(int position){
            return items.get(position);
        }
    }
}
