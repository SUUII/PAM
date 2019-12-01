package com.sui.testingwarnet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Menu_Maps_FasilitasWarnet extends AppCompatActivity {
    TextView judul, alamat, harga,jambuka,jamtutup,processor,vga,ram,banyak,gamenya;
    Button back,sewapc,listgame;
    private DatabaseReference databasewarnet;
    private FirebaseDatabase mFirebaseInstance;
    int aktif = 0;
    int hargaa;
    String jambukaa,jamtutupp;
    String username,saldo;
    String seat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapuser_datawarnet);
        judul = findViewById(R.id.judulgan);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        saldo = intent.getExtras().getString("saldouser");
        final String namawarnet = intent.getExtras().getString("namawarnet");
        mFirebaseInstance = FirebaseDatabase.getInstance();
        databasewarnet = mFirebaseInstance.getReference("ListWarnet");
        alamat = findViewById(R.id.alamat);
        harga = findViewById(R.id.hargaperjam);
        jambuka = findViewById(R.id.jambuka);
        jamtutup = findViewById(R.id.jamtutup);
        banyak = findViewById(R.id.banyak);
        processor = findViewById(R.id.processor);
        vga = findViewById(R.id.vga);
        ram = findViewById(R.id.ram);
        gamenya = findViewById(R.id.gamenya);
        databasewarnet.child(namawarnet).child("DataWarnet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data_Warnet dataWarnet = dataSnapshot.getValue(Data_Warnet.class);
                alamat.setText(dataWarnet.getAlamat());
                harga.setText("Rp. "+dataWarnet.getHargaperjam()+",-");
                jambuka.setText(dataWarnet.getJambuka()+".00");
                jambukaa = dataWarnet.getJambuka();
                jamtutup.setText(dataWarnet.getJamtutup()+".00");
                jamtutupp = dataWarnet.getJamtutup();
                banyak.setText("("+dataWarnet.getBanyakpc()+" pc)");
                seat = dataWarnet.getBanyakpc();
                processor.setText(dataWarnet.getProcessor());
                vga.setText(dataWarnet.getVga());
                ram.setText(dataWarnet.getRam()+" GB");
                hargaa = Integer.parseInt(dataWarnet.getHargaperjam());
                gamenya.setText(dataWarnet.getGame().replace("|","\n"));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sewapc = findViewById(R.id.sewapc);
        sewapc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Menu_Maps_FasilitasWarnet.this, Menu_Maps_SewaSeat.class);
                intent1.putExtra("namawarnet",namawarnet);
                intent1.putExtra("hargaperjam",hargaa);
                intent1.putExtra("jambuka",jambukaa);
                intent1.putExtra("jamtutup",jamtutupp);
                intent1.putExtra("username",username);
                intent1.putExtra("saldouser",saldo);
                intent1.putExtra("banyakkursi",seat);
                startActivity(intent1);
            }
        });
        listgame = findViewById(R.id.listgame);
        listgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aktif == 0){
                    gamenya.setVisibility(View.VISIBLE);
                    judul.setText("List Game");
                    listgame.setText("Tutup List Game");
                    aktif = 1;
                }else{
                    gamenya.setVisibility(View.INVISIBLE);
                    judul.setText(namawarnet);
                    listgame.setText("Lihat List Game");
                    aktif = 0;
                }
            }
        });
        judul.setText(namawarnet);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu_Maps_FasilitasWarnet.super.onBackPressed();
            }
        });

    }
}
