package com.example.harita2.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.harita2.model.place;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface placedou {
    @Query("SELECT*FROM place")
   Flowable <List<place>>gettAll();

    @Insert
    Completable insert(place Place);

    @Delete
    Completable delete(place Place);
}
