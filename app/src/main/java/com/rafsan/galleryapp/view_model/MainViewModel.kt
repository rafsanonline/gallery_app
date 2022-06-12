package com.rafsan.galleryapp.view_model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rafsan.galleryapp.data.model.PhotoResponseItem
import com.rafsan.galleryapp.data.paging.PhotoSource
import com.rafsan.galleryapp.repo.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalPagingApi
@HiltViewModel
class MainViewModel @Inject constructor(
    var mainRepository: MainRepository
):  ViewModel() {

    var imageURL: String?=null
    var data: Flow<PagingData<PhotoResponseItem>>?=null
    var imageData: Bitmap?=null
    var isPermissionGranted: Boolean= false


    fun apiPhotos(): Flow<PagingData<PhotoResponseItem>> {
        return Pager(config = PagingConfig(pageSize = 30)) {
            PhotoSource(mainRepository = mainRepository, viewModelScope)
        }.flow.cachedIn(viewModelScope)
    }

    init {
        viewModelScope.launch {
            data = apiPhotos()
        }
    }


}