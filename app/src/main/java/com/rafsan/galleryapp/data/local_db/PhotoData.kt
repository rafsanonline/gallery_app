package com.rafsan.galleryapp.data.local_db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "photos")
data class PhotoData(
    @PrimaryKey(autoGenerate = true) val id : Int ,
    val image_small : String,
    val regular_small : String,
)