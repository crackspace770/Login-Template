package com.example.logintemplate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.logintemplate.databinding.ItemMainBinding
import com.example.logintemplate.model.MainItem

class MainAdapter(private val listItem: ArrayList<MainItem>): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: ItemClickCallBack
    private val mainItem = ArrayList<MainItem>()

    fun setOnItemClickCallback(onItemClickCallback: ItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<MainItem>){
        val diffUtil = MyDiffUtil(mainItem, items)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        mainItem.clear()
        mainItem.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = listItem.size

    inner class ViewHolder(private var binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mainItem: MainItem) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(mainItem.gambar)
                    .centerCrop()
                    .into(imgItemPhoto)
                tvTitle.text = mainItem.nama
                tvDescription.text = mainItem.deskripsi

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(mainItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mainItem[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listItem[holder.adapterPosition]) }
    }



}