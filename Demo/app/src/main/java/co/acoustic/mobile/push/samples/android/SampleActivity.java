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
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import co.acoustic.mobile.push.samples.android.layout.ResourcesHelper;

public abstract class SampleActivity extends AppCompatActivity {
    protected ResourcesHelper resourcesHelper;

    protected abstract String getMenuName();

    protected abstract String getSettingsName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourcesHelper = new ResourcesHelper(getResources(), getPackageName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String menuName= getMenuName();
        if(menuName != null){
            getMenuInflater().inflate(resourcesHelper.getMenuId(getMenuName()), menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == resourcesHelper.getId(getSettingsName())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
