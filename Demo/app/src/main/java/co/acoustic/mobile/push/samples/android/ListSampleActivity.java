/********************************************************************************************
 * Copyright (C) 2019 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 ********************************************************************************************/
package co.acoustic.mobile.push.samples.android;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import co.acoustic.mobile.push.samples.android.layout.CustomListAdapter;
import co.acoustic.mobile.push.sdk.plugin.inapp.InAppManager;
import co.acoustic.mobile.push.sdk.plugin.inapp.InAppStorage;

import java.util.ArrayList;
import java.util.List;

public abstract class ListSampleActivity extends SampleActivity implements AdapterView.OnItemClickListener, CustomListAdapter.ValueChangeListener {

    protected Object[] uiValues;
    protected ListAdapter adapter;
    protected boolean inAppShown = false;

    protected static final String IN_APP_SHOWN_KEY = "inAppShown";

    protected abstract List<String> getListItems();

    protected abstract Object[] createUIValues(String[] itemsArray);

    protected ListAdapter createListAdapter() {
        return new CustomListAdapter(this, this, uiValues);
    }

    protected void setupListView(ListView listView) {
        List<String> items = getListItems();
        String[] itemsArray = items.toArray(new String[items.size()]);
        uiValues = createUIValues(itemsArray);
        adapter = createListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(resourcesHelper.getLayoutId(getLayoutName()));
        handleActionIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleActionIntent(intent);
    }

    protected void handleActionIntent(Intent intent) {
        if(intent != null && intent.getExtras() != null) {
            if(intent.hasExtra(SampleNotifier.ACTION_KEY)) {
                String action = intent.getStringExtra(SampleNotifier.ACTION_KEY);
                if (action != null) {
                    if (SampleNotifier.ACTION_SDK_REGISTRATION.equals(action)) {
                        handleSdkRegistration();
                    } else if (SampleNotifier.ACTION_MSG_SVC_REGISTRATION.equals(action)) {
                        handleGcmRegistration();
                    }
                }
            }
        }
    }

    protected void handleSdkRegistration() {

    }

    protected void handleGcmRegistration() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IN_APP_SHOWN_KEY, inAppShown);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        updateInAppShown(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateInAppShown(savedInstanceState);
    }

    void updateInAppShown(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(IN_APP_SHOWN_KEY)) {
            inAppShown = savedInstanceState.getBoolean(IN_APP_SHOWN_KEY);
        }
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        showInAppIfExists();

    }

    protected void showInAppIfExists() {
        if(!inAppShown) {
            int inAppFragmentId = resourcesHelper.getId("con");
            if (inAppFragmentId > 0) {
                List<String> values = new ArrayList<String>(1);
                values.add("appOpen");
                InAppManager.show(getApplicationContext(), InAppStorage.KeyName.RULE, values, getSupportFragmentManager(), inAppFragmentId);
                inAppShown = true;
            }
        }
    }

    @Override
    protected void onStop() {
        inAppShown = false;
        super.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void valueChanged(CustomListAdapter adapter, int position, Object value) {

    }

    protected abstract String getLayoutName();
}
