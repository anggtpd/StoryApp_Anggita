package com.tata.storyapp_anggita.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.tata.storyapp_anggita.data.response.ListStoryItem
import com.tata.storyapp_anggita.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Detail"

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)
        binding.apply {
            tvUsername.text = story?.name
            tvDescription.text = story?.description
        }
        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.imgDetail)
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}