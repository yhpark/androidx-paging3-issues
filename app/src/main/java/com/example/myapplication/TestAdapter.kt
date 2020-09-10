package com.example.myapplication

import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TestAdapter : PagingDataAdapter<TestData, TestAdapter.TestViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<TestData>() {
        override fun areItemsTheSame(oldItem: TestData, newItem: TestData): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: TestData, newItem: TestData): Boolean =
            oldItem == newItem
    }

    class TestViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val tv = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(10)
        }
        return TestViewHolder(tv)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val item = getItem(position)
        holder.view.text = item?.id?.toString() ?: "placeholder at $position"
    }
}