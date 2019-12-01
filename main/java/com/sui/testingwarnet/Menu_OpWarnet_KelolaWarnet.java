package com.sui.testingwarnet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Menu_OpWarnet_KelolaWarnet extends AppCompatActivity {
    String namawarnet;
    private DatabaseReference databasewarnet, databasewarnet2, databaseuser2, databaseReference, databaseReference2;
    private FirebaseDatabase mFirebaseInstance;
    TextView hari, statuspenghapusan, namawarnett;
    EditText seat, jammulai, jamselesai;
    String jambuka, jamtutup, jumlahseat, tanggal, usernameop;
    Button daftarkan, back;
    int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_opwarnet_kelolawarnet);
        Intent intent = getIntent();
        namawarnet = intent.getExtras().getString("namawarnet");
        usernameop = intent.getExtras().getString("usernameop");
        mFirebaseInstance = FirebaseDatabase.getInstance();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        hari = findViewById(R.id.hari);
        hari.setText(df.format(c));
        daftarkan = findViewById(R.id.daftarkan);
        statuspenghapusan = findViewById(R.id.statuspenghapusan);
        seat = findViewById(R.id.seat);
        jammulai = findViewById(R.id.jammulai);
        jamselesai = findViewById(R.id.jamselesai);
        seat.addTextChangedListener(cekbanyak);
        jammulai.addTextChangedListener(cekbanyak);
        jamselesai.addTextChangedListener(cekbanyak);
        tanggal = df.format(c);
        namawarnett = findViewById(R.id.namawarnet);
        namawarnett.setText(namawarnet);

        databasewarnet = mFirebaseInstance.getReference("ListWarnet").child(namawarnet);
        databasewarnet.child("DataWarnet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data_Warnet dataWarnet = dataSnapshot.getValue(Data_Warnet.class);
                jambuka = dataWarnet.getJambuka();
                jamtutup = dataWarnet.getJamtutup();
                jumlahseat = dataWarnet.getBanyakpc();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        daftarkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 1) {
                    mFirebaseInstance.getReference("ListWarnet").child(namawarnet).child("ListTanggal").child(tanggal).child(seat.getText().toString()).child(jammulai.getText().toString() + "-" + jamselesai.getText().toString()).setValue(usernameop);
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Menu_OpWarnet_KelolaWarnet.this);
                    dlgAlert.setTitle("Berhasil");
                    dlgAlert.setMessage("Seat Berhasil Ditutup");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                } else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Menu_OpWarnet_KelolaWarnet.this);
                    dlgAlert.setTitle("PERINGATAN");
                    dlgAlert.setMessage("Seat Sudah Disewa");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                }

            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu_OpWarnet_KelolaWarnet.super.onBackPressed();
            }
        });
    }

    private TextWatcher cekbanyak = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                if (Integer.parseInt(jammulai.getText().toString()) < Integer.parseInt(jambuka) || Integer.parseInt(jammulai.getText().toString()) > Integer.parseInt(jamtutup)) {
                    jammulai.setText("");
                    status = 0;
                    statuspenghapusan.setText("Masukan data lengkap");
                }
                if (Integer.parseInt(jamselesai.getText().toString()) < Integer.parseInt(jambuka) || Integer.parseInt(jamselesai.getText().toString()) > Integer.parseInt(jamtutup)) {
                    jamselesai.setText("");
                    status = 0;
                    statuspenghapusan.setText("Masukan data lengkap");
                }
                if (jamselesai.getText().toString().equals("1") || jamselesai.getText().toString().equals("2")) {
                } else {
                    if (Integer.parseInt(jamselesai.getText().toString()) < Integer.parseInt(jammulai.getText().toString())) {
                        jamselesai.setText("");
                        status = 0;
                        statuspenghapusan.setText("Masukan data lengkap");
                    }
                }
            } catch (Exception e) {
            }
            if (jamselesai.getText().toString().isEmpty()) {
                statuspenghapusan.setText("Masukan data lengkap");
                status = 0;
            }
            if (jammulai.getText().toString().isEmpty()) {
                statuspenghapusan.setText("Masukan data lengkap");
                status = 0;
            }
            if (seat.getText().toString().isEmpty()) {
                statuspenghapusan.setText("Masukan data lengkap");
                status = 0;
            }
            if (Integer.parseInt(seat.getText().toString()) > Integer.parseInt(jumlahseat)) {
                statuspenghapusan.setText("Pc Tidak Tersedia");
                status = 0;
            } else {
                if (!jammulai.getText().toString().isEmpty() && !jamselesai.getText().toString().isEmpty() && !seat.getText().toString().isEmpty()) {
                    databasewarnet2 = mFirebaseInstance.getReference("ListWarnet").child(namawarnet).child("ListTanggal").child(tanggal).child(seat.getText().toString());
                    if (Integer.parseInt(jamselesai.getText().toString()) > Integer.parseInt(jammulai.getText().toString())) {
                        databasewarnet2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() == 0) {
                                    statuspenghapusan.setText("Seat Dapat Ditutup pada jam berapapun");
                                    status = 1;
                                } else {
                                    ArrayList<String> jamuser = new ArrayList<>();
                                    for (int j = Integer.parseInt(jammulai.getText().toString()); j < Integer.parseInt(jamselesai.getText().toString()); j++) {
                                        jamuser.add("nomer" + j);
                                        Log.e("added", "nomer" + j);
                                    }
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.e("datasnapshot", dataSnapshot1.getKey());
                                        boolean cek = true;
                                        String[] sebelum = dataSnapshot1.getKey().split("-");
                                        for (int x = Integer.parseInt(sebelum[0]); x < Integer.parseInt(sebelum[1]); x++) {
                                            Log.e("cek", "nomer" + x);
                                            if (jamuser.contains("nomer" + x)) {
                                                cek = false;
                                                Log.e("cek", "false");
                                                break;
                                            }
                                        }
                                        if (cek) {
                                            Log.e("cek", "true");
                                            statuspenghapusan.setText("Seat Dapat Ditutup");
                                            status = 1;
                                        } else {
                                            Log.e("cek", "false");
                                            statuspenghapusan.setText("Seat sudah disewa pada jam tersebut");
                                            status = 0;
                                            break;
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        }
    };

}
