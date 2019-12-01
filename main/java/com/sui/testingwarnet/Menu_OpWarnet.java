package com.sui.testingwarnet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Menu_OpWarnet extends AppCompatActivity {
    TextView selamatdatang;
    TextView q,w,e,f ;
    DatePicker date;
    String namawarnet;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    String saldo = null, usernameop;
    Button lihatpemesanan,kelolawarnet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_opwarnet);
        final Intent intent = getIntent();
        namawarnet = intent.getExtras().getString("namawarnet");
        q = findViewById(R.id.namauserbaru);
        w = findViewById(R.id.emailuserbaru);
        e = findViewById(R.id.nomerhpuserbaru);
        f = findViewById(R.id.bocashsaldo);
        q.setText(namawarnet);
        lihatpemesanan = findViewById(R.id.lihatpemesanan);
        kelolawarnet = findViewById(R.id.kelolawarnet);
        databaseReference = FirebaseDatabase.getInstance().getReference("ListWarnet");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Data_OpWarnet dataOpWarnet = dataSnapshot.child(namawarnet).child("DataOpWarnet").getValue(Data_OpWarnet.class);
//               try{
                    usernameop = dataOpWarnet.getId();
                   w.setText(dataOpWarnet.getEmail());
                   e.setText(dataOpWarnet.getNohp());
                   f.setText("Rp. "+dataOpWarnet.getBocash()+",-");
//               }
//               catch (Exception e){}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        kelolawarnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Menu_OpWarnet.this,Menu_OpWarnet_KelolaWarnet.class);
                intent1.putExtra("namawarnet",namawarnet);
                intent1.putExtra("usernameop",usernameop);
                startActivity(intent1);
            }
        });
        lihatpemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Menu_OpWarnet.this,Menu_OpWarnet_LihatPesanan.class);
                intent1.putExtra("namawarnet",namawarnet);
                startActivity(intent1);
            }
        });
    }

    public void logout(View view){
        this.finish();
        System.exit(0);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }
}
