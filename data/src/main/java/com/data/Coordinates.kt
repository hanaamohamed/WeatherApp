package com.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coordinates(
    val lat: Float,
    val lon: Float
) : Parcelable