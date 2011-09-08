package com.imdeity.helpticket;

import java.util.ArrayList;
import java.util.TimerTask;

import org.bukkit.entity.Player;

import com.imdeity.helpticket.object.SQLTicket;
import com.imdeity.helpticket.utils.ChatTools;

@SuppressWarnings("unused")
public class TicketTimerTask extends TimerTask{

    private HelpTicket plugin;
    public TicketTimerTask(HelpTicket instance) {
        plugin = instance;
    }
    
    @Override
    public void run() {
       ArrayList<Player> staff = new ArrayList<Player>();
       
       staff = HelpTicket.getOnlineStaff();
       
       for(Player player : staff) {
           int size = SQLTicket.getAllOpenTickets().size();
           if (size == 1) {
               ChatTools.formatAndSend("<option>There is <yellow>" + size + " <gray>open ticket.", "HelpTicket", player);
               ChatTools.formatAndSend("<option>Please attend to it with \"/check\".", "HelpTicket", player);
           } else {
               ChatTools.formatAndSend("<option>There are <yellow>" + size + " <gray>open tickets.", "HelpTicket", player);
               ChatTools.formatAndSend("<option>Please attend to them with \"/check\".", "HelpTicket", player);
           }
           
       }
        
    }

}

