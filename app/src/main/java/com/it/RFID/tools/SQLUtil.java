package com.it.RFID.tools;

import android.database.Cursor;
import android.util.Log;

import org.xutils.ex.DbException;

import static com.it.RFID.MyApplication.xdm;

public class SQLUtil {
    public static String renameTable(String sTab, String tTab) {
        String TABHead = "table_";
        return String.format("ALTER TABLE %s RENAME TO %s;", TABHead + sTab, TABHead + tTab);
    }

    public static String checkTable() {

        return "SELECT name FROM sqlite_master WHERE name LIKE \"table_%\"; ";
    }

    public static String creatTable(String tName) {

        return String.format("create table %s(EPC VARCHAR(100) not null primary key,SKU VARCHAR(20) not null); ", "table_" + tName);
    }

    public static String delTable(String tName) {

        return String.format("DROP TABLE %s; ", "table_" + tName);
    }

    public static String queryEPC(String tName) {

        return String.format("SELECT EPC FROM %s;", "table_" + tName);
    }

    public static String querySKU(String tName) {

        return String.format("SELECT SKU FROM %s;", "table_" + tName);
    }

    public static String queryEPCandSKU(String tName) {

        return String.format("SELECT * FROM %s;", "table_" + tName);
    }

    public static String queryCount(String tName) {

        return String.format("select count(*) from %s;", "table_" + tName);
    }

    public static String queryskuCount(String tName, String sku) {

        return String.format("SELECT count(*) FROM %s WHERE SKU='%s';", "table_" + tName, sku);
    }

    public static String queryEPCCount(String tName, String EPC) {

        return String.format("SELECT count(*) FROM %s WHERE EPC='%s';", "table_" + tName, EPC);
    }

    public static String queryEPCCount(String EPC) {

        Log.e("tag", "queryEPCCount: " + EPC.substring(0, 13));
        return String.format("SELECT count(*) FROM SKUEPC WHERE EPC='%s';", EPC.substring(0, 13));
    }

    public static String querySKUCount(String tName, String SKU) {

        return String.format("SELECT count(*) FROM %s WHERE SKU='%s';", "table_" + tName, SKU);
    }

    public static String getSKU(String tName, String EPC) {
        try {
            Cursor cursor = xdm.execQuery(String.format("SELECT SKU FROM %s WHERE EPC='%s';",
                    "table_" + tName, EPC));
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex("SKU"));
            }


        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public static String getSKU(String EPC) {
        try {
            Cursor cursor = xdm.execQuery(String.format("SELECT SKU FROM SKUEPC WHERE EPC='%s';",
                    EPC.substring(0, 13)));
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex("SKU"));
            }


        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public static String EPC2SKU(String EPC) {

        return String.format("SELECT SKU FROM SKUEPC WHERE EPC='%s';", EPC.substring(0, 12));
    }


    public static String insertEPCandSKU(String tName, String EPC, String SKU) {

        return String.format("INSERT INTO %s (epc,sku) VALUES ('%s', '%s');", "table_" + tName, EPC, SKU);
    }


}
