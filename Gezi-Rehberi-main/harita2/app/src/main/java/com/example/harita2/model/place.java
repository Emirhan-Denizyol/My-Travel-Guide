package com.example.harita2.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class place implements Serializable {

    @PrimaryKey(autoGenerate = true)
public int id;
    @ColumnInfo(name="isim")
public String name;
    @ColumnInfo(name="latidude")
public double latitud;
    @ColumnInfo(name="logitud")
public double longitud;

    public place(String name,Double latitud,Double longitud){
        this.name=name;
        this.latitud=latitud;
        this.longitud=longitud;
    }

}
