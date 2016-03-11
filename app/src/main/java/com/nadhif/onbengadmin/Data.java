package com.nadhif.onbengadmin;

/**
 * Created by nadhif on 29/02/2016.
 */
public class Data {
    String id_marker, bengkel_name, bengkel_company, contact, email, location, price_per_km, lat, lng;

    public Data(String id_marker, String bengkel_name, String bengkel_company, String contact, String email, String location, String price_per_km, String lat, String lng) {
        this.id_marker = id_marker;
        this.bengkel_name = bengkel_name;
        this.bengkel_company = bengkel_company;
        this.contact = contact;
        this.email = email;
        this.location = location;
        this.price_per_km = price_per_km;
        this.lat = lat;
        this.lng = lng;
    }

    public String getBengkel_id_marker() {
        return id_marker;
    }

    public String getBengkel_name() {
        return bengkel_name;
    }

    public String getBengkel_company() {
        return bengkel_company;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice_per_km() {
        return price_per_km;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
