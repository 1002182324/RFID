package com.it.RFID.DB;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "PDtable")
public class PD {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "EPC", property = "UNIQUE")
    private String EPC;
    @Column(name = "SKU")
    private String SKU;
    @Column(name = "bachid")
    private String bachid;

    @Override
    public String toString() {
        return "PD{" +
                "id=" + id +
                ", EPC='" + EPC + '\'' +
                ", SKU='" + SKU + '\'' +
                ", bachid='" + bachid + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEPC() {
        return EPC;
    }

    public void setEPC(String EPC) {
        this.EPC = EPC;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getBachid() {
        return bachid;
    }

    public void setBachid(String bachid) {
        this.bachid = bachid;
    }
}
