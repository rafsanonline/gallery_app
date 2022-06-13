package com.rafsan.galleryapp.data.local_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_key_")
data class PhotoKeyData(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)