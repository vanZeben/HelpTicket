package com.imdeity.helpticket;

import java.util.ArrayList;
import java.util.TimerTask;

import org.bukkit.entity.Player;

import com.imdeity.helpticket.object.SQLTicket;
import com.imdeity.helpticket.utils.ChatTools;

public class TicketTimerTask extends TimerTask {

	private HelpTicket plugin;

	public TicketTimerTask(HelpTicket instance) {
		plugin = instance;
	}

	@Override
	public void run() {
		ArrayList<Player> staff = new ArrayList<Player>();

		staff = HelpTicket.getOnlineStaff();

		for (Player player : staff) {
			int size = SQLTicket.getAllOpenTickets().size();
			if (size == 0) {
			} else if (size == 1) {
				ChatTools.formatAndSend(plugin.language.getHeader()
						+ plugin.language.getAutoStaffMessageSingular()
								.replaceAll("%numTickets", "" + size), player);
			} else {
				ChatTools.formatAndSend(plugin.language.getHeader()
						+ plugin.language.getAutoStaffMessagePlural()
								.replaceAll("%numTickets", "" + size), player);
			}

		}

	}

}
