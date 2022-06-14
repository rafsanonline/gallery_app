package com.rafsan.galleryapp.data.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rafsan.galleryapp.data.model.PhotoKeyData
import com.rafsan.galleryapp.data.model.PhotoResponseItem

@Database(entities = [PhotoResponseItem::class, PhotoKeyData::class],version = 2)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao() : PhotoDao
    abstract fun photoKeyDao() : PhotoKeyDao
}