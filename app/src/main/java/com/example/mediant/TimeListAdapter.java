package com.example.mediant;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.TimeListViewHolder>{

    private ArrayList<String> timeList;
    private ItemClickListener itemClickListener;

    public TimeListAdapter(ArrayList<String> timeList, ItemClickListener itemClickListener) {
        this.timeList = timeList;
        this.itemClickListener = itemClickListener;
    }

    public class TimeListViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public TimeListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.timeInListId);
            imageView = itemView.findViewById(R.id.deleteTimeId);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public TimeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item, parent, false);
        return new TimeListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeListViewHolder holder, int position) {
        holder.textView.setText(timeList.get(position));
        holder.imageView.setImageResource(R.drawable.ic_close_black);
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }
}
