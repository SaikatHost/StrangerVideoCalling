package models;

public class user {
    private String uid,name,profile,city;
    public  user(){
    }

    public user(String uid, String name, String profile, String city) {
        this.uid = uid;
        this.name = name;
        this.profile = profile;
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
