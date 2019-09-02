package com.it.RFID.tools;

import android.widget.Toast;

import com.it.RFID.MyApplication;

public class ToastUtil {
    public static void makeLToast(String s) {
        Toast.makeText(MyApplication.getContext(), s, Toast.LENGTH_LONG).show();
    }

    public static void makeSToast(String s) {
        Toast.makeText(MyApplication.getContext(), s, Toast.LENGTH_SHORT).show();
    }
}
