package com.tata.storyapp_anggita.viewmodel

import androidx.lifecycle.ViewModel
import com.tata.storyapp_anggita.data.repository.MainRepository

class RegisterViewModel(private val repo: MainRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) = repo.register(name, email, password)
}