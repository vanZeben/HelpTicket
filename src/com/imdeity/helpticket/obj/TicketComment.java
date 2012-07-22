package com.imdeity.helpticket.obj;

import java.util.Date;

import com.imdeity.deityapi.DeityAPI;

public class TicketComment {
    private int id;
    private int ticketId;
    private String commenter;
    private String comment;
    private Date date;
    
    public TicketComment(int id, int ticketId, String commenter, String comment, Date date) {
        this.id = id;
        this.ticketId = ticketId;
        this.commenter = commenter;
        this.comment = comment;
        this.date = date;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getTicketId() {
        return this.ticketId;
    }
    
    public String getCommenter() {
        return this.commenter;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }
    
    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String showInfo() {
        return "&e" + getCommenter() + "&7: &e" + getComment() + "&e &7[" + DeityAPI.getAPI().getUtilAPI().getTimeUtils().timeApproxToDate(this.date) + "&7]";
    }
}