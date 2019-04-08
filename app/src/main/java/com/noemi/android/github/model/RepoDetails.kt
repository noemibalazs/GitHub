package com.noemi.android.github.model

import com.google.gson.annotations.SerializedName

data class RepoDetails(
        @field: SerializedName("id") val repoId: Int = 0,
        @field: SerializedName("size") val repoSize: Int = 0,
        @field: SerializedName("stargazers_count") val repoGazers: Int = 0,
        @field: SerializedName("forks_count") val repoForks: Int = 0)