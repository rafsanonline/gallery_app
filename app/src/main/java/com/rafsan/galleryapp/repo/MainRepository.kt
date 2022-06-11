package com.rafsan.galleryapp.repo
import androidx.paging.ExperimentalPagingApi
import com.rafsan.galleryapp.BuildConfig
import com.rafsan.galleryapp.data.network.IApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


@ExperimentalPagingApi
class MainRepository @Inject constructor(
    private val apiService: IApiService) {

    companion object {
        const val PHOTOS = "photos"
        const val CLIENT_ID = BuildConfig.ACCESS_KEY
    }

     suspend fun apiGetPhotos(
        hashMap: HashMap<String, String>
    ): Response<ResponseBody> {
        return withContext(Dispatchers.IO) {
            hashMap["client_id"] = CLIENT_ID
            apiService.getRequest(PHOTOS, hashMap = hashMap)
        }
    }

}