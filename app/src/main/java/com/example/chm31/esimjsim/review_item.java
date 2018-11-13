package com.example.chm31.esimjsim;

public class review_item {
    String id;
    String content;
    String date;
    String addr;
    String real_id;
    String login_type;

    public review_item(String id, String content, String date, float rating, String addr, String real_id, String login_type) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.rating = rating;
        this.addr = addr;
        this.real_id = real_id;
        this.login_type = login_type;
    }

    float rating;

    public String getLogin_type(){
        return login_type;
    }
    public void setLogin_type(String login_type){
        this.login_type = login_type;
    }
    public String getReal_id(){
        return real_id;
    }
    public void setReal_id(String real_id){
        this.real_id = real_id;
    }
    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr){
        this.addr = addr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
