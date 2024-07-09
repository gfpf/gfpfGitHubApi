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

    private final CardItemBinding binding;
    public ImageView image;
    public TextView name;
    public TextView htmlUrl;

    private RecyclerViewClickListener mRecyclerViewClickListener;

    public CardViewHolder(@NonNull CardItemBinding binding, RecyclerViewClickListener recyclerViewClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.mRecyclerViewClickListener = recyclerViewClickListener;

        // Initialize views from the binding object
        image = binding.ghuserImage;
        name = binding.ghuserName;
        htmlUrl = binding.ghuserHtmlUrl;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mRecyclerViewClickListener.recyclerViewListClicked(view, getLayoutPosition());
    }
}
