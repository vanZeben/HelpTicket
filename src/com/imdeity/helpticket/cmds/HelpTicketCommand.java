package com.imdeity.helpticket.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.helpticket.HelpTicket;
import com.imdeity.helpticket.HelpTicketSettings;
import com.imdeity.helpticket.object.SQLTicket;
import com.imdeity.helpticket.object.Ticket;
import com.imdeity.helpticket.utils.ChatTools;

public class HelpTicketCommand implements CommandExecutor {

	private HelpTicket plugin;

	public HelpTicketCommand(HelpTicket instance) {
		this.plugin = instance;

	}

	public void sendCommands(Player player) {
		List<String> output = new ArrayList<String>();
		output.add(ChatTools.formatTitle("/ticket"));
		output.add(ChatTools.formatCommand("", "/ticket", "",
				"Checks your open Ticket's"));
		output.add(ChatTools.formatCommand("", "/ticket", "new [msg]",
				"Creates a new ticket"));
		output.add(ChatTools.formatCommand("", "/ticket", "view [id]",
				"Views a specific Ticket"));
		output.add(ChatTools.formatCommand("", "/ticket",
				"comment [id] [message]", "Comments on a Ticket"));
		output.add(ChatTools.formatCommand("", "/ticket", "close [id]",
				"Closes a Ticket"));
		if (plugin.isStaff(player)) {
			output.add(ChatTools.formatCommand("Moderator", "/ticket",
					"reopen [id]", "Reopen a closed ticket"));
			output.add(ChatTools.formatCommand("Moderator", "/ticket",
					"port [id]", "Teleport to a ticket"));
			output.add(ChatTools.formatCommand("Moderator", "/ticket",
					"assign [id] [player]",
					"Assigns a ticket to the specified player."));
			output.add(ChatTools.formatCommand("Moderator", "/ticket",
					"set-priority [id] [level]",
					"Sets the priority of a ticket"));
			output.add(ChatTools.formatCommand("Moderator", "/ticket",
					"search [-a] [name]", "Searches a players past tickets"));
		}
		for (String s : output) {
			ChatTools.formatAndSend(s, player);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (HelpTicketSettings.isUsingPermissions()) {
				if (player.hasPermission("helpticket.help") || player.isOp()) {
					parseCommand(player, args);
				}
			} else {
				parseCommand(player, args);
			}
		} else {
			// Console
			plugin.out("Sorry you need to be in game to use that command");
		}
		return true;
	}

	private void parseCommand(Player player, String[] split) {

		if (split.length == 0) {
			viewAllCommand(player, split);
		} else if (split[0].equalsIgnoreCase("new")) {
			newCommand(player, split);
		} else if (split[0].equalsIgnoreCase("view")) {
			viewCommand(player, split);
		} else if (split[0].equalsIgnoreCase("comment")) {
			commentCommand(player, split);
		} else if (split[0].equalsIgnoreCase("close")) {
			closeCommand(player, split);
		} else if (split[0].equalsIgnoreCase("reopen")) {
			reopenCommand(player, split);
		} else if (split[0].equalsIgnoreCase("help")
				|| split[0].equalsIgnoreCase("?")) {
			sendCommands(player);
		} else if (split[0].equalsIgnoreCase("port")
				|| split[0].equalsIgnoreCase("warp")
				|| split[0].equalsIgnoreCase("tp")) {
			if (plugin.isStaff(player)) {
				portCommand(player, split);
			} else {
				ChatTools.formatAndSend(plugin.language.getHeader()
						+ plugin.language.getNotStaffMessage(), player);
			}
		} else if (split[0].equalsIgnoreCase("assign")) {
			if (plugin.isStaff(player)) {
				assignCommand(player, split);
			} else {
				ChatTools.formatAndSend(plugin.language.getHeader()
						+ plugin.language.getNotStaffMessage(), player);
			}
		} else if (split[0].equalsIgnoreCase("search")) {
			if (plugin.isStaff(player)) {
				searchCommand(player, split);
			} else {
				ChatTools.formatAndSend(plugin.language.getHeader()
						+ plugin.language.getNotStaffMessage(), player);
			}
		} else if (split[0].equalsIgnoreCase("set-priority")) {
			if (plugin.isStaff(player)) {
				priorityCommand(player, split);
			} else {
				ChatTools.formatAndSend(plugin.language.getHeader()
						+ plugin.language.getNotStaffMessage(), player);
			}
		}
	}

	public void newCommand(Player player, String[] split) {

		String message = "";
		for (int i = 1; i < split.length; i++) {
			if (i == 1)
				message += split[i];
			else
				message += " " + split[i];
		}

		Ticket t = new Ticket(player.getName(), player.getWorld().getName(),
				player.getLocation().getX(), player.getLocation().getY(),
				player.getLocation().getZ(), player.getLocation().getPitch(),
				player.getLocation().getYaw(), message, "", true, false, 0);

		SQLTicket.newTicket(t);

		ChatTools
				.formatAndSend(
						plugin.language.getHeader()
								+ plugin.language
										.getTicketSubmittedPlayer()
										.replaceAll(
												"%ticketId",
												""
														+ SQLTicket
																.getNewestTicketID(player
																		.getName())),
						player);

		plugin.informStaff(plugin.language
				.getTicketSubmittedStaff()
				.replaceAll("%ticketId",
						"" + SQLTicket.getNewestTicketID(player.getName()))
				.replaceAll("%player", player.getName()));
	}

	public void viewCommand(Player player, String[] split) {
		if (split.length == 2) {
			int id = 0;
			Ticket ticket = null;
			try {
				id = Integer.parseInt(split[1]);
			} catch (NumberFormatException ex) {
				invalid(player);
			}
			if (!plugin.isStaff(player)) {
				ticket = SQLTicket.getSpecificTicket(id, player.getName());

				if (ticket == null) {
					ChatTools.formatAndSend(
							plugin.language.getHeader()
									+ plugin.language.getTicketNotExist()
											.replaceAll("%ticketId", "" + id),
							player);
					return;
				}
			} else {
				ticket = SQLTicket.getSpecificTicket(id);
				if (ticket == null) {
					ChatTools.formatAndSend(
							plugin.language.getHeader()
									+ plugin.language.getTicketNotExist()
											.replaceAll("%ticketId", "" + id),
							player);
					return;
				}
			}
			for (String line : ticket.preformReplace(plugin.language
					.getTicketFullInfo())) {
				ChatTools.formatAndSend(line, player);
			}
			if (ticket.isOpen())
				ticket.setHasRead(true);
		} else {
			help(player);
		}
	}

	public void viewAllCommand(Player player, String[] split) {
		if (!plugin.isStaff(player)) {
			ChatTools.formatAndSend(plugin.language.getHeader()
					+ plugin.language.getOpenTicketsMessagePlayer(), player);
			ChatTools.formatAndSend(plugin.language.getTicketColorCoding(),
					player);
			for (Ticket t : SQLTicket.getPlayersOpenTickets(player.getName())) {
				for (String line : t.preformReplace(plugin.language
						.getTicketShortInfo())) {
					ChatTools.formatAndSend(line, player);
				}
			}
		} else {
			ChatTools.formatAndSend(plugin.language.getHeader()
					+ plugin.language.getOpenTicketsMessageStaff(), player);
			ChatTools.formatAndSend(plugin.language.getTicketColorCoding(),
					player);
			for (Ticket t : SQLTicket.getAllOpenTickets()) {
				for (String line : t.preformReplace(plugin.language
						.getTicketShortInfo())) {
					ChatTools.formatAndSend(line, player);
				}
			}
		}
		help(player);
	}

