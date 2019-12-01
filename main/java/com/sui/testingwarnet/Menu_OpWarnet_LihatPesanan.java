package com.sui.testingwarnet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Menu_OpWarnet_LihatPesanan extends AppCompatActivity {
    Button back;
    TextView listView;
    private FirebaseDatabase mFirebaseInstance;

    DatabaseReference databaseReference;
    String catat = "", namawarnet, tanggal;
    EditText seat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_opwarnet_lihatpesanan);
        Intent intent = getIntent();
        seat = findViewById(R.id.seat);
        namawarnet = intent.getExtras().getString("namawarnet");
        listView = findViewById(R.id.listView);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        seat.addTextChangedListener(cekbanyak);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        tanggal = df.format(c);


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu_OpWarnet_LihatPesanan.super.onBackPressed();
            }
        });
    }

    private TextWatcher cekbanyak = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            catat = "";
            if (seat.getText().toString().isEmpty()) {
                catat = "";
            } else {
                databaseReference = mFirebaseInstance.getReference("ListWarnet").child(namawarnet).child("ListTanggal").child(tanggal).child(seat.getText().toString());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        catat = "\nPc Nomer-"+dataSnapshot.getKey();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.e("masuk", "masuk");
                            catat += "\nDipinjam pada jam : " + dataSnapshot1.getKey();
                            catat += "\nOleh Username :" + dataSnapshot1.getValue() + "\n";
                            listView.setText(catat);
                        }
                        if (dataSnapshot.getChildrenCount() == 0) {
                            catat = "\nPc Nomer-"+dataSnapshot.getKey()+" Belum Memiliki Pelanggan Pada Jam Berapapun";
                            listView.setText(catat);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
    };
}
