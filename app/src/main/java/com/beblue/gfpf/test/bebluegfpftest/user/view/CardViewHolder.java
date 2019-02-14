package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beblue.gfpf.test.bebluegfpftest.R;
import com.beblue.gfpf.test.bebluegfpftest.user.view.CardRecyclerViewAdapter.RecyclerViewClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.ghuser_image)
    public ImageView image;

    @BindView(R.id.ghuser_name)
    public TextView name;

    @BindView(R.id.ghuser_html_url)
    public TextView htmlUrl;

    private RecyclerViewClickListener mRecyclerViewClickListener;

    public CardViewHolder(@NonNull View itemView, RecyclerViewClickListener recyclerViewClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
        mRecyclerViewClickListener = recyclerViewClickListener;
    }

    @Override
    public void onClick(View view) {
        mRecyclerViewClickListener.recyclerViewListClicked(view, getLayoutPosition());
    }
}
