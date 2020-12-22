package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityAddPharmacyBinding;
import com.fantechlabs.lailaa.models.Contact;
import com.fantechlabs.lailaa.models.PreferredPharmacies;
import com.fantechlabs.lailaa.models.pharmact_places.AddressComponent;
import com.fantechlabs.lailaa.models.pharmact_places.GoogleAddress;
import com.fantechlabs.lailaa.models.pharmact_places.NearByPlaces;
import com.fantechlabs.lailaa.models.pharmact_places.Result;
import com.fantechlabs.lailaa.models.response_models.PharmacyResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.LocationUtils;
import com.fantechlabs.lailaa.utils.PermissionsUtils;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.AddPharmacyViewModel;
import com.fantechlabs.lailaa.view_models.GoogleAddressViewModel;
import com.fantechlabs.lailaa.view_models.PlacesViewModel;
import com.fantechlabs.lailaa.view_models.PreferredPharmaciesViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

import static com.fantechlabs.lailaa.utils.Constants.PHARMACY;

//***********************************************************
public class AddPharmacyActivity extends BaseActivity
        implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        AddPharmacyViewModel.AddPharmacyListener,
        PlacesViewModel.PlaceViewModelListener,
        GoogleAddressViewModel.GoogleAddressViewModelListener,
        PreferredPharmaciesViewModel.PreferredPharmaciesViewModelListener
