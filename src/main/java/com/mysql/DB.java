package com.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DB {

    public static final ArrayList<String> numbers = new ArrayList<>();

    static {
        numbers.add("901");
        numbers.add("801");
        numbers.add("701");
        numbers.add("601");
    }
    public int id;
    public String group;
    public boolean isUpdated;

    public DB (int id, String group) {
        this.id = id;
        this.group = group;
        isUpdated = false;

        DBWorker dbWorker = new DBWorker();
        isUpdated = updateValueInTable(id, group, dbWorker);

        insertValueInTable(id, group, dbWorker, isUpdated);

        showTable(dbWorker);

    }

    public boolean updateValueInTable (int id, String group, DBWorker dbWorker) {


        try(Statement statement = dbWorker.getConnection().createStatement()) {

            ResultSet resultSet = statement.executeQuery("select * from `groups`.enteredGroups");

            while (resultSet.next()) {
                if (resultSet.getInt("id") == id) {
                    statement.executeUpdate(
                            "update `groups`.enteredGroups set `group` = '"
                                    + group + "' where `id` = " + id
                    );

                    System.out.println("=================================================");
                    System.out.println("ID: " + id + " with group: " + group + " updated.");
                    System.out.println("=================================================");
                    System.out.println();

                    return true;
                }
            }

        } catch (SQLException ex) {
            System.out.println("Не удалось выполнить запрос.");
        }

        return false;
    }

    public boolean insertValueInTable (int id, String group, DBWorker dbWorker, boolean isUpdated) {

        if (isUpdated) return false;

        try(Statement statement = dbWorker.getConnection().createStatement()) {

            statement.execute(
                    "insert into `groups`.enteredGroups (`id`, `group`) "
                    + "values (" + id + ", '" + group + "')"
            );

            System.out.println("==================================================");
            System.out.println("ID: " + id + " with group: " + group + " inserted.");
            System.out.println("==================================================");
            System.out.println();

            return true;

        } catch (SQLException ex) {
            System.out.println("Не удалось выполнить запрос.");
        }

        return false;
    }

    public ArrayList<User> showTable(DBWorker dbWorker) {
        ArrayList<User> users = new ArrayList<>();

        try(Statement statement = dbWorker.getConnection().createStatement()) {

            ResultSet resultSet = statement.executeQuery("select * from `groups`.enteredGroups");

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setGroup(resultSet.getString("group"));
                users.add(user);

                System.out.println(user.toString());
            }

            System.out.println("================");
            System.out.println("Запрос выполнен.");
            System.out.println("================");
            System.out.println();

        } catch (SQLException ex) {
            System.out.println("Не удалось выполнить запрос.");
        }
        return users;
    }

    public boolean isCorrectInput(String string) {
        Pattern pattern = Pattern.compile("^[А-Я]{3}[-][0-9]{3}$");
        Matcher matcher = pattern.matcher(string);
        String [] groupAndNumber = string.split("-");

        if (matcher.matches()) {
            if (numbers.stream().anyMatch(number -> number.contains(groupAndNumber[1]))
                    && Arrays.toString(Groups.values()).contains(groupAndNumber[0])) {
                return true;
            }
        }
        System.out.println("Такой группы нет.");
        return false;
    }

}
