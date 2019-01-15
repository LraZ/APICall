package com.example.apicall.entities;

import com.google.gson.annotations.SerializedName;

public class AccessPoint {

    @SerializedName("mac")
    private String mac;

    @SerializedName("type")
    private Integer type;         //wlan or bluetooth ....
    @SerializedName("activity")
    private Boolean activity;
    @SerializedName("desc")
    private String description;
    @SerializedName("signal")
    private int signal;

    public AccessPoint(){

    }

    public AccessPoint(String BSSID, Integer type, boolean status, String desc, Integer signal){
        this.mac = BSSID;
        this.type = type;
        this.activity = status;
        this.description = desc;
        this.signal = signal;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getActivity() {
        return activity;
    }

    public void setActivity(Boolean activity) {
        this.activity = activity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    @Override
    public String toString(){
        return (description + " - " + mac + "    " + signal + " dBm");
    }
}
