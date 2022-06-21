package com.tata.storyapp_anggita.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.tata.storyapp_anggita.R
import com.tata.storyapp_anggita.databinding.ActivityRegisterBinding
import com.tata.storyapp_anggita.viewmodel.RegisterViewModel
import com.tata.storyapp_anggita.data.Result
import com.tata.storyapp_anggita.viewmodel_factory.RecyclerViewModelFactory


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            tvLinkLogin.setOnClickListener{
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            btnRegister.setOnClickListener {
                register()
            }
        }
        setRegisterViewModel()
    }

    private fun setRegisterViewModel() {
        val factory: RecyclerViewModelFactory = RecyclerViewModelFactory.getInstance(this)
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }

    private fun register() {
        val txtName = binding.etName.text.toString().trim()
        val txtEmail = binding.emailEditText.text.toString().trim()
        val txtPw = binding.passwordEditText.text.toString().trim()
        when {
            txtName.isEmpty() -> {
                binding.etName.error = "Enter your name"
            }
            txtEmail.isEmpty() -> {
                binding.emailEditText.error = "Enter your email"
            }
            txtPw.isEmpty() -> {
                binding.passwordEditText.error = "Enter your password"
            }
            else -> {
                registerViewModel.register(txtName, txtEmail, txtPw).observe(this){ result ->
                    if (result != null){
                        when(result) {
                            is Result.Loading -> {
                                showProgressBar(true)
                            }
                            is Result.Success -> {
                                showProgressBar(false)
                                val user = result.data
                                if (user.error) {
                                    Toast.makeText(this@RegisterActivity, user.message, Toast.LENGTH_SHORT).show()
                                }else{
                                    AlertDialog.Builder(this@RegisterActivity).apply {
                                        setMessage("Congrats, your account has created!")
                                        setPositiveButton("Next") {_, _ ->
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                                }
                            }
                            is Result.Error -> {
                                showProgressBar(false)
                                Toast.makeText(
                                    this,
                                    resources.getString(R.string.register_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.apply {
            etName.isEnabled = !isLoading
            emailEditText.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading

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