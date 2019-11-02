package com.example.mediant;

//import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapterforAllMedicine extends RecyclerView.Adapter<ViewHolderforMedicine> {

    AllMedicineActivity listActivity;
    List<MedicineInfo> modelList;
    Context context;

    public CustomAdapterforAllMedicine(AllMedicineActivity listActivity, List<MedicineInfo> modelList) {
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
                listActivity.showDetails(position);

            }

            @Override
            public void onItemLongClick(View view, int position) {

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
