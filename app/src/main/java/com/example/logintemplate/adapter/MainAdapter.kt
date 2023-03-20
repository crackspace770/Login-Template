package com.example.logintemplate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.logintemplate.R
import com.example.logintemplate.databinding.ItemMainBinding
import com.example.logintemplate.model.MainItem
import java.util.ArrayList

class MainAdapter(private val listItem: ArrayList<MainItem>) : RecyclerView.Adapter<MainAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    inner class ListViewHolder(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MainItem) {
            binding.apply {
                binding.apply {
                    Glide.with(itemView.context)
                        .load({data.gambar})
                        .centerCrop()
                        .into(imgItemPhoto)
                    tvTitle.setText(data.nama)
                    tvDescription.setText(data.deskripsi)

                    itemView.setOnClickListener {
                        onItemClickCallback.onItemClicked(data)
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int = listItem.size



    interface OnItemClickCallback {
        fun onItemClicked(data: MainItem)
    }

}