package com.example.mediant;

//import android.app.ListActivity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    AmbulanceSearchListActivity listActivity;
    List<Model> modelList;
    Context context;

    public CustomAdapter(AmbulanceSearchListActivity listActivity, List<Model> modelList) {
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
            public void onItemLongClick(View view,final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(listActivity,R.style.AlertDialogStyle);
                builder.setTitle("Warning");
                builder.setMessage("Are you sure to call this ambulance?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Make Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listActivity.getPhoneNumber(position);
                        listActivity.makeCall();

                    }
                });
                builder.create()
                        .show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ambulanceName.setText(modelList.get(position).getName());
        holder.contactNumber.setText(modelList.get(position).getContactNumber());
        holder.serviceArea.setText(modelList.get(position).getServiceArea());
        //
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
