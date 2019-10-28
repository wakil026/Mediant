package com.example.mediant;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class ViewHolderforMedicine extends RecyclerView.ViewHolder {
    TextView brandName;
    TextView genericName;
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
