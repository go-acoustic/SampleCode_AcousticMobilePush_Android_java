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

import static co.acoustic.mobile.push.sdk.plugin.carousel.CarouselIterationOperation.DIALOG_MESSAGE;
import static co.acoustic.mobile.push.sdk.plugin.carousel.CarouselIterationOperation.DIALOG_TITLE;
import static co.acoustic.mobile.push.sdk.plugin.carousel.CarouselIterationOperation.DIALOG_TYPE;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import co.acoustic.mobile.push.sdk.api.MceSdk;
import co.acoustic.mobile.push.sdk.api.OperationCallback;
import co.acoustic.mobile.push.sdk.api.OperationResult;
import co.acoustic.mobile.push.sdk.api.message.MessageProcessor;
import co.acoustic.mobile.push.sdk.api.message.MessageSync;
import co.acoustic.mobile.push.sdk.api.registration.RegistrationDetails;
import co.acoustic.mobile.push.sdk.plugin.inapp.InAppManager;
import co.acoustic.mobile.push.sdk.plugin.inapp.InAppMessageProcessor;
import co.acoustic.mobile.push.sdk.plugin.inbox.InboxMessageProcessor;
import co.acoustic.mobile.push.sdk.plugin.inbox.InboxMessagesClient;

import java.util.LinkedList;
import java.util.List;


public class MainSampleMenuActivity extends ListSampleActivity {

