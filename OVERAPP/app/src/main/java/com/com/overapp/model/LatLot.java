package com.com.overapp.model;

import java.io.Serializable;

/**
 * Created by acer on 2016/8/11.
 */
public class LatLot implements Serializable {
    private double marklat;
    private double marklot;
    private double endlat;
    private double endlot;
    private String endAdd;
    private static final long serialVersionUID = -7620435178023928252L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEndAdd() {
        return endAdd;
    }

    public void setEndAdd(String endAdd) {
        this.endAdd = endAdd;
    }

    public double getEndlot() {
        return endlot;
    }

    public void setEndlot(double endlot) {
        this.endlot = endlot;
    }

    public double getEndlat() {
        return endlat;
    }

    public void setEndlat(double endlat) {
        this.endlat = endlat;
    }

    public double getMarklat() {
        return marklat;
    }

    public void setMarklat(double marklat) {
        this.marklat = marklat;
    }

    public double getMarklot() {
        return marklot;
    }

    public void setMarklot(double marklot) {
        this.marklot = marklot;
    }
}
