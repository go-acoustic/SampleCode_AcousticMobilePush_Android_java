/*
 * Copyright (C) 2024 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */
package co.acoustic.mobile.push.samples.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import co.acoustic.mobile.push.sdk.api.MceSdk;
import co.acoustic.mobile.push.sdk.api.OperationCallback;
import co.acoustic.mobile.push.sdk.api.OperationResult;
import co.acoustic.mobile.push.sdk.api.attribute.Attribute;
import co.acoustic.mobile.push.sdk.api.attribute.StringAttribute;
import co.acoustic.mobile.push.sdk.api.event.Event;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class EventSampleActivity extends ListSampleActivity {

    private static final String TAG = "EventSampleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListView((ListView) findViewById(resourcesHelper.getId("listView2")));
    }

    @Override
    protected List<String> getListItems() {
        return new LinkedList<String>();
    }

    @Override
    protected Object[] createUIValues(String[] itemsArray) {
        return null;
    }

    @Override
    protected ListAdapter createListAdapter() {
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {resourcesHelper.getString("event_send_title")});
    }

    @Override
    protected String getLayoutName() {
        return "activity_event_test";
    }

    @Override
    protected String getMenuName() {
        return "menu_event_test";
    }

    @Override
    protected String getSettingsName() {
        return "action_settings";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<Attribute> attributes = new LinkedList<Attribute>();
        attributes.add(new StringAttribute("payload", "{\"sampleData\": \"A sample value\"}"));
        Event event = new Event("custom", "appOpened", new Date(), attributes, "sampleAttribution", "sampleMailingId");
        OperationCallback<Event> callback = new OperationCallback<Event>() {
            @Override
            public void onSuccess(Event event, OperationResult result) {
                Log.d(TAG, "Event sent successfully: " + event.getType());
                EventSampleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EventSampleActivity.this.getApplicationContext(), resourcesHelper.getString("event_send_succeeded"), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Event event, OperationResult result) {
                Log.d(TAG, "Event sending failed: " + event.getType() + ". " + result.getMessage());
                EventSampleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EventSampleActivity.this.getApplicationContext(), resourcesHelper.getString("event_send_failed"), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        MceSdk.getEventsClient(false).sendEvent(getApplicationContext(), event, callback);
        /**
         * Comment out the line above and uncomment this section in order to send the event in queue
         MceSdk.getQueuedEventsClient().sendEvent(getApplicationContext(), event);
         */
    }
}
