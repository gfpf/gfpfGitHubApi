package com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.beblue.gfpf.test.bebluegfpftest.databinding.UserCardItemBinding

class UserCardViewHolder(
    val binding: UserCardItemBinding,
    private val mUserRecyclerViewClickListener: UserRecyclerViewAdapter.UserRecyclerViewClickListener
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        mUserRecyclerViewClickListener.recyclerViewClicked(view, layoutPosition)
    }
}
