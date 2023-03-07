package com.woongsnote.mcb.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(

    var back: String? = null,
    var poster: String? = null,
    var title: String? = null,
    var releaseDate: String? = null,
    var overview: String? = null,

    val rating: Double? = null,
    var viewDate: String? = null,
    var comment: String? = null

    ) : Parcelable
