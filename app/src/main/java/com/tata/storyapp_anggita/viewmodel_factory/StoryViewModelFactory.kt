package com.tata.storyapp_anggita.viewmodel_factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tata.storyapp_anggita.data.repository.MainRepository
import com.tata.storyapp_anggita.data.repository.StoryRepository
import com.tata.storyapp_anggita.di.MainInjection
import com.tata.storyapp_anggita.di.StoryInjection
import com.tata.storyapp_anggita.viewmodel.RecyclerViewModel
import com.tata.storyapp_anggita.viewmodel.StoryViewModel

class StoryViewModelFactory private constructor(private val userRepo: MainRepository, private val storyRepo: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RecyclerViewModel::class.java) -> {
                RecyclerViewModel(userRepo, storyRepo) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(userRepo, storyRepo) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryViewModelFactory? = null
        fun getInstance(context: Context): StoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryViewModelFactory(MainInjection.provideRepository(context), StoryInjection.provideRepository(context))
            }.also { instance = it }
    }
    }