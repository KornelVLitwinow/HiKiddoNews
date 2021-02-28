package com.litwinow.hikiddonews.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "posts", indices = [Index(value = ["id"])])
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val description: String,
    val icon: String,
    val title: String
) : Parcelable