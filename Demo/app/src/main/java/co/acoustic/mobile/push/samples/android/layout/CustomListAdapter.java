/*
 * Copyright (C) 2024 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */
package co.acoustic.mobile.push.samples.android.layout;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomListAdapter extends BaseAdapter {

    private static final Map<Class, Class> CLASS_TO_LAYOUT_CLASS = new HashMap<>();

    private final Activity activity;
    private final Object[] items;
    private int focusedPosition = -1;
    private ValueChangeListener valueChangeListener;
    private final Map<Integer, CustomItemLayout> positionToLayout = new HashMap<>();
    private String id = null;

    public static void registerCustomLayout(Class cls, CustomItemLayout layout) {
        CLASS_TO_LAYOUT_CLASS.put(cls, layout.getClass());
    }

    public static CustomItemLayout getRegisteredLayout(Class cls) {
        try {
            return (CustomItemLayout)(CLASS_TO_LAYOUT_CLASS.get(cls).newInstance());
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    static {
        registerCustomLayout(KeyValueLayout.KeyValueString.class, new KeyValueLayout());
        registerCustomLayout(KeyValueLayout.KeyValueOptions.class, new KeyValueLayout());
        registerCustomLayout(ClickItemLayout.ClickItem.class, new ClickItemLayout());
    }

    public CustomListAdapter(Activity activity, ValueChangeListener valueChangeListener, Object... items) {
        this.activity = activity;
        this.valueChangeListener = valueChangeListener;
        this.items = items;
    }

    public CustomListAdapter(Activity activity, ValueChangeListener valueChangeListener, Collection items) {
        this(activity, valueChangeListener, items.toArray());
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setValueChangeListener(ValueChangeListener listener) {
        valueChangeListener = listener;
    }

    public void valueChanged(int position, Object value) {
        if(valueChangeListener != null) {
            valueChangeListener.valueChanged(this, position, value);
        }
    }


    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object value = getItem(position);
        CustomItemLayout layout = positionToLayout.get(position);
        if(layout == null) {
            layout = getRegisteredLayout(value.getClass());
            positionToLayout.put(position, layout);
        }
        if(layout != null) {
            convertView = layout.getView(value, activity, this, position);
            if(position == focusedPosition) {
                convertView.requestFocus();
            }
        }
        return convertView;
    }

    public void setFocusedPosition(int position) {
        focusedPosition = position;
    }

    public interface CustomItemLayout {
        View getView(Object item, Activity activity, CustomListAdapter adapter, int position);
    }

    public interface ValueChangeListener {
        void valueChanged(CustomListAdapter adapter, int position, Object value);
    }

}
