package com.bot;

public class HelpCommands {
    public static String getHelp(){
        return "- !date = Для того, чтобы узнать дату, чётность недели и её номер\n\n" +
                "- !kalendar = Для того, чтобы увидеть календарь\n\n" +
                "- !Schedule номер_курса группа = Для того, чтобы получить расписание\n\n" +
                "- !ScheduleByDay номер_курса группа день_недели(возможные дни: пн, вт, ср, чт, пт, сб) = Для того, чтобы получить расписание \n\n";

    }
}
