package com.project.dasi.donasi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DonasiModel(
    var name: String? = null,
    var owner: String? = null,
    var ownerId: String? = null,
    var nominal: Long? = null,
    var to: String? = null,
    var description: String? = null,
    var dateStart: Long? = null,
    var dateEnd: Long? = null,
    var image: String? = null,
    var uid: String? = null,
    var donateValue: Long? = null,
) : Parcelable