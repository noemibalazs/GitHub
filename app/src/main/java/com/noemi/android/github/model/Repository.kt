package com.noemi.android.github.model

import com.google.gson.annotations.SerializedName

data class Repository(
        @field:SerializedName("id") val id: Int = 0,
        @field:SerializedName("name") val name: String? = null,
        @field: SerializedName("full_name") val fullName: String? = null)