package com.dmio.org.tut.activity.list;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmio.org.tut.R;
import com.dmio.org.tut.data.model.Warrior;

import java.util.List;

/**
 * 功能说明：RecyclerView对应的数据适配器<br/>
 * 作者：Mars.Wong on 2017/3/29 20:23<br/>
 * 邮箱：noneorone@yeah.net
 */

public class ListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Warrior> data;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private final Typeface tf;

    public void setData(List<Warrior> data) {
        this.data = data;
    }

    public ListRecyclerAdapter(Context context, List<Warrior> data) {
        this.data = data;
        this.context = context;
        this.tf = Typeface.createFromAsset(context.getAssets(), "font/DancingScript-Regular.otf");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recycler_view_item, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_recycler_view_footer, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_ITEM && holder instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            Warrior warrior = data.get(position);
            viewHolder.bindData(warrior);
        } else if (viewType == TYPE_FOOTER) {
            //do nothing
        }
    }

    @Override
    public int getItemCount() {
        if (this.data != null && !this.data.isEmpty()) {
            return data.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public final class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDesc;
        private final ImageView ivPortrait;

        public ItemViewHolder(View view) {
            super(view);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            tvDesc.setTypeface(tf);
            ivPortrait = (ImageView) view.findViewById(R.id.iv_portrait);
        }

        public void bindData(Warrior warrior) {
            tvDesc.setText(warrior.getDesc());
            Glide.with(context).load(warrior.getImgUrl()).crossFade().into(ivPortrait);
        }
    }

    public final class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

}
