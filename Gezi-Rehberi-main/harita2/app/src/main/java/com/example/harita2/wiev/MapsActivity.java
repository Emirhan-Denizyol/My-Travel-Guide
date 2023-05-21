package com.example.harita2.wiev;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.harita2.R;
import com.example.harita2.model.place;
import com.example.harita2.placedatabase;
import com.example.harita2.room.placedou;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.harita2.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ActivityResultLauncher<String> izin;
    LocationManager konumyöneticisi;
    LocationListener konumdinleyicisi;
    SharedPreferences test;
    boolean bilgi;
    public int i;
    placedatabase pd;
    placedou Placedou;
    place selectplace;

    Double seçilenenlem;


    Double seçilenboylam;
//rxjava
    private CompositeDisposable compositeDisposable=new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        izinyönetici();
        test=MapsActivity.this.getSharedPreferences("com.example.harita2",MODE_PRIVATE);
        bilgi=false;

        pd= Room.databaseBuilder(MapsActivity.this,placedatabase.class,"places").build();
        Placedou=pd.Placedou();

        seçilenboylam=0.0;
        seçilenenlem=0.0;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        Intent intent=getIntent();
        String info=intent.getStringExtra("info");

        if(info.equals("new")){

            //konum yöneticisi
            konumyöneticisi= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            //konum değiştiğide uyarı alma konumu güncelleme
            konumdinleyicisi=new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {


                    bilgi=test.getBoolean(" bilgi",false);


                    //tek seferlik çalıştırma
                    if(!bilgi){

                        test.edit().putBoolean("bilgi",true).apply();
                        LatLng kullanıcıkonum=new LatLng(location.getLatitude(),location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kullanıcıkonum,15));
                    }



                }
            };
//izinler
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                    Snackbar.make(binding.getRoot(),"harita izni",Snackbar.LENGTH_INDEFINITE).setAction("izin ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            izin.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                    }).show();
                }else{

                    izin.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }else{
                konumyöneticisi.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,konumdinleyicisi);

                Location sonkonum=konumyöneticisi.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                if(sonkonum!=null){
                    LatLng latLng=new LatLng(sonkonum.getLatitude(),sonkonum.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                }

                mMap.setMyLocationEnabled(true);
            }
        }else{

            mMap.clear();

            selectplace= (place) intent.getSerializableExtra("place");

            LatLng latLng=new LatLng(selectplace.latitud,selectplace.longitud);

            mMap.addMarker(new MarkerOptions().position(latLng).title("name"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            binding.yer.setText(selectplace.name);


        }

    }
    private void izinyönetici(){
        izin=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    if(ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                        konumyöneticisi.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, konumdinleyicisi);

                        //yüklenen son konuma yönlendirme
                        Location sonkonum=konumyöneticisi.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(sonkonum!=null){
                            LatLng latLng=new LatLng(sonkonum.getLatitude(),sonkonum.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                        }
                    }
                }else{
                    Toast.makeText(MapsActivity.this,"izin verilmeden kullanılmaz",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("seçilen bölge"));
        seçilenboylam=latLng.longitude;
        seçilenenlem=latLng.latitude;


        binding.save.setEnabled(true);
    }

    public void save(View view){

        place Place=new place(binding.yer.getText().toString(),seçilenenlem,seçilenboylam);
       compositeDisposable.add(Placedou.insert(Place)
               //işlemin yapılcağı yer
               .subscribeOn(Schedulers.io())
               // işlemin gözleneceği yer
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(MapsActivity.this::handleresponse)

       );
    }

    public void handleresponse(){
        Intent intent=new Intent(MapsActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void delete(View view){

        compositeDisposable.add(Placedou.delete(selectplace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MapsActivity.this::handleresponse)
        );
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        compositeDisposable.clear();
    }
}