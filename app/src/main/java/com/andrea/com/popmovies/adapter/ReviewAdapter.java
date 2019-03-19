package com.andrea.com.popmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrea.com.popmovies.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter <ReviewAdapter.ViewHolder> {

    private String[] mDataSet;

    public ReviewAdapter(String[] dataSet) { mDataSet = dataSet; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_review, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        if(mDataSet == null){return 0;}
        if (mDataSet.length <= 3){return mDataSet.length;}
        else {return 3;} }

    public void setData (String[] newData){
        mDataSet = newData;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.iv_review_holder); }

        TextView getTextView() { return textView; }
    }
}