package com.yavuz.cameraxdemo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UriModel(
    val modelUri: Uri? = null
):Parcelable
