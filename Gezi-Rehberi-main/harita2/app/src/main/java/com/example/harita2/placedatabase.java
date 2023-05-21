package com.example.harita2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.harita2.model.place;
import com.example.harita2.room.placedou;

@Database(entities = {place.class},version = 1)
public abstract class placedatabase extends RoomDatabase {
public abstract placedou Placedou();
}
