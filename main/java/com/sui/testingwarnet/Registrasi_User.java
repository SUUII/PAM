package com.sui.testingwarnet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrasi_User extends AppCompatActivity {
    EditText username, email, password, passwordulang, namauser, nomerhpuser;
    ImageView penandasemua;
    Button back, registrasi, hide, hide2;
    int statusemail = 0, statuspassword = 0, statusnomerhp = 0, statusnamauser=0, statuspasswordulang = 0, statususername = 0;
    private DatabaseReference databaseuser;
    private FirebaseDatabase mFirebaseInstance;
    List<String> hehe = new ArrayList<>();
    int hidestatus = 0;
    int hidestatus2 = 0;
    private static final String TAG = Registrasi_User.class.getSimpleName();
    TextView registrasiwarnet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        databaseuser = mFirebaseInstance.getReference("users");
        databaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Log.e("berhasil", dataSnapshot1.getKey());
                    hehe.add(dataSnapshot1.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordulang = findViewById(R.id.passwordulang);
        namauser = findViewById(R.id.namapengguna);
        nomerhpuser= findViewById(R.id.nomerhp);
        penandasemua= findViewById(R.id.gambarpenandasemua);
        back = findViewById(R.id.back);
        registrasi = findViewById(R.id.registrasi);
        hide = findViewById(R.id.hide);
        hide2 = findViewById(R.id.hide2);
        username.addTextChangedListener(cekusername);
        email.addTextChangedListener(cekemail);
        passwordulang.addTextChangedListener(cekpassword);
        password.addTextChangedListener(cekpassword);
        nomerhpuser.addTextChangedListener(ceknohp);
        namauser.addTextChangedListener(ceknama);
        registrasiwarnet = findViewById(R.id.pemilikwarnet);

        registrasiwarnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registrasi_User.this, Registrasi_Warnet_Map.class);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrasi_User.super.onBackPressed();
            }
        });


        penandasemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gg = cekstatus();
                if(!gg.equals("")) {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Registrasi_User.this);
                    dlgAlert.setTitle("PERINGATAN");
                    dlgAlert.setMessage(gg.toUpperCase());
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            }
        });
        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = username.getText().toString();
                String q = email.getText().toString();
                String w = password.getText().toString();
                String e = namauser.getText().toString();
                String r = nomerhpuser.getText().toString();
                String c = "0";
                String gg = cekstatus();
                if(gg.equals("")){
                    supergg(x,q,w,e,r,c);
                    Intent intent = new Intent(Registrasi_User.this, Menu_PengaturUtama.class);
                    intent.putExtra("username",x);
                    startActivity(intent);
                    finish();
                }else{
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Registrasi_User.this);
                    dlgAlert.setTitle("PERINGATAN");
                    dlgAlert.setMessage(gg.toUpperCase());
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hidestatus == 0){
                    // Show Password
                    hide.setBackgroundResource(R.drawable.unhide);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidestatus = 1;
                }else{
                    // Hide Password
                    hide.setBackgroundResource(R.drawable.hide);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hidestatus = 0;
                }
            }
        });
        hide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hidestatus2 == 0){
                    // Show Password
                    hide2.setBackgroundResource(R.drawable.unhide);
                    passwordulang.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidestatus2 = 1;
                }else{
                    // Hide Password
                    hide2.setBackgroundResource(R.drawable.hide);
                    passwordulang.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hidestatus2 = 0;
                }
            }
        });
    }
    public void supergg(String username, String email,String password,String nama,String nohp,String bocash){
        Data_User datauser = new Data_User(email,password,nama,nohp,bocash);
        databaseuser.child(username).setValue(datauser);
        addsuperggChangeListener();
    }
    private void addsuperggChangeListener(){
        databaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data_User dataUser= dataSnapshot.getValue(Data_User.class);
                if(dataUser == null){
                    Log.e(TAG, "User data is null");
                    return;
                }
                Log.e(TAG, "User data is changed!" + dataUser.password+ ", " + dataUser.nama+ ", " + dataUser.nohp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read user", databaseError.toException());
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private TextWatcher cekusername = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(username.getText().toString().isEmpty()){
                statususername = 0;
            }else if(!hehe.contains(username.getText().toString())){
                statususername = 1;
            }else{
                statususername = 2;
            }
            if(statusnomerhp == 1 && statusemail == 1 && statuspassword == 1 && statusnamauser ==1 &&statuspasswordulang ==1&&statususername ==1){
                penandasemua.setImageResource(R.drawable.greenemailsign);
            }else{
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher cekemail = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(email.getText().toString().isEmpty()){
                statusemail = 0;
            }else if(isEmailValid(email.getText().toString())){
                statusemail = 1;
            }else{
                statusemail = 2;
            }
            if(statusnomerhp == 1 && statusemail == 1 && statuspassword == 1 && statusnamauser ==1 &&statuspasswordulang ==1&&statususername ==1){
                penandasemua.setImageResource(R.drawable.greenemailsign);
            }else{
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
            if(!namauser.getText().toString().isEmpty()){
                statusnamauser = 1;
            }else{
                statusnamauser = 0;
            }
            if(statusnomerhp == 1 && statusemail == 1 && statuspassword == 1 && statusnamauser ==1 &&statuspasswordulang ==1&&statususername ==1){
                penandasemua.setImageResource(R.drawable.greenemailsign);
            }else{
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher cekpassword = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(password.getText().toString().equals(passwordulang.getText().toString())){
                statuspassword = 1;
                statuspasswordulang = 1;
            }else{
                statuspassword = 2;
                statuspasswordulang = 2;
            }
            if(password.getText().toString().isEmpty()){
                statuspassword = 0;
            }
            if(passwordulang.getText().toString().isEmpty()){
                statuspasswordulang = 0;
            }
            if(statusnomerhp == 1 && statusemail == 1 && statuspassword == 1 && statusnamauser ==1 &&statuspasswordulang ==1&&statususername ==1){
                penandasemua.setImageResource(R.drawable.greenemailsign);
            }else{
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private TextWatcher ceknohp = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nohp = nomerhpuser.getText().toString();
            try{
                if (!nohp.substring(0,2).equals("08")) {
                    statusnomerhp = 3;
                } else if (nohp.length() == 12) {
                    statusnomerhp = 1;
                } else {
                    statusnomerhp = 2;
                }
                if (nohp.isEmpty()) {
                    statusnomerhp = 0;
                }
            }catch (Exception e){}
            if(statusnomerhp == 1 && statusemail == 1 && statuspassword == 1 && statusnamauser ==1 &&statuspasswordulang ==1&&statususername ==1){
                penandasemua.setImageResource(R.drawable.greenemailsign);
            }else{
                penandasemua.setImageResource(R.drawable.redemailsign);
            }
        }
    };
    private String cekstatus(){
        String status = "";
        if(statususername == 0){
            status +="\nUsername tidak boleh kosong\n";
        }
        if(statususername == 2){
            status +="\nUsername Sudah Digunakan\n";
        }
        if(statusemail == 0){
            status +="\nEmail tidak boleh kosong\n";
        }
        if(statusemail == 2){
            status +="\nEmail yang anda masukan TIDAK BENAR\n";
        }
        if(statuspassword == 0){
            status +="\nPassword Tidak boleh kosong\n";
        }else if(statuspassword == 2 && statuspasswordulang == 0){
            status +="\nMasukan ulang password\n";
        }else if(statuspassword == 2 && statuspasswordulang == 2){
            status +="\nPassword yang anda masukan TIDAK SAMA\n";
        }
        if(statusnamauser == 0){
            status +="\nNama Tidak Boleh kosong\n";
        }
        if(statusnomerhp == 0){
            status +="\nNomer HP Tidak boleh kosong\n";
        }
        if(statusnomerhp == 2||statusnomerhp == 3){
            status +="\nNomer HP yang anda masukan TIDAK BENAR\n";
        }
        return status;
    }

}
