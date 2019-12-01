package com.sui.testingwarnet;

import android.content.DialogInterface;
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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Menu_Maps_SewaSeat extends AppCompatActivity {
    String namawarnet, jambuka, jamtutup;
    TextView nama, hari, hargatotal, seat, saldouser, sisasaldo;
    EditText jammulai, jamselesai;
    int hargaperjam = 0;
    Button back, daftarkan;
    private DatabaseReference databasewarnet, databasewarnet2, databaseuser2, databaseReference, databaseReference2;
    private FirebaseDatabase mFirebaseInstance;
    CalendarView calendarView2;
    String username;
    String uanguser = "0";
    String pcseat = "";
    int sisa;
    int kursi;
    String nomerpc = "-";
    int bocash = 0;
    int hargato = 0;
    int totaljam = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapuser_sewaseat);
        Intent intent = getIntent();
        nama = findViewById(R.id.namawarnet);
        hari = findViewById(R.id.hari);
        jammulai = findViewById(R.id.jammulai);
        jamselesai = findViewById(R.id.jamselesai);
        hargatotal = findViewById(R.id.hargatotal);
        seat = findViewById(R.id.seat);
        saldouser = findViewById(R.id.saldouser);
        sisasaldo = findViewById(R.id.sisasaldo);
        namawarnet = intent.getExtras().getString("namawarnet");
        hargaperjam = intent.getExtras().getInt("hargaperjam");
        jambuka = intent.getExtras().getString("jambuka");
        jamtutup = intent.getExtras().getString("jamtutup");
        username = intent.getExtras().getString("username");
        kursi = Integer.parseInt(intent.getExtras().getString("banyakkursi"));
        mFirebaseInstance = FirebaseDatabase.getInstance();
        nama.setText(namawarnet);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        hari.setText(df.format(c));
        jammulai.addTextChangedListener(cekbanyak);
        jamselesai.addTextChangedListener(cekbanyak);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu_Maps_SewaSeat.super.onBackPressed();
            }
        });
        munculkan();
        daftarkan = findViewById(R.id.daftarkan);
        daftarkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nomerpc.equals("-")) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Menu_Maps_SewaSeat.this);
                    dlgAlert.setTitle("Konfirmasi");
                    dlgAlert.setMessage("Konfirmasi Menyewa Seat di " + namawarnet + " di Pc Nomer-" + nomerpc + " Bermain Selama " + totaljam + " Jam  Dengan Total Harga Rp. " + hargato + ",-");
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFirebaseInstance.getReference("users").child(username).child("bocash").setValue(Integer.toString(sisa));
                            databasewarnet.child(nomerpc).child(jammulai.getText().toString() + "-" + jamselesai.getText().toString()).setValue(username);
                            int hargaa = hargato + bocash;
                            String hargaaa = Integer.toString(hargaa);
                            databasewarnet2.child("bocash").setValue(hargaaa);
                            finish();
                        }
                    });
                    dlgAlert.setNegativeButton("Batal", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                } else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Menu_Maps_SewaSeat.this);
                    dlgAlert.setTitle("PERINGATAN");
                    dlgAlert.setMessage("Tolong Masukkan Jam lain");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            }
        });
        saldouser.setText("Rp. " + uanguser + ",-");
        databasewarnet = mFirebaseInstance.getReference("ListWarnet").child(namawarnet).child("ListTanggal").child(df.format(c));
    }

    private void munculkan() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference2 = databaseReference.child(username);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data_User test = dataSnapshot.getValue(Data_User.class);
                uanguser = test.getBocash();
                saldouser.setText("Rp. " + uanguser + ",-");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databasewarnet2 = mFirebaseInstance.getReference("ListWarnet").child(namawarnet).child("DataOpWarnet");
        databasewarnet2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bocash = Integer.parseInt(dataSnapshot.getValue(Data_OpWarnet.class).getBocash());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private TextWatcher cekbanyak = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            saldouser.setText("Rp. " + uanguser + ",-");
            try {
                if (jammulai.getText().length() == jambuka.length() && Integer.parseInt(jammulai.getText().toString()) < Integer.parseInt(jambuka) || jammulai.getText().length() == jamtutup.length() && Integer.parseInt(jammulai.getText().toString()) > Integer.parseInt(jamtutup)) {
                    jammulai.setText("");
                    hargatotal.setText("Rp. 0,-");
                    sisasaldo.setText("Rp. 0,-");
                    seat.setText("Pc Nomer -");
                    nomerpc = "-";
                }
                if (jamselesai.getText().length() == jambuka.length()&&Integer.parseInt(jamselesai.getText().toString()) < Integer.parseInt(jambuka) || jamselesai.getText().length() == jamtutup.length()&&Integer.parseInt(jamselesai.getText().toString()) > Integer.parseInt(jamtutup)) {
                    jamselesai.setText("");
                    hargatotal.setText("Rp. 0,-");
                    sisasaldo.setText("Rp. 0,-");
                    seat.setText("Pc Nomer -");
                    nomerpc = "-";
                }
                if (jamselesai.getText().length() == jammulai.getText().length() && Integer.parseInt(jamselesai.getText().toString()) < Integer.parseInt(jammulai.getText().toString())) {
                    jamselesai.setText("");
                    hargatotal.setText("Rp. 0,-");
                    sisasaldo.setText("Rp. 0,-");
                    seat.setText("Pc Nomer -");
                    nomerpc = "-";
                }

            } catch (Exception e) {
            }
            if (jamselesai.getText().toString().isEmpty()) {
                hargatotal.setText("Rp. 0,-");
                sisasaldo.setText("Rp. 0,-");
                seat.setText("Pc Nomer -");
                nomerpc = "-";
            }
            if (jammulai.getText().toString().isEmpty()) {
                hargatotal.setText("Rp. 0,-");
                sisasaldo.setText("Rp. 0,-");
                seat.setText("Pc Nomer -");
                nomerpc = "-";
            }
            if (!jammulai.getText().toString().isEmpty() && !jamselesai.getText().toString().isEmpty()) {
                if (Integer.parseInt(jamselesai.getText().toString()) > Integer.parseInt(jammulai.getText().toString())) {
                    totaljam = Integer.parseInt(jamselesai.getText().toString()) - Integer.parseInt(jammulai.getText().toString());
                    hargato = (hargaperjam * totaljam);
                    int uang = Integer.parseInt(uanguser);
                    sisa = uang - hargato;
                    String hargatot = Integer.toString(hargato);
                    hargatotal.setText("Rp. " + hargatot + ",-");
                    sisasaldo.setText("Rp. " + Integer.toString(sisa) + ",-");
                    if (sisa < 0) {
                        nomerpc = "-";
                    } else {
                        databasewarnet.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() == 0) {
                                    seat.setText("Pc Nomer 1");
                                    nomerpc = "1";
                                } else {
                                    ArrayList<String> jamuser = new ArrayList<>();
                                    for (int j = Integer.parseInt(jammulai.getText().toString()); j < Integer.parseInt(jamselesai.getText().toString()); j++) {
                                        jamuser.add("nomer" + j);
                                        Log.e("added", "nomer" + j);
                                    }
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.e("datasnapshot", dataSnapshot1.getKey());
                                        boolean cek = true;
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                            Log.e("datasnapshot2", dataSnapshot2.getKey());
                                            String[] sebelum = dataSnapshot2.getKey().split("-");
                                            for (int x = Integer.parseInt(sebelum[0]); x < Integer.parseInt(sebelum[1]); x++) {
                                                Log.e("cek", "nomer" + x);
                                                if (jamuser.contains("nomer" + x)) {
                                                    cek = false;
                                                    Log.e("cekstatus", "false");
                                                    break;
                                                }
                                            }
                                        }
                                        if (cek) {
                                            Log.e("masuk", "true");
                                            seat.setText("Pc Nomer " + dataSnapshot1.getKey());
                                            nomerpc = dataSnapshot1.getKey();
                                            break;
                                        } else {
                                            Log.e("masuk", "false");
                                            int sementara = Integer.parseInt(dataSnapshot1.getKey()) + 1;
                                            if (sementara > kursi) {
                                                seat.setText("Tidak tersedia kursi");
                                                nomerpc = "-";
                                                break;
                                            }
                                            pcseat = Integer.toString(sementara);
                                            seat.setText("Pc Nomer " + pcseat);
                                            nomerpc = pcseat;
                                        }
//
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
