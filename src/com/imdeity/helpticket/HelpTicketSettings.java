package com.imdeity.helpticket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.config.Configuration;

import com.imdeity.helpticket.utils.FileMgmt;


@SuppressWarnings("unused")
public class HelpTicketSettings {

    public HelpTicket plugin = null;
    private static Configuration config;

    public HelpTicketSettings(HelpTicket plugin) {
        this.plugin = plugin;
    }

    ///////////////////////////////

    public HelpTicketSettings() {}

    public static void loadConfig(String filepath, String defaultRes)
            throws IOException {
        File file = FileMgmt.CheckYMLexists(filepath, defaultRes);
        if (file != null) {
            // read the config.yml into memory
            config = new Configuration(file);
            config.load();
            file = null;

        }
    }

    // Functions to pull data from the config and language files

    private static String[] parseString(String str) {
        return parseSingleLineString(str).split("@");
    }

    public static String parseSingleLineString(String str) {
        return str.replaceAll("&", "\u00A7");
    }

    public static Boolean getBoolean(String root) {
        return config.getBoolean(root.toLowerCase(), true);
    }

    private static Double getDouble(String root) {
        return config.getDouble(root.toLowerCase(), 0);
    }

    private static Integer getInt(String root) {
        return config.getInt(root.toLowerCase(), 0);
    }

    private static Long getLong(String root) {
        return Long.parseLong(getString(root).trim());
    }

    /*
     * Public Functions to read data from the Configuration and Language data
     */

    public static String getString(String root) {
        return config.getString(root.toLowerCase());
    }

    // read a comma delimited string into an Integer list
    public static List<Integer> getIntArr(String root) {

        String[] strArray = getString(root.toLowerCase()).split(",");
        List<Integer> list = new ArrayList<Integer>();
        if (strArray != null) {
            for (int ctr = 0; ctr < strArray.length; ctr++)
                if (strArray[ctr] != null)
                    list.add(Integer.parseInt(strArray[ctr]));
        }
        return list;
    }

    // read a comma delimited string into a trimmed list.
    public static List<String> getStrArr(String root) {

        String[] strArray = getString(root.toLowerCase()).split(",");
        List<String> list = new ArrayList<String>();
        if (strArray != null) {
            for (int ctr = 0; ctr < strArray.length; ctr++)
                if (strArray[ctr] != null)
                    list.add(strArray[ctr].trim());
        }
        return list;
    }

    public static String getMySQLServerAddress() {
        return getString("mysql.server.ADDRESS");
    }

    public static int getMySQLServerPort() {
        return getInt("mysql.server.PORT");
    }

    public static String getMySQLDatabaseName() {
        return getString("mysql.database.NAME");
    }

    public static String getMySQLTablePrefix() {
        return getString("mysql.database.TABLE_PREFIX");
    }

    public static String getMySQLDatabaseUsername() {
        return getString("mysql.database.USERNAME");
    }

    public static String getMySQLDatabasePassword() {
        return getString("mysql.database.PASSWORD");
    }

    public static int getNotificationTimer() {
        return getInt("helpticket.NOTIFY_DELAY");
    }

    public static boolean isUsingPermissions() {
        return getBoolean("helpticket.USING_PERMISSIONS");
    }

}
