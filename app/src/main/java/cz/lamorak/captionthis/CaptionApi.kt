package cz.lamorak.captionthis

import cz.lamorak.captionthis.model.DescriptionResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CaptionApi {

    @Multipart
    @POST("/vision/v2.0/describe")
    fun describeImage(@Part imageFile: MultipartBody.Part): Single<DescriptionResponse>
}