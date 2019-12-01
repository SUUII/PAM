package com.sui.testingwarnet;

public class Data_OpWarnet {
    public String id;
    public String password;
    public String email;
    public String nohp;
    public String bocash;

    public Data_OpWarnet() {}
    public Data_OpWarnet(String id,String email, String password, String nohp,String bocash){
        this.id = id;
        this.email = email;
        this.password = password;
        this.nohp = nohp;
        this.bocash = bocash;
    }
    public String getId(){return  id;}
    public String getPassword(){return  password;}
    public String getNohp(){return nohp;}
    public String getEmail(){return email;}
    public String getBocash(){return bocash;}
}
