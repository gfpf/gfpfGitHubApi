package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beblue.gfpf.test.bebluegfpftest.R;
import com.beblue.gfpf.test.bebluegfpftest.databinding.CardItemBinding;
import com.beblue.gfpf.test.bebluegfpftest.databinding.ContentMainFragBinding;
import com.beblue.gfpf.test.bebluegfpftest.user.view.CardRecyclerViewAdapter.RecyclerViewClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final CardItemBinding binding;

    private final RecyclerViewClickListener mRecyclerViewClickListener;

    public CardViewHolder(@NonNull CardItemBinding binding, RecyclerViewClickListener recyclerViewClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.mRecyclerViewClickListener = recyclerViewClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mRecyclerViewClickListener.recyclerViewListClicked(view, getLayoutPosition());
    }
}
