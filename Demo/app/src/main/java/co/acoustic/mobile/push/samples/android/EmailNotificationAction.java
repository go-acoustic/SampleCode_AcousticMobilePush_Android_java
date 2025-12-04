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

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.Map;

import co.acoustic.mobile.push.sdk.api.MceSdk;
import co.acoustic.mobile.push.sdk.api.notification.Action;
import co.acoustic.mobile.push.sdk.api.notification.MceNotificationAction;
import co.acoustic.mobile.push.sdk.api.notification.NotificationDetails;
import co.acoustic.mobile.push.sdk.util.Logger;

public class EmailNotificationAction implements MceNotificationAction {

    public static final String TAG = "EmailNotificationAction";

    public static final String EMAIL_ADDRESS_KEY = "recipient";
    public static final String EMAIL_SUBJECT_KEY = "subject";
    public static final String EMAIL_BODY_KEY = "body";


    @Override
    public void handleAction(Context context, String type, String name, String attribution, String mailingId, Map<String, String> payload, boolean fromNotification) {
        String valueJSONStr = payload.get(Action.KEY_VALUE);

        if(valueJSONStr != null && !(valueJSONStr.trim().isEmpty())) {
            try {
                JSONObject valueJSON = new JSONObject(valueJSONStr);
                String emailAddress = valueJSON.getString(EMAIL_ADDRESS_KEY);
                String subject = valueJSON.getString(EMAIL_SUBJECT_KEY);
                String body = valueJSON.getString(EMAIL_BODY_KEY);
                String address = emailAddress + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body);
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", address, null));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                intent.setData(Uri.parse("mailto:" + address));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                try {
                    if(fromNotification) {
                        MceSdk.closeSystemDialogs(context);
                    }
                    context.startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Logger.e(TAG, "No Email activity found:" + e.getMessage(), e);
                }
            } catch (JSONException jsone) {
                Logger.e(TAG, "Failed to parse JSON email message", jsone);
            }
        } else {
            Log.e(TAG, "No address for mail action");
        }
    }

    @Override
    public void init(Context context, JSONObject jsonObject) {

    }

    @Override
    public void update(Context context, JSONObject jsonObject) {

    }

    @Override
    public boolean shouldDisplayNotification(Context context, NotificationDetails notificationDetails, Bundle bundle) {
        return true;
    }

    @Override
    public boolean shouldSendDefaultEvent(Context context) {
        return true;
    }
}