	public void closeCommand(Player player, String[] split) {
		if (split.length == 2) {
			int id = 0;

			try {
				id = Integer.parseInt(split[1]);
			} catch (NumberFormatException ex) {
				invalid(player);
			}

			Ticket ticket = SQLTicket.getSpecificTicket(id);
			if (ticket != null) {
				if (plugin.isStaff(player)
						&& !ticket.getOwner()
								.equalsIgnoreCase(player.getName())) {

					ticket.setAssignee(player.getName());
					ticket.addLog(player.getName(),
							plugin.language.getClosedLog());
					ticket.setStatus(false);
					ticket.setPriorityClose();
					for (String s : ticket.preformReplace(plugin.language
							.getClosedPlayer().replaceAll("%player",
									player.getName()))) {
						plugin.informPlayer(ticket.getOwner(), s);
					}
					// Inform staff of close
					for (String s : ticket.preformReplace(plugin.language
							.getClosedStaff().replaceAll("%player",
									player.getName()))) {
						plugin.informStaff(s);
					}
					if (ticket.isOpen()) {
						Player tmp = plugin.getServer().getPlayer(
								ticket.getOwner());
						if (tmp != null && !tmp.isOnline())
							ticket.setHasRead(false);
					}
				} else if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
					ticket.setStatus(false);
					ticket.setPriorityClose();
					for (String s : ticket.preformReplace(plugin.language
							.getClosedUser().replaceAll("%player",
									player.getName()))) {
						plugin.informPlayer(player.getName(), s);
					}
					for (String s : ticket.preformReplace(plugin.language
							.getClosedSelf().replaceAll("%player",
									player.getName()))) {
						plugin.informStaff(s);
					}
				}
			} else
				help(player);
		} else if (split.length >= 3) {
			int id = 0;
			String comment = "";

			try {
				id = Integer.parseInt(split[1]);
			} catch (NumberFormatException ex) {
				invalid(player);
			}

			for (int i = 2; i < split.length; i++) {
				if (i == 2)
					comment = split[i];
				else
					comment += " " + split[i];
			}

			Ticket ticket = SQLTicket.getSpecificTicket(id);
			if (ticket != null) {
				if (plugin.isStaff(player)) {
					ticket.addLog(
							player.getName(),
							plugin.language.getClosedLogComment()
									.replaceAll("%comment", comment)
									.replaceAll("%player", player.getName()));
					ticket.setStatus(false);
					for (String s : ticket.preformReplace(plugin.language
							.getClosedStaffComment()
							.replaceAll("%comment", comment)
							.replaceAll("%player", player.getName()))) {
						plugin.informStaff(s);
					}
					for (String s : ticket.preformReplace(plugin.language
							.getClosedPlayerComment()
							.replaceAll("%comment", comment)
							.replaceAll("%player", player.getName()))) {
						plugin.informPlayer(ticket.getOwner(), s);
					}
					if (ticket.isOpen()) {
						Player tmp = plugin.getServer().getPlayer(
								ticket.getOwner());
						if (tmp != null && !tmp.isOnline())
							ticket.setHasRead(false);
					}
				} else if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
					ticket.addLog(
							player.getName(),
							plugin.language.getClosedLogComment()
									.replaceAll("%comment", comment)
									.replaceAll("%player", player.getName()));
					ticket.setStatus(false);
					for (String s : ticket.preformReplace(plugin.language
							.getClosedUserComment()
							.replaceAll("%comment", comment)
							.replaceAll("%player", player.getName()))) {
						plugin.informPlayer(ticket.getOwner(), s);
					}
					for (String s : ticket.preformReplace(plugin.language
							.getClosedSelfComment()
							.replaceAll("%comment", comment)
							.replaceAll("%player", player.getName()))) {
						plugin.informStaff(s);
					}
				}
			} else
				help(player);
		} else {
			help(player);
		}
	}

	// Reopen a ticket
	public void reopenCommand(Player player, String[] split) {
		if (split.length == 2) {
			int id = 0;

			try {
				id = Integer.parseInt(split[1]);
			} catch (NumberFormatException ex) {
				invalid(player);
			}

			Ticket ticket = SQLTicket.getSpecificTicket(id);
			if (ticket != null) {
				if (plugin.isStaff(player)) {
					ticket.addLog(player.getName(),
							plugin.language.getReopenLog());
					ticket.setStatus(true);
					for (String s : ticket.preformReplace(plugin.language
							.getReopenUser().replaceAll("%player",
									player.getName()))) {
						plugin.informPlayer(ticket.getOwner(), s);
					}
					for (String s : ticket.preformReplace(plugin.language
							.getReopenStaff().replaceAll("%player",
									player.getName()))) {
						plugin.informStaff(s);
					}
					if (ticket.isOpen()) {
						Player tmp = plugin.getServer().getPlayer(
								ticket.getOwner());
						if (tmp != null && !tmp.isOnline())
							ticket.setHasRead(false);
					}
				} else {
					plugin.informPlayer(player.getName(),
							plugin.language.getReopenError());
				}
			} else
				help(player);
		} else {
			help(player);
		}
	}

	public void portCommand(Player player, String[] split) {
		if (split.length == 2) {
			int id = 0;
			try {
				id = Integer.parseInt(split[1]);
			} catch (NumberFormatException ex) {
				invalid(player);
			}
			Ticket ticket = SQLTicket.getSpecificTicket(id);

			if (ticket == null) {
				ChatTools.formatAndSend(
						plugin.language.getHeader()
								+ plugin.language.getTicketNotExist()
										.replaceAll("%ticketId", "" + id),
						player);
				return;
			}
			if (ticket.isOpen()) {
				player.teleport(ticket.getLocation(plugin.getWorld(ticket
						.getWorld())));
				ticket.setAssignee(player.getName());

				for (String line : ticket.preformReplace(plugin.language
						.getTicketFullInfo())) {
					ChatTools.formatAndSend(line, player);
				}

				for (String line : ticket.preformReplace(plugin.language
						.getTicketFullInfo().replaceAll("%player",
								player.getName()))) {
					plugin.informPlayer(ticket.getOwner(), line);
				}

			} else {
				for (String line : ticket.preformReplace(plugin.language
						.getTicketFullInfo())) {
					ChatTools.formatAndSend(plugin.language.getHeader() + line,
							player);
				}
				this.help(player);
			}
		} else {
			help(player);
		}
	}

	public void assignCommand(Player player, String[] split) {
		if (split.length == 3) {
			int id = 0;
			String assignee = split[2];

			try {
				id = Integer.parseInt(split[1]);
			} catch (NumberFormatException ex) {
				invalid(player);
			}

			Ticket ticket = SQLTicket.getSpecificTicket(id);
			if (ticket != null && assignee != null) {
				ticket.setAssignee(assignee);

				for (String line : ticket.preformReplace(plugin.language
						.getAssignUser()
						.replaceAll("%player", player.getName()))) {
					plugin.informPlayer(ticket.getOwner(), line);
				}
				for (String line : ticket.preformReplace(plugin.language
						.getAssignStaff().replaceAll("%player",
								player.getName()))) {
					plugin.informStaff(line);
				}
				if (ticket.isOpen()) {
					Player tmp = plugin.getServer()
							.getPlayer(ticket.getOwner());
					if (tmp != null && !tmp.isOnline())
						ticket.setHasRead(false);
				}
			} else
				help(player);
		} else {
			help(player);
		}
	}

	public void searchCommand(Player player, String[] split) {
		if (split.length == 2) {
			String name = (split[1]);
			ArrayList<Ticket> ticket = SQLTicket.getPlayersTickets(name);
			if (!ticket.isEmpty()) {
				ChatTools.formatAndSend(
						plugin.language.getHeader()
								+ plugin.language.getSearchTicketTitle()
										.replaceAll("%player", name), player);
				for (Ticket t : ticket) {
					for (String line : t.preformReplace(plugin.language
							.getTicketShortInfo())) {
						ChatTools.formatAndSend(line, player);
					}
				}

			} else
				ChatTools.formatAndSend(
						plugin.language.getHeader()
								+ plugin.language.getSearchInvalid()
										.replaceAll("%player", name), player);
		} else if (split.length == 3 && split[1].equalsIgnoreCase("-a")) {
			String name = (split[2]);
			ArrayList<Ticket> ticket = SQLTicket
					.getPlayersAssignedTickets(name);
			if (!ticket.isEmpty()) {
				ChatTools.formatAndSend(
						plugin.language.getHeader()
								+ plugin.language.getSearchTicketTitle()
										.replaceAll("%player", name), player);
				for (Ticket t : ticket) {
					for (String line : t.preformReplace(plugin.language
							.getTicketShortInfo())) {
						ChatTools.formatAndSend(line, player);
					}
				}
				int i = SQLTicket.getNumAssigned(name);
				if (i == 1) {
					ChatTools.formatAndSend(plugin.language.getHeader() + "&b"
							+ name + " &fhas closed &b" + i + " &fticket",
							player);
				} else {
					ChatTools.formatAndSend(plugin.language.getHeader() + "&b"
							+ name + " &fhas closed &b" + i + " &ftickets",
							player);
				}
			} else
				ChatTools.formatAndSend(
						plugin.language.getHeader()
								+ plugin.language.getSearchInvalid()
										.replaceAll("%player", name), player);

		} else {
			help(player);
		}
	}

	public void commentCommand(Player player, String[] split) {
		if (split.length >= 3) {
			int id = 0;
			String comment = "";

			try {
				id = Integer.parseInt(split[1]);
			} catch (NumberFormatException ex) {
				invalid(player);
			}

			for (int i = 2; i < split.length; i++) {
				if (i == 2)
					comment = split[i];
				else
					comment += " " + split[i];
			}

			Ticket ticket = SQLTicket.getSpecificTicket(id);
			if (ticket != null) {
				if (plugin.isStaff(player)) {
					ticket.addLog(player.getName(), comment);
					for (String line : ticket.preformReplace(plugin.language
							.getTicketCommentUser()
							.replaceAll("%player", player.getName())
							.replaceAll("%comment", comment))) {
						plugin.informPlayer(ticket.getOwner(), line);
					}
					for (String line : ticket.preformReplace(plugin.language
							.getTicketCommentStaff()
							.replaceAll("%player", player.getName())
							.replaceAll("%comment", comment))) {
						plugin.informStaff(line);
					}
					if (ticket.isOpen()) {
						Player tmp = plugin.getServer().getPlayer(
								ticket.getOwner());
						if (tmp != null && !tmp.isOnline())
							ticket.setHasRead(false);
					}
				} else if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
					ticket.addLog(player.getName(), comment);
					for (String line : ticket.preformReplace(plugin.language
							.getTicketCommentStaff()
							.replaceAll("%player", player.getName())
							.replaceAll("%comment", comment))) {
						plugin.informStaff(line);
					}
				}
			}

		}
	}

	public void priorityCommand(Player player, String[] split) {
		if (split.length == 3) {
			int id = 0;
			int priority = 0;
			try {
				id = Integer.parseInt(split[1]);
				priority = Integer.parseInt(split[2]);
			} catch (NumberFormatException ex) {
				invalid(player);
			}
			if (priority > 4 || priority < 0) {
				ChatTools.formatAndSend(plugin.language.getHeader()
						+ plugin.language.getTicketPriorityError(), player);
				return;
			}
			Ticket ticket = SQLTicket.getSpecificTicket(id);
			if (ticket != null) {
				if (plugin.isStaff(player)) {
					ticket.setPriority(priority);
					for (String line : ticket.preformReplace(plugin.language
							.getTicketPriorityUser().replaceAll("%player",
									player.getName()))) {
						plugin.informPlayer(ticket.getOwner(), line);
					}
					for (String line : ticket.preformReplace(plugin.language
							.getTicketPriorityStaff().replaceAll("%player",
									player.getName()))) {
						plugin.informStaff(line);
					}
				}
				if (ticket.isOpen()) {
					Player tmp = plugin.getServer()
							.getPlayer(ticket.getOwner());
					if (tmp != null && !!tmp.isOnline())
						ticket.setHasRead(false);
				}
			}

		}
	}

	private void help(Player player) {
		ChatTools
				.formatAndSend(
						plugin.language.getHeader() + plugin.language.getHelp(),
						player);
	}

	private void invalid(Player player) {
		ChatTools.formatAndSend(
				plugin.language.getHeader() + plugin.language.getHelpInvalid(),
				player);
	}
}