package com.example.apicall.entities;

import java.util.List;

public class GrindPoint {
    private String Id;

    private Integer PosX;

    private Integer PosY;

    public GrindPoint(String GP_ID, Integer X, Integer Y){
        this.Id = GP_ID;
        this.PosX = X;
        this.PosY = Y;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Integer getPosX() {
        return PosX;
    }

    public void setPosX(Integer posX) {
        PosX = posX;
    }

    public Integer getPosY() {
        return PosY;
    }

    public void setPosY(Integer posY) {
        PosY = posY;
    }
}
