package com.project.movieapp.services.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenreResponse(
    @field:SerializedName("genres")
    val genres: List<GenresItem>,

    @field:SerializedName("status_code")
    val statusCode: String? = null,

    @field:SerializedName("status_message")
    val statusMessage: String? = null
) : Parcelable


@Parcelize
data class GenresItem(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("is_selected")
    var isSelected: Boolean = false
) : Parcelable
