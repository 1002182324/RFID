package com.codequery.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }
}
