package com.imdeity.helpticket.obj;

import java.util.HashMap;
import java.util.Map;

public class PlayerSession {
    private static Map<String, PlayerSession> playerSessions = new HashMap<String, PlayerSession>();
    private String playername;
    private Ticket ticket;
    
    public PlayerSession(String playername, int ticketId) {
        this.playername = playername;
        setTicket(ticketId);
    }
    
    public String getPlayername() {
        return this.playername;
    }
    
    public Ticket getTicket() {
        return this.ticket;
    }
    
    public void setTicket(int id) {
        this.ticket = TicketManager.getTicket(id);
    }
    
    public static PlayerSession getPlayerSession(String playername) {
        if (playerSessions.containsKey(playername.toLowerCase())) return (PlayerSession) playerSessions.get(playername.toLowerCase());
        return null;
    }
    
    public static PlayerSession addPlayerSession(String playername, int ticketId) {
        playerSessions.put(playername.toLowerCase(), new PlayerSession(playername, ticketId));
        return getPlayerSession(playername);
    }
    
    public static void removePlayerSession(String playername) {
        if (getPlayerSession(playername) != null) playerSessions.remove(playername.toLowerCase());
    }
}