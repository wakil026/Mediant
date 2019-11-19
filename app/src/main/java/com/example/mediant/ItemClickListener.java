package com.example.mediant;

public interface ItemClickListener{
    void onClick(int position);
    void onLongClick(int position);
    void onChecked(int position, Boolean status);
}