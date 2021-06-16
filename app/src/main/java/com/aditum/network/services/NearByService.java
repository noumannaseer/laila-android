package com.aditum.network.services;
 import com.aditum.models.pharmact_places.GoogleAddress;
 import com.aditum.models.pharmact_places.NearByPlaces;

 import retrofit2.Call;
 import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NearByService
{

    //**************************************************************
    @Headers("Accept: application/json")
    @GET("/maps/api/place/nearbysearch/json")
    Call<NearByPlaces>
    getPlacesApi(@Query(value = "location",encoded = true) String location, @Query("radius") int radius, @Query("types") String types, @Query("key") String key);
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @GET("/maps/api/place/details/json")
    Call<GoogleAddress>
    getGoogleAddress(@Query(value = "fields",encoded = true) String fields, @Query("place_id") String place_id, @Query("key") String key);
    //**************************************************************

}
