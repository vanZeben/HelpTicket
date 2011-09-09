package com.imdeity.helpticket.object;

import java.util.ArrayList;
import java.util.HashMap;

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
        String info = "";
        String assignee = "";
        boolean status = false;
        boolean hasRead = false;
        int priority;

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
            info = out.get(1).get(8);
            assignee = out.get(1).get(9);
            status = (Integer.parseInt(out.get(1).get(10)) == 1 ? false : true);
            hasRead = (Integer.parseInt(out.get(1).get(11)) == 1 ? false : true);
            priority = Integer.parseInt(out.get(1).get(13));
        } catch (NumberFormatException ex) {
            System.out.println("[HelpTicket] Input Mismatch on id of " + id);
            ex.printStackTrace();
            return null;
        }

        Ticket tmp = new Ticket(id, owner, world, x, y, z, pitch, yaw, info,
                assignee, status, hasRead, priority);
        getComments(tmp);
        return tmp;
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
        String info = "";
        String assignee = "";
        boolean status = false;
        boolean hasRead = false;
        int priority;

        sql = "SELECT * FROM " + MySQLConnector.tableName("data")
                + " WHERE `id` = '" + id + "' && `owner` = '" + playerName
                + "';";

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
            info = out.get(1).get(8);
            assignee = out.get(1).get(9);
            status = (Integer.parseInt(out.get(1).get(10)) == 1 ? false : true);
            hasRead = (Integer.parseInt(out.get(1).get(11)) == 1 ? false : true);
            priority = Integer.parseInt(out.get(1).get(13));
        } catch (NumberFormatException ex) {
            System.out.println("[HelpTicket] Input Mismatch on id of " + id);
            ex.printStackTrace();
            return null;
        }

        Ticket tmp = new Ticket(id, owner, world, x, y, z, pitch, yaw, info,
                assignee, status, hasRead, priority);
        getComments(tmp);
        return tmp;
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
        String info = "";
        String assignee = "";
        boolean status = false;
        boolean hasRead = false;
        int priority;

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
                info = out.get(i).get(8);
                assignee = out.get(i).get(9);
                status = (Integer.parseInt(out.get(1).get(10)) == 1 ? false
                        : true);
                hasRead = (Integer.parseInt(out.get(1).get(11)) == 1 ? false
                        : true);
                priority = Integer.parseInt(out.get(1).get(13));
                tickets.add(new Ticket(id, owner, world, x, y, z, pitch, yaw,
                        info, assignee, status, hasRead, priority));
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
        String info = "";
        String assignee = "";
        boolean status = false;
        boolean hasRead = false;
        int priority;

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
                info = out.get(i).get(8);
                assignee = out.get(i).get(9);
                status = (Integer.parseInt(out.get(1).get(10)) == 1 ? false
                        : true);
                hasRead = (Integer.parseInt(out.get(1).get(11)) == 1 ? false
                        : true);
                priority = Integer.parseInt(out.get(1).get(13));
                tickets.add(new Ticket(id, owner, world, x, y, z, pitch, yaw,
                        info, assignee, status, hasRead, priority));
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
        String info = "";
        String assignee = "";
        boolean status = false;
        boolean hasRead = false;
        int priority;

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
                info = out.get(i).get(8);
                assignee = out.get(i).get(9);
                status = (Integer.parseInt(out.get(1).get(10)) == 1 ? false
                        : true);
                hasRead = (Integer.parseInt(out.get(1).get(11)) == 1 ? false
                        : true);
                priority = Integer.parseInt(out.get(1).get(13));
                tickets.add(new Ticket(id, owner, world, x, y, z, pitch, yaw,
                        info, assignee, status, hasRead, priority));
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
            if (!out.isEmpty()) {
                id = Integer.parseInt(out.get(1).get(0));
            } else {
                id = 1;
            }

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
                + " `info`, " + " `creation_time`,"
                + "`priority` ) VALUES (?,?,?,?,?,?,?,?,?, NOW(), 0);";

        HelpTicket.database.Write(sql, ticket.getOwner(), ticket.getWorld(),
                ticket.getX(), ticket.getY(), ticket.getZ(), ticket.getPitch(),
                ticket.getYaw(), 0, ticket.getInfo());
        return true;
    }

    public static String updateTicket(Ticket ticket, String... variable) {
        String sql = "";
        if (variable[0].equalsIgnoreCase("assignee")) {
            sql = "UPDATE " + MySQLConnector.tableName("data")
                    + " SET  `assignee` =  '" + ticket.getAssignee()
                    + "' WHERE `id` = '" + ticket.getID() + "';";
            HelpTicket.database.Write(sql);
            return ticket.getAssignee() + " was assigned to ticket "
                    + ticket.getID();
        } else if (variable[0].equalsIgnoreCase("status")) {
            sql = "UPDATE " + MySQLConnector.tableName("data")
                    + " SET  `status` =  '" + (ticket.isOpen() ? 0 : 1)
                    + "' WHERE `id` = '" + ticket.getID() + "';";
            HelpTicket.database.Write(sql);
            return "Closed Ticket #" + ticket.getID();
        } else if (variable[0].equalsIgnoreCase("log")) {
            sql = "INSERT INTO " + MySQLConnector.tableName("comments") + " ("
                    + "`ticket_id`," + " `owner`," + " `commenter`,"
                    + " `comment`" + ") VALUES (?,?,?,?);";
            if (ticket.getLog().size() >= 1) {
                HelpTicket.database.Write(sql, ticket.getID(),
                        ticket.getOwner(), variable[1],
                        ticket.getLog().get(ticket.getLog().size() - 1)[1]);
                return "Added a comment to ticket #" + ticket.getID();
            } else
                return "Nothing was changed.";
        } else if (variable[0].equalsIgnoreCase("read")) {
            sql = "UPDATE " + MySQLConnector.tableName("data") + " SET "
                    + "`has_read` = '0' WHERE `id` = " + ticket.getID() + ";";
            HelpTicket.database.Write(sql);
            return "";
        } else if (variable[0].equalsIgnoreCase("notread")) {
            sql = "UPDATE " + MySQLConnector.tableName("data") + " SET "
                    + "`has_read` = '1' WHERE `id` = " + ticket.getID() + ";";
            HelpTicket.database.Write(sql);
            return "";
        } else if (variable[0].equalsIgnoreCase("priority")) {
            sql = "UPDATE " + MySQLConnector.tableName("data") + " SET "
                    + "`priority` = '" + ticket.getRawPriority()
                    + "' WHERE `id` = " + ticket.getID() + ";";
            HelpTicket.database.Write(sql);
            return "Priority was set to " + ticket.getPriority();
        } else if (variable[0].equalsIgnoreCase("priorityclose")) {
            sql = "UPDATE " + MySQLConnector.tableName("data") + " SET "
                    + "`priority` = '-1' WHERE `id` = " + ticket.getID() + ";";
            HelpTicket.database.Write(sql);
            return "";
        }
        return "Nothing was changed.";
    }

    public static void getComments(Ticket ticket) {
        String sql = "";

        sql = "SELECT * FROM " + MySQLConnector.tableName("comments")
                + " WHERE" + "`ticket_id` = '" + ticket.getID() + "';";
        HashMap<Integer, ArrayList<String>> query = HelpTicket.database
                .Read(sql);

        for (int i = 1; i <= query.size(); i++) {
            ticket.addLog(query.get(i).get(3), query.get(i).get(4));
        }
    }

    public static int getPriority(Ticket ticket) {
        String sql = "";

        sql = "SELECT * FROM " + MySQLConnector.tableName("data") + " WHERE"
                + "`id` = '" + ticket.getID() + "';";
        HashMap<Integer, ArrayList<String>> query = HelpTicket.database
                .Read(sql);

        return Integer.parseInt(query.get(1).get(13));
    }

    public static void setHasRead(Ticket ticket) {
        String sql = "";

        sql = "SELECT * FROM " + MySQLConnector.tableName("data") + " WHERE"
                + "`id` = '" + ticket.getID() + "';";
        HashMap<Integer, ArrayList<String>> query = HelpTicket.database
                .Read(sql);

        for (int i = 1; i <= query.size(); i++) {
            ticket.setHasRead(Integer.parseInt(query.get(1).get(11)) == 1 ? false
                    : true);
        }
    }

}
