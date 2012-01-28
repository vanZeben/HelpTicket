package com.imdeity.helpticket.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.imdeity.helpticket.HelpTicket;
import com.imdeity.helpticket.object.SQLTicket;
import com.imdeity.helpticket.object.Ticket;
import com.imdeity.helpticket.utils.ChatTools;

public class HelpTicketPlayerListener implements Listener {
    private final HelpTicket plugin;

    public HelpTicketPlayerListener(HelpTicket instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.isStaff(player)) {
        	int playerOpenTickets = SQLTicket.getPlayersOpenTickets(player.getName()).size();
            if (playerOpenTickets == 1) {
                ChatTools
                        .formatAndSend(
                                "<option> You have "
                                        + SQLTicket.getPlayersOpenTickets(
                                                player.getName()).size()
                                        + " ticket open.", "HelpTicket", player);
            } else if (playerOpenTickets > 1) {
                ChatTools.formatAndSend("<option> You have "
                        + SQLTicket.getPlayersOpenTickets(player.getName())
                                .size() + " tickets open.", "HelpTicket",
                        player);
            }
        } else {
            if (SQLTicket.getAllOpenTickets().size() == 1) {
                ChatTools
                        .formatAndSend(
                                "<option>There is "
                                        + SQLTicket.getAllOpenTickets().size()
                                        + " ticket open.", "HelpTicket", player);
            } else {
                ChatTools.formatAndSend("<option>There are "
                        + SQLTicket.getAllOpenTickets()
                                .size() + " tickets open.", "HelpTicket",
                        player);
            }
            plugin.addToStaff(player);
        }
        for (Ticket t : SQLTicket.getPlayersOpenTickets(player.getName())) {
            SQLTicket.setHasRead(t);
            if (!t.getHasRead()) {
                ChatTools.formatAndSend("<option>Ticket "
                        + t.getID() + " has been updated.", "HelpTicket",
                        player);
            }
        }

    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.isStaff(player)) {
            plugin.removeFromStaff(player);
        }
    }

}