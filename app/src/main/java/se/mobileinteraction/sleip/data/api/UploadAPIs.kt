package se.mobileinteraction.sleip.data.api

import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import se.mobileinteraction.sleip.entities.UploadResponse


interface UploadAPIs {

    @Multipart
    fun uploadImage(
        @Part image: MultipartBody.Part?
    ): Single<UploadResponse>?
}