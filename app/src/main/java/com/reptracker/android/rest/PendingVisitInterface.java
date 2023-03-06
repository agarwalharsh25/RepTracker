package com.reptracker.android.rest;

import com.reptracker.android.models.DefaultResponse;
import com.reptracker.android.models.PendingVisit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PendingVisitInterface {

    @GET("pendingVisit/{id}")
    Call<List<PendingVisit>> getPendingVisits(@Path("id") String id);

    @FormUrlEncoded
    @POST("pendingVisit/{id}")
    Call<PendingVisit> getHospitalPendingVisit(
            @Path("id") String id,
            @Field("hospitalId") String hospitalId,
            @Field("reachedHospitalTime") String reachedHospitalTime
    );

    @FormUrlEncoded
    @PUT("pendingVisit/visitStartTime/{id}")
    Call<DefaultResponse> updateVisitStartTime(
            @Path("id") String id,
            @Field("hospitalId") String hospitalId,
            @Field("visitStartTime") String visitStartTime
    );

    @FormUrlEncoded
    @PUT("pendingVisit/{id}")
    Call<DefaultResponse> update(
            @Path("id") String id,
            @Field("visitStatus") String visitStatus,
            @Field("visitFinishTime") String visitFinishTime,
            @Field("startPresentationTime") String startPresentationTime,
            @Field("finishPresentationTime") String finishPresentationTime,
            @Field("signature") String signature,
            @Field("picture") String picture,
            @Field("notes") String notes
    );

}
