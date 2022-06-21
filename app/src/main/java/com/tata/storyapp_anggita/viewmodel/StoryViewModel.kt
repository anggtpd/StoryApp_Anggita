package com.tata.storyapp_anggita.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tata.storyapp_anggita.data.repository.MainRepository
import com.tata.storyapp_anggita.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val userRepo: MainRepository, private val storyRepo: StoryRepository) : ViewModel() {
    fun getToken() : LiveData<String> {
        return userRepo.getToken().asLiveData()
    }

    fun uploadStory(token: String, imageMultipart: MultipartBody.Part, desc: RequestBody) = storyRepo.uploadStory(token, imageMultipart, desc)
}