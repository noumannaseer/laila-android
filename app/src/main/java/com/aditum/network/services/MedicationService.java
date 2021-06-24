package com.aditum.network.services;


import com.aditum.models.Special;
import com.aditum.models.response_models.FollowUpResponse;
import com.aditum.models.response_models.UpcResponse;
import com.aditum.models.updates.response_models.ActiveIngredientsResponse;
import com.aditum.models.updates.response_models.EmergencyContactResponse;
import com.aditum.models.updates.response_models.MedicationResponse;
import com.aditum.models.updates.response_models.MedicineInfoResponse;
import com.aditum.models.updates.response_models.MedicineInteractionResponse;
import com.aditum.models.updates.response_models.PharmacyResponse;
import com.aditum.models.updates.response_models.PrefferedPharmacyResponse;
import com.aditum.models.updates.response_models.RefillReminderResponse;
import com.aditum.models.updates.response_models.SearchMedicationResponse;
import com.aditum.request_models.FollowUpRequest;
import com.aditum.request_models.FollowUpUpdateRequest;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MedicationService {

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/add_medication")
    Call<MedicationResponse>
    addMedication(@Body Map<String, Object> addMedication);

    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/update_medication")
    Call<MedicationResponse>
    updateMedication(@Body Map<String, Object> updateMedication);

    //**************************************************************
    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/get_medication")
    Call<MedicationResponse>
    getMedications(
            @Body HashMap<String, String> getMedications
    );

    //***************************************************************
    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/delete_medication")
    Call<MedicationResponse>
    deleteMedication(@Body Map<String, String> deleteMedication);
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("pharmacies/add_pharmacy")
    Call<PharmacyResponse>
    addPharmacy(
            @Body Map<String, Object> addPharmacy
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/search_medication")
    Call<SearchMedicationResponse>
    searchMedication(
            @Body HashMap<String, String> searchMedication
    );


    //***************************************************************░

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/search_medication")
    Call<SearchMedicationResponse>
    searchDin(
            @Body HashMap<String, String> searchDin
    );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/med_intraction")
    Call<MedicineInteractionResponse>
    drugCheckMedication(
            @Body Map<String, Object> drugCheckMedication
    );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/get_med_info")
    Call<MedicineInfoResponse>
    getMedicineInformation(
//            @Query("din") String medicineCode
            @Body HashMap<String, String> getMedicineInformation
    );

    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/activeingredient")
    Call<ActiveIngredientsResponse>
    getMedicineIngredients
    (
            @Body HashMap<String, String> getMedicineIngredients
    );
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
    @POST("pharmacies/check_place_ids")
    Call<PrefferedPharmacyResponse>
    getPreferredPharmacies(
            @Body HashMap<String, String> preferredPharmacies
    );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("pharmacies/")
    Call<PharmacyResponse>
    getPharmacies(
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
    @POST("medications/addfollowup")
    Call<FollowUpResponse>
    insertFollowUp(
            @Body FollowUpRequest followUpRequest
    );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/edit_followup")
    Call<FollowUpResponse>
    updateFollowUp(
            @Body FollowUpUpdateRequest followUpUpdateRequest
    );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("refill_request/getRefillMedIds")
    Call<RefillReminderResponse>
    getRefillReminders(
            @Body HashMap<String, String> getRefillReminders
    );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("users/add_emergency_contact")
    Call<EmergencyContactResponse>
    createEmergencyContact(@Body Map<String, Object> createEmergencyContact);

    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("users/update_emergency_contact")
    Call<EmergencyContactResponse>
    updateEmergencyContact(@Body Map<String, Object> updateEmergencyContact);

    //**************************************************************
    //**************************************************************
    @Headers("Accept: application/json")
    @POST("users/get_emergency_contact")
    Call<EmergencyContactResponse>
    getEmergencyContacts(
            @Body HashMap<String, String> getEmergencyContacts
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("users/delete_emergency_contact")
    Call<EmergencyContactResponse>
    deleteContact(@Body Map<String, String> deleteContact);
    //**************************************************************


}
