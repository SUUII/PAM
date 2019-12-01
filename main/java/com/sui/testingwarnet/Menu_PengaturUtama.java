package com.sui.testingwarnet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class Menu_PengaturUtama extends AppCompatActivity {
    private ListView listView;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    List<Data_User> testingList;
    Button coba,back,topupbocash;
    Button limaribu,sepuluhribu,duapuluhribu,limapuluhribu,seratusribu,duaratusribu,limaratusribu,satujuta;
    TextView judul;
    BottomNavigationView menu;
    private ViewPager viewPager;
    private SectionStatePagerAdapter sectionStatePagerAdapter;
    private static final String TAG = "menu";
    private GoogleMap mMap;
    TextView q,w,e,f ;
    String username;
    String saldo = null;
    int status = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menucontainer);
        viewPager = (ViewPager) findViewById(R.id.containter);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(1);
        topupbocash = findViewById(R.id.topupbocash);
        if(status==1){
            munculkan();
        }

    }
    private void munculkan(){
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference2 = databaseReference.child(username);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data_User test = dataSnapshot.getValue(Data_User.class);
                q = findViewById(R.id.namauserbaru);
                w = findViewById(R.id.emailuserbaru);
                e = findViewById(R.id.nomerhpuserbaru);
                f = findViewById(R.id.bocashsaldo);
                saldo = test.getBocash();
                q.setText(test.getNama());
                w.setText(test.getEmail());
                e.setText(test.getNohp());
                f.setText("Rp. "+test.getBocash()+",-");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void setTopupbocash(View view){
        viewPager.setCurrentItem(0);
        status = 0;
    }
    public void menu1(MenuItem menuItem){
        Intent intent = new Intent(Menu_PengaturUtama.this, Menu_Maps.class);
        intent.putExtra("username",username);
        intent.putExtra("saldouser",saldo);
        startActivity(intent);
    }
    public void menu2(MenuItem menuItem){
        viewPager.setCurrentItem(0);
        status = 0;
    }
    public void menu3(MenuItem menuItem){
        viewPager.setCurrentItem(1);
        status =1;
        munculkan();
    }

    public void setLimaribu(View view) {
        tambahbocash(5000);
    }
    public void setSepuluhribu(View view) {
        tambahbocash(10000);
    }
    public void setDuapuluhribu(View view) {
        tambahbocash(20000);
    }
    public void setLimapuluhribu(View view) {
        tambahbocash(50000);
    }
    public void setSeratusribu(View view) {
        tambahbocash(100000);
    }
    public void setDuaratusribu(View view) {
        tambahbocash(200000);
    }
    public void setLimaratusribu(View view) {
        tambahbocash(500000);
    }
    public void setSatujuta(View view) {
        tambahbocash(1000000);
    }
    public void tambahbocash(int banyak){
        ceksaldodanupdate(banyak);
        berhasil(banyak);
    }
    private void berhasil(int banyak){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Menu_PengaturUtama.this);
        dlgAlert.setTitle("Berhasil");
        dlgAlert.setMessage("Saldo anda telah berhasil ditambahkan sebanyak Rp. " +banyak+",-");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

    }
    private void ceksaldodanupdate(final int tambah){
        Intent intent = getIntent();
        final String username = intent.getExtras().getString("username");
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference2 = databaseReference.child(username);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Data_User test = dataSnapshot.getValue(Data_User.class);
               saldo = test.getBocash();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        String saldo2 = Integer.toString(Integer.parseInt(saldo)+tambah);
        databaseReference2.child("bocash").setValue(saldo2);
    }
    public void supergg(String username, String email,String password,String nama,String nohp,String bocash){
        Data_User datauser = new Data_User(email,password,nama,nohp,bocash);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference2 = databaseReference.child(username);
        databaseReference2.setValue(datauser);
    }
    private void setupViewPager(ViewPager viewPager){
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Menu_TopUpBoCash(), "Menu2");
        adapter.addFragment(new Menu_UserAccount(), "Menu3");
        viewPager.setAdapter(adapter);
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
