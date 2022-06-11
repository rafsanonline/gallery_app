package com.rafsan.galleryapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rafsan.galleryapp.data.preference.PreferencesHelper
import com.rafsan.galleryapp.repo.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalPagingApi
@HiltViewModel
class MainViewModel @Inject constructor(
    var mainRepository: MainRepository
):  ViewModel() {

      fun apiPhotos() {
         viewModelScope.launch {
             val hashMap = HashMap<String, String>()
             hashMap["page"] = "1"
             mainRepository.apiGetPhotos(hashMap = hashMap)
         }
    }


}