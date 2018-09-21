package com.example.vasha.vasharadio.musicplayer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.vasha.vasharadio.R;
import com.example.vasha.vasharadio.musicplayer.model.MP3Song;

import java.util.ArrayList;
import java.util.List;

public class MP3SongAdapter extends RecyclerView.Adapter<MP3SongAdapter.MP3SongViewHolder> implements Filterable {

    private ArrayList<MP3Song> mp3SongArrayList;
    private ArrayList<MP3Song> mp3SongArrayListFull;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class MP3SongViewHolder extends RecyclerView.ViewHolder{

        public TextView songTitleTextView;
        public TextView songArtistTextView;

        public MP3SongViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            songTitleTextView = itemView.findViewById(R.id.songTitle);
            songArtistTextView = itemView.findViewById(R.id.songArtist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }

    public MP3SongAdapter(ArrayList<MP3Song> mp3SongArrayList){
        this.mp3SongArrayList = mp3SongArrayList;
        mp3SongArrayListFull = new ArrayList<>(mp3SongArrayList);
    }

    @NonNull
    @Override
    public MP3SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mp3songitem,parent,false);
        MP3SongViewHolder mp3svh = new MP3SongViewHolder(v,mListener);

        return mp3svh;
    }

    @Override
    public void onBindViewHolder(@NonNull MP3SongViewHolder holder, int position) {
        holder.songTitleTextView.setText(mp3SongArrayList.get(position).getSongTitle());
        holder.songArtistTextView.setText(mp3SongArrayList.get(position).getSongArtist());
    }

    @Override
    public int getItemCount() {
        return mp3SongArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return mp3SongArrayListFilter;
    }

    Filter mp3SongArrayListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MP3Song> mp3SongArrayListFiltered = new ArrayList<>();
            if(constraint == null || constraint.length() == 0)
            {
                mp3SongArrayListFiltered.addAll(mp3SongArrayListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(MP3Song mp3Song : mp3SongArrayList)
                {
                    if(mp3Song.getSongTitle().toLowerCase().contains(filterPattern))
                    {
                        mp3SongArrayListFiltered.add(mp3Song);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values= mp3SongArrayListFiltered;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mp3SongArrayList.clear();
            mp3SongArrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

}
