package com.tata.storyapp_anggita.viewmodel

import androidx.lifecycle.ViewModel
import com.tata.storyapp_anggita.data.repository.StoryRepository

class MapsViewModel(private val storyRepo: StoryRepository) : ViewModel() {
    fun getAllStoriesWithLocation(token: String) = storyRepo.getLoc(token)
}