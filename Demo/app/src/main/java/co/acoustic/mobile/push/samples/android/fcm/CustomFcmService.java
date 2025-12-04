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

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import co.acoustic.mobile.push.sdk.api.MessagingApi;
import co.acoustic.mobile.push.sdk.api.fcm.FcmApi;

public class CustomFcmService extends FirebaseMessagingService {

    private static final String TAG = "CustomFcmService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(FcmApi.isFcmMessage(remoteMessage)) {
            FcmApi.handleMceFcmMessage(getApplicationContext(), remoteMessage);
        } else {
            Log.i(TAG, "Non MCE message received "+remoteMessage);
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        MessagingApi.reportToken(getApplicationContext(), s);
    }
}
