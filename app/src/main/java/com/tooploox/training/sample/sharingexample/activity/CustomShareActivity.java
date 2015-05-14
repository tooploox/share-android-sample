package com.tooploox.training.sample.sharingexample.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tooploox.training.sample.sharingexample.R;

import java.util.ArrayList;
import java.util.List;

public class CustomShareActivity extends AppCompatActivity {

    public interface OnItemClickListener {
        void onClick(ResolveInfo pickedAppInfo);
    }

    private static class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.Holder> {

        protected class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView ivIcon;
            TextView tvAppName;

            OnItemClickListener itemClickListener;

            public Holder(View itemView, OnItemClickListener itemClickListener) {
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
        private final OnItemClickListener itemClickListener;

        ShareAdapter(Context context, List<ResolveInfo> apps, OnItemClickListener itemClickListener) {
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

    public static final String MESSAGE_INTENT_EXTRA = "message-intent-extra";
    public static final String APPS_LIST_EXTRA = "apps-list-extra";

    RecyclerView rvShareApps;
    ArrayList<ResolveInfo> apps;
    Intent messageIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_share);
        rvShareApps = (RecyclerView) findViewById(R.id.rv_share_apps);

        Intent extras = getIntent();
        messageIntent = extras.getParcelableExtra(MESSAGE_INTENT_EXTRA);
        apps = extras.getParcelableArrayListExtra(APPS_LIST_EXTRA);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        OnItemClickListener clickListener = new OnItemClickListener() {
            @Override
            public void onClick(ResolveInfo pickedAppInfo) {
                messageIntent.setPackage(pickedAppInfo.activityInfo.packageName);
                startActivity(messageIntent);
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvShareApps.setLayoutManager(layoutManager);
        rvShareApps.setAdapter(new ShareAdapter(this, apps, clickListener));
    }
}
