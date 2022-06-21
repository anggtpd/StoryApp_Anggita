package com.tata.storyapp_anggita.viewmodel

import androidx.lifecycle.ViewModel
import com.tata.storyapp_anggita.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepo: StoryRepository) : ViewModel() {

    fun uploadStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        desc: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) = storyRepo.uploadStory(token, imageMultipart, desc, lat, lon)
}