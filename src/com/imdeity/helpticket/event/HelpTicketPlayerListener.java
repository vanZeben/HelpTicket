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
		if (plugin.isStaff(player)) {
			int size = SQLTicket.getAllOpenTickets().size();
			if (size == 0) {
			} else if (size == 1) {
				ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getAutoStaffMessageSingular().replaceAll("%numTickets", "" + size), player);
			} else {
				ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getAutoStaffMessagePlural().replaceAll("%numTickets", "" + size), player);
			}
			plugin.addToStaff(player);
		}
		for (Ticket t : SQLTicket.getPlayersTickets(player.getName())) {
			if (!t.getHasRead()) {
				String line = t.preformReplaceSingle(plugin.language.getUpdateMessage());
				ChatTools.formatAndSend(plugin.language.getHeader() + line, player);
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