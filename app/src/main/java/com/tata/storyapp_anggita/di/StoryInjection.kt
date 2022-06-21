package com.tata.storyapp_anggita.di

import android.content.Context
import com.tata.storyapp_anggita.data.repository.StoryRepository
import com.tata.storyapp_anggita.data.retrofit.ApiConfig
import com.tata.storyapp_anggita.data.room.StoryDatabase

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        return StoryRepository(apiService, database)
    }
}