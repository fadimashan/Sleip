package se.mobileinteraction.sleip.data.api

import io.reactivex.Single
import okhttp3.MultipartBody
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
        @Query("registration_id") accessKey: String,
        @Query("name") deviceName: String?,
        @Query("active") active: Boolean?,
        @Query("type") type: String
    ): Single<Device>


    @GET("/horse")
    fun getHorseList(
    ): Single<List<Horse>>

    @POST("/horse/")
    fun createHorse(
        @Body horse: CreateHorseBody
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
    ): Single<List<Recording>>


    @GET("/recording/")
    fun recList(
    ): Single<Recording>

    @POST("/recording/")
    fun createRec(
        @Query("comment") comment: String?,
        @Query("name") recName: String?,
        @Query("video") video: String?,
        @Query("horse") horseName: String?
    ): Single<Recording>

    @GET("/recording/{id}")
    fun getRecordsList(
        @Path("id") recId: Int
    ): Single<List<Recording>>

    @PUT("/recording/{id}")
    fun updateRec(
        @Path("id") recId: Int,
        @Query("comment") comment: String?,
        @Query("video") video: String?
    ): Single<Recording>

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

    @Multipart
    @POST("/horse/")
    fun uploadImage(
        @Part image: MultipartBody.Part?
    ): Single<UploadResponse>?
}
