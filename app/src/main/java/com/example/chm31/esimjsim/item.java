package com.example.chm31.esimjsim;

public class item {
    private String tel;
    private String title;
    private String addr;
    private String lat;
    private String lng;

    public item(String title, String tel, String addr, String lat, String lng) {
        this.title = title;
        this.tel = tel;
        this.addr = addr;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTel() {

        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}