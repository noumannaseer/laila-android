package com.fantechlabs.lailaa.network.services;


import com.fantechlabs.lailaa.models.Ingredient;
import com.fantechlabs.lailaa.models.PreferredPharmacies;
import com.fantechlabs.lailaa.models.SearchMedicine;
import com.fantechlabs.lailaa.models.Special;
import com.fantechlabs.lailaa.models.response_models.DrugCheckResponse;
import com.fantechlabs.lailaa.models.response_models.FollowUpResponse;
import com.fantechlabs.lailaa.models.response_models.MedicationResponse;
import com.fantechlabs.lailaa.models.response_models.MedicineInformationResponse;
import com.fantechlabs.lailaa.models.response_models.PharmacyResponse;
import com.fantechlabs.lailaa.models.response_models.RefillRemindersResponse;
import com.fantechlabs.lailaa.models.response_models.UpcResponse;
import com.fantechlabs.lailaa.request_models.FollowUpRequest;
import com.fantechlabs.lailaa.request_models.FollowUpUpdateRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MedicationService
{

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("updatemedication/1")
    Call<MedicationResponse>
    addMedication(@Body Map<String, Object> addMedication);
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("deletemedication/1")
    Call<MedicationResponse>
    deleteMedication(@Body Map<String, Object> deleteMedication);
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("updatecontact/1")
    Call<PharmacyResponse>
    addPharmacy(
            @Body Map<String, Object> addPharmacy
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @GET("drugproduct/")
    Call<List<SearchMedicine>>
    searchMedication(
            @Query("brandname") String searchMedicine
    );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @GET("drugproduct/")
    Call<List<SearchMedicine>>
    searchDin(
            @Query("din") String searchMedicine
    );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("getInteractionWithMedications/")
    Call<DrugCheckResponse>
    drugCheckMedication(
            @Body Map<String, Object> drugCheckMedication
    );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @GET("drugproduct/")
    Call<List<MedicineInformationResponse>>
    getMedicineInformation(
            @Query("din") String medicineCode
     );

    //**************************************************************
//    @Headers("Accept: application/xml")
//    @GET("query/")
//    Call<NlmSearchResult>
//    getAllergyDetails(
//            @Query("db") String healthTopics,
//            @Query("term") String alergyName
//    );
    //**************************************************************

    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @GET
    Call<List<Ingredient>> getMedicineIngredients(@Url String url);
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("getPhmsNearMe/")
    Call<Special>
    special(
            @Body Map<String, String> special
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("check_place_ids/")
    Call<PreferredPharmacies>
    getPreferredPharmacies(
            @Body HashMap<String, String> preferredPharmacies
            );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @GET("lookup?")
    Call<UpcResponse>
    getUpcLookUp(@Query("upc") String upc);
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medfollowup/new")
    Call<FollowUpResponse>
    insertFollowUp(
            @Body FollowUpRequest followUpRequest
            );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medfollowup/update")
    Call<FollowUpResponse>
    updateFollowUp(
            @Body FollowUpUpdateRequest followUpUpdateRequest
            );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("getRefillMedIds/")
    Call<RefillRemindersResponse>
    getRefillReminders(
            @Body HashMap<String, String> userPrivateCode
    );
    //***************************************************************

}
