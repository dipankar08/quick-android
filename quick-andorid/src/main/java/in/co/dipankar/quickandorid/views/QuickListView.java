package in.co.dipankar.quickandorid.views;

import android.content.Context;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import in.co.dipankar.quickandorid.R;

import static in.co.dipankar.quickandorid.views.QuickListView.Type.HORIZONTAL;

public class QuickListView extends RelativeLayout {

    public interface Callback {
        void onClick(String id);
        void onLongClick(String id);
    }

    public interface Item{
        String getTitle();
        String getSubTitle();
        String getImageUrl();
        String getId();
    }

    public enum Type{
        VERTICAL,
        HORIZONTAL
    }
    private List<Item> mItemList;
    private Callback mCallback;

    private RecyclerView mRecyclerView;
    private RVAdapter adapter;
    private Context mContext;
    private int mItemLayout;
    private Type mType;

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

    public void init( Callback callback) {
        init(new ArrayList<Item>(), callback, 0, HORIZONTAL);
    }

    public void init(List<Item> items, Callback callback) {
        init(items, callback,0, HORIZONTAL );
    }

    public void init(List<Item> items, Callback callback, int layoutId, Type type) {
        mItemList = items;
        mCallback = callback;
        mItemLayout = layoutId;
        mType = type;
        initItem(items);
    }

    private void initItem(List<Item> items) {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        View mRootView = mInflater.inflate(R.layout.view_quick_list, this, true);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager;
        if(mType == HORIZONTAL) {
            layoutManager
                    = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        } else {
           layoutManager
                    = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        }
        adapter = new RVAdapter(items, mContext,mItemLayout);
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
        int mItemLayout;

        RVAdapter(List<Item> persons, Context c,int mItemLayout) {
            if (persons != null) {
                this.nodes = persons;
            }
            mContext = c;
            this.mItemLayout = mItemLayout;
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
            TextView sl;
            TextView title;
            TextView subtitle;
            ImageView img;

            ItemViewHolder(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.image);
                sl = (TextView) itemView.findViewById(R.id.sl);
                title = (TextView) itemView.findViewById(R.id.title);
                subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            }
        }

        @Override
        public int getItemCount() {
            return nodes.size();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view;
            if(mItemLayout == 0) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_quick_list, viewGroup, false);
            } else{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mItemLayout, viewGroup, false);
            }
            ItemViewHolder pvh = new ItemViewHolder(view);
            return pvh;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder personViewHolder, int i) {
                if(personViewHolder.title != null) {
                    personViewHolder.title.setText(nodes.get(i).getTitle());
                }
                if(personViewHolder.sl != null) {
                    personViewHolder.sl.setText((i+1)+"");
                }
                if(personViewHolder.subtitle != null) {
                    personViewHolder.subtitle.setText(Html.fromHtml(nodes.get(i).getSubTitle()), TextView.BufferType.SPANNABLE);
                }
                if(personViewHolder.img != null) {
                    Glide.with(mContext)
                            .load(nodes.get(i).getImageUrl())
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_unknown)
                                    .centerCrop()
                                    .dontAnimate()
                                    .dontTransform())
                            .into(personViewHolder.img);
                }

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

    public void updateList(List<Item> data){
        adapter.update(data);
    }


}
