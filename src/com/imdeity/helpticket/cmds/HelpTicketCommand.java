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

@SuppressWarnings("unused")
public class HelpTicketCommand implements CommandExecutor {

    private HelpTicket plugin;
    private static final List<String> output = new ArrayList<String>();

    static {
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
                "Completes a Ticket"));

    }

    public HelpTicketCommand(HelpTicket instance) {
        this.plugin = instance;

    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (HelpTicket.Permissions.has(player, "helpticket.help")
                    || plugin.isStaff(player)) {
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
            viewAllCommand(player, split);
        } else if (split[0].equalsIgnoreCase("new")) {
            newCommand(player, split);
        } else if (split[0].equalsIgnoreCase("view")) {
            viewCommand(player, split);
        } else if (split[0].equalsIgnoreCase("comment")) {
            
        } else if (split[0].equalsIgnoreCase("close")) {

        } else if (split[0].equalsIgnoreCase("help")
                || split[0].equalsIgnoreCase("?")) {
            for (String s : output) {
                ChatTools.formatAndSend(s, "", player);
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
                player.getLocation().getYaw(), message, "", true);

        SQLTicket.newTicket(t);

        ChatTools.formatAndSend(
                "<option>Your Ticket has been submitted. You can check it with \"/ticket view "
                        + SQLTicket.getNewestTicketID(player.getName()) + "\"",
                "HelpTicket", player);
    }

    public void viewCommand(Player player, String[] split) {
        if (split.length == 2) {
            int id = 0;
            try {
                id = Integer.parseInt(split[1]);
            } catch (NumberFormatException ex) {
                invalid(player);
            }
            Ticket ticket = SQLTicket.getSpecificTicket(id, player.getName());

            List<String> out = new ArrayList<String>();

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

    public void viewAllCommand(Player player, String[] split) {
        player.sendMessage(ChatTools.formatSitTitle("Your Open Tickets"));
        for (Ticket t : SQLTicket.getPlayersOpenTickets(player.getName())) {
            player.sendMessage(t.getSentence(true));
        }
    }

    private void warn(Player player, String msg) {
        ChatTools.formatAndSend("<option><red>" + msg, "HelpTicket", player);
    }

    private void help(Player player) {
        String msg = "Use \"/ticket ?\" for help";
        ChatTools.formatAndSend("<option><yellow>" + msg, "HelpTicket", player);
    }

    private void invalid(Player player) {
        String msg = "Invalid Syntax!";
        ChatTools.formatAndSend("<option><yellow>" + msg, "HelpTicket", player);
        msg = "Use \"/ticket ?\" for help";
        ChatTools.formatAndSend("<option><yellow>" + msg, "HelpTicket", player);
    }
}
