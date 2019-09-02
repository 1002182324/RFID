package com.it.RFID.DB;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "SKUEPC")
public class SKUEPC {
    @Column(name = "id", isId = true)
    private int id;


    @Column(name = "EPC", property = "UNIQUE")
    private String EPC;
    @Column(name = "SKU")
    private String SKU;

    @Override
    public String toString() {
        return "SKUEPC{" +
                "id=" + id +
                ", EPC='" + EPC + '\'' +
                ", SKU='" + SKU + '\'' +
                '}';
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
}
