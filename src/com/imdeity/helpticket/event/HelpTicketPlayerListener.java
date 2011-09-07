package com.imdeity.helpticket.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.imdeity.helpticket.HelpTicket;
import com.imdeity.helpticket.object.SQLTicket;
import com.imdeity.helpticket.utils.ChatTools;

public class HelpTicketPlayerListener extends PlayerListener {
    private final HelpTicket plugin;

    public HelpTicketPlayerListener(HelpTicket instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.isStaff(player)) {
            if (SQLTicket.getPlayersOpenTickets(player.getName()).size() == 1)
                ChatTools
                        .formatAndSend(
                                "<option> You have "
                                        + SQLTicket.getPlayersOpenTickets(
                                                player.getName()).size()
                                        + " ticket open.", "HelpTicket", player);
            else
                ChatTools.formatAndSend("<option> You have "
                        + SQLTicket.getPlayersOpenTickets(player.getName())
                                .size() + " tickets open.", "HelpTicket",
                        player);
        } else {
            if (SQLTicket.getAllOpenTickets().size() == 1)
                ChatTools
                        .formatAndSend(
                                "<option>There is "
                                        + SQLTicket.getAllOpenTickets().size()
                                        + " ticket open.", "HelpTicket", player);
            else
                ChatTools.formatAndSend("<option>There are "
                        + SQLTicket.getAllOpenTickets()
                                .size() + " tickets open.", "HelpTicket",
                        player);
        }

    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.isStaff(player)) {
            HelpTicket.staff.remove(player);
        }
    }

}