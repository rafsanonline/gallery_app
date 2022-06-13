package com.rafsan.galleryapp.view_model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rafsan.galleryapp.data.local_db.PhotoDatabase
import com.rafsan.galleryapp.data.model.PhotoResponseItem
import com.rafsan.galleryapp.data.network.IApiService
import com.rafsan.galleryapp.repo.MainRepository
import com.rafsan.galleryapp.utils.PhotoRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalPagingApi
@HiltViewModel
class MainViewModel @Inject constructor(
    var mainRepository: MainRepository,
    var photoDatabase: PhotoDatabase,
    var apiService: IApiService
):  ViewModel() {

    var imageURL: String?=null
    var data: Flow<PagingData<PhotoResponseItem>>?=null
    var imageData: Bitmap?=null
    var isPermissionGranted: Boolean= false


    fun apiPhotos(): Flow<PagingData<PhotoResponseItem>> {
        val pagingSourceFactory = { photoDatabase.photoDao().photoList()}
        return Pager(config = PagingConfig(pageSize = 30),
            remoteMediator = PhotoRemoteMediator(
                repository = mainRepository,
                photoDatabase = photoDatabase,
            ), pagingSourceFactory = pagingSourceFactory) /*{
            PhotoSource(mainRepository = mainRepository, viewModelScope)
        }*/.flow.cachedIn(viewModelScope)
    }

    init {
        viewModelScope.launch {
            data = apiPhotos()
        }
    }

}