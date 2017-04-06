package com.dmio.org.tut.activity.list;

import android.content.Context;
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
 * 作者：wangmeng on 2017/3/29 20:23<br/>
 * 邮箱：wangmeng@pycredit.cn
 */

public class ListRefreshRecyclerViewAdapter extends RecyclerView.Adapter<ListRefreshRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Warrior> data;

    public void setData(List<Warrior> data) {
        this.data = data;
    }

    public ListRefreshRecyclerViewAdapter(Context context, List<Warrior> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_refresh_recycler_view_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Warrior warrior = data.get(position);
        holder.tvDesc.setText(warrior.getDesc());
        Glide.with(context).load(warrior.getImgUrl()).crossFade().into(holder.ivPortrait);
    }

    @Override
    public int getItemCount() {
        if (this.data != null) {
            return data.size();
        }
        return 0;
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDesc;
        private final ImageView ivPortrait;

        public ViewHolder(View view) {
            super(view);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            ivPortrait = (ImageView) view.findViewById(R.id.iv_portrait);
        }

    }

}
