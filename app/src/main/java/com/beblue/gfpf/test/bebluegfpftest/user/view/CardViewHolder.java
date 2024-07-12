package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.view.View;
import com.beblue.gfpf.test.bebluegfpftest.databinding.CardItemBinding;
import com.beblue.gfpf.test.bebluegfpftest.user.view.CardRecyclerViewAdapter.RecyclerViewClickListener;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    //TODO GFPF - How to clear anim on detach?
    /*@Override
    public void onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow();
        // Perform cleanup tasks when item view is detached from window
        // Example: Clear animations
        itemView.clearAnimation();
    }*/
}
