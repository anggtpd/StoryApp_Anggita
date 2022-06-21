package com.tata.storyapp_anggita.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tata.storyapp_anggita.data.repository.MainRepository
import com.tata.storyapp_anggita.data.repository.StoryRepository
import kotlinx.coroutines.launch

class RecyclerViewModel(private val userRepo: MainRepository, private val storyRepo: StoryRepository) : ViewModel() {
    fun getToken() : LiveData<String> {
        return userRepo.getToken().asLiveData()
    }

    fun isLogin() : LiveData<Boolean> {
        return userRepo.isLogin().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
        }
    }

    fun getStories(token: String) = storyRepo.getStories(token)
}