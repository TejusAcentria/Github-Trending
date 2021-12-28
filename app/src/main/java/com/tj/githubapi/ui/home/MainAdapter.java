package com.tj.githubapi.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tj.githubapi.R;
import com.tj.githubapi.data.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private static List<ItemModel> mData = new ArrayList<>();

    public MainAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trending_item_view, parent, false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder mViewHolder, int position) {
        ItemModel data = mData.get(position);
        if (data.getName() != null) mViewHolder.setTitle(data.getName());
        if (data.getOwners() != null) mViewHolder.setAvatar(data.getOwners().getAvatar_url());

    }

    @Override
    public int getItemCount() {
        return (mData == null ? 0 : mData.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final CircleImageView mAvatatImg;


        public ViewHolder(@NonNull final View mView) {
            super(mView);
            mTitle = mView.findViewById(R.id.title);
            mAvatatImg = mView.findViewById(R.id.avatarImg);
        }

        public void setTitle(String title) {
            mTitle.setText(title);
        }

        public void setAvatar(String avatar) {
            Picasso.get().load(avatar).into(mAvatatImg);
        }


    }


    public void clearData() {
        mData = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addData(List<ItemModel> mData) {
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

}
