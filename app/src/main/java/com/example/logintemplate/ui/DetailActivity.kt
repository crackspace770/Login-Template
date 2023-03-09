package com.example.logintemplate.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.logintemplate.R
import com.example.logintemplate.databinding.ActivityDetailBinding
import com.example.logintemplate.model.MainItem

class DetailActivity:AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: MainItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        item = intent.extras?.getParcelable<MainItem>(EXTRA_ITEM) as MainItem

        item.nama.let{
            setActionBarTitle(item.nama.toString())
        }

        binding.apply {
            val nama = tvName
            val deskripsi = tvDescription
            val image = imgDetail

            nama.text = item.nama
            deskripsi.text = item.deskripsi
            image.setImageResource(item.gambar)
        }
        Glide.with(this)
            .load(item.gambar)
            .fitCenter()
            .into(binding.imgDetail)

    }


    private fun setActionBarTitle(nama: String) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = nama
        }
    }


    companion object {
        const val EXTRA_ITEM = "extra_user"
        const val EXTRA_FAVORITE = "extra_favorite"
    }
}