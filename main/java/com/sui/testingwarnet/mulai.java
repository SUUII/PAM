package com.sui.testingwarnet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class mulai extends AppCompatActivity {

    ImageView login,registrasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mulai);
        login  = findViewById(R.id.login);
        registrasi = findViewById(R.id.registrasi);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrasi();
            }
        });
    }
    private void login(){
        Intent login = new Intent (mulai.this,login.class);
        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
    }
    private void registrasi(){
        Intent registrasi = new Intent (mulai.this, Registrasi_User.class);
        registrasi.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(registrasi);
    }
}
