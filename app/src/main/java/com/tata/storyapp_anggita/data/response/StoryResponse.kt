package com.tata.storyapp_anggita.data.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

@Parcelize
@Entity(tableName = "story")
data class ListStoryItem(

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("lat")
    val lat: Double?,

    @field:SerializedName("lon")
    val lon: Double?,
) : Parcelable