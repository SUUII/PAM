package com.sui.testingwarnet;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Menu_Maps extends android.support.v4.app.FragmentActivity implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {
    public GoogleMap mMap;
    TextView judulgan;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    int penanda = 0;
    ArrayList<Double> lat = new ArrayList<>();
    ArrayList<Double> lon = new ArrayList<>();
    int total = 0;
    String username;
    String saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapuser);
        FloatingActionButton floatingActionButton;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Intent intentt = getIntent();
        username = intentt.getExtras().getString("username");
        saldo = intentt.getExtras().getString("saldouser");

        judulgan = findViewById(R.id.judulgan);

        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Menu_Maps.this));
        // Add a marker in Sydney and move the camera
        databaseReference = FirebaseDatabase.getInstance().getReference("ListWarnet");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    total++;
                    Data_Warnet dataWarnet = dataSnapshot1.child("DataWarnet").getValue(Data_Warnet.class);
                    LatLng sementara = new LatLng(Double.parseDouble(dataWarnet.getLat()), Double.parseDouble(dataWarnet.getLon()));
                    MarkerOptions markerOptions = new MarkerOptions().position(sementara).title(dataSnapshot1.getKey()).snippet(dataWarnet.getAlamat());
                    mMap.addMarker(markerOptions);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        LatLng salatiga = new LatLng(-7.324915, 110.504660);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(salatiga));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(salatiga, 15.0f));
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        judulgan.setText("Cari Warnet");
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(Menu_Maps.this, Menu_Maps_FasilitasWarnet.class);
        intent.putExtra("namawarnet", marker.getTitle());
        intent.putExtra("username", username);
        intent.putExtra("saldouser", saldo);
        startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        judulgan.setText("Klik Alamat Untuk Data Warnet");
        return false;
    }


    public void back(View view) {
        Menu_Maps.super.onBackPressed();
    }
}
