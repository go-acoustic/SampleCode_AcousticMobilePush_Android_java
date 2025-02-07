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

import com.google.android.gms.location.LocationRequest;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import co.acoustic.mobile.push.samples.android.layout.ResourcesHelper;
import co.acoustic.mobile.push.sdk.api.MceApplication;
import co.acoustic.mobile.push.sdk.api.MceSdk;
import co.acoustic.mobile.push.sdk.api.MceSdkConfiguration;
import co.acoustic.mobile.push.sdk.api.notification.NotificationsPreference;
import co.acoustic.mobile.push.sdk.db.DefaultSdkDatabaseSecretKeyGenerator;
import co.acoustic.mobile.push.sdk.db.android.AndroidDatabaseImpl;
import co.acoustic.mobile.push.sdk.encryption.DefaultSdkEncryptionProvider;
import co.acoustic.mobile.push.sdk.registration.RegistrationClientImpl;
import co.acoustic.mobile.push.sdk.util.Logger;

public class SampleApplication extends MceApplication {

    public static final String MCE_SAMPLE_NOTIFICATION_CHANNEL_ID = "mce_sample_channel";

    private static final boolean READ_CONFIG_FROM_ASSETS = true;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("VersionTest", "v = "+RegistrationClientImpl.getVersion(getApplicationContext()));

        ResourcesHelper resourcesHelper = new ResourcesHelper(getResources(), getPackageName());

        /**
         * Custom layout
         */

        MceSdk.getNotificationsClient().setCustomNotificationLayout(this,
                resourcesHelper.getString("expandable_layout_type"),
                resourcesHelper.getLayoutId("custom_notification"),
                resourcesHelper.getId("bigText"),
                resourcesHelper.getId("bigImage"), resourcesHelper.getId("action1"),
                resourcesHelper.getId("action2"),
                resourcesHelper.getId("action3"));

        MceSdk.getNotificationsClient().getNotificationsPreference().setSoundEnabled(getApplicationContext(), true);
        MceSdk.getNotificationsClient().getNotificationsPreference().setSound(getApplicationContext(), resourcesHelper.getRawId("notification_sound"));
        MceSdk.getNotificationsClient().getNotificationsPreference().setVibrateEnabled(getApplicationContext(), true);
        long[] vibrate = { 0, 100, 200, 300 };
        MceSdk.getNotificationsClient().getNotificationsPreference().setVibrationPattern(getApplicationContext(), vibrate);
        MceSdk.getNotificationsClient().getNotificationsPreference().setIcon(getApplicationContext(),resourcesHelper.getDrawableId("icon"));
        MceSdk.getNotificationsClient().getNotificationsPreference().setLightsEnabled(getApplicationContext(), true);
        int ledARGB = 0x00a2ff;
        int ledOnMS = 300;
        int ledOffMS = 1000;
        MceSdk.getNotificationsClient().getNotificationsPreference().setLights(getApplicationContext(), new int[]{ledARGB, ledOnMS, ledOffMS});

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(getApplicationContext());
        }
    }

    @Override
    public MceSdkConfiguration getMceSdkConfiguration() {
        if(READ_CONFIG_FROM_ASSETS) {
            return null;
        } else {
            MceSdkConfiguration mceSdkConfiguration = new MceSdkConfiguration("YOUR_APP_KEY");
            mceSdkConfiguration.setBaseUrl("https://mobile-sdk-lib-ca-1.brilliantcollector.com");
            mceSdkConfiguration.setAutoInitialize(true);
            mceSdkConfiguration.setAutoReinitialize(true);

            mceSdkConfiguration.setGroupNotificationsByAttribution(true);
            mceSdkConfiguration.setInvalidateExistingUser(false);
            mceSdkConfiguration.setMessagingService(MceSdkConfiguration.MessagingService.fcm);

            mceSdkConfiguration.setSessionsEnabled(true);
            mceSdkConfiguration.setSessionTimeout(20);

            mceSdkConfiguration.setMetricTimeInterval(20);
            mceSdkConfiguration.setLogFile(false);
            mceSdkConfiguration.setLogLevel(Logger.LogLevel.error);
            mceSdkConfiguration.setLogIterations(1);
            mceSdkConfiguration.setLogIterationDurationInHours(0);
            mceSdkConfiguration.setLogBufferSize(10);


            mceSdkConfiguration.setUseFileImageCache(true);
            mceSdkConfiguration.setUseInMemoryImageCache(true);
            mceSdkConfiguration.setFileImageCacheCapacityInMB(100);
            mceSdkConfiguration.setInMemoryImageCacheCapacityInMB(10);

            MceSdkConfiguration.LocationConfiguration.SyncConfiguration syncConfiguration = mceSdkConfiguration.getLocationConfiguration().getSyncConfiguration();
            syncConfiguration.setLocationResponsiveness(300);
            syncConfiguration.setLocationSearchRadius(100000);
            syncConfiguration.setSyncInterval(300);
            syncConfiguration.setMaxLocationsForSearch(1);
            syncConfiguration.setMaxLocationsForSearch(20);

            MceSdkConfiguration.LocationConfiguration.RequestConfiguration requestConfiguration = mceSdkConfiguration.getLocationConfiguration().getRequestConfiguration();
            requestConfiguration.setFastestInterval(1000);
            requestConfiguration.setInterval(5000);
            requestConfiguration.setSmallestDisplacement(100);
            requestConfiguration.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            MceSdkConfiguration.LocationConfiguration.IBeaconConfiguration iBeaconConfiguration = mceSdkConfiguration.getLocationConfiguration().getiBeaconConfiguration();
            iBeaconConfiguration.setUuid("YOUR UUID");
            iBeaconConfiguration.setBeaconForegroundScanDuration(5);
            iBeaconConfiguration.setBeaconForegroundScanInterval(30);
            iBeaconConfiguration.setBeaconBackgroundScanDuration(30);
            iBeaconConfiguration.setBeaconBackgroundScanInterval(300);

            MceSdkConfiguration.DatabaseConfiguration databaseConfiguration = mceSdkConfiguration.getDatabaseConfiguration();
            databaseConfiguration.setDatabaseImplClassName(AndroidDatabaseImpl.class.getName());
            databaseConfiguration.setEncrypted(false);
            databaseConfiguration.setEncryptionProviderClassName(DefaultSdkEncryptionProvider.class.getName());
            databaseConfiguration.setKeyGeneratorClassName(DefaultSdkDatabaseSecretKeyGenerator.class.getName());
            databaseConfiguration.setKeyRotationIntervalInDays(30);

            return mceSdkConfiguration;
        }

    }

    @TargetApi(26)
    private static void createNotificationChannel(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = notificationManager.getNotificationChannel(MCE_SAMPLE_NOTIFICATION_CHANNEL_ID);
        if(channel == null) {
            CharSequence name = context.getString(R.string.notif_channel_name);
            String description = context.getString(R.string.notif_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(MCE_SAMPLE_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            NotificationsPreference notificationsPreference = MceSdk.getNotificationsClient().getNotificationsPreference();
            notificationsPreference.setNotificationChannelId(context, MCE_SAMPLE_NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
