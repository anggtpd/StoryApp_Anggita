package com.tata.storyapp_anggita.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tata.storyapp_anggita.data.response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(storyEntity: List<ListStoryItem>)

    @Query("SELECT * FROM story")
    fun getStory():  PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM story")
    fun deleteAll()
}