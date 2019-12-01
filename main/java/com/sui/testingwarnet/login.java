package com.sui.testingwarnet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    EditText username, password;
    Button submit, hide, back;
    private int statushide = 0;
    int turn = 0;

    int unlock = 0;

    private DatabaseReference databaseuser;
    private DatabaseReference databaseuser2;
    private FirebaseDatabase mFirebaseInstance;

    Map<String, String> haha = new HashMap<>();
    Map<String, String> hehe = new HashMap<>();
    Map<String, String> hehe2 = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        hide = findViewById(R.id.hide);
        back = findViewById(R.id.back);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        databaseuser = mFirebaseInstance.getReference("users");
        databaseuser2 = mFirebaseInstance.getReference("ListWarnet");
        cekdatates();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.super.onBackPressed();
            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statushide == 0) {
                    // Show Password
                    hide.setBackgroundResource(R.drawable.unhide);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    statushide = 1;
                } else {
                    // Hide Password
                    hide.setBackgroundResource(R.drawable.hide);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    statushide = 0;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testing();
                cekdata2();
            }
        });
    }


    private void testing() {
        if(haha.isEmpty()&&hehe.isEmpty()&&hehe2.isEmpty()){
            cekdatates();
        }
        if (username.getText().toString().equals("")) {
            unlock = 0;
        } else if (password.getText().toString().equals("")) {
            unlock = 3;
        } else {
            try {
                String passwordsementara = haha.get(username.getText().toString());
                if (passwordsementara.equals(password.getText().toString())) {
                    unlock = 2;
                } else {
                    unlock = 1;
                }
            } catch (Exception e) {
                unlock = 1;
            }
            try {
                String passwordsementara = hehe.get(username.getText().toString());
                if (passwordsementara.equals(password.getText().toString())) {
                    unlock = 5;
                }
            } catch (Exception e) {
            }
        }
    }

    private void cekdata2() {
        if (unlock == 5) {
            Intent intent = new Intent(login.this, Menu_OpWarnet.class);
            intent.putExtra("namawarnet",hehe2.get(username.getText().toString()));
            startActivity(intent);
            finish();
        } else if (unlock == 2) {
            Intent intent = new Intent(login.this, Menu_PengaturUtama.class);
            intent.putExtra("username", username.getText().toString());
            startActivity(intent);
            finish();
        } else if (unlock == 1) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(login.this);
            dlgAlert.setTitle("PERINGATAN");
            dlgAlert.setMessage("Username atau Password anda Salah");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        } else if (unlock == 0) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(login.this);
            dlgAlert.setTitle("PERINGATAN");
            dlgAlert.setMessage("Username tidak boleh kosong");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            Log.e("berhasil", Integer.toString(unlock));
        } else if (unlock == 3) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(login.this);
            dlgAlert.setTitle("PERINGATAN");
            dlgAlert.setMessage("Password tidak boleh kosong");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            Log.e("berhasil", Integer.toString(unlock));
        } else if (unlock == 4) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(login.this);
            dlgAlert.setTitle("PERINGATAN");
            dlgAlert.setMessage("Username atau Password anda Salah");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    private void cekdatates() {
        databaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.e("berhasil", "berhasil");
                    Log.e(dataSnapshot1.getKey(), dataSnapshot1.getValue(Data_User.class).getPassword());
                    haha.put(dataSnapshot1.getKey(), dataSnapshot1.getValue(Data_User.class).getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseuser2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try{
                        Data_OpWarnet dataOpWarnet = dataSnapshot1.child("DataOpWarnet").getValue(Data_OpWarnet.class);
                        Log.e(dataOpWarnet.getId(), dataOpWarnet.getPassword());
                        hehe.put(dataOpWarnet.getId(), dataOpWarnet.getPassword());
                        Log.e(dataOpWarnet.getId(), dataSnapshot1.getKey());
                        hehe2.put(dataOpWarnet.getId(),dataSnapshot1.getKey());
                    }catch(Exception e){}
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
