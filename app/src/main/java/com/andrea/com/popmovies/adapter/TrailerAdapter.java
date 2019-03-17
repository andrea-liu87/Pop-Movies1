package com.andrea.com.popmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.getTextView().setText("Trailer "+Integer.toString(position+1));
    }

    @Override
    public int getItemCount() {
        if (urls == null){return 0;}
        return urls.length;
    }

    public interface clickHandler{
        void onCLick (String trailerUrl);
    }

    /**
     * To generate view if new data is recieved
     * @param newUrls
     */
    public void setData (String[] newUrls){
        urls = newUrls;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.iv_string_holder);
            v.setOnClickListener(this);}

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String url = urls[position];
            mClickHandler.onCLick(url);
        }

        public TextView getTextView() { return textView; }
    }
}