    private static final int REGISTRATION_DETAILS_INDEX = 0;
    private static final int SEND_TEST_EVENTS_INDEX = 1;
    private static final int SEND_USER_ATTRIBUTES_INDEX = 2;
    private static final int INAPP_INDEX = 3;
    private static final int INBOX_INDEX = 4;
    private static final int LOCATIONS_INDEX = 5;
    private static final String TAG = "MainSampleMenuActivity";
    private static final int REQUEST_NOTIFICATION_PERMISSIONS = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMainView();
        requestPermissions();
        checkToShowDialog();
    }

    @Override
    protected void handleSdkRegistration() {
        super.handleSdkRegistration();
        Intent goToIntent = new Intent();
        goToIntent.setClass(getApplicationContext(), getRegistrationActivityClass());
        startActivity(goToIntent);
    }

    @Override
    protected void handleGcmRegistration() {
        super.handleGcmRegistration();
        handleSdkRegistration();
    }

    private void showMainView() {
        setupListView((ListView) findViewById(resourcesHelper.getId("listView")));
        if(isTitle()) {
            TextView titleView = (TextView) findViewById(resourcesHelper.getId("title"));
            titleView.setText(resourcesHelper.getString("title") + " " + MceSdk.getSdkVerNumber());
        }
    }

    @Override
    protected List<String> getListItems() {
        LinkedList<String> items = new LinkedList<String>();
        items.add(resourcesHelper.getString("registration_details_title"));
        items.add(resourcesHelper.getString("send_test_events_title"));
        items.add(resourcesHelper.getString("send_user_attributes_title"));
        items.add(resourcesHelper.getString("inapp_title"));
        items.add(resourcesHelper.getString("inbox_title"));
        items.add(resourcesHelper.getString("title_activity_location"));
        return items;
    }

    @Override
    protected Object[] createUIValues(String[] itemsArray) {
        return null;
    }

    @Override
    protected ListAdapter createListAdapter() {
        List<String> items = getListItems();
        String[] values = items.toArray(new String[items.size()]);

        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
    }

    @Override
    protected String getMenuName() {
        return null;
    }

    @Override
    protected String getSettingsName() {
        return "action_settings";
    }

    @Override
    protected String getLayoutName() {
        return "activity_main";
    }

    protected boolean isTitle() {
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == REGISTRATION_DETAILS_INDEX) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), getRegistrationActivityClass());
            startActivity(intent);
        } else if (position == SEND_TEST_EVENTS_INDEX) {

            RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(getApplicationContext());
            if (registrationDetails.getChannelId() == null || registrationDetails.getChannelId().isEmpty()) {
                Toast.makeText(MainSampleMenuActivity.this.getApplicationContext(), resourcesHelper.getString("no_sdk_reg_toast"), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), getEventsActivityClass());
                startActivity(intent);
            }
        } else if (position == SEND_USER_ATTRIBUTES_INDEX) {
            RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(getApplicationContext());
            if (registrationDetails.getChannelId() == null || registrationDetails.getChannelId().isEmpty()) {
                Toast.makeText(MainSampleMenuActivity.this.getApplicationContext(), resourcesHelper.getString("no_sdk_reg_toast"), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), getAttributesActivityClass());
                startActivity(intent);
            }
        } else if (position == INAPP_INDEX) {
            MessageSync.syncMessages(getApplicationContext(), new OperationCallback<MessageSync.SyncReport>() {
                @Override
                public void onSuccess(MessageSync.SyncReport syncReport, OperationResult result) {
                    InAppManager.show(getApplicationContext(), getSupportFragmentManager(), resourcesHelper.getId("con"));
                    publishReport(syncReport);
                }

                @Override
                public void onFailure(final MessageSync.SyncReport syncReport, final OperationResult result) {
                    Log.d(TAG, "Message sync failed: "+syncReport.getFailureCause());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainSampleMenuActivity.this.getApplicationContext(), resourcesHelper.getString("message_sync_failed_toast")+": "+syncReport.getFailureCause(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    InAppManager.show(getApplicationContext(), getSupportFragmentManager(), resourcesHelper.getId("con"));
                }
            });

            inAppShown = true;
        } else if (position == INBOX_INDEX) {
            MessageSync.syncMessages(getApplicationContext(),  new OperationCallback<MessageSync.SyncReport>() {
                @Override
                public void onSuccess(MessageSync.SyncReport syncReport, OperationResult result) {
                    InboxMessagesClient.showInbox(getApplicationContext());
                    publishReport(syncReport);
                }

                @Override
                public void onFailure(final MessageSync.SyncReport syncReport, final OperationResult result) {
                    Log.d(TAG, "Message sync failed: "+syncReport.getFailureCause());
                    InboxMessagesClient.showInbox(getApplicationContext());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainSampleMenuActivity.this.getApplicationContext(), resourcesHelper.getString("message_sync_failed_toast")+": "+syncReport.getFailureCause(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else if (position == LOCATIONS_INDEX) {
            RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(getApplicationContext());
            if (registrationDetails.getChannelId() == null || registrationDetails.getChannelId().isEmpty()) {
                Toast.makeText(MainSampleMenuActivity.this.getApplicationContext(), resourcesHelper.getString("no_sdk_reg_toast"), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LocationActivity.class);
                startActivity(intent);
            }
        }
    }

    private void checkToShowDialog() {
        String dialogTitle = getIntent().getStringExtra(DIALOG_TITLE);
        String dialogType = getIntent().getStringExtra(DIALOG_TYPE);
        String msg = getIntent().getStringExtra(DIALOG_MESSAGE);
        if (dialogType != null && dialogTitle != null && msg != null) {
            if (dialogType.equals("carousel")) {
                new AlertDialog.Builder(this)
                        .setTitle(dialogTitle)
                        .setMessage(msg)
                        .setPositiveButton("Okay", (dialog, which) -> {
                        })
                        .create()
                        .show();
            }
        }
    }

    private void publishReport(MessageSync.SyncReport syncReport) {
        final StringBuilder msg = new StringBuilder();
        for(MessageProcessor.ProcessReport processReport : syncReport.getReports()) {
            if(processReport instanceof InAppMessageProcessor.Report) {
                Log.d(TAG, "Message sync successful: Received "+processReport.getNewMessages().size()+" new inapp messages");
                msg.append((msg.length()!= 0 ? "\n" : "")+processReport.getNewMessages().size()+" inapp new messages");
            }
            if(processReport instanceof InboxMessageProcessor.Report) {
                Log.d(TAG, "Message sync successful: Received "+processReport.getNewMessages().size()+" new inbox messages");
                msg.append((msg.length()!= 0 ? "\n" : "")+processReport.getNewMessages().size()+" inbox new messages");
            }
            if(msg.length() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainSampleMenuActivity.this.getApplicationContext(), msg.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    /**
     * Requests for the Post_Notifications permission which is required on os 33
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= 33) {
            String[] primaryPermissions = {"android.permission.POST_NOTIFICATIONS"};
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                ActivityCompat.requestPermissions(
                        this,
                        primaryPermissions,
                        REQUEST_NOTIFICATION_PERMISSIONS
                );
            }
        }
    }

    public Class getRegistrationActivityClass() {
        return RegistrationDetailsSampleActivity.class;
    }

    public Class getAttributesActivityClass() {
        return AttributesSampleActivity.class;
    }

    public Class getEventsActivityClass() {
        return EventSampleActivity.class;
    }
}

