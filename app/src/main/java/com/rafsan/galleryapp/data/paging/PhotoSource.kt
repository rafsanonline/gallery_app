package com.rafsan.galleryapp.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rafsan.galleryapp.data.model.PhotoResponseItem
import com.rafsan.galleryapp.repo.MainRepository
import kotlinx.coroutines.CoroutineScope

class PhotoSource @OptIn(ExperimentalPagingApi::class) constructor(private val mainRepository: MainRepository, viewModelScope: CoroutineScope) : PagingSource<Int, PhotoResponseItem>(){

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResponseItem> {
        return try {
            val page = params.key ?: 1
            val map = HashMap<String, String>()
            map["page"] = page.toString()
            val data = mainRepository.apiGetPhotos(map)
            val type = object : TypeToken<List<PhotoResponseItem>>() {}.type
            val photo = Gson().fromJson<List<PhotoResponseItem>>(
                data.body()?.string(),
                type
            )
            LoadResult.Page(
                data = photo,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page.plus(1)
            )

        } catch (e: Exception) {
            Log.i("data-->", e.message.toString())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoResponseItem>): Int? {
        return 1
    }
}