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
                "Closes a Ticket"));
        output.add(ChatTools.formatCommand("Moderator", "/ticket", "port [id]",
                "Teleport a ticket"));
        output.add(ChatTools.formatCommand("Moderator", "/ticket",
                "assign [id] [player]",
                "Assigns a ticket to the specified player."));
        output.add(ChatTools.formatCommand("Moderator", "/ticket", "search [name]",
                "Searches a players past tickets"));
    }

    public HelpTicketCommand(HelpTicket instance) {
        this.plugin = instance;

    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (HelpTicket.Permissions.has(player, "helpticket.help")) {
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
            closeCommand(player, split);
        } else if (split[0].equalsIgnoreCase("help")
                || split[0].equalsIgnoreCase("?")) {
            for (String s : output) {
                ChatTools.formatAndSend(s, "", player);
            }
        } else if (split[0].equalsIgnoreCase("port")) {
            if (plugin.isStaff(player)) {
                portCommand(player, split);
            } else {
                warn(player,
                        "You need to be at least a moderator to preform this action");
            }
        } else if (split[0].equalsIgnoreCase("assign")) {
            if (plugin.isStaff(player)) {
                assignCommand(player, split);
            } else {
                warn(player,
                        "You need to be at least a moderator to preform this action");
            }
        } else if (split[0].equalsIgnoreCase("search")) {
            if (plugin.isStaff(player)) {
                searchCommand(player, split);
            } else {
                warn(player,
                        "You need to be at least a moderator to preform this action");
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
                player.getLocation().getYaw(), message, "", true, false);

        SQLTicket.newTicket(t);

        ChatTools.formatAndSend(
                "<option>Your Ticket has been submitted. You can check it with \"/ticket view "
                        + SQLTicket.getNewestTicketID(player.getName()) + "\"",
                "HelpTicket", player);

        HelpTicket.informStaff("<white>" + player.getName()
                + " <gray> opened Ticket #<yellow>"
                + SQLTicket.getNewestTicketID(player.getName()));
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
                    ticket.getInfo()));
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
        if (!plugin.isStaff(player)) {
            player.sendMessage(ChatTools.formatSitTitle("Your Open Tickets"));
            for (Ticket t : SQLTicket.getPlayersOpenTickets(player.getName())) {
                player.sendMessage(t.getSentence(true));
            }
        } else {
            player.sendMessage(ChatTools.formatSitTitle("Open Tickets"));
            for (Ticket t : SQLTicket.getAllOpenTickets()) {
                player.sendMessage(t.getSentence(true));
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
                if (plugin.isStaff(player)) {
                    if (ticket.getAssignee().equalsIgnoreCase(player.getName())) {
                        ticket.setStatus(false);
                        ChatTools
                                .formatAndSend(
                                        "<option>"
                                                + SQLTicket.updateTicket(
                                                        ticket, "status"),
                                        "HelpTicket", player);
                        plugin.informPlayer(ticket.getOwner(), "<white>"
                                + player.getName()
                                + " <gray>closed your Ticket <yellow>[ID #"
                                + ticket.getID() + "]");
                    } else {
                        warn(player,
                                "You need to be assigned to the ticket to close it.");
                    }
                } else {
                    ticket.setStatus(false);
                    ChatTools.formatAndSend(
                            "<option>"
                                    + SQLTicket.updateTicket(ticket, "status"),
                            "HelpTicket", player);
                    HelpTicket.informStaff("<white>" + player.getName()
                            + " <gray> closed Ticket #<yellow>"
                            + ticket.getID());
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
                    ticket.getInfo()));
            out.add(ChatTools.formatSituation("", "Status",
                    (!ticket.isOpen() ? "Complete" : "Incomplete")));

            for (String line : out) {
                player.sendMessage(line);
            }

            plugin.informPlayer(
                    ticket.getOwner(),
                    "<white>"
                            + ticket.getAssignee()
                            + " <gray>is reviewing your Ticket <yellow>[ID #"
                            + ticket.getID() + "]");
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
                    plugin.informPlayer(
                            ticket.getOwner(),
                            "<white>"
                                    + ticket.getAssignee()
                                    + " <gray>has been assigned to your Ticket <yellow>[ID #"
                                    + ticket.getID() + "]");
                } else {
                    warn(player, "You cannot assign a non-staff to a ticket.");
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
                player.sendMessage(ChatTools.formatSitTitle(name+"'s Tickets"));
                for (Ticket t : ticket) {
                    player.sendMessage(t.getSentence(true));
                }
                
            } else
               ChatTools.formatAndSend("<option><white>"+name+"<gray> hasn't submitted any tickets.", "HelpTicket", player);
        } else {
            help(player);
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
