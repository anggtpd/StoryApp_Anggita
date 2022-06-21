package com.tata.storyapp_anggita.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tata.storyapp_anggita.data.repository.MainRepository
import com.tata.storyapp_anggita.data.retrofit.ApiConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

object MainInjection {
    fun provideRepository(context: Context): MainRepository {
        val apiService = ApiConfig.getApiService()
        return MainRepository.getInstance(context.dataStore, apiService)
    }
}