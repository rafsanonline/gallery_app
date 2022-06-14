package com.rafsan.galleryapp.data.local_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafsan.galleryapp.data.model.PhotoKeyData


@Dao
interface PhotoKeyDao {
    @Query("SELECT * FROM photo_key_ WHERE id =:id")
    suspend fun photoKeys(id: String): PhotoKeyData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotoKeys(remoteKeys: List<PhotoKeyData>)

    @Query("DELETE FROM photo_key_")
    suspend fun deleteAllPhotoKeys()

}