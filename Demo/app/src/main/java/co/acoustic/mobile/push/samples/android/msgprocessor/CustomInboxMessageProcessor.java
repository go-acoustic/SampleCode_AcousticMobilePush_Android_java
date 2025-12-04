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

import co.acoustic.mobile.push.sdk.plugin.inbox.InboxMessageProcessor;
import co.acoustic.mobile.push.sdk.plugin.inbox.RichContent;

/**
 * This class filters out inbox messages that were sent over a day ago
 */
public class CustomInboxMessageProcessor extends InboxMessageProcessor {

    @Override
    public ProcessReport<RichContent> process(Context context, List<RichContent> messages) {
        List<RichContent> filteredMessages = new LinkedList<>();
        Date aDayAgo = new Date(System.currentTimeMillis() - 24L * 60L * 60L * 1000L);
        for(RichContent message : messages) {
            if(message.getSendDate().after(aDayAgo)) {
                filteredMessages.add(message);
            }
        }
        return super.process(context, filteredMessages);
    }
}
