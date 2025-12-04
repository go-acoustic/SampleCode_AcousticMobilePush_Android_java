/*
 *  Copyright © 2011, 2020 Acoustic, L.P. All rights reserved.
 *
 *  NOTICE: This file contains material that is confidential and proprietary to
 *  Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 *  industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 *  Acoustic, L.P. Any unauthorized copying or distribution of content from this file is prohibited.
 *
 */

package co.acoustic.mobile.push.samples.android.fcm;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import android.content.Context;

import co.acoustic.mobile.push.sdk.api.MceSdkConfiguration;
import co.acoustic.mobile.push.sdk.api.MessagingApi;
import co.acoustic.mobile.push.sdk.api.MessagingService;

public class CustomMessagingService implements MessagingService {

    @Override
    public void initialize(Context context, MceSdkConfiguration mceSdkConfiguration) {
        FirebaseApp.initializeApp(context);
    }

    @Override
    public boolean register(final Context context) {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> MessagingApi.reportToken(context, token));
        return true;
    }

    @Override
    public String getServiceName() {
        return "Custom FCM";
    }
}
