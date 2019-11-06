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

public class CustomAdapterforRemoveAmbulance extends RecyclerView.Adapter<ViewHolder> {

    AmbulanceRemoveListActivity listActivity;
    List<Model> modelList;
    Context context;

    public CustomAdapterforRemoveAmbulance(AmbulanceRemoveListActivity listActivity, List<Model> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_all_ambulance, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);
                String options[] ={"Update","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listActivity.getData(position);
                        if(i==0){
                            listActivity.updateAmbulanceData();
                        }
                        if(i==1){
                            listActivity.deleteAmbulanceData();
                        }

                    }
                }).create().show();

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
