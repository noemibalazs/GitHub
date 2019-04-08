package com.noemi.android.github.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Contributors(
        @field:SerializedName("id") val id: Int = 0,
        @field:SerializedName("login") val name: String? = "",
        @field:SerializedName("avatar_url") val avatar: String? = "")