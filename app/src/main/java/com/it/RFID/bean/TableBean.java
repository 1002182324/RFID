package com.it.RFID.bean;

public class TableBean {

    private String batchid;
    private String boxid;
    private String shopid;

    public String getTableName() {

        return
                batchid + "-"
                        + shopid + "-" +
                        boxid;
    }

    @Override
    public String toString() {
        return
                batchid + "_"
                        + shopid + "_" +
                        boxid;
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public String getBoxid() {
        return boxid;
    }

    public void setBoxid(String boxid) {
        this.boxid = boxid;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
}
