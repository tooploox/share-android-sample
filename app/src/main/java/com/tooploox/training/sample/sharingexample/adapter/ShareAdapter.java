package com.tooploox.training.sample.sharingexample.adapter;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tooploox.training.sample.sharingexample.R;

import java.util.List;

/**
 * Created by Damian Walczak on 14/05/15.
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.Holder> {

    public interface OnShareAppClickedListener {
        void onClick(ResolveInfo pickedAppInfo);
    }

    protected class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivIcon;
        TextView tvAppName;

        OnShareAppClickedListener itemClickListener;

        public Holder(View itemView, OnShareAppClickedListener itemClickListener) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvAppName = (TextView) itemView.findViewById(R.id.tv_app_name);
            itemView.setOnClickListener(this);
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(apps.get(getPosition()));
        }
    }

    private final Context context;
    private final LayoutInflater inflater;
    private final List<ResolveInfo> apps;
    private final OnShareAppClickedListener itemClickListener;

    public ShareAdapter(Context context, List<ResolveInfo> apps, OnShareAppClickedListener itemClickListener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.apps = apps;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int itemIndex) {
        return new Holder(inflater.inflate(R.layout.share_item, viewGroup, false), itemClickListener);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int itemIndex) {
        ResolveInfo resolveInfo = apps.get(itemIndex);
        viewHolder.ivIcon.setImageDrawable(resolveInfo.loadIcon(context.getPackageManager()));
        viewHolder.tvAppName.setText(resolveInfo.loadLabel(context.getPackageManager()));
    }

    @Override
    public int getItemCount() {
        return apps != null ? apps.size() : 0;
    }
}
