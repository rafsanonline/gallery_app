package com.rafsan.galleryapp.utils


import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rafsan.galleryapp.data.local_db.PhotoDatabase
import com.rafsan.galleryapp.data.local_db.PhotoKeyData
import com.rafsan.galleryapp.data.model.PhotoResponseItem
import com.rafsan.galleryapp.repo.MainRepository

@ExperimentalPagingApi
class PhotoRemoteMediator(
    private val repository: MainRepository,
    private val photoDatabase: PhotoDatabase,
) : RemoteMediator<Int, PhotoResponseItem>() {

    private val photoDao = photoDatabase.photoDao()
    private val photoKeyDao = photoDatabase.photoKeyDao()
    private var endOfPaginationReached = false

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoResponseItem>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

                val hashMap = HashMap<String, String>()
                hashMap["page"] = currentPage.toString()
                hashMap["per_page"] = "30"
                val response =  repository.apiGetPhotos(hashMap = hashMap)
                val type = object : TypeToken<List<PhotoResponseItem>>() {}.type
                val photo = Gson().fromJson<List<PhotoResponseItem>>(
                    response.body()?.string(),
                    type
                )
                endOfPaginationReached = photo.isEmpty()
                val prevPage = if (currentPage == 1) null else currentPage - 1
                val nextPage = if (endOfPaginationReached) null else currentPage + 1

                        if (loadType == LoadType.REFRESH) {
                            photoDao.deleteAllPhoto()
                            photoKeyDao.deleteAllPhotoKeys()
                        }

                        val keys = photo.map { photo_item ->
                            PhotoKeyData(
                                id = photo_item.id,
                                prevPage = prevPage,
                                nextPage = nextPage
                            )
                        }

                    photoDatabase.withTransaction {
                        photoKeyDao.insertPhotoKeys(remoteKeys = keys)
                        photoDao.insertPhotos(photo = photo)
                    }


            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PhotoResponseItem>
    ): PhotoKeyData? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                photoKeyDao.photoKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, PhotoResponseItem>
    ): PhotoKeyData? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { photo_item ->
                photoKeyDao.photoKeys(id = photo_item.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, PhotoResponseItem>
    ): PhotoKeyData? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { photo_item ->
                photoKeyDao.photoKeys(id = photo_item.id)
            }
    }

}