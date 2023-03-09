package com.example.logintemplate.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MainItem(
    val nama: String ,
    val deskripsi: String,
    val gambar: Int,
    ): Parcelable

