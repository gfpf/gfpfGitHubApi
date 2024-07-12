package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beblue.gfpf.test.bebluegfpftest.R;
import com.beblue.gfpf.test.bebluegfpftest.databinding.CardItemBinding;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardViewHolder> {

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position);
    }

    private final Context mContext;
    private List<GHUser> mUsers = new ArrayList<>();
    private final RecyclerViewClickListener mRecyclerViewClickListener;

    public CardRecyclerViewAdapter(Context context, RecyclerViewClickListener recyclerViewClickListener) {
        mContext = context;
        mRecyclerViewClickListener = recyclerViewClickListener;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CardItemBinding itemBinding = CardItemBinding.inflate(layoutInflater, parent, false);
        return new CardViewHolder(itemBinding, mRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if (mUsers != null && position < mUsers.size()) {
            GHUser user = mUsers.get(position);

            //User image
            if (user != null) {
                Picasso.get()
                        .load(user.getAvatarUrl())
                        .placeholder(R.drawable.ic_thumbnail)
                        //.resize(150, 150)
                        //.centerCrop()
                        .into(holder.binding.ghuserImage);

                //User name
                holder.binding.ghuserName.setText(user.getLogin());

                //Git hub url
                holder.binding.ghuserHtmlUrl.setText(user.getGHUrl());
            }
        }
        animateView(holder.itemView);
    }

    private void animateView(View itemView) {
        // Apply animation
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.card_fade_in);
        itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public boolean isEmpty() {
        return mUsers.isEmpty();
    }

    public GHUser getItem(int position) {
        return mUsers.get(position);
    }

    public List<GHUser> getItems() {
        return mUsers;
    }

    public void replaceData(List<GHUser> users) {
        setList(users);
        notifyDataSetChanged();
    }

    public void appendData(List<GHUser> users) {
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    private void setList(@NonNull List<GHUser> users) {
        mUsers = users;
    }
}