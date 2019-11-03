package com.example.mediant;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class ViewHolderforMedicine extends RecyclerView.ViewHolder {
    TextView brandName;
    TextView genericName;
    TextView medicineContains;
    TextView medicineType;
    TextView companyName;
    View mView;

    public ViewHolderforMedicine(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view,getAdapterPosition());
                return true;
            }
        });

        brandName = itemView.findViewById(R.id.medicineBrandName);
        genericName = itemView.findViewById(R.id.medicineGenericName);
        companyName = itemView.findViewById(R.id.companyName);
        medicineContains = itemView.findViewById(R.id.medicineContains);
        medicineType = itemView.findViewById(R.id.medicineType);


    }

    private ViewHolderforMedicine.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnClickListener(ViewHolderforMedicine.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
