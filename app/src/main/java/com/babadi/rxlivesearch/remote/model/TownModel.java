package com.babadi.rxlivesearch.remote.model;

import com.google.gson.annotations.SerializedName;

public class TownModel {
    /**
     * id : 91310000
     * town_name_fa : اردبيل
     */

    @SerializedName("id")
    private int townId;

    @SerializedName("town_name_fa")
    private String townNameFa;

    @SerializedName("province_id")
    private int provinceId;

    @SerializedName("province_name_fa")
    private String provinceNameFa;


    public int getTownId() {
        return townId;
    }

    public void setTownId(int townId) {
        this.townId = townId;
    }

    public String getTownNameFa() {
        return townNameFa;
    }

    public void setTownNameFa(String townNameFa) {
        this.townNameFa = townNameFa;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceNameFa() {
        return provinceNameFa;
    }

    public void setProvinceNameFa(String provinceNameFa) {
        this.provinceNameFa = provinceNameFa;
    }
}
