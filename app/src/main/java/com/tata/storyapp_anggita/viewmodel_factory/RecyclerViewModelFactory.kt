package com.tata.storyapp_anggita.viewmodel_factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tata.storyapp_anggita.data.repository.MainRepository
import com.tata.storyapp_anggita.di.MainInjection
import com.tata.storyapp_anggita.viewmodel.LoginViewModel
import com.tata.storyapp_anggita.viewmodel.RegisterViewModel

class RecyclerViewModelFactory(private val userRepo: MainRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class" + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: RecyclerViewModelFactory? = null
        fun getInstance(context: Context): RecyclerViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RecyclerViewModelFactory(MainInjection.provideRepository(context))
            }.also { instance = it }
    }
}