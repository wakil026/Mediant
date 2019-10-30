package com.example.mediant;

//import android.app.ListActivity;
import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapterforAllAmbulance extends RecyclerView.Adapter<ViewHolder> {

    AllAmbulanceActivity listActivity;
    List<Model> modelList;
    Context context;

    public CustomAdapterforAllAmbulance(AllAmbulanceActivity listActivity, List<Model> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ambulanceName.setText(modelList.get(position).getName());
        holder.contactNumber.setText(modelList.get(position).getContactNumber());
        //
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
