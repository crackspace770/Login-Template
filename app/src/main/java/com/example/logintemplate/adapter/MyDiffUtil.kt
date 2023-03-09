package com.example.logintemplate.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.logintemplate.model.MainItem

class MyDiffUtil(
    private val oldList: List<MainItem>,
    private val newList: List<MainItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].nama == newList[newItemPosition].nama

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}