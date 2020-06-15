package com.bot;

import java.text.SimpleDateFormat;
import java.util.Date;

class WeekCheck {

    static String getEvenWeek() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("w");

        int week = Integer.parseInt(formatForDateNow.format(dateNow));
        if(week%2 == 1){
            return "Нечётная";
        }else{
            return "Чётная";
        }

    }//Выдаёт чётность/нечётность текущей учебной недели(Время мск)
    static String getNumberOfWeek(){
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("w");
        SimpleDateFormat MonthNow = new SimpleDateFormat("M");
        String month = MonthNow.format(dateNow);
        int week;
        if(new Date( Integer.parseInt(month)).before(new Date ( 9))){
            week = Integer.parseInt(formatForDateNow.format(dateNow)) - 6;//до сентября(костыль на 2020 год, пока ничего более толкового не придумал)
        }else{
            week = Integer.parseInt(formatForDateNow.format(dateNow)) - 35;//после сентября
        }

        return String.valueOf(week);
    }
    static String getDay(){
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E , dd.MM");

        String result = formatForDateNow.format(dateNow);
        return result;
    }
}
