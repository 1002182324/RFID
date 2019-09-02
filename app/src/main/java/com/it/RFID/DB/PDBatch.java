package com.it.RFID.DB;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.util.List;

@Table(name = "PDBatch")
public class PDBatch {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "batch", property = "UNIQUE")
    private String batch;

    @Column(name = "boxid")
    private String boxid;
    @Column(name = "shopid")
    private String shopid;

    @Override
    public String toString() {
        return "PDBatch{" +
                "id=" + id +
                ", batch='" + batch + '\'' +
                ", boxid='" + boxid + '\'' +
                ", shopid='" + shopid + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getbatch() {
        return batch;
    }

    public void setbatch(String EPC) {
        this.batch = EPC;
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

    public List<PD> getChildren(DbManager db) throws DbException {
        return db.selector(PD.class).where("bachid", "=", this.batch).findAll();
    }
}
