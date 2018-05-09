package in.co.dipankar.quickandorid.views;

import android.content.Context;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.co.dipankar.quickandorid.R;

public class QuickListView extends RelativeLayout {

    public interface Callback {
        void onClick(String id);
        void onLongClick(String id);
    }

    public interface Item{
        String getTitle();
        String getImageUrl();
        String getId();
    }

    private List<Item> mItemList;
    private Callback mCallback;

    private RecyclerView mRecyclerView;
    private RVAdapter adapter;
    private Context mContext;

    public QuickListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public QuickListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public QuickListView(Context context) {
        super(context);
        initView(context);
    }

    public void init(List<Item> items, Callback callback) {
        mItemList = items;
        mCallback = callback;
        initItem(items);
    }

    private void initItem(List<Item> items) {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        View mRootView = mInflater.inflate(R.layout.view_quick_list, this, true);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        adapter = new RVAdapter(items, mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(mCallback != null){
                            mCallback.onClick(mItemList.get(position).getId());
                        }
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        if(mCallback != null){
                            mCallback.onLongClick(mItemList.get(position).getId());
                        }
                    }
                })
        );
    }

    private void initView(Context context) {
        mContext = context;
    }


    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.ItemViewHolder> {
        List<Item> nodes = new ArrayList<>();
        Context mContext;

        RVAdapter(List<Item> persons, Context c) {
            if (persons != null) {
                this.nodes = persons;
            }
            mContext = c;
        }

        public Item getItem(int idx) {
            if(nodes != null && idx >= 0 && idx <nodes.size()){
                return nodes.get(idx);
            } else
            {
                return null;
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            ImageView img;

            ItemViewHolder(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.image);
                title = (TextView) itemView.findViewById(R.id.title);
            }
        }

        @Override
        public int getItemCount() {
            return nodes.size();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v =
                    LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_quick_list, viewGroup, false);
            ItemViewHolder pvh = new ItemViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder personViewHolder, int i) {
                personViewHolder.title.setText(nodes.get(i).getTitle());
                Glide.with(mContext)
                        .load(nodes.get(i).getImageUrl())
                        .into(personViewHolder.img);
        }

        public void update(List<Item> datas) {
            if (datas == null) return;
            if (nodes != null && nodes.size() >= 0) {
                nodes.clear();
            }
            nodes.addAll(datas);
            notifyDataSetChanged();
        }
    }


}
