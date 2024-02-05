/********************************************************************************************
 * Copyright (C) 2019 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 ********************************************************************************************/
package co.acoustic.mobile.push.samples.android.layout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class ClickItemLayout implements CustomListAdapter.CustomItemLayout {

    @Override
    public View getView(Object item, Activity activity, CustomListAdapter adapter, int position) {
        ResourcesHelper resourcesHelper = new ResourcesHelper(activity.getResources(), activity.getPackageName());
        LayoutInflater inflater = activity.getLayoutInflater();
        final View clickItemView = inflater.inflate(resourcesHelper.getLayoutId("click_item"), null, true);
        TextView itemView = (TextView)clickItemView.findViewById(resourcesHelper.getId("itemView"));
        ClickItem clickItem = (ClickItem)item;
        itemView.setText(clickItem.getItemText());
        return clickItemView;
    }

    public static class ClickItem {
        private String itemText;

        public ClickItem(String itemText) {
            this.itemText = itemText;
        }

        public String getItemText() {
            return itemText;
        }
    }
}
