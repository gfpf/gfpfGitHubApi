package com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.beblue.gfpf.test.bebluegfpftest.R
import com.beblue.gfpf.test.bebluegfpftest.databinding.UserCardItemBinding
import com.beblue.gfpf.test.bebluegfpftest.data.domain.GHUser
import com.squareup.picasso.Picasso
import javax.inject.Inject

class UserRecyclerViewAdapter @Inject constructor(
    private val mContext: Context,
    private val mUserRecyclerViewClickListener: UserRecyclerViewClickListener
) : RecyclerView.Adapter<UserCardViewHolder>() {

    interface UserRecyclerViewClickListener {
        fun recyclerViewClicked(v: View, position: Int)
    }

    private var mUsers: List<GHUser> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = UserCardItemBinding.inflate(layoutInflater, parent, false)
        return UserCardViewHolder(itemBinding, mUserRecyclerViewClickListener)
    }

    override fun onBindViewHolder(holder: UserCardViewHolder, position: Int) {
        if (position < mUsers.size) {
            val user = mUsers[position]

            // User image
            Picasso.get()
                .load(user.avatarUrl)
                .placeholder(R.drawable.ic_thumbnail)
                //.resize(150, 150)
                //.centerCrop()
                .into(holder.binding.ghuserImage)

            // User name
            holder.binding.ghuserName.text = user.login

            // GitHub URL
            holder.binding.ghuserHtmlUrl.text = user.ghUrl
        }
        animateView(holder.itemView)
    }

    private fun animateView(itemView: View) {
        // Apply animation
        val animation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.card_fade_in)
        itemView.startAnimation(animation)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    fun isEmpty(): Boolean {
        return mUsers.isEmpty()
    }

    fun getItem(position: Int): GHUser {
        return mUsers[position]
    }

    fun getItems(): List<GHUser> {
        return mUsers
    }

    fun replaceData(users: List<GHUser>) {
        setList(users)
        notifyDataSetChanged()
    }

    fun appendData(users: List<GHUser>) {
        mUsers = mUsers + users
        notifyDataSetChanged()
    }

    private fun setList(users: List<GHUser>) {
        mUsers = users
    }
}
