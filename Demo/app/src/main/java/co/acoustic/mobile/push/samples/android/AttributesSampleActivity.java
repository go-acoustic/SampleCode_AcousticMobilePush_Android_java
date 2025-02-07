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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import co.acoustic.mobile.push.sdk.api.MceSdk;
import co.acoustic.mobile.push.sdk.api.attribute.Attribute;
import co.acoustic.mobile.push.sdk.api.attribute.StringAttribute;
import co.acoustic.mobile.push.samples.android.layout.ClickItemLayout;
import co.acoustic.mobile.push.samples.android.layout.KeyValueLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;


public class AttributesSampleActivity extends ListSampleActivity {

    private static final String TAG = "AttributesActivity";

    // Action indices
    private static final int ATTRIBUTE_KEY_INDEX = 0;
    private static final int ATTRIBUTE_VALUE_INDEX = 1;
    private static final int ATTRIBUTE_ACTION_INDEX = 2;
    private static final int ATTRIBUTE_SEND_INDEX = 3;

    protected KeyValueLayout.KeyValueString attributeKey;
    protected KeyValueLayout.KeyValueString attributeValue;
    protected KeyValueLayout.KeyValueOptions attributeActions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListView((ListView) findViewById(resourcesHelper.getId("listView2")));
    }

    @Override
    protected List<String> getListItems() {
        LinkedList<String> items = new LinkedList<String>();
        items.add(resourcesHelper.getString("attr_key_title"));
        items.add(resourcesHelper.getString("attr_value_title"));
        items.add(resourcesHelper.getString("attr_action_title"));
        items.add(resourcesHelper.getString("attr_send_title"));
        return items;
    }

    protected List<String> getAttributesOperations() {
        List<String> operations = new LinkedList<String>();
        operations.add( resourcesHelper.getString("attribute_action_update"));
        operations.add( resourcesHelper.getString("attribute_action_delete"));
        return operations;
    }

    @Override
    protected Object[] createUIValues(String[] itemsArray) {
        Object[] uiValues = new Object[itemsArray.length];

        attributeKey = new KeyValueLayout.KeyValueString(itemsArray[ATTRIBUTE_KEY_INDEX] ,resourcesHelper.getString("attribute_default_key"), true);
        attributeValue =  new KeyValueLayout.KeyValueString(itemsArray[ATTRIBUTE_VALUE_INDEX] ,resourcesHelper.getString("attribute_default_value"), true);
        List<String> operations = getAttributesOperations();
        attributeActions = new KeyValueLayout.KeyValueOptions(itemsArray[ATTRIBUTE_ACTION_INDEX] ,(String[])(operations.toArray(new String[operations.size()])));


        uiValues[ATTRIBUTE_KEY_INDEX] = attributeKey;
        uiValues[ATTRIBUTE_VALUE_INDEX] = attributeValue;
        uiValues[ATTRIBUTE_ACTION_INDEX] = attributeActions;
        uiValues[ATTRIBUTE_SEND_INDEX] = new ClickItemLayout.ClickItem(itemsArray[ATTRIBUTE_SEND_INDEX]);

        return uiValues;
    }

    @Override
    protected String getLayoutName() {
        return "activity_attributes";
    }

    @Override
    protected String getMenuName() {
        return "menu_attributes";
    }

    @Override
    protected String getSettingsName() {
        return "action_settings";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == ATTRIBUTE_SEND_INDEX) {
            StringAttribute attribute = new StringAttribute(attributeKey.getValue(), attributeValue.getValue());
            List<Attribute> attributes = new ArrayList<>(1);
            attributes.add(attribute);
            List<String> attributeKeys = new ArrayList<String>(1);
            attributeKeys.add(attribute.getKey());
            String action = attributeActions.getValue();
            performAttributesAction(attributes, attributeKeys, action);
        }
    }

    protected void performAttributesAction(List<Attribute> attributes, List<String> attributeKeys, String action) {
        if (resourcesHelper.getString("attribute_action_update").equals(action)) {
            MceSdk.getQueuedAttributesClient().updateUserAttributes(getApplicationContext(), attributes);
        } else if (resourcesHelper.getString("attribute_action_delete").equals(action)) {
            MceSdk.getQueuedAttributesClient().deleteUserAttributes(getApplicationContext(), attributeKeys);
        }
    }


}
