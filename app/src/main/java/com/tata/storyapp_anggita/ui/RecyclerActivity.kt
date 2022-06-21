package com.tata.storyapp_anggita.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tata.storyapp_anggita.R
import com.tata.storyapp_anggita.databinding.ActivityRecyclerBinding
import com.tata.storyapp_anggita.viewmodel.RecyclerViewModel
import com.tata.storyapp_anggita.viewmodel_factory.StoryViewModelFactory
import com.tata.storyapp_anggita.adapter.ListAdapter
import com.tata.storyapp_anggita.adapter.LoadingAdapter

class RecyclerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerBinding
    private lateinit var recyclerViewModel: RecyclerViewModel
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Story"
        binding.rvStories.layoutManager = LinearLayoutManager(this)

        setViewModel()

    }

    private fun setViewModel() {
        val factory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
        recyclerViewModel = ViewModelProvider(this, factory)[RecyclerViewModel::class.java]

        recyclerViewModel.isLogin().observe(this){
            if (!it){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        recyclerViewModel.getToken().observe(this){token ->
            this.token = token
            if (token.isNotEmpty()){
                val adapter = ListAdapter()
                binding.rvStories.adapter = adapter.withLoadStateFooter(
                    footer = LoadingAdapter {
                        adapter.retry()
                    }
                )
                recyclerViewModel.getStories(token).observe(this){result ->
                    adapter.submitData(lifecycle, result)
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                recyclerViewModel.logout()
                true
            }
            R.id.upload_story -> {
                startActivity(Intent(this, StoryActivity::class.java))
                true
            }
            R.id.toggle_location -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(MapsActivity.EXTRA_TOKEN, token)
                startActivity(intent)
                true
            }
            else -> true
        }
    }
}