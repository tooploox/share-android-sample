package com.tooploox.training.sample.sharingexample.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tooploox.training.sample.sharingexample.R;
import com.tooploox.training.sample.sharingexample.adapter.ShareAdapter;

import java.util.ArrayList;

public class CustomShareActivity extends AppCompatActivity {

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
        ShareAdapter.OnShareAppClickedListener clickListener = new ShareAdapter.OnShareAppClickedListener() {
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
