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

import android.os.Bundle;
import android.widget.ListView;

import co.acoustic.mobile.push.sdk.api.MceSdk;
import co.acoustic.mobile.push.sdk.api.registration.RegistrationDetails;
import co.acoustic.mobile.push.samples.android.layout.KeyValueLayout;

import java.util.LinkedList;
import java.util.List;

public class RegistrationDetailsSampleActivity extends ListSampleActivity {

    private static final int USER_ID_INDEX = 0;
    private static final int CHANNEL_ID_INDEX = 1;
    private static final int APPKEY_INDEX = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListView((ListView) findViewById(resourcesHelper.getId("listView2")));
    }

    @Override
    protected List<String> getListItems() {
        LinkedList<String> items = new LinkedList<>();
        items.add(resourcesHelper.getString("user_id_title"));
        items.add(resourcesHelper.getString("channel_id_title"));
        items.add(resourcesHelper.getString("appkey_title"));
        return items;
    }

    @Override
    protected Object[] createUIValues(String[] itemsArray) {
        String notRegistered = resourcesHelper.getString("not_registered");

        KeyValueLayout.KeyValue[] uiValues = new KeyValueLayout.KeyValue[itemsArray.length];
        RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(getApplicationContext());

        String userId = registrationDetails.getUserId() != null ? registrationDetails.getUserId() : notRegistered;
        String channelId = registrationDetails.getChannelId() != null ? registrationDetails.getChannelId() : notRegistered;
        uiValues[USER_ID_INDEX] = new KeyValueLayout.KeyValueString(itemsArray[USER_ID_INDEX] ,userId);
        uiValues[CHANNEL_ID_INDEX] = new KeyValueLayout.KeyValueString(itemsArray[CHANNEL_ID_INDEX] ,channelId);
        uiValues[APPKEY_INDEX] = new KeyValueLayout.KeyValueString(itemsArray[APPKEY_INDEX] ,MceSdk.getRegistrationClient().getAppKey(getApplicationContext()));

        return uiValues;
    }

    @Override
    public String getLayoutName() {
        return "activity_credentials";
    }

    @Override
    protected String getMenuName() {
        return "menu_main";
    }

    @Override
    protected String getSettingsName() {
        return "action_settings";
    }
}
