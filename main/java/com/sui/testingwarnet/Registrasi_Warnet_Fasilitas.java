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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Registrasi_Warnet_Fasilitas extends AppCompatActivity {
    Button back, registrasi, hide, hide2;
    EditText alamatwarnet, namawarnet, processor, vga, ram, hargaperjam, jambuka, jamtutup, banyak;
    ImageView penandasemua;
    int statusnamawarnet = 0, statusalamat = 1, statusprocessor = 0, statusvga = 0, statusram = 0, statusharga = 0, statusjambuka = 0, statusjamtutup = 0, statusbanyak = 0;
    private DatabaseReference databaseuser, databaseuser2;
    CheckBox dota, csgo, pubg, tf2, fortnite, lol, rocket, overwatch, pb, dn, ls, ayodance;
    private FirebaseDatabase mFirebaseInstance;
    List<String> listnamawarnet = new ArrayList<>();
    String game = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi_warnet_fasilitas);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        databaseuser = mFirebaseInstance.getReference("ListWarnet");
        databaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    listnamawarnet.add(dataSnapshot1.getKey());
                    Log.e("berhasil", dataSnapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        alamatwarnet = findViewById(R.id.alamat);
        Intent intent = getIntent();
        final String lat = intent.getExtras().getString("koordinatlat");
        final String lon = intent.getExtras().getString("koordinatlong");
        String alamat = intent.getExtras().getString("alamat");
        alamatwarnet.setText(alamat);
        alamatwarnet.addTextChangedListener(cekalamat);

        registrasi = findViewById(R.id.registrasi);
        namawarnet = findViewById(R.id.namawarnet);
        processor = findViewById(R.id.processor);
        vga = findViewById(R.id.vga);
        ram = findViewById(R.id.ram);
        hargaperjam = findViewById(R.id.hargaperjam);
        jambuka = findViewById(R.id.jambuka);
        jamtutup = findViewById(R.id.jamtutup);
        banyak = findViewById(R.id.banyak);
        namawarnet.addTextChangedListener(ceknama);
        processor.addTextChangedListener(cekprocessor);
        vga.addTextChangedListener(cekvga);
        ram.addTextChangedListener(cekram);
        hargaperjam.addTextChangedListener(cekharga);
        jambuka.addTextChangedListener(cekjambuka);
        jamtutup.addTextChangedListener(cekjamtutup);
        banyak.addTextChangedListener(cekbanyak);

        penandasemua = findViewById(R.id.gambarpenandasemua);
        back = findViewById(R.id.back);
        hide = findViewById(R.id.hide);
        hide2 = findViewById(R.id.hide2);
        dota = findViewById(R.id.dota);
        csgo = findViewById(R.id.csgo);
        pubg = findViewById(R.id.pubg);
        tf2 = findViewById(R.id.tf2);
        fortnite = findViewById(R.id.fortnite);
        lol = findViewById(R.id.lol);
        rocket = findViewById(R.id.rocket);
        overwatch = findViewById(R.id.overwatch);
        pb = findViewById(R.id.pb);
        dn = findViewById(R.id.dn);
        ls = findViewById(R.id.ls);
        ayodance = findViewById(R.id.ayodance);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrasi_Warnet_Fasilitas.super.onBackPressed();
            }
        });
        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked(dota);
                checked(csgo);
                checked(pubg);
                checked(tf2);
                checked(fortnite);
                checked(lol);
                checked(rocket);
                checked(overwatch);
                checked(pb);
                checked(dn);
                checked(ls);
                checked(ayodance);
                String e = namawarnet.getText().toString();
                String x = alamatwarnet.getText().toString();
                String w = processor.getText().toString();
                String r = vga.getText().toString();
                String t = ram.getText().toString();
                String harga = hargaperjam.getText().toString();
                String jam1 = jambuka.getText().toString();
                String jam2 = jamtutup.getText().toString();
                String banya = banyak.getText().toString();
                supergg(e, x, lat, lon, w, r, t, game, harga, jam1, jam2, banya);
                Intent intent = new Intent(Registrasi_Warnet_Fasilitas.this, Registrasi_Warnet_OpWarnet.class);
                intent.putExtra("namawarnet", namawarnet.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    public void supergg(String namawarnet, String alamatwarnet, String lat, String lon, String processor, String vga, String ram, String game, String hargaperjam, String jambuka, String jamtutup, String banyakpc) {
        Data_Warnet dataWarnet = new Data_Warnet(alamatwarnet, lat, lon, processor, vga, ram, game, hargaperjam, jambuka, jamtutup, banyakpc);
        databaseuser.child(namawarnet).child("DataWarnet").setValue(dataWarnet);
    }

    private void checked(CheckBox cek) {
        if (cek.isChecked()) {
            game += cek.getText().toString() + "|";
        }
    }

    private TextWatcher cekbanyak = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!banyak.getText().toString().isEmpty()) {
                statusbanyak = 1;
            } else {
                statusbanyak = 0;
            }
            if (statusbanyak == 1 && statusnamawarnet == 1 && statusalamat == 1 && statusprocessor == 1 && statusvga == 1 && statusram == 1 && statusharga == 1 && statusjambuka == 1 && statusjamtutup == 1) {
                penandasemua.setImageResource(R.drawable.greenemailsign);
            } else {
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher cekjamtutup = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                if (Integer.parseInt(jambuka.getText().toString()) < 0) {
                    jambuka.setText("");
                    statusjamtutup = 0;
                }
                if (Integer.parseInt(jamtutup.getText().toString()) > 24) {
                    jamtutup.setText("");
                    statusjamtutup = 0;
                }
                if (jamtutup.getText().toString().equals("1") || jamtutup.getText().toString().equals("2")) {
                } else {
                    if (Integer.parseInt(jamtutup.getText().toString()) < Integer.parseInt(jambuka.getText().toString())) {
                        jamtutup.setText("");
                        statusjamtutup = 0;
                    }
                }
            } catch (Exception e) {
            }
            if (!jamtutup.getText().toString().isEmpty()) {
                statusjamtutup = 1;
            } else {
                statusjamtutup = 0;
            }
            if (statusbanyak == 1 && statusnamawarnet == 1 && statusalamat == 1 && statusprocessor == 1 && statusvga == 1 && statusram == 1 && statusharga == 1 && statusjambuka == 1 && statusjamtutup == 1) {
                penandasemua.setImageResource(R.drawable.greenemailsign);
            } else {
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher cekjambuka = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                if (Integer.parseInt(jambuka.getText().toString()) < 0 || Integer.parseInt(jambuka.getText().toString()) > 23) {
                    jambuka.setText("");
                    statusjamtutup = 0;
                }
                if (Integer.parseInt(jamtutup.getText().toString()) > 24 || Integer.parseInt(jamtutup.getText().toString()) < 0) {
                    jamtutup.setText("");
                    statusjamtutup = 0;
                }
                if (jamtutup.getText().length() == jambuka.getText().length() && Integer.parseInt(jamtutup.getText().toString()) < Integer.parseInt(jambuka.getText().toString())) {
                    jamtutup.setText("");
                    statusjamtutup = 0;
                }

            } catch (Exception e) {
            }
            if (!jambuka.getText().toString().isEmpty()) {
                statusjambuka = 1;
            } else {
                statusjambuka = 0;
            }
            if (statusbanyak == 1 && statusnamawarnet == 1 && statusalamat == 1 && statusprocessor == 1 && statusvga == 1 && statusram == 1 && statusharga == 1 && statusjambuka == 1 && statusjamtutup == 1) {
                penandasemua.setImageResource(R.drawable.greenemailsign);
            } else {
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher cekharga = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!hargaperjam.getText().toString().isEmpty()) {
                statusharga = 1;
            } else {
                statusharga = 0;
            }
            if (statusbanyak == 1 && statusnamawarnet == 1 && statusalamat == 1 && statusprocessor == 1 && statusvga == 1 && statusram == 1 && statusharga == 1 && statusjambuka == 1 && statusjamtutup == 1) {
                penandasemua.setImageResource(R.drawable.greenemailsign);
            } else {
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher cekram = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!ram.getText().toString().isEmpty()) {
                statusram = 1;
            } else {
                statusram = 0;
            }
            if (statusbanyak == 1 && statusnamawarnet == 1 && statusalamat == 1 && statusprocessor == 1 && statusvga == 1 && statusram == 1 && statusharga == 1 && statusjambuka == 1 && statusjamtutup == 1) {
                penandasemua.setImageResource(R.drawable.greenemailsign);
            } else {
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher cekvga = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!vga.getText().toString().isEmpty()) {
                statusvga = 1;
            } else {
                statusvga = 0;
            }
            if (statusbanyak == 1 && statusnamawarnet == 1 && statusalamat == 1 && statusprocessor == 1 && statusvga == 1 && statusram == 1 && statusharga == 1 && statusjambuka == 1 && statusjamtutup == 1) {
                penandasemua.setImageResource(R.drawable.greenemailsign);
            } else {
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher cekprocessor = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!processor.getText().toString().isEmpty()) {
                statusprocessor = 1;
            } else {
                statusprocessor = 0;
            }
            if (statusbanyak == 1 && statusnamawarnet == 1 && statusalamat == 1 && statusprocessor == 1 && statusvga == 1 && statusram == 1 && statusharga == 1 && statusjambuka == 1 && statusjamtutup == 1) {
                penandasemua.setImageResource(R.drawable.greenemailsign);
            } else {
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher cekalamat = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!alamatwarnet.getText().toString().isEmpty()) {
                statusalamat = 1;
            } else {
                statusalamat = 0;
            }
            if (statusbanyak == 1 && statusnamawarnet == 1 && statusalamat == 1 && statusprocessor == 1 && statusvga == 1 && statusram == 1 && statusharga == 1 && statusjambuka == 1 && statusjamtutup == 1) {
                penandasemua.setImageResource(R.drawable.greenemailsign);
            } else {
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher ceknama = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (namawarnet.getText().toString().isEmpty()) {
                statusnamawarnet = 0;
            } else if (!listnamawarnet.contains(namawarnet.getText().toString())) {
                statusnamawarnet = 1;
            } else {
                statusnamawarnet = 2;
            }
            if (statusbanyak == 1 && statusnamawarnet == 1 && statusalamat == 1 && statusprocessor == 1 && statusvga == 1 && statusram == 1 && statusharga == 1 && statusjambuka == 1 && statusjamtutup == 1) {
                penandasemua.setImageResource(R.drawable.greenemailsign);
            } else {
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };

    private String cekstatus() {
        String status = "";
        if (statusalamat == 0) {
            status += "\nAlamat Tidak Boleh kosong\n";
        }
        if (statusnamawarnet == 0) {
            status += "\nnama warnet tidak boleh kosong\n";
        }
        if (statusnamawarnet == 2) {
            status += "\nWarnet sudah didaftarkan\n";
        }
        if (statusharga == 0) {
            status += "\nHarga Tidak Boleh kosong\n";
        }
        if (statusjambuka == 0) {
            status += "\njam buka Tidak Boleh kosong\n";
        }
        if (statusjamtutup == 0) {
            status += "\nJam tutup Tidak Boleh kosong\n";
        }
        if (statusbanyak == 0) {
            status += "\nbanyak pc Tidak Boleh kosong\n";
        }
        if (statusprocessor == 0) {
            status += "\nSpesifikasi processor Tidak Boleh kosong\n";
        }
        if (statusvga == 0) {
            status += "\nSpesifikasi VGA Tidak Boleh kosong\n";
        }
        if (statusram == 0) {
            status += "\nSpesifikasi RAM Tidak Boleh kosong\n";
        }

        return status;
    }
}
