package com.example.harita2.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Placeholder;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;

import com.example.harita2.databinding.RowBinding;
import com.example.harita2.model.place;
import com.example.harita2.wiev.MapsActivity;

import java.util.List;

public class adaptos extends RecyclerView.Adapter<adaptos.Placeholder> {
    List<place>placeList;

    public adaptos(List<place>placeList){
        this.placeList=placeList;
    }

    @NonNull
    @Override
    //nereye bağlanacağı
    public Placeholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowBinding rowBinding=RowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new Placeholder(rowBinding);
    }

    @Override
    //bağlanınca ne yapılcağı
    public void onBindViewHolder(@NonNull adaptos.Placeholder holder, int position) {

        //gelen listenin verilerini nereye yazdıracağımız
        holder.rowBinding.isim.setText(placeList.get(position).name);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.itemView.getContext(), MapsActivity.class);
                intent.putExtra("place",placeList.get(position));
                intent.putExtra("info","old");
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }



    @Override
    //kaç adet row oluşacağı
    public int getItemCount() {
        return placeList.size();
    }

    public class Placeholder extends RecyclerView.ViewHolder {
        RowBinding rowBinding;
        public Placeholder(RowBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding=rowBinding;
        }
    }
}
