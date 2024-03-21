/********************************************************************************************
 * Copyright (C) 2019 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 ********************************************************************************************/
package co.acoustic.mobile.push.samples.android.plugin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import co.acoustic.mobile.push.sdk.api.Constants;
import co.acoustic.mobile.push.sdk.api.MceSdk;
import co.acoustic.mobile.push.sdk.api.attribute.Attribute;
import co.acoustic.mobile.push.sdk.api.attribute.StringAttribute;
import co.acoustic.mobile.push.sdk.api.event.Event;
import co.acoustic.mobile.push.sdk.util.Logger;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ExampleActivity extends AppCompatActivity {

    final String TAG = "ExampleActivity";

    @Override
    protected void onCreate(Bundle context) {
        HashMap<String, String> payload = (HashMap<String, String>) getIntent().getExtras().getSerializable(ExampleAction.EXTRA_KEY_PAYLOAD);
        String payloadAsString = payload.toString();
        boolean sendCustomEvent = getIntent().getBooleanExtra(ExampleAction.EXTRA_KEY_SEND_CUSTOM_EVENT, true);
        boolean openForAction = getIntent().getBooleanExtra(ExampleAction.EXTRA_KEY_OPEN_FOR_ACTION, true);


        //send custom metric to server
        if (sendCustomEvent) {
            String attribution = getIntent().getStringExtra(ExampleAction.EXTRA_KEY_ATTRIBUTION);
            String mailingId = getIntent().getStringExtra(ExampleAction.EXTRA_KEY_MAILING_ID);
            List<Attribute> attributes = new LinkedList<Attribute>();
            attributes.add(new StringAttribute("payload", "{\"customData1\":\"exampleEvent\", \"customData2\":" + openForAction + ", \"customData3\":" + sendCustomEvent));
            Event event = new Event("custom", "custom", new Date(), attributes, attribution, mailingId);
            MceSdk.getQueuedEventsClient().sendEvent(getApplicationContext(), event);
        }

        //send payload to screen
        if (openForAction) {
            super.onCreate(context);
            int layoutId = getResources().getIdentifier("activity_example", "layout", getPackageName());
            setContentView(layoutId);
            int exampleId = getResources().getIdentifier("payload_values", "id", getPackageName());
            TextView payloadTextView = (TextView) findViewById(exampleId);
            payloadTextView.setText(payloadAsString);
        }
        //send payload to log
        else {
            Logger.d(TAG, "Payload is: " + payloadAsString);
        }
    }
}