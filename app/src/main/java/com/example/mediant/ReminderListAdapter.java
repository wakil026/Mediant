package com.example.mediant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ReminderListViewHolder> {
    private ArrayList<ReminderItem> reminderList;
    private ItemClickListener itemClickListener;

    public ReminderListAdapter(ArrayList<ReminderItem> reminderList, ItemClickListener itemClickListener) {
        this.reminderList = reminderList;
        this.itemClickListener = itemClickListener;
    }

    public class ReminderListViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView descriptionTextView;
        public Switch aSwitch;

        public ReminderListViewHolder(@NonNull final View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.reminderNameId);
            descriptionTextView = itemView.findViewById(R.id.reminderDescriptionId);
            aSwitch = itemView.findViewById(R.id.switch1Id);

            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    itemClickListener.onChecked(getAdapterPosition(), isChecked);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemClickListener.onLongClick(getAdapterPosition());
                    return true;
                }
            });

        }

    }


    @NonNull
    @Override
    public ReminderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent, false);
        return new ReminderListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderListViewHolder holder, int position) {
        ReminderItem currentItem = reminderList.get(position);
        holder.titleTextView.setText(currentItem.getName());
        holder.descriptionTextView.setText(currentItem.getDescription());
        holder.aSwitch.setChecked(currentItem.getIsEnabled());
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }
}
