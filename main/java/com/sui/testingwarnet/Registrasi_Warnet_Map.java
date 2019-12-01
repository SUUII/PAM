package com.sui.testingwarnet;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class Registrasi_Warnet_Map extends FragmentActivity implements GoogleMap.OnCameraMoveListener,GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    TextView judulmap;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi_warnet_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        judulmap = findViewById(R.id.judulmap);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrasi_Warnet_Map.super.onBackPressed();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    LatLng salatiga;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Registrasi_Warnet_Map.this));
        salatiga = new LatLng(-7.324915, 110.504660);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(salatiga));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(salatiga, 15.0f));
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnCameraMoveListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.clear();
        judulmap.setText("Klik tahan MAP dilokasi Warnet");
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("koordinat : " + latitude + ", " + longitude).snippet(getAdrress(latLng));
        mMap.addMarker(markerOptions).showInfoWindow();
        judulmap.setText("Klik Alamat jika sudah benar");
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(Registrasi_Warnet_Map.this, Registrasi_Warnet_Fasilitas.class);
        String lat = Double.toString(marker.getPosition().latitude);
        String lon = Double.toString(marker.getPosition().longitude);
        intent.putExtra("koordinatlat", lat);
        intent.putExtra("koordinatlong", lon);
        intent.putExtra("alamat", getAdrress(marker.getPosition()));
        startActivity(intent);
        finish();
    }

    @Override
    public void onCameraMove() {
        mMap.clear();
        judulmap.setText("Klik tahan MAP dilokasi Warnet");
    }

    public String getAdrress(LatLng latLng) {
        String strAddress = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null) {
                Address returnnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i <= returnnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnnedAddress.getAddressLine(i)).append("\n");
                }
                strAddress = strReturnedAddress.toString();
                Log.w("Current Location", strReturnedAddress.toString());
            } else {
                Log.w("Current Location", "No Address returned!");
            }
        } catch (Exception x) {
            x.printStackTrace();
            ;
            Log.w("Current location", "Can't get address!");
        }
        return strAddress;
    }
}

