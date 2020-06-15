package com.bot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Kalendar {
    public static String getKalendar(){
        Date dateNow = new Date();
        SimpleDateFormat MonthNow = new SimpleDateFormat("M");
        String month = MonthNow.format(dateNow);
        String result = "src/main/resources/pictures/"+ Integer.parseInt(month) +".png";
       return result;
    }
}
