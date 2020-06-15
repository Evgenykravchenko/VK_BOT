package com.bot;

import com.mysql.DB;
import com.parser.Parser;
import com.parser.TableParser;
import com.petersamokhin.bots.sdk.clients.Group;
import com.petersamokhin.bots.sdk.objects.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import org.jsoup.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    private static Connection connection;
    private static Document doc;
    private static ArrayList<String> daysOfWeek = new ArrayList<>();
    private static ArrayList<Element> daySchedule = new ArrayList<>();

    public static void main(String[] args) {
        Group group = new Group(194194997, Prop.getDataFromProp("vk_key"));
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("w");
        AtomicInteger id = new AtomicInteger();
        StringBuilder sms = new StringBuilder();
        StringBuilder schedule = new StringBuilder();
        AtomicReference<DB> db = null;

        ArrayList<String> numbers = new ArrayList<>();
        numbers.add("901");
        numbers.add("801");
        numbers.add("701");
        numbers.add("601");

        ArrayList<String> daysOfWeek = new ArrayList<>();
        daysOfWeek.add("ПН");
        daysOfWeek.add("ВТ");
        daysOfWeek.add("СР");
        daysOfWeek.add("ЧТ");
        daysOfWeek.add("ПТ");
        daysOfWeek.add("СБ");

        ArrayList<String> groups = new ArrayList<>();
        groups.add("СИБ");
        groups.add("СПБ");
        groups.add("СББ");
        groups.add("СБС");
        groups.add("СМБ");

        group.onSimpleTextMessage(message ->
                new Message()
                        .from(group)
                        .to(message.authorId())
                        .text("Воспользуйтесь командой !help")
                        .send()
        );

        group.onPhotoMessage(message ->
                new Message()
                        .from(group)
                        .to(message.authorId())
                        .text("Я умею только читать( Но есть шанс ,что твоя картинка мне нравится")
                        .send()
        );
        group.onCommand("!kalendar" , message ->
                new Message()
                .from(group)
                .to(message.authorId())
                        .text(WeekCheck.getDay())
                .photo(Kalendar.getKalendar())
                .send()
                );

        for (String currentGroup: groups) {
            for (String number: numbers) {
                group.onCommand("!changeGroup " + currentGroup + "-" + number , message -> {
                    id.set(message.authorId());
                    sms.append(message);
                    new Message()
                            .from(group)
                            .to(message.authorId())
                            .text(id)
                            .send();
                    db.set(new DB(message.authorId(), currentGroup + "-" + number));
                    sms.delete(0, sms.length());
                });
            }
        }


        for (String currentGroup: groups) {
            for (String number: numbers) {
                System.out.println(currentGroup + "-" + number + " " + groups.indexOf(currentGroup));
                group.onCommand("!Schedule " + (numbers.indexOf(number) + 1) + " " + currentGroup + "-" + number, message -> {
                    id.set(message.authorId());

                    for (String day: daysOfWeek) {
                        schedule.append(getSchedule(numbers.indexOf(number) + 1, currentGroup + "-" + number, day));

                        new Message()
                                .from(group)
                                .to(message.authorId())
                                .text(schedule)
                                .send();

                        schedule.delete(0, schedule.length());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                for (String day: daysOfWeek) {
                    group.onCommand("!ScheduleByDay " + (numbers.indexOf(number) + 1) + " " + currentGroup + "-" + number + " " + day, message -> {
                        id.set(message.authorId());

                        schedule.append(getSchedule(numbers.indexOf(number) + 1, currentGroup + "-" + number, day));
                        new Message()
                                .from(group)
                                .to(message.authorId())
                                .text(schedule)
                                .send();

                        schedule.delete(0, schedule.length());

                    });
                }
            }
        }


        group.onCommand("!help", message ->
                new Message()
                        .from(group)
                        .to(message.authorId())
                        .text(HelpCommands.getHelp())
                        .send()
        );
        group.onCommand("!date", message ->
                        new Message()

                                .from(group)
                                .to(message.authorId())
                                .text("Идёт " +WeekCheck.getNumberOfWeek() + "ая неделя , " + WeekCheck.getEvenWeek() + ". " + WeekCheck.getDay())
                                .send()
        );


    }
    public static StringBuilder getSchedule(final int course, final String group, final String dayOfWeek) {
        StringBuilder scheduleData = new StringBuilder();

        connection = setConnection("http://fkn.univer.omsk.su/academics/Schedule/schedule" + course + "_2.htm");
        doc = createDocument(connection);
        Parser tableParser = new TableParser(doc);
        List<List<String>> table = tableParser.parseTable();


        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).get(0).contains(dayOfWeek.toUpperCase()))
                scheduleData.append(table.get(i).get(0)).append(" ").append(table.get(i).get(1)).append(" ").append(table.get(i).get(getIndexOfColumnWithGroup(table, group.toUpperCase()))).append("\n");
            /*System.out.println(table.get(i).get(0)
                    + " "
                    + table.get(i).get(1)
                    + " "
                    + table.get(i).get(getIndexOfColumnWithGroup(table, group)));*/
        }

        return scheduleData;
    }

    public static int getIndexOfColumnWithGroup(List<List<String>> table, String group) {
        for (int i = 0; i < table.get(0).size(); i++) {
            if (table.get(0).get(i).contains(group)) {
                return i;
            }
        }
        return -1;
    }

    //ОСТАВИТЬ
    public static Connection setConnection(String url) {
        Connection connectionLocale = null;
        try {
            connectionLocale = Jsoup.connect(url);
        } catch (Exception ex) {
            System.out.println("Check you're ethernet connection or url.");
        }
        System.out.println("Connection established.\r\n");
        return connectionLocale;
    }

    //ОСТАВИТЬ
    public static Document createDocument(Connection connection) {
        Document documentLocale = null;
        try {
            documentLocale = connection.get();
        } catch (IOException ex) {
            System.out.println("Can't create document.");
        }
        System.out.println("The document was created.\r\n");
        return documentLocale;
    }
}
