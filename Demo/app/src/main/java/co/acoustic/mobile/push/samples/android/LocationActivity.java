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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import co.acoustic.mobile.push.sdk.location.LocationApi;
import co.acoustic.mobile.push.sdk.location.LocationManager;
import co.acoustic.mobile.push.sdk.location.LocationPreferences;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    /*
     *  This is the list of permissions required for DLA for all versions
     *  of Android up to and including Android Pie (API Level 28)
     */
    private static final String[] LOCATION_PERMISSIONS_ANDROID_ALL_VERSION_THROUGH_P = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    /*
     *  This is the list of permissions required for DLA starting with
     *  Android 10 (API Level 29). Note, the ACCESS_BACKGROUND_LOCATION
     *  permission was added with Android 10.
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static final String[] LOCATION_PERMISSIONS_ANDROID_Q_OR_LATER = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };

    /*
     *  Starting with Android 11 (API Level 30), the ACCESS_BACKGROUND_LOCATION
     *  permission must be requested separately and after the Fine and Coarse
     *  location permissions have been granted. As such, the ACCESS_BACKGROUND_LOCATION
     *  has been moved to a secondary list.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private static final String[] LOCATION_PERMISSIONS_ANDROID_R_OR_LATER = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    private static final String[] BACKGROUND_PERMISSIONS_ANDROID_R_OR_LATER = {
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };

    /*
     *  Starting with Android 12 (API Level 31), the the new BLUETOOTH_SCAN
     *  permission must be requested in order for iBeacons to work.
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    private static final String[] LOCATION_PERMISSIONS_ANDROID_S_OR_LATER = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN
    };

    /*
     *  Example permission request codes to use when requesting permissions
     */
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;
    private static final int REQUEST_BACKGROUND_PERMISSIONS = 1;

    private GoogleMap mMap;
    private Button enableLocations;
    private Button showGeofences;
    private boolean mapEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        enableLocations = (Button)findViewById(R.id.enableLocations);
        showGeofences = (Button)findViewById(R.id.showGeofences);
        boolean locationsEnabled = LocationPreferences.isEnableLocations(getApplicationContext());
        enableLocations.setText(locationsEnabled ? getResources().getString(R.string.disable_locations_text) : getResources().getString(R.string.enable_locations_text));
        showGeofences.setEnabled(false);

        enableLocations.setOnClickListener(this);
        showGeofences.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Log.e("Location", "Failed to start map", e);
        }

    }

    @Override
    public void onClick(View view) {
        if(enableLocations == view) {
            boolean locationsEnabled = LocationPreferences.isEnableLocations(getApplicationContext());
            if(locationsEnabled) {
                LocationManager.disableLocationSupport(getApplicationContext());
                updateUIPermissionsDenied();

            } else {
                requestPermissions(this);
            }

        } else if(showGeofences == view) {
            mMap.clear();
            List<LocationApi> locations = LocationManager.getAllLocations(getApplicationContext());

            for(LocationApi location : locations) {
                LatLng center = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addCircle(new CircleOptions()
                        .center(center)
                        .radius(location.getRadius())
                        .strokeColor(Color.RED)
                        .strokeWidth(1)
                        .fillColor(0x5500ff00));
                mMap.addMarker(new MarkerOptions().position(center).title(location.getId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }

    private void requestPermissions(@NonNull Activity activity) {

        /*
         *  Requesting permissions for Android 11 (API Level 30) or later
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            /* First, get the right batch of primary location permissions to check on.
             */
            String[] primaryPermissions = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                    ? LOCATION_PERMISSIONS_ANDROID_S_OR_LATER
                    : LOCATION_PERMISSIONS_ANDROID_R_OR_LATER;

            /*
             *  First check if the primary location permissions have been granted.
             *  Be sure to request these first before ACCESS_BACKGROUND_LOCATION
             */
            if (!checkSelfPermissions(activity, primaryPermissions)) {
                ActivityCompat.requestPermissions(
                        activity,
                        primaryPermissions,
                        REQUEST_LOCATION_PERMISSIONS
                );

            }

            /*
             *  If the primary location permissions have already been granted, be sure the
             *  ACCESS_BACKGROUND_LOCATION permission has been granted as well. Be sure to
             *  follow Android's recommended practices for requesting location permissions
             *  at run time, seen here: https://developer.android.com/training/location/permissions
             *  This would include displaying to users the rationale for granting this permission
             *  if the operating system deems this necessary.
             */
            else if (!checkSelfPermissions(activity, BACKGROUND_PERMISSIONS_ANDROID_R_OR_LATER)) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    displayBackgroundLocationRationale();

                } else {
                    ActivityCompat.requestPermissions(
                            activity,
                            BACKGROUND_PERMISSIONS_ANDROID_R_OR_LATER,
                            REQUEST_BACKGROUND_PERMISSIONS
                    );
                }
            }

            /*
             *  If all required location permissions have been granted,
             *  you may now enable location support.
             */
            else {
                LocationManager.enableLocationSupport(getApplicationContext());
                updateUIPermissionsGranted();
            }
        }

        /*
         *  Requesting Location Permissions for Android 10 (API Level 29) and before
         */
        else {

            /*
             *  Request the correct permissions based on the version of Android
             */
            String[] permissions = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                    ? LOCATION_PERMISSIONS_ANDROID_Q_OR_LATER
                    : LOCATION_PERMISSIONS_ANDROID_ALL_VERSION_THROUGH_P;

            if (!checkSelfPermissions(activity, permissions)) {
                ActivityCompat.requestPermissions(
                        activity,
                        permissions,
                        REQUEST_LOCATION_PERMISSIONS
                );
            }

            /*
             *  If permissions have already been granted, you may enable location support
             */
            else {
                LocationManager.enableLocationSupport(getApplicationContext());
                updateUIPermissionsGranted();
            }
        }
    }

    private boolean checkSelfPermissions(@NonNull Context context, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void displayBackgroundLocationRationale() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.bg_permission_required_title)
                .setMessage(getString(R.string.bg_permission_required_message, getPackageManager().getBackgroundPermissionOptionLabel()))
                .setPositiveButton(R.string.bg_permission_required_positive, (dialog, which) -> {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(
                            this,
                            BACKGROUND_PERMISSIONS_ANDROID_R_OR_LATER,
                            REQUEST_BACKGROUND_PERMISSIONS
                    );
                })
                .setNegativeButton(R.string.bg_permission_required_negative, (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mapEnabled = true;
        boolean locationsEnabled = LocationPreferences.isEnableLocations(getApplicationContext());
        showGeofences.setEnabled(locationsEnabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        /*
         *  If the request was for ACCESS_BACKGROUND_LOCATIONS and the
         *  permission was granted, enable location support
         */
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BACKGROUND_PERMISSIONS) {
            if (checkPermissionsGranted(grantResults)) {
                LocationManager.enableLocationSupport(getApplicationContext());
                updateUIPermissionsGranted();
            }
        }

        /*
         *  If the request was for the base location permissions,
         *  and the permissions were granted, check the Android version.
         *  If running Android 11 (API Level 30) or later, you may still
         *  need to request the ACCESS_BACKGROUND_LOCATION permission.
         *  Allow the user to grant this permission. For all previous
         *  versions of Android, or if ACCESS_BACKGROUND_LOCATION has
         *  been granted, you may now enable location support.
         */
        else if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            if (checkPermissionsGranted(grantResults)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!checkSelfPermissions(this, BACKGROUND_PERMISSIONS_ANDROID_R_OR_LATER)) {
                        updateUIAccessBackgroundNeeded();

                    } else {
                        LocationManager.enableLocationSupport(getApplicationContext());
                        updateUIPermissionsGranted();
                    }

                } else {
                    LocationManager.enableLocationSupport(getApplicationContext());
                    updateUIPermissionsGranted();
                }
            }
        }
    }

    private boolean checkPermissionsGranted(@NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    private void updateUIPermissionsGranted() {
        showGeofences.setEnabled(true);
        enableLocations.setText(getResources().getString(R.string.disable_locations_text));
        showGeofences.setEnabled(mapEnabled);
    }

    private void updateUIPermissionsDenied() {
        showGeofences.setEnabled(false);
        enableLocations.setText(getResources().getString(R.string.enable_locations_text));
    }

    private void updateUIAccessBackgroundNeeded() {
        showGeofences.setEnabled(false);
        enableLocations.setText(getResources().getString(R.string.request_background_location_text));

    }
}
