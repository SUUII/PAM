package com.sui.testingwarnet;

public class Data_User {
    public String email;
    public String nama;
    public String nohp;
    public String password;
    public String bocash;

    public Data_User() {}
    public Data_User(String email, String password, String nama, String nohp, String  bocash){
        this.email = email;
        this.nama = nama;
        this.nohp = nohp;
        this.password = password;
        this.bocash= bocash;
    }

    public String getEmail(){return  email;}
    public String getPassword(){return  password;}
    public String getNama(){return  nama;}
    public String getNohp(){return  nohp;}
    public String getBocash(){return  bocash;}
}
