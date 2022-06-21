package com.tata.storyapp_anggita.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tata.storyapp_anggita.databinding.ActivityLoginBinding
import com.tata.storyapp_anggita.viewmodel.LoginViewModel
import com.tata.storyapp_anggita.data.Result
import com.tata.storyapp_anggita.viewmodel_factory.RecyclerViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            tvNewRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            btnLogin.setOnClickListener{
                login()
            }
        }

        setupViewModel()

    }

    private fun login() {
        val txtEmail = binding.etEmail.text.toString().trim()
        val txtPw = binding.etPw.text.toString().trim()
        when {
            txtEmail.isEmpty() -> {
                binding.etEmail.error = "Enter your email"
            }
            txtPw.isEmpty() -> {
                binding.etPw.error = "Enter your password"
            }
            else -> {
                loginViewModel.login(txtEmail, txtPw).observe(this){result ->
                    if (result != null) {
                        when(result) {
                            is Result.Loading -> {
                                showProgressBar(true)
                            }
                            is Result.Success -> {
                                showProgressBar(false)
                                val user = result.data
                                if (user.error) {
                                    Toast.makeText(this@LoginActivity, user.message, Toast.LENGTH_SHORT).show()
                                }else{
                                    val token = user.loginResult?.token ?: ""
                                    loginViewModel.setToken(token, true)
                                }
                            }
                            is Result.Error -> {
                                showProgressBar(false)
                                Toast.makeText(
                                    this, "Login failed, please try again!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        val factory: RecyclerViewModelFactory = RecyclerViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this){ token ->
            if (token.isNotEmpty()){
                startActivity(Intent(this, RecyclerActivity::class.java))
                finish()
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.apply {
            etEmail.isEnabled = !isLoading
            etPw.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading

            if (isLoading) {
                loadingSpinner.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .start()
            } else {
                loadingSpinner.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .start()
            }
        }
    }
}