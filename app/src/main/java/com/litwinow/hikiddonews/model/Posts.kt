package com.litwinow.hikiddonews.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Posts(
    val posts: List<Post>
) : Parcelable