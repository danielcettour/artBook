package com.example.artbook.repo

import androidx.lifecycle.LiveData
import com.example.artbook.model.ImageResponse
import com.example.artbook.roomdb.Art
import com.example.artbook.utils.Resource

interface ArtRepositoryInterface {

    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art: Art)

    fun getArts(): LiveData<List<Art>>

    suspend fun searchImage(imageString: String): Resource<ImageResponse>
}
