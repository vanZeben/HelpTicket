package com.imdeity.helpticket.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.imdeity.helpticket.HelpTicket;
import com.imdeity.helpticket.db.MySQLConnector;

public class SQLTicket {

    public static Ticket getSpecificTicket(int instanceId) {
        String sql = "";

        int id = instanceId;
        String owner = "";
        String world = null;
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        float pitch = 0.0f;
        float yaw = 0.0f;
        String title = "";
        String assignee = "";
        ArrayList<String> log = new ArrayList<String>();
        boolean status = false;

        sql = "SELECT * FROM " + MySQLConnector.tableName("data")
                + " WHERE `id` = '" + id + "';";

        HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);

        if (out.isEmpty()) {
            return null;
        }
        try {
            owner = out.get(1).get(1);
            world = out.get(1).get(2);
            x = Double.parseDouble(out.get(1).get(3));
            y = Double.parseDouble(out.get(1).get(4));
            z = Double.parseDouble(out.get(1).get(5));
            pitch = Float.parseFloat(out.get(1).get(6));
            yaw = Float.parseFloat(out.get(1).get(7));
            title = out.get(1).get(8);
            assignee = out.get(1).get(9);
            if (Integer.parseInt(out.get(1).get(10)) == 1) {
                status = false;
            } else
                status = true;
            if (out.get(1).get(11) != null) {
                Scanner in = new Scanner(out.get(1).get(11));
                in.useDelimiter("//|");
                while (in.hasNext())
                    log.add(in.next());
            }
        } catch (NumberFormatException ex) {
            System.out.println("[HelpTicket] Input Mismatch on id of " + id);
            ex.printStackTrace();
            return null;
        }

