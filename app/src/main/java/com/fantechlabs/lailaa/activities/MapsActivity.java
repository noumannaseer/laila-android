package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.geofence.GeofenceHelper;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.permissions.Permission;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import lombok.val;

//***********************************************************************
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        Permission.OnResult, Permission.OnDenied, GoogleMap.OnMapLongClickListener
//***********************************************************************
{
    private GoogleMap mMap;
    private GeofencingClient mGeofencingClient;
    private int LOCATION_REQUEST_CODE = 100;
    private Permission mPermission;
    private float GEOFENCE_RADIUS = 100;
    private GeofenceHelper mGeofenceHelper;
    private String GEOFENCE_ID = "GEOFENCE_ID";

    //***********************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState)
    //***********************************************************************
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initControls();
    }

    //***********************************************************************
    @Override
    public void onMapReady(GoogleMap googleMap)
    //***********************************************************************
    {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        initMap();
    }

    //***********************************************************************
    private void initControls()
    //***********************************************************************
    {
        mGeofencingClient = LocationServices.getGeofencingClient(this);
        mGeofenceHelper = new GeofenceHelper(this);

    }

    //***********************************************************************
    private void initMap()
    //***********************************************************************
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            initPermission();
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLongClickListener(this);
    }

    //*********************************************************************
    private void initPermission()
    //*********************************************************************
    {
        mPermission = new Permission(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MapsActivity.this);
        mPermission.request(MapsActivity.this);
    }

    //******************************************************************
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    //******************************************************************
    {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            mPermission.request(this);
    }

    //************************************************************************************
    @Override
    public void onPermission(@NonNull String permission, @NonNull Permission.Status status)
    //************************************************************************************
    {
        switch (status) {
            case Granted:
                break;
            case DeniedForNow:
            case DeniedPermanently:
                initPermission();
                break;
        }
    }

    //*************************************************************
    @Override
    public void onPermissionDenied(@NonNull String permission)
    //*************************************************************
    {
        AndroidUtil.toast(false, "" + permission);
    }

    //*************************************************************
    @Override
    public void onMapLongClick(LatLng latLng)
    //*************************************************************
    {
        mMap.clear();
        addMarker(latLng);
        addCircle(latLng, GEOFENCE_RADIUS);
        addGeofence(latLng, GEOFENCE_RADIUS);
    }

    //*************************************************************
    private void addGeofence(LatLng latLng, float radius)
    //*************************************************************
    {
        val transitionTypes = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT;
        Geofence geofence = mGeofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, transitionTypes);

        GeofencingRequest geofencingRequest = mGeofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = mGeofenceHelper.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            initPermission();
        }
        mGeofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.geofence_added), AndroidUtil.getString(R.string.alert),MapsActivity.this);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AndroidUtil.displayAlertDialog(e.getLocalizedMessage(), AndroidUtil.getString(R.string.alert), MapsActivity.this);
                    }
                });
    }

    //*************************************************************
    private void addMarker(LatLng latLng)
    //*************************************************************
    {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    //*************************************************************
    private void addCircle(LatLng latLng, float radius)
    //*************************************************************
    {

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 0, 0, 255));
        circleOptions.fillColor(Color.argb(54, 0, 0, 255));
        circleOptions.strokeWidth(2);
        mMap.addCircle(circleOptions);
    }
}