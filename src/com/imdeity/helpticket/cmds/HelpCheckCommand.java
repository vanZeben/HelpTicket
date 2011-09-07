package com.imdeity.helpticket.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.helpticket.HelpTicket;
import com.imdeity.helpticket.object.SQLTicket;
import com.imdeity.helpticket.object.Ticket;
import com.imdeity.helpticket.utils.ChatTools;

public class HelpCheckCommand implements CommandExecutor {

    private HelpTicket plugin;
    private static final List<String> output = new ArrayList<String>();

    static {
        output.add(ChatTools.formatTitle("/check"));
        output.add(ChatTools.formatCommand("", "/check", "",
                "Checks for any new Ticket's"));
        output.add(ChatTools.formatCommand("", "/check", "[id]",
                "Checks a Ticket"));
        output.add(ChatTools.formatCommand("", "/check",
                "comment [id] [message]", "Comments on a Ticket"));
        output.add(ChatTools.formatCommand("", "/check",
                "reassign [id] [staff]", "Reassigns a Ticket"));
        output.add(ChatTools.formatCommand("", "/check", "assign [id] [staff]",
                "Assigns a Ticket"));
        output.add(ChatTools.formatCommand("", "/check", "close [id]",
                "Completes a Ticket"));

    }

    public HelpCheckCommand(HelpTicket instance) {
        this.plugin = instance;

    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.isStaff(player)) {
                parseCommand(player, args);
            }
        } else {
            // Console
            for (String line : output)
                sender.sendMessage(ChatTools.strip(line));
        }
        return true;
    }

    private void parseCommand(Player player, String[] split) {

        if (split.length == 0) {
            checkCommand(player);
        } else if (split.length == 1) {
            viewCommand(player, split);
        } else if (split[0].equalsIgnoreCase("comment")) {

        } else if (split[0].equalsIgnoreCase("reassign")
                || split[0].equalsIgnoreCase("assign")) {
            assignCommand(player, split);
        } else if (split[0].equalsIgnoreCase("close")) {
            closeCommand(player, split);
        }
    }

    public void checkCommand(Player player) {
        player.sendMessage(ChatTools.formatSitTitle("Open Tickets"));
        for (Ticket t : SQLTicket.getAllOpenTickets()) {
            player.sendMessage(t.getSentence(true));
        }
    }

    public void viewCommand(Player player, String[] split) {
        if (split.length == 1) {
            int id = 0;
            try {
                id = Integer.parseInt(split[0]);
            } catch (NumberFormatException ex) {
                invalid(player);
            }
            Ticket ticket = SQLTicket.getSpecificTicket(id);

            List<String> out = new ArrayList<String>();

            if (ticket.isOpen()) {
                player.teleport(ticket.getLocation(plugin.getWorld(ticket
                    .getWorld())));
                ticket.setAssignee(player.getName());
                SQLTicket.updateTicket(ticket, "assignee");
            }

            out.add(ChatTools.formatSitTitle(ticket.getOwner()));
            out.add(ChatTools.formatSituation("", "ID Number",
                    "" + ticket.getID()));
            if (ticket.getAssignee() != null) {
                out.add(ChatTools.formatSituation("", "Assigned To",
                        ticket.getAssignee()));
            }
            out.add(ChatTools.formatSituation("", "Situation",
                    ticket.getTitle()));
            out.add(ChatTools.formatSituation("", "Status",
                    (!ticket.isOpen() ? "Complete" : "Incomplete")));

            for (String line : out) {
                player.sendMessage(line);
            }
        } else {
            help(player);
        }
    }

    public void assignCommand(Player player, String[] split) {
        if (split.length == 3) {
            int id = 0;

            try {
                id = Integer.parseInt(split[1]);
            } catch (NumberFormatException ex) {
                invalid(player);
            }

            Ticket ticket = SQLTicket.getSpecificTicket(id);
            if (ticket != null) {
                String assignee = split[2];
                if (plugin.isStaff(assignee)) {
                    ticket.setAssignee(assignee);
                    ChatTools.formatAndSend(
                            "<option>"
                                    + SQLTicket
                                            .updateTicket(ticket, "assignee"),
                            "HelpTicket", player);
                } else {
                    warn(player, "You cannot assign a non-staff to a ticket.");
                }
            } else
                help(player);
        } else {
            help(player);
        }
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
                ticket.setStatus(false);
                ChatTools
                        .formatAndSend(
                                "<option>"
                                        + SQLTicket.updateTicket(ticket,
                                                "status"), "HelpTicket",
                                player);

            } else
                help(player);
        } else {
            help(player);
        }
    }

    private void warn(Player player, String msg) {
        ChatTools.formatAndSend("<option><red>" + msg, "HelpTicket", player);
    }

    private void help(Player player) {
        String msg = "Use \"/check ?\" for help";
        ChatTools.formatAndSend("<option><yellow>" + msg, "HelpTicket", player);
    }

    private void invalid(Player player) {
        String msg = "Invalid Syntax!";
        ChatTools.formatAndSend("<option><yellow>" + msg, "HelpTicket", player);
        msg = "Use \"/check ?\" for help";
        ChatTools.formatAndSend("<option><yellow>" + msg, "HelpTicket", player);
    }
}