//***********************************************************
{
    private ActivityAddPharmacyBinding mBinding;
    private GoogleMap mGoogleMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MAP_VIEW_BUNDLE_KEY";
    public final static int TAG_PERMISSION_CODE = 1;
    private AddPharmacyViewModel mAddPharmacyViewModel;
    public static final int PERMISSION_GRANTED = 0;
    public static final int PERMISSION_DENIED = PERMISSION_GRANTED + 1;
    public static final int PERMISSION_DENIED_PERMANENTLY = PERMISSION_DENIED + 1;
    public static final int PERMISSION_DEFAULT = -100;
    public static final int PERMISSION_REQUEST_CODE = 100;
    public static final int MIN_TIME = 500;
    public static final int MIN_DISTANCE = 0;
    private Double mSourceLatitude;
    private Double mSourceLongitude;
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String TAG = AddPharmacyActivity.class.getCanonicalName();
    private PlacesViewModel mPlacesViewModel;
    private List<Result> mPlacesResult;
    private GoogleAddressViewModel mGoogleAddressViewModel;
    private String mPlaceIds, mPreferredId = "";
    private ArrayList<AddressComponent> mAddressComponents;
    private ArrayList<String> mPreferredPharmaciesIds;
    private PreferredPharmaciesViewModel mPreferredPharmaciesViewModel;
    private boolean mIsFirstTime = true;

    //***********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //***********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_pharmacy);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Pharmacy");
        mPlacesViewModel = new PlacesViewModel(AddPharmacyActivity.this);
        mGoogleAddressViewModel = new GoogleAddressViewModel(this);
        mPreferredPharmaciesViewModel = new PreferredPharmaciesViewModel(this);
        initGoogleMap(savedInstanceState);
        initControls();
    }

    //***************************************************************
    private void initControls()
    //***************************************************************
    {
        mAddPharmacyViewModel = new AddPharmacyViewModel(this);
        mBinding.saveBtn.setOnClickListener(v -> validation());

    }

    //******************************************************************
    private void initGoogleMap(Bundle savedInstanceState)
    //******************************************************************
    {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mBinding.mapview.onCreate(mapViewBundle);
        mBinding.mapview.getMapAsync(this);
    }

    //******************************************************************
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map)
    //******************************************************************
    {
        mGoogleMap = map;
        mGoogleMap.getUiSettings()
                .setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings()
                .setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings()
                .setCompassEnabled(true);
        mGoogleMap.getUiSettings()
                .setZoomGesturesEnabled(true);

        int permissionStatus = checkPermissionStatus(LOCATION_PERMISSION);
        Log.d(TAG, "permissionStatus : " + permissionStatus);
        switch (permissionStatus) {
            case PERMISSION_GRANTED:
                mGoogleMap.setMyLocationEnabled(true);
                break;
            case PERMISSION_DENIED:
                showPermissionSnackBar("Location permission");
                break;
            case PERMISSION_DENIED_PERMANENTLY:
                PermissionsUtils.showSnackBarWithSettings(getString(R.string.location_permission),
                        mBinding.main, this);
                break;
        }

        LocationUtils mLocationUtils = LocationUtils.getInstance(this);
        mLocationUtils.getCurrentLocation(true,
                MIN_TIME,
                MIN_DISTANCE,
                new LocationUtils.CurrentLocationListener() {

                    //******************************************************************
                    @Override
                    public void onLastKnownLocation(double latitude, double longitude)
                    //******************************************************************
                    {
//                        mSourceLatitude = latitude;
//                        mSourceLongitude = longitude;
//                        mLocationUtils.stopListener();
//                        searchNearByPharmacy(latitude, longitude);
                    }

                    //******************************************************************
                    @Override
                    public void onLocationUpdate(double latitude, double longitude)
                    //******************************************************************
                    {
                        mSourceLatitude = latitude;
                        mSourceLongitude = longitude;
                        mLocationUtils.stopListener();
                        setCameraCoordinates(latitude, longitude);
                        searchNearByPharmacy(latitude, longitude);
                        onCameraChange();
                    }

                    //********************************
                    @Override
                    public void onFailed()
                    //********************************
                    {
                    }
                });
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay()
                .getSize(displaySize);

    }

    //******************************************************************
    private void onCameraChange()
    //******************************************************************
    {

        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (mIsFirstTime) {
                    return;
                }
                AndroidUtil.handler.removeCallbacksAndMessages(this);
                AndroidUtil.handler
                        .postAtTime(() ->
                                {
                                    val log = cameraPosition.target.longitude;
                                    val lat = cameraPosition.target.latitude;
                                    mSourceLongitude = log;
                                    mSourceLatitude = lat;
                                    searchNearByPharmacy(lat, log);
                                }
                                , this,
                                SystemClock
                                        .uptimeMillis() + 1000);

            }
        });
    }


    //******************************************************************
    private void searchNearByPharmacy(@NonNull double lat, @NonNull double log)
    //******************************************************************
    {
        addMarkerToMap(Constants.LOCATION,
                Constants.CURRENT_LOCATION,
                lat,
                log,
                "",
                0);
        showLoadingDialog();
        mPlacesViewModel.getNearByPharmacy(
                lat + "," + log,
                Constants.RADIUS,
                Constants.TYPES,
                Constants.KEY);
    }

    //******************************************************************
    private void setCameraCoordinates(@NonNull double lat, @NonNull double log)
    //******************************************************************
    {
        LatLngBounds.Builder mBuilder = new LatLngBounds.Builder();
        mBuilder = new LatLngBounds.Builder();
        mBuilder.include(new LatLng(lat, log));
        LatLngBounds bounds = mBuilder.build();
        int padding = 100;

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(
                bounds,
                padding);
        mGoogleMap.moveCamera(cu);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), 12.0f));
        mIsFirstTime = false;

    }

    //******************************************************************
    private void addMarkerToMap(String title, String snippet, double lat, double lng, String placeId, int index)
    //******************************************************************
    {
        String markerColor = "#ED4C3C";
        LatLng sportsLocation = new LatLng(lat, lng);
        if (snippet.equals("Current location"))
            markerColor = "#3878C7";

        if (mPreferredPharmaciesIds != null)
            for (val ids : mPreferredPharmaciesIds) {
                if (ids.equals(placeId))
                    markerColor = "#4ad285";
            }
        mGoogleMap.addMarker(new MarkerOptions()
                .position(sportsLocation)
                .title(title)
                .snippet(snippet)
                .icon(getMarkerIcon(markerColor))
                .draggable(false)
                .visible(true))
                .setTag(index);

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (snippet.equals("Current location"))
                    return false;
                val getZindex = (int) marker.getTag();
                if (mPlacesResult.get(getZindex) == null)
                    return false;

                val placeId = mPlacesResult.get(getZindex).getPlace_id();
                if (placeId != null) {
                    for (val ids : mPreferredPharmaciesIds) {
                        if (ids.equals(placeId))
                            mPreferredId = ids;
                        else
                            mPreferredId = "";
                    }
                    showLoadingDialog();
                    mGoogleAddressViewModel.getGoogleAddress(Constants.FIELDS, placeId, Constants.KEY);
                }

                return true;
            }
        });
    }

    //***********************************************************
    public BitmapDescriptor getMarkerIcon(String color)
    //***********************************************************
    {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    //***********************************************************
    private int checkPermissionStatus(@NonNull String permission)
    //***********************************************************
    {

        if (ActivityCompat.checkSelfPermission(AndroidUtil.getContext(),
                permission) == PERMISSION_GRANTED)
            return PERMISSION_GRANTED;

        if (ActivityCompat.checkSelfPermission(AndroidUtil.getContext(),
                permission) == PERMISSION_DENIED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return PERMISSION_DENIED_PERMANENTLY;
            return PERMISSION_DENIED;
        }
        return PERMISSION_DEFAULT;
    }

    //***********************************************************************
    private void showPermissionSnackBar(@NonNull String message)
    //***********************************************************************
    {
        Log.d(TAG, "showPermissionSnackBar: ");
        val snackBar = Snackbar.make(mBinding.main,
                AndroidUtil.getString(R.string.permission_request, message),
                Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction(getString(R.string.request), v ->
        {
            requestPermission(LOCATION_PERMISSION);
        });

        View snackBarView = snackBar.getView();
        snackBarView.setTranslationY(-(80));
        snackBar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        snackBar.show();
    }

    //***********************************************************
    private void requestPermission(@NonNull String permission)
    //***********************************************************
    {
        ActivityCompat.requestPermissions(this, new String[]{permission},
                PERMISSION_REQUEST_CODE);
    }

    //******************************************************************
    @Override
    public void onResume()
    //******************************************************************
    {
        super.onResume();
        mBinding.mapview.onResume();
    }

    //***************************************************
    @Override
    public void onInfoWindowClick(Marker marker)
    //***************************************************
    {
    }

    //*************************************************************
    private void validation()
    //*************************************************************
    {
        val name = mBinding.pharmacyName.getText()
                .toString();
        val phone = mBinding.phone.getText()
                .toString();
        val email = mBinding.email.getText()
                .toString();
        val province = mBinding.pharmacyProvince.getText()
                .toString();
        val country = mBinding.pharmacyCountry.getText()
                .toString();
        val postalCode = mBinding.pharmacyPostalCode.getText()
                .toString();
        val address = mBinding.pharmacyAddress.getText()
                .toString();
        val address2 = mBinding.pharmacyAddress2.getText()
                .toString();

        if (TextUtils.isEmpty(name)) {
            mBinding.pharmacyName.setError(AndroidUtil.getString(R.string.required));
            mBinding.pharmacyName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            mBinding.pharmacyAddress.setError(AndroidUtil.getString(R.string.required));
            mBinding.pharmacyAddress.requestFocus();
            return;
        }

        val addPharmacyRequest = Laila.instance()
                .getMAddPharmacyRequest()
                .Builder();

        addPharmacyRequest.setFirst_name(name);
        addPharmacyRequest.setPhone(phone);
        addPharmacyRequest.setEmail(email);
        addPharmacyRequest.setAddress_province(province);
        addPharmacyRequest.setAddress_country(country);
        addPharmacyRequest.setAddress_pobox(postalCode);
        addPharmacyRequest.setAddress_line1(address);
        addPharmacyRequest.setAddress_line2(address2);
        addPharmacyRequest.setIs_preferred(mPreferredId);
        addPharmacyRequest.setContact_type(PHARMACY);
        addPharmacyRequest.setUser_private_code(Laila.instance().getMUser().getProfile().getUserPrivateCode());
        addPharmacyRequest.setUser_name(Laila.instance().getMUser().getProfile().getFirstName() + Laila.instance().getMUser().getProfile().getLastName());

        showLoadingDialog();
        mAddPharmacyViewModel.addPharmacy(addPharmacyRequest);

    }

    //*******************************************************************************************
    @Override
    public void onPharmacySuccessfullyAdded(@Nullable PharmacyResponse Response)
    //*******************************************************************************************
    {
        hideLoadingDialog();
        // AndroidUtil.toast(false, "Successfully added pharmacy.");
        val contact = Response.getContact();
        if (Laila.instance()
                .getMUser() == null || contact == null)
            return;

        ArrayList<Contact> contactList = Laila.instance()
                .getMUser()
                .getContacts();
        if (contactList == null)
            contactList = new ArrayList<>();
        contactList.add(contact);
        Laila.instance()
                .getMUser()
                .setContacts(contactList);
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance()
                .getMUser());
        Laila.instance().is_pharmacy_added = true;
        finish();
    }

    //*******************************************************************************************
    @Override
    public void onPharmacyFailedToAdded(@NonNull String errorMessage)
    //*******************************************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.toast(false, errorMessage);
    }

    //*********************************************************************
    @Override
    public void onSuccessFullyCalled(NearByPlaces nearByPlaces)
    //*********************************************************************
    {
        int index = 0;
        mPlacesResult = nearByPlaces.getResults();
        for (val place : nearByPlaces.getResults()) {
            mPlaceIds += place.getPlace_id() + ";";
            index++;
        }

        showLoadingDialog();
        mPreferredPharmaciesViewModel.getPreferredPharmacies(mPlaceIds);

    }

    //**********************************************
    @Override
    public void onSuccess(GoogleAddress response)
    //**********************************************
    {
        hideLoadingDialog();
        if (response == null || response.getResult() == null || response.getResult().getAddressComponents() == null)
            return;

        mAddressComponents = new ArrayList<>();
        val addressComponents = response.getResult().getAddressComponents();

        for (val address : addressComponents) {
            mAddressComponents.add(address);
        }

        val city = getAddressParameters("administrative_area_level_2");
        val province = getAddressParameters("administrative_area_level_1");
        val country = getAddressParameters("country");
        val postalCode = getAddressParameters("postal_code");
        val name = response.getResult().getName();
        val address = response.getResult().getFormattedAddress();
        val phone = response.getResult().getFormattedPhoneNumber();

        val currentCountry = AndroidUtil.getDeviceCountryCode(this);
        if (currentCountry.equals("us"))
            mBinding.provinceTitle.setText(R.string.state);

        mBinding.pharmacyName.setText(name);
        mBinding.pharmacyAddress.setText(address);
        mBinding.pharmacyProvince.setText(province);
        mBinding.pharmacyCountry.setText(country);
        mBinding.pharmacyPostalCode.setText(postalCode);
        mBinding.phone.setText(phone);
        mBinding.pharmacyCity.setText(city);


        Log.d(TAG, "" + response.getResult());
    }

    //*********************************************************************
    private String getAddressParameters(String valueType)
    //*********************************************************************
    {
        for (val adComponent : mAddressComponents) {
            for (val type : adComponent.getTypes()) {
                if (type.equals(valueType))
                    return adComponent.getLongName();
            }
        }
        return null;
    }

    //*********************************************************************
    @Override
    public void onSuccessFullyGetPreferredPharmacies(PreferredPharmacies response)
    //*********************************************************************
    {
        hideLoadingDialog();
        if (response == null || response.getPreferredIds() == null)
            return;
        mPreferredPharmaciesIds = new ArrayList<>();

        for (val ids : response.getPreferredIds())
            mPreferredPharmaciesIds.add(ids);

        int index = 0;


        for (val place : mPlacesResult) {
            addMarkerToMap(place.getName(),
                    place.getVicinity(),
                    place.getGeometry()
                            .getLocation()
                            .getLat(), place.getGeometry()
                            .getLocation()
                            .getLng(),
                    place.getPlace_id(),
                    index);
            index++;
        }

        Log.d(TAG, "" + mPreferredPharmaciesIds);
    }

    //**********************************************
    @Override
    public void onFailed(String message)
    //**********************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(message, AndroidUtil.getString(R.string.error), this);
    }

    //***********************************************************
    @Override
    protected boolean showStatusBar()
    //***********************************************************
    {
        return false;
    }

}