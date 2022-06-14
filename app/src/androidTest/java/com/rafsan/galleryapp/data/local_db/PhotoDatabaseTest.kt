package com.rafsan.galleryapp.data.local_db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.rafsan.galleryapp.data.model.PhotoResponseItem
import com.rafsan.galleryapp.data.model.Urls
import com.rafsan.galleryapp.data.paging.getData
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoDatabaseTest : TestCase() {

    private lateinit var db : PhotoDatabase
    private lateinit var photoDao : PhotoDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context.applicationContext, PhotoDatabase::class.java).build()
        photoDao = db.photoDao()
    }

    @After
    fun closeDB() {
        db.close()
    }

    @Test
    fun insertAndGetData() = runBlocking {
        val data = PhotoResponseItem("1", Urls("small","small3","thumb","raw","regular","full"))
        val photoList = arrayListOf(data)
        photoDao.insertPhotos(photoList)
        val photo = photoDao.photoList().getData()
        assertThat(photoList as List<PhotoResponseItem> == photo).isTrue()
    }

}