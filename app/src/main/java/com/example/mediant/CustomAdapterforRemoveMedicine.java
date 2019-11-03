package com.example.mediant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapterforRemoveMedicine extends RecyclerView.Adapter<ViewHolderforMedicine> {
    MedicineRemoveListActivity listActivity;
    List<MedicineInfo> modelList;
    Context context;

    public CustomAdapterforRemoveMedicine(MedicineRemoveListActivity listActivity, List<MedicineInfo> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;

    }


    @NonNull
    @Override
    public ViewHolderforMedicine onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_medicine, parent, false);
        ViewHolderforMedicine viewHolder = new ViewHolderforMedicine(itemView);

        viewHolder.setOnClickListener(new ViewHolderforMedicine.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);
                String options[] ={"Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            listActivity.deleteMedicineData(position);
                        }

                    }
                }).create().show();

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderforMedicine holder, int position) {
        holder.brandName.setText(modelList.get(position).getBrandName());
        holder.genericName.setText(modelList.get(position).getGenericName());
        holder.medicineType.setText(modelList.get(position).getType());
        holder.medicineContains.setText(modelList.get(position).getContains());
        holder.companyName.setText(modelList.get(position).getCompanyName());
        //
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}