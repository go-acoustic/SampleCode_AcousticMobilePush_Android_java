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

import android.content.res.Resources;

public class ResourcesHelper {

    private Resources resources;
    private String packageName;

    public ResourcesHelper(Resources resources, String packageName) {
        this.resources = resources;
        this.packageName = packageName;
    }

    public int getLayoutId(String layoutName) {
        return getResourceId(layoutName, "layout");
    }

    public int getMenuId(String menuName) {
        return getResourceId(menuName, "menu");
    }

    public int getDrawableId(String drawableName) {
        return getResourceId(drawableName, "drawable");
    }

    public int getRawId(String rawName) {
        return getResourceId(rawName, "raw");
    }

    public int getStringId(String stringName) {
        return getResourceId(stringName, "string");
    }

    public String getString(String stringName) {
        return resources.getString(getStringId(stringName));
    }

    public int getId(String name) {
        return getResourceId(name, "id");
    }

    private int getResourceId(String resourceName, String resourceType) {
        return resources.getIdentifier(resourceName, resourceType, packageName);
    }
}
