package com.it.RFID.tools;

import android.database.Cursor;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;

import static com.it.RFID.MyApplication.xdm;

public class DaoUtils {

    public static Cursor querEPC(String tableName) {

        try {
            Cursor cursor = xdm.execQuery(SQLUtil.queryEPC(tableName));
            return cursor;

        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getCount(String tableName, String SKU) {


        Cursor cursor = null;
        try {
            cursor = xdm.execQuery(SQLUtil.querySKUCount(tableName, SKU));
            cursor.moveToFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return cursor.getString(cursor.getColumnIndex("count(*)"));

    }

    public static Cursor querEPCandSKU(String tableName) {

        try {
            Cursor cursor = xdm.execQuery(SQLUtil.queryEPCandSKU(tableName));
            return cursor;

        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ArrayList<HashMap<String, String>> getSKU_list(String tableName) {

        String SKU;
        String count;
        ArrayList<HashMap<String, String>> taglist = null;
        HashMap<String, String> map;
        try {
            Cursor cursor = xdm.execQuery(SQLUtil.querySKU(tableName));
            if (cursor != null && cursor.moveToFirst()) {
                taglist = new ArrayList<HashMap<String, String>>();
                do {
                    SKU = cursor.getString(cursor.getColumnIndex("SKU"));
                    count = getCount(tableName, SKU);
                    map = new HashMap<String, String>();
                    map.put("sku", SKU);
                    map.put("tagCount", count);
                    taglist.add(map);
                } while (cursor.moveToNext());

            }
            return taglist;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }


}
