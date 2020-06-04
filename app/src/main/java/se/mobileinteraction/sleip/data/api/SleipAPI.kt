package se.mobileinteraction.sleip.data.api

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import se.mobileinteraction.sleip.entities.*


interface SleipAPI {

    @POST("/login/")
    fun userLogin(
        @Body loginBody: LoginData
    ): Single<TokenResponse>

    @POST("/devices/")
    fun createDevice(
    @Body device: Device

    ): Single<DeviceResponse>


    @GET("/horse")
    fun getHorseList(
    ): Single<List<Horse>>

    @POST("/horse/")
    fun createHorse(
            @Body horse: RequestBody
    ): Single<Horse>

    @GET("/horse/{id}/")
    fun getHorse(
        @Path("id") id: Int
    ): Single<Horse>

    @PUT("/horse/{id}/")
    fun updateHorse(
        @Path("id") horseId: Int,
        @Query("name") horseName: String,
        @Query("date_of_birth") horseBirth: String?,
        @Query("description") description: String?,
        @Query("image") horseImage: String?
    ): Single<Horse>

    @PATCH("/horse/{id}/")
    fun partialUpdateHorse(
        @Path("id") horseId: Int,
        @Query("name") horseName: String?,
        @Query("date_of_birth") horseBirth: String?,
        @Query("description") description: String?,
        @Query("image") horseImage: String?
    ): Single<Horse>

    @DELETE("/horse/{id}/")
    fun deleteHorse(
        @Path("id") horseId: Int
    ): Single<ResponseBody>

    @GET("/horse/{id}/recording/")
    fun recordingList(
        @Path("id") horseId: Int
    ): Single<List<RecordingResponse>>

    @POST("/recording/")
    fun createRec(
        @Body record: RequestBody
    ): Single<RecordingResponse>

    @PUT("/recording/{id}")
    fun updateRec(
        @Path("id") recId: Int,
        @Query("comment") comment: String?,
        @Query("video") video: String?
    ): Single<RecordingResponse>

    @PATCH("/recording/{id}")
    fun partialUpdateRec(
        @Path("id") recId: Int,
        @Query("comment") comment: String?,
        @Query("video") video: String?
    ): Single<Recording>

    @DELETE("/recording/{id}")
    fun deleteRecList(
        @Path("id") recId: Int
    ): Single<ResponseBody>

}