        return new Ticket(id, owner, world, x, y, z, pitch, yaw, title,
                assignee, status);
    }
    
    public static Ticket getSpecificTicket(int instanceId, String playerName) {
        String sql = "";

        int id = instanceId;
        String owner = "";
        String world = null;
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        float pitch = 0.0f;
        float yaw = 0.0f;
        String title = "";
        String assignee = "";
        ArrayList<String> log = new ArrayList<String>();
        boolean status = false;

        sql = "SELECT * FROM " + MySQLConnector.tableName("data")
                + " WHERE `id` = '" + id + "' && `owner` = '"+playerName+"';";

        HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);

        if (out.isEmpty()) {
            return null;
        }
        try {
            owner = out.get(1).get(1);
            world = out.get(1).get(2);
            x = Double.parseDouble(out.get(1).get(3));
            y = Double.parseDouble(out.get(1).get(4));
            z = Double.parseDouble(out.get(1).get(5));
            pitch = Float.parseFloat(out.get(1).get(6));
            yaw = Float.parseFloat(out.get(1).get(7));
            title = out.get(1).get(8);
            assignee = out.get(1).get(9);
            if (Integer.parseInt(out.get(1).get(10)) == 1) {
                status = false;
            } else
                status = true;
            if (out.get(1).get(11) != null) {
                Scanner in = new Scanner(out.get(1).get(11));
                in.useDelimiter("//|");
                while (in.hasNext())
                    log.add(in.next());
            }
        } catch (NumberFormatException ex) {
            System.out.println("[HelpTicket] Input Mismatch on id of " + id);
            ex.printStackTrace();
            return null;
        }

        return new Ticket(id, owner, world, x, y, z, pitch, yaw, title,
                assignee, status);
    }

    public static ArrayList<Ticket> getPlayersOpenTickets(String playerName) {
        String sql = "";
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        
        int id = 0;
        String owner = "";
        String world = null;
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        float pitch = 0.0f;
        float yaw = 0.0f;
        String title = "";
        String assignee = "";
        ArrayList<String> log = new ArrayList<String>();
        boolean status = false;

        sql = "SELECT * FROM " + MySQLConnector.tableName("data")
                + " WHERE `owner` = '" + playerName + "' && `status` = '0';";

        HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);
        for (int i = 1; i <= out.size(); i++) {
            try {
                id = Integer.parseInt(out.get(i).get(0));
                owner = out.get(i).get(1);
                world = out.get(i).get(2);
                x = Double.parseDouble(out.get(i).get(3));
                y = Double.parseDouble(out.get(i).get(4));
                z = Double.parseDouble(out.get(i).get(5));
                pitch = Float.parseFloat(out.get(i).get(6));
                yaw = Float.parseFloat(out.get(i).get(7));
                title = out.get(i).get(8);
                assignee = out.get(i).get(9);
                status = Boolean.parseBoolean(out.get(i).get(10));
                if (out.get(i).get(11) != null) {
                    Scanner in = new Scanner(out.get(i).get(11));
                    in.useDelimiter("//|");
                    while (in.hasNext())
                        log.add(in.next());
                }
                tickets.add(new Ticket(id, owner, world, x, y, z, pitch, yaw, title,
                        assignee, status));
            } catch (NumberFormatException ex) {
                System.out
                        .println("[HelpTicket] Input Mismatch on id of " + id);
                ex.printStackTrace();
                return null;
            }
        }

        return tickets;
    }
    
    public static ArrayList<Ticket> getPlayersTickets(String playerName) {
        String sql = "";
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        
        int id = 0;
        String owner = "";
        String world = null;
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        float pitch = 0.0f;
        float yaw = 0.0f;
        String title = "";
        String assignee = "";
        ArrayList<String> log = new ArrayList<String>();
        boolean status = false;

        sql = "SELECT * FROM " + MySQLConnector.tableName("data")
                + " WHERE `owner` = '" + playerName + "';";

        HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);
        for (int i = 1; i <= out.size(); i++) {
            try {
                id = Integer.parseInt(out.get(i).get(0));
                owner = out.get(i).get(1);
                world = out.get(i).get(2);
                x = Double.parseDouble(out.get(i).get(3));
                y = Double.parseDouble(out.get(i).get(4));
                z = Double.parseDouble(out.get(i).get(5));
                pitch = Float.parseFloat(out.get(i).get(6));
                yaw = Float.parseFloat(out.get(i).get(7));
                title = out.get(i).get(8);
                assignee = out.get(i).get(9);
                status = Boolean.parseBoolean(out.get(i).get(10));
                if (out.get(i).get(11) != null) {
                    Scanner in = new Scanner(out.get(i).get(11));
                    in.useDelimiter("//|");
                    while (in.hasNext())
                        log.add(in.next());
                }
                tickets.add(new Ticket(id, owner, world, x, y, z, pitch, yaw, title,
                        assignee, status));
            } catch (NumberFormatException ex) {
                System.out
                        .println("[HelpTicket] Input Mismatch on id of " + id);
                ex.printStackTrace();
                return null;
            }
        }

        return tickets;
    }

    public static ArrayList<Ticket> getAllOpenTickets() {
        String sql = "";
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        
        int id = 0;
        String owner = "";
        String world = null;
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        float pitch = 0.0f;
        float yaw = 0.0f;
        String title = "";
        String assignee = "";
        ArrayList<String> log = new ArrayList<String>();
        boolean status = false;

        sql = "SELECT * FROM " + MySQLConnector.tableName("data")
                + " WHERE `status` = '0';";

        HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);
        for (int i = 1; i <= out.size(); i++) {
            try {
                id = Integer.parseInt(out.get(i).get(0));
                owner = out.get(i).get(1);
                world = out.get(i).get(2);
                x = Double.parseDouble(out.get(i).get(3));
                y = Double.parseDouble(out.get(i).get(4));
                z = Double.parseDouble(out.get(i).get(5));
                pitch = Float.parseFloat(out.get(i).get(6));
                yaw = Float.parseFloat(out.get(i).get(7));
                title = out.get(i).get(8);
                assignee = out.get(i).get(9);
                status = Boolean.parseBoolean(out.get(i).get(10));
                if (out.get(i).get(11) != null) {
                    Scanner in = new Scanner(out.get(i).get(11));
                    in.useDelimiter("//|");
                    while (in.hasNext())
                        log.add(in.next());
                }
                tickets.add(new Ticket(id, owner, world, x, y, z, pitch, yaw, title,
                        assignee, status));
            } catch (NumberFormatException ex) {
                System.out
                        .println("[HelpTicket] Input Mismatch on id of " + id);
                ex.printStackTrace();
                return null;
            }
        }

        return tickets;
    }
    
    public static int getNewestTicketID(String playerName) {
        String sql = "";

        int id = 0;
        String owner = playerName;

        sql = "SELECT `id` FROM " + MySQLConnector.tableName("data")
                + " WHERE `owner` = '" + owner
                + "' ORDER BY `id` DESC LIMIT 1;";

        HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);

        try {
            id = Integer.parseInt(out.get(1).get(0));

        } catch (NumberFormatException ex) {
            System.out.println("[HelpTicket] Input Mismatch on id of " + id);
            return -1;
        }

        return id;
    }

    public static boolean newTicket(Ticket ticket) {
        String sql = "";

        sql = "INSERT INTO " + MySQLConnector.tableName("data") + " ("
                + "`owner`," + " `world`," + " `x_coord`," + " `y_coord`,"
                + " `z_coord`," + " `pitch`," + " `yaw`," + " `status`,"
                + " `title`" + ") VALUES (?,?,?,?,?,?,?,?,?);";

        HelpTicket.database.Write(sql, ticket.getOwner(), ticket.getWorld(),
                ticket.getX(), ticket.getY(), ticket.getZ(), ticket.getPitch(),
                ticket.getYaw(), 0, ticket.getTitle());
        return true;
    }

    public static String updateTicket(Ticket ticket, String variable) {
        String sql = "";
        if (variable.equalsIgnoreCase("assignee")) {
            sql = "UPDATE " + MySQLConnector.tableName("data")
                    + " SET  `assignee` =  '" + ticket.getAssignee()
                    + "' WHERE `id` = '" + ticket.getID() + "';";
            HelpTicket.database.Write(sql);
            return "Assignee was set to "+ticket.getAssignee();
        } else if (variable.equalsIgnoreCase("status")) {
            sql = "UPDATE " + MySQLConnector.tableName("data")
                    + " SET  `status` =  '" + (ticket.isOpen() ? 0 : 1)
                    + "' WHERE `id` = '" + ticket.getID() + "';";
            HelpTicket.database.Write(sql);
            return "Closed Ticket #"+ticket.getID();
        } else if (variable.equalsIgnoreCase("log")) {
            // TODO logging
        }

        HelpTicket.database.Write(sql);
        return "Nothing was changed.";
    }

}
