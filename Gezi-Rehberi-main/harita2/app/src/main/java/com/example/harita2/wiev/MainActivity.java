package com.example.harita2.wiev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.harita2.R;
import com.example.harita2.adapter.adaptos;
import com.example.harita2.databinding.ActivityMainBinding;
import com.example.harita2.databinding.ActivityMapsBinding;
import com.example.harita2.model.place;
import com.example.harita2.placedatabase;
import com.example.harita2.room.placedou;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    placedou Placedao;
    placedatabase pd;
    place Place;
    adaptos Adaptos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//binding bağlama
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        pd= Room.databaseBuilder(getApplicationContext(),placedatabase.class,"places").build();
        Placedao=pd.Placedou();

        //dao dan flowable gelir
        compositeDisposable.add(Placedao.gettAll()
                //işlemin yapılcağı yer
                .subscribeOn(Schedulers.io())
                // işlemin gözleneceği yer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MainActivity.this::hanlerresponse)

        );

    }

    private void hanlerresponse(List<place>placeList){
//gelen verileri adaptöre atama
        binding.view.setLayoutManager(new LinearLayoutManager(this));

        adaptos Adaptos=new adaptos(placeList);
        binding.view.setAdapter(Adaptos);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.kaynakmenu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.deneme){
            Intent intent=new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}