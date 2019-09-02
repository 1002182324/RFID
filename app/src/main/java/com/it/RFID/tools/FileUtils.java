package com.it.RFID.tools;

import android.database.Cursor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static boolean writeText(String path, String text) {

        try {

            File file = new File(path);
            if (!file.exists()) {
                //先得到文件的上级目录，并创建上级目录，在创建文件
                file.getParentFile().mkdir();
                //创建文件
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write((text + "\n").getBytes());
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }

    public static boolean wEPC2TXT(String tableName, String fileName) {

        Cursor cursor = DaoUtils.querEPC(tableName);
        if (cursor != null && cursor.moveToFirst()) {

            do {
                writeText("/sdcard/pgit/TestData/" + fileName + ".txt",
                        cursor.getString(cursor.getColumnIndex("EPC")));


            } while (cursor.moveToNext());

        } else {
            return false;
        }

        return true;

    }


}
