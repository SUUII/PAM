package com.sui.testingwarnet;

public class Data_Warnet {
    public String alamat;
    public String lat;
    public String lon;
    public String processor;
    public String vga;
    public String ram;
    public String game;
    public String hargaperjam;
    public String jambuka;
    public String jamtutup;
    public String banyakpc;

    public Data_Warnet() {
    }

    public Data_Warnet(String alamat, String lat, String lon, String processor, String vga, String ram, String game, String hargaperjam, String jambuka, String jamtutup,String banyakpc) {
        this.alamat = alamat;
        this.lat = lat;
        this.lon = lon;
        this.processor = processor;
        this.vga = vga;
        this.ram = ram;
        this.game = game;
        this.hargaperjam = hargaperjam;
        this.jambuka = jambuka;
        this.jamtutup = jamtutup;
        this.banyakpc = banyakpc;
    }

    public String getBanyakpc() {
        return banyakpc;
    }

    public String getHargaperjam() {
        return hargaperjam;
    }

    public String getJambuka() {
        return jambuka;
    }

    public String getJamtutup() {
        return jamtutup;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getProcessor() {
        return processor;
    }

    public String getVga() {
        return vga;
    }

    public String getRam() {
        return ram;
    }

    public String getGame() {
        return game;
    }
}
