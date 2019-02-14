package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beblue.gfpf.test.bebluegfpftest.R;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardViewHolder> {

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position);
    }

    private Context mContext;
    private List<GHUser> mUsers = new ArrayList<>();
    private RecyclerViewClickListener mRecyclerViewClickListener;

    public CardRecyclerViewAdapter(Context context, RecyclerViewClickListener recyclerViewClickListener) {
        mContext = context;
        mRecyclerViewClickListener = recyclerViewClickListener;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(layoutView, mRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

        if (mUsers != null && position < mUsers.size()) {
            GHUser user = mUsers.get(position);

            //User image
            if (user != null) {
                Picasso.with(mContext)
                        .load(user.getAvatarUrl())
                        .placeholder(R.drawable.ic_thumbnail)
                        //.resize(150, 150)
                        //.centerCrop()
                        .into(holder.image);

                //User name
                holder.name.setText(user.getLogin());

                //Git hub url
                holder.htmlUrl.setText(user.getGHUrl());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public GHUser getItem(int position) {
        return mUsers.get(position);
    }

    public void replaceData(List<GHUser> users) {
        setList(users);
        notifyDataSetChanged();
    }

    public void appendData(List<GHUser> users) {
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    private void setList(List<GHUser> users) {
        mUsers = checkNotNull(users);
    }
}