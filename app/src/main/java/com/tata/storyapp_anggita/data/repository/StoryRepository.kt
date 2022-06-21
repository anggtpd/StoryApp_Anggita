package com.tata.storyapp_anggita.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.tata.storyapp_anggita.data.retrofit.ApiService
import com.tata.storyapp_anggita.data.Result
import com.tata.storyapp_anggita.data.StoryRemoteMediator
import com.tata.storyapp_anggita.data.response.ListStoryItem
import com.tata.storyapp_anggita.data.response.StoryResponse
import com.tata.storyapp_anggita.data.response.UploadResponse
import com.tata.storyapp_anggita.data.room.StoryDatabase
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.tata.storyapp_anggita.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow


class StoryRepository(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) {

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        wrapEspressoIdlingResource {
            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
                pagingSourceFactory = {
                    storyDatabase.storyDao().getStory()
                }
            ).liveData
        }
    }

    fun uploadStory(token: String, imageMultipart: MultipartBody.Part, desc: RequestBody, lat: RequestBody?, lon: RequestBody?): LiveData<Result<UploadResponse>> = liveData{
        emit(Result.Loading)
        try {
            val client = apiService.uploadStory("Bearer $token",imageMultipart, desc, lat, lon)
            emit(Result.Success(client))
        }catch (e : java.lang.Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getLoc(token: String): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.getStories("Bearer $token", location = 1)
            emit(Result.Success(client))
        }catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

}