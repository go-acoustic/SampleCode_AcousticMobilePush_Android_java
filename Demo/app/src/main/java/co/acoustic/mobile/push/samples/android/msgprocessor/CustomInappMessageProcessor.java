/*
 *  Copyright © 2011, 2020 Acoustic, L.P. All rights reserved.
 *
 *  NOTICE: This file contains material that is confidential and proprietary to
 *  Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 *  industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 *  Acoustic, L.P. Any unauthorized copying or distribution of content from this file is prohibited.
 *
 */

package co.acoustic.mobile.push.samples.android.msgprocessor;

import android.content.Context;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.acoustic.mobile.push.sdk.plugin.inapp.InAppMessageProcessor;
import co.acoustic.mobile.push.sdk.plugin.inapp.InAppPayload;

/**
 * This class filters out inapp messages whose trigger date is over a day ago
 */
public class CustomInappMessageProcessor extends InAppMessageProcessor {

    @Override
    public ProcessReport<InAppPayload> process(Context context, List<InAppPayload> messages) {
        List<InAppPayload> filteredMessages = new LinkedList<>();
        Date aDayAgo = new Date(System.currentTimeMillis() - 24L * 60L * 60L * 1000L);
        for(InAppPayload message : messages) {
            if(message.getTriggerDate().after(aDayAgo)) {
                filteredMessages.add(message);
            }
        }
        return super.process(context, filteredMessages);
    }
}
