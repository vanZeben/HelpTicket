package com.imdeity.helpticket.obj;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.records.DatabaseResults;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.enums.OpenStatusType;
import com.imdeity.helpticket.enums.PriorityType;
import com.imdeity.helpticket.enums.ReadStatusType;

public class TicketManager {
    private static List<TicketLocation> ticketLocations = new ArrayList<TicketLocation>();
    private static Map<OpenStatusType, List<Ticket>> tickets = new HashMap<OpenStatusType, List<Ticket>>();
    private static Map<Integer, List<TicketComment>> ticketComments = new HashMap<Integer, List<TicketComment>>();
    
    public static TicketLocation getTicketLocation(int spawnId) {
        for (TicketLocation tl : ticketLocations) {
            if (tl.getId() == spawnId) return tl;
        }
        String sql = "SELECT * FROM " + HelpTicketMain.getTicketLocationsTableName() + " WHERE id = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, new Object[] { Integer.valueOf(spawnId) });
        if ((query != null) && (query.hasRows())) {
            try {
                int id = query.getInteger(0, "id").intValue();
                String world = query.getString(0, "world");
                int xCoord = query.getInteger(0, "x_coord").intValue();
                int yCoord = query.getInteger(0, "y_coord").intValue();
                int zCoord = query.getInteger(0, "z_coord").intValue();
                int pitch = query.getInteger(0, "pitch").intValue();
                int yaw = query.getInteger(0, "yaw").intValue();
                TicketLocation location = new TicketLocation(id, HelpTicketMain.plugin.getServer().getWorld(world), xCoord, yCoord, zCoord, pitch, yaw);
                ticketLocations.add(location);
                return location;
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static TicketLocation getTicketLocation(Location location) {
        String sql = "SELECT id FROM " + HelpTicketMain.getTicketLocationsTableName() + " WHERE world = ? AND x_coord = ? AND y_coord = ? AND z_coord = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, new Object[] { location.getWorld().getName(), Integer.valueOf(location.getBlockX()), Integer.valueOf(location.getBlockY()), Integer.valueOf(location.getBlockZ()) });
        if ((query != null) && (query.hasRows())) {
            try {
                int id = query.getInteger(0, "id").intValue();
                return getTicketLocation(id);
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static Ticket addNewTicket(String owner, Location location, String info) {
        String sql = "INSERT INTO " + HelpTicketMain.getTicketsTableName() + " (owner, location_id, info) VALUES (?,?,?);";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, new Object[] { owner, Integer.valueOf(addNewTicketLocation(location).getId()), info });
        return getTicket(owner, info);
    }
    
    public static Ticket getTicket(int ticketId) {
        for (OpenStatusType ost : tickets.keySet()) {
            for (Ticket t : tickets.get(ost)) {
                if (t.getId() == ticketId) return t;
            }
        }
        String sql = "SELECT * FROM " + HelpTicketMain.getTicketsTableName() + " WHERE id = ?;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, new Object[] { Integer.valueOf(ticketId) });
        if ((query != null) && (query.hasRows())) {
            try {
                int id = query.getInteger(0, "id").intValue();
                String owner = query.getString(0, "owner");
                String info = query.getString(0, "info");
                TicketLocation location = getTicketLocation(query.getInteger(0, "location_id").intValue());
                String assignee = query.getString(0, "assignee");
                PriorityType priority = PriorityType.getFromString(query.getString(0, "priority"));
                OpenStatusType openStatus = OpenStatusType.getFromString(query.getString(0, "open_status"));
                ReadStatusType readStatus = ReadStatusType.getFromString(query.getString(0, "read_status"));
                Date creationDate = query.getDate(0, "creation_date");
                Ticket ticket = new Ticket(id, owner, location, assignee, info, priority, openStatus, readStatus, creationDate, getTicketComments(id));
                tickets.get(openStatus).add(ticket);
                return ticket;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    private static Ticket getTicket(String owner, String info) {
        String sql = "SELECT id FROM " + HelpTicketMain.getTicketsTableName() + " WHERE owner = ? AND info = ?;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, new Object[] { owner, info });
        if ((query != null) && (query.hasRows())) {
            try {
                int id = query.getInteger(0, "id").intValue();
                return getTicket(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static void loadAllTickets() {
        if (!tickets.containsKey(OpenStatusType.CLOSED)) {
            tickets.put(OpenStatusType.CLOSED, new ArrayList<Ticket>());
        }
        if (!tickets.containsKey(OpenStatusType.OPEN)) {
            tickets.put(OpenStatusType.OPEN, new ArrayList<Ticket>());
        }
        String sql = "SELECT * FROM " + HelpTicketMain.getTicketsTableName() + ";";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, new Object[0]);
        if ((query != null) && (query.hasRows())) for (int i = 0; i < query.rowCount(); i++)
            try {
                int id = query.getInteger(i, "id").intValue();
                String owner = query.getString(i, "owner");
                String info = query.getString(i, "info");
                TicketLocation location = getTicketLocation(query.getInteger(i, "location_id").intValue());
                String assignee = query.getString(i, "assignee");
                PriorityType priority = PriorityType.getFromString(query.getString(i, "priority"));
                OpenStatusType openStatus = OpenStatusType.getFromString(query.getString(i, "open_status"));
                ReadStatusType readStatus = ReadStatusType.getFromString(query.getString(i, "read_status"));
                Date creationDate = query.getDate(i, "creation_date");
                Ticket ticket = new Ticket(id, owner, location, assignee, info, priority, openStatus, readStatus, creationDate, getTicketComments(id));
                tickets.get(openStatus).add(ticket);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    
    public static TicketComment addNewComment(int ticketId, String commenter, String comment) {
        String sql = "INSERT INTO " + HelpTicketMain.getTicketCommentsTableName() + " (ticket_id, commenter, comment) VALUES (?,?,?);";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, new Object[] { Integer.valueOf(ticketId), commenter, comment });
        return getTicketComment(ticketId, commenter, comment);
    }
    
    public static TicketComment getTicketComment(int ticketId, String commenter, String comment) {
        String sql = "SELECT id, date FROM " + HelpTicketMain.getTicketCommentsTableName() + " WHERE ticket_id = ? AND commenter = ? AND comment = ?;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, new Object[] { Integer.valueOf(ticketId), commenter, comment });
        if ((query != null) && (query.hasRows())) {
            int id = -1;
            Date date = null;
            try {
                id = query.getInteger(0, "id").intValue();
                date = query.getDate(0, "date");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            TicketComment ticketComment = new TicketComment(id, ticketId, commenter, comment, date);
            if (!ticketComments.containsKey(Integer.valueOf(ticketId))) {
                ticketComments.put(Integer.valueOf(ticketId), new ArrayList<TicketComment>());
            }
            ticketComments.get(Integer.valueOf(ticketId)).add(ticketComment);
            return ticketComment;
        }
        return null;
    }
    
    public static List<TicketComment> getTicketComments(int ticketId) {
        if (ticketComments.containsKey(Integer.valueOf(ticketId))) return ticketComments.get(Integer.valueOf(ticketId));
        String sql = "SELECT * FROM " + HelpTicketMain.getTicketCommentsTableName() + " WHERE ticket_id = ?;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, new Object[] { Integer.valueOf(ticketId) });
        if ((query != null) && (query.hasRows())) {
            for (int i = 0; i < query.rowCount(); i++) {
                int id = 0;
                String commenter = "";
                String comment = "";
                Date date = null;
                try {
                    id = query.getInteger(i, "id").intValue();
                    commenter = query.getString(i, "commenter");
                    comment = query.getString(i, "comment");
                    date = query.getDate(i, "date");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                TicketComment ticketComment = new TicketComment(id, ticketId, commenter, comment, date);
                if (!ticketComments.containsKey(Integer.valueOf(ticketId))) {
                    ticketComments.put(Integer.valueOf(ticketId), new ArrayList<TicketComment>());
                }
                ticketComments.get(Integer.valueOf(ticketId)).add(ticketComment);
            }
            return ticketComments.get(Integer.valueOf(ticketId));
        }
        return null;
    }
    
    public static TicketLocation addNewTicketLocation(Location location) {
        if (getTicketLocation(location) == null) {
            String sql = "INSERT INTO " + HelpTicketMain.getTicketLocationsTableName() + " (world, x_coord, y_coord, z_coord, pitch, yaw) VALUES (?,?,?,?,?,?);";
            DeityAPI.getAPI().getDataAPI().getMySQL()
                    .write(sql, new Object[] { location.getWorld().getName(), Integer.valueOf(location.getBlockX()), Integer.valueOf(location.getBlockY()), Integer.valueOf(location.getBlockZ()), Integer.valueOf((int) location.getPitch()), Integer.valueOf((int) location.getYaw()) });
        }
        return getTicketLocation(location);
    }
    
    public static void closeTicket(Ticket ticket) {
        List<Ticket> tmpTickets = tickets.get(OpenStatusType.OPEN);
        int ticketId = -1;
        for (int i = 0; i < tmpTickets.size(); i++) {
            Ticket tmpTicket = (Ticket) tmpTickets.get(i);
            if (tmpTicket.getId() == ticket.getId()) {
                ticketId = i;
                break;
            }
        }
        if (ticketId != -1) {
            tickets.get(OpenStatusType.OPEN).remove(ticketId);
            tickets.get(OpenStatusType.CLOSED).add(ticket);
        }
    }
    
    public static List<Ticket> getAllTicketType(OpenStatusType type) {
        return tickets.get(type);
    }
    
    public static Ticket getTicketFromPlayer(String name, String info) {
        Calendar currDay = Calendar.getInstance();
        currDay.add(5, -1);
        Date previousDay = currDay.getTime();
        for (OpenStatusType ost : tickets.keySet()) {
            for (Ticket ticket : tickets.get(ost)) {
                if ((ticket.getOwner().equalsIgnoreCase(name)) && (DeityAPI.getAPI().getUtilAPI().getStringUtils().getLevenshteinDistance(info, ticket.getInfo()) <= 10) && (ticket.getCreationDate().after(previousDay))) return ticket;
            }
        }
        return null;
    }
    
    public static List<Ticket> getAllTicketsFromPlayer(String name) {
        List<Ticket> playerTickets = new ArrayList<Ticket>();
        
        for (OpenStatusType ost : tickets.keySet()) {
            for (Ticket ticket : tickets.get(ost)) {
                if (ticket.getOwner().equalsIgnoreCase(name)) {
                    playerTickets.add(ticket);
                }
            }
        }
        return playerTickets;
    }
    
    public static List<Ticket> getAllOpenTickets(String name, OpenStatusType type) {
        List<Ticket> playerTickets = new ArrayList<Ticket>();
        
        for (Ticket ticket : tickets.get(type)) {
            if (ticket.getOwner().equalsIgnoreCase(name)) {
                playerTickets.add(ticket);
            }
        }
        return playerTickets;
    }
    
    public static void removeAllTicketsFromPlayer(String player_name) {
        List<Ticket> playerTickets = getAllTicketsFromPlayer(player_name);
        for (Ticket t : playerTickets) {
            t.remove();
            for (OpenStatusType ost : tickets.keySet()) {
                if (tickets.get(ost).contains(t)) {
                    tickets.remove(t);
                }
            }
        }
    }
}