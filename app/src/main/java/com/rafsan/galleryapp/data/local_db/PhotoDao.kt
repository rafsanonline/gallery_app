package com.rafsan.galleryapp.data.local_db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafsan.galleryapp.data.model.PhotoResponseItem


@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos_table")
    fun photoList() : PagingSource<Int, PhotoResponseItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photo: List<PhotoResponseItem>)

    @Query("DELETE FROM photos_table")
    suspend fun deleteAllPhoto()

}