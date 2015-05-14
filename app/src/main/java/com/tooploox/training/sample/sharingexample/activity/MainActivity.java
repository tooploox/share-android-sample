/*
Copyright 2015 Damian Walczak

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.tooploox.training.sample.sharingexample.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tooploox.training.sample.sharingexample.R;
import com.tooploox.training.sample.sharingexample.util.IntentFilterUtil;

import java.util.ArrayList;
import java.util.Collection;


public class MainActivity extends AppCompatActivity {


    EditText etSubject;
    EditText etMessage;
    Button btnEmail;
    Button btnMessage;
    Button btnAll;

    // This is quite lazy way of implementing listener, but I hate to use onClick from XML
    // and this way keeps only one instance of OnClickListener object.
    // In production apps we're using ButterKnife, but for sample it's overkill.
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_email:
                    onEmailClicked();
                    break;
                case R.id.btn_message:
                    onMessageClicked();
                    break;
                case R.id.btn_all:
                    onAllClicked();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etSubject = (EditText) findViewById(R.id.et_subject);
        etMessage = (EditText) findViewById(R.id.et_message);
        btnEmail = (Button) findViewById(R.id.btn_email);
        btnMessage = (Button) findViewById(R.id.btn_message);
        btnAll = (Button) findViewById(R.id.btn_all);

        btnEmail.setOnClickListener(onClickListener);
        btnMessage.setOnClickListener(onClickListener);
        btnAll.setOnClickListener(onClickListener);
    }

    private Intent getMessageIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
        intent.setType("text/plain");
        return intent;
    }

    private void onAllClicked() {
        Intent messageIntent = getMessageIntent();
        Collection<ResolveInfo> allMatching = IntentFilterUtil.findAllMatching(getPackageManager(), messageIntent);
        startCustomPicker(messageIntent, allMatching);
    }

    private void onMessageClicked() {
        Intent messageIntent = getMessageIntent();
        Collection<ResolveInfo> allMatching = IntentFilterUtil.findMatchingMessage(getPackageManager(), messageIntent);
        startCustomPicker(messageIntent, allMatching);
    }

    private void onEmailClicked() {
        Intent messageIntent = getMessageIntent();
        Collection<ResolveInfo> allMatching = IntentFilterUtil.findMatchingEmail(getPackageManager(), messageIntent);
        startCustomPicker(messageIntent, allMatching);
    }

    private void startCustomPicker(Intent messageIntent, Collection<ResolveInfo> allMatching) {
        Intent shareIntent = new Intent(this, CustomShareActivity.class);
        shareIntent.putExtra(CustomShareActivity.MESSAGE_INTENT_EXTRA, messageIntent);
        shareIntent.putParcelableArrayListExtra(CustomShareActivity.APPS_LIST_EXTRA, new ArrayList<>(allMatching));
        startActivity(shareIntent);
    }
}
