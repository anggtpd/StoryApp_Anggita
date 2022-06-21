package com.tata.storyapp_anggita.ui

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.tata.storyapp_anggita.databinding.ActivityStoryBinding
import com.tata.storyapp_anggita.utils.Utils
import com.tata.storyapp_anggita.viewmodel.StoryViewModel
import com.tata.storyapp_anggita.viewmodel_factory.StoryViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.tata.storyapp_anggita.data.Result
import com.tata.storyapp_anggita.utils.Utils.uriToFile

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var token: String
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Story"

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        setViewModel()
        binding.apply {
            btnCamera.setOnClickListener { startTakePhoto() }
            btnGallery.setOnClickListener { startGaleri() }
            btnUpload.setOnClickListener { uploadImage() }
        }
    }

    private fun setViewModel() {
        val factory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
        storyViewModel = ViewModelProvider(this, factory) [StoryViewModel::class.java]

        storyViewModel.getToken().observe(this) {token ->
            if (token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                this.token = token
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Can't get permission", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadImage() {
        if (getFile != null) {
            val description = binding.etDesc.text.toString().trim()
            if (description.isEmpty()) {
                binding.etDesc.error = "Enter your description"
            }else{
                binding.progressBar.visibility = View.VISIBLE
                val file = Utils.reduceFileImage(getFile as File)
                val description = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
                storyViewModel.uploadStory(token, imageMultipart, description).observe(this) {result ->
                    if (result != null) {
                        when(result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "Upload Success", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "Failed. Cause: ${result.error}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }else {
            Toast.makeText(this@StoryActivity, "Please input a picture and a write a description.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        Utils.createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(this@StoryActivity, "com.tata.storyapp_anggita.ui.story", it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGaleri() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGaleri.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.ivPreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGaleri = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@StoryActivity)
            getFile = myFile
            binding.ivPreview.setImageURI(selectedImg)
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}