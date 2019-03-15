package com.andrea.com.popmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrea.com.popmovies.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private String[] urls;

    private final clickHandler mClickHandler;

    public TrailerAdapter (Context context, clickHandler clickHandler){
        this.mClickHandler = clickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_trailer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText("Trailer "+Integer.toString(position));
    }

    @Override
    public int getItemCount() {
        if (urls == null){return 0;}
        return urls.length;
    }

    public interface clickHandler{
        void onCLick (String trailerSelected);
    }

    /**
     * To generate view if new data is recieved
     * @param newUrls
     */
    public void setData (String[] newUrls){
        urls = newUrls;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private final ImageView playIcon;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.iv_trailer_holder);
            playIcon = (ImageView) v.findViewById(R.id.iv_play_button);}

        public TextView getTextView() { return textView; }
    }
}
