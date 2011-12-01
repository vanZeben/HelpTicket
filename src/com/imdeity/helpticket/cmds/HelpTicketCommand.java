package com.imdeity.helpticket.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.imdeity.helpticket.HelpTicket;
import com.imdeity.helpticket.HelpTicketSettings;
import com.imdeity.helpticket.object.SQLTicket;
import com.imdeity.helpticket.object.Ticket;
import com.imdeity.helpticket.utils.ChatTools;

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
        output.add(ChatTools.formatCommand("Moderator", "/ticket",
                "reopen [id]", "Reopen a closed ticket"));
        output.add(ChatTools.formatCommand("Moderator", "/ticket", "port [id]",
                "Teleport to a ticket"));
        output.add(ChatTools.formatCommand("Moderator", "/ticket",
                "assign [id] [player]",
                "Assigns a ticket to the specified player."));
        output.add(ChatTools.formatCommand("Moderator", "/ticket",
                "priority [id] [level]", "Sets the priority of a ticket"));
        output.add(ChatTools.formatCommand("Moderator", "/ticket",
                "search [name]", "Searches a players past tickets"));

    }

    public HelpTicketCommand(HelpTicket instance) {
        this.plugin = instance;

    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (HelpTicketSettings.isUsingPermissions()) {
                if (player.hasPermission("helpticket.help")
                        || player.isOp()) {
                    parseCommand(player, args);
                }
            } else {
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
            commentCommand(player, split);
        } else if (split[0].equalsIgnoreCase("close")) {
            closeCommand(player, split);
        } else if (split[0].equalsIgnoreCase("reopen")) {
            reopenCommand(player, split);
        } else if (split[0].equalsIgnoreCase("help")
                || split[0].equalsIgnoreCase("?")) {
            for (String s : output) {
                ChatTools.formatAndSend(s, "", player);
            }
        } else if (split[0].equalsIgnoreCase("port")
                || split[0].equalsIgnoreCase("warp")
                || split[0].equalsIgnoreCase("tp")) {
            if (plugin.isStaff(player)) {
                portCommand(player, split);
            } else {
                warn(player,
                        "You need to be at least a moderator to perform this action");
            }
        } else if (split[0].equalsIgnoreCase("assign")) {
            if (plugin.isStaff(player)) {
                assignCommand(player, split);
            } else {
                warn(player,
                        "You need to be at least a moderator to perform this action");
            }
        } else if (split[0].equalsIgnoreCase("search")) {
            if (plugin.isStaff(player)) {
                searchCommand(player, split);
            } else {
                warn(player,
                        "You need to be at least a moderator to perform this action");
            }
        } else if (split[0].equalsIgnoreCase("priority")) {
            if (plugin.isStaff(player)) {
                priorityCommand(player, split);
            } else {
                warn(player,
                        "You need to be at least a moderator to perform this action");
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

        ChatTools.formatAndSend(
                "<option>Your Ticket has been submitted. You can check it with \"/ticket view "
                        + SQLTicket.getNewestTicketID(player.getName()) + "\"",
                "HelpTicket", player);

        HelpTicket.informStaff("<white>" + player.getName()
                + "<gray> opened Ticket <yellow>#"
                + SQLTicket.getNewestTicketID(player.getName()));
    }

    public void viewCommand(Player player, String[] split) {
        if (split.length == 2) {
            int id = 0;
            List<String> out = new ArrayList<String>();
            Ticket ticket = null;
            try {
                id = Integer.parseInt(split[1]);
            } catch (NumberFormatException ex) {
                invalid(player);
            }
            if (!plugin.isStaff(player)) {
                ticket = SQLTicket.getSpecificTicket(id, player.getName());

                if (ticket == null) {
                    ChatTools.formatAndSend("<option>Ticket #" + id
                            + " does not exist.", "HelpTicket", player);
                    return;
                }
            } else {
                ticket = SQLTicket.getSpecificTicket(id);

                if (ticket == null) {
                    ChatTools.formatAndSend("<option>Ticket #" + id
                            + " does not exist.", "HelpTicket", player);
                    return;
                }
            }
            out.add(ChatTools.formatSitTitle(ticket.getOwner()));
            out.add(ChatTools.formatSituation("ID Number", "" + ticket.getID(),
                    ""));
            if (ticket.getRawPriority() != -1) {
                out.add(ChatTools.formatSituation("Priority",
                        ticket.getPriority(), ""));
            }
            if (ticket.getAssignee() != null) {
                out.add(ChatTools.formatSituation("Assigned To",
                        ticket.getAssignee(), ""));
            }
            out.add(ChatTools.formatSituation("Situation", ticket.getInfo(), ""));
            out.add(ChatTools.formatSituation("Status",
                    (!ticket.isOpen() ? "Complete" : "Incomplete"), ""));

            if (ticket.getLog().size() >= 1) {
                out.add(ChatTools.formatSituation("Comments", "", ""));
                for (String[] s : ticket.getLog()) {
                    out.add(ChatTools.formatComment(" ", s[0], s[1]));
                }
            } else
                out.add(ChatTools.formatSituation("No Comments", "", ""));

            for (String line : out) {
                player.sendMessage(line);
            }
            if (ticket.isOpen())
                SQLTicket.updateTicket(ticket, "read");
        } else {
            help(player);
        }
    }

    public void viewAllCommand(Player player, String[] split) {
        if (!plugin.isStaff(player)) {
            player.sendMessage(ChatTools.formatSitTitle("Your Open Tickets"));
            ChatTools
                    .formatAndSend(
                            "[<darkblue>lowest<white>, <blue>low<white>, <yellow>medium<white>, <red>high<white>, <darkred>highest<white>]",
                            "", player);
            for (Ticket t : SQLTicket.getPlayersOpenTickets(player.getName())) {
                SQLTicket.getComments(t);
                t.setPriority(SQLTicket.getPriority(t));
                ChatTools.formatAndSend((t.getSentence(true)), "", player);
            }
        } else {
            player.sendMessage(ChatTools.formatSitTitle("Open Tickets"));
            ChatTools
                    .formatAndSend(
                            "[<darkblue>lowest<white>, <blue>low<white>, <yellow>medium<white>, <red>high<white>, <darkred>highest<white>]",
                            "", player);
            for (Ticket t : SQLTicket.getAllOpenTickets()) {
                SQLTicket.getComments(t);
                t.setPriority(SQLTicket.getPriority(t));
                ChatTools.formatAndSend((t.getSentence(true)), "", player);
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
                    SQLTicket.updateTicket(ticket, "assignee");
                    ticket.addLog(player.getName(), "Closed the ticket ");
                    ticket.setStatus(false);
                    SQLTicket.updateTicket(ticket, "log", player.getName());
                    SQLTicket.updateTicket(ticket, "priorityclose",
                            player.getName());
                    ChatTools.formatAndSend(
                            "<option>"
                                    + SQLTicket.updateTicket(ticket, "status"),
                            "HelpTicket", player);
                    plugin.informPlayer(ticket.getOwner(),
                            "<white>" + player.getName()
                                    + " <gray>closed your Ticket <yellow>[ID #"
                                    + ticket.getID() + "]");

                    // Inform staff of close
                    HelpTicket.informStaff("<white>" + player.getName()
                            + " <gray>closed Ticket <yellow>[ID #"
                            + ticket.getID() + "]");

                } else if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
                    ticket.setStatus(false);
                    SQLTicket.updateTicket(ticket, "priorityclose",
                            player.getName());
                    ChatTools.formatAndSend(
                            "<option>"
                                    + SQLTicket.updateTicket(ticket, "status"),
                            "HelpTicket", player);
                    HelpTicket.informStaff("<white>" + player.getName()
                            + " <gray> closed Ticket #<yellow>"
                            + ticket.getID());
                }
                if (ticket.isOpen()) {
                    Player tmp = plugin.getServer()
                            .getPlayer(ticket.getOwner());
                    if (tmp != null && !tmp.isOnline())
                        SQLTicket.updateTicket(ticket, "read");
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
                    ticket.addLog(player.getName(), "Closed the ticket - "
                            + comment);
                    ticket.setStatus(false);
                    SQLTicket.updateTicket(ticket, "log", player.getName());
                    ChatTools.formatAndSend(
                            "<option>"
                                    + SQLTicket.updateTicket(ticket, "status"),
                            "HelpTicket", player);
                    plugin.informPlayer(ticket.getOwner(),
                            "<white>" + player.getName()
                                    + " <gray>closed your Ticket <yellow>[ID #"
                                    + ticket.getID() + "] - " + comment);

                    // Inform staff of close
                    HelpTicket.informStaff("<white>" + player.getName()
                            + " <gray>closed Ticket <yellow>[ID #"
                            + ticket.getID() + "] - " + comment);
                } else if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
                    ticket.setStatus(false);
                    ChatTools.formatAndSend(
                            "<option>"
                                    + SQLTicket.updateTicket(ticket, "status"),
                            "HelpTicket", player);
                    HelpTicket.informStaff("<white>" + player.getName()
                            + " <gray> closed Ticket #<yellow>"
                            + ticket.getID());
                }
                if (ticket.isOpen()) {
                    Player tmp = plugin.getServer()
                            .getPlayer(ticket.getOwner());
                    if (tmp != null && !tmp.isOnline())
                        SQLTicket.updateTicket(ticket, "read");
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

                    ticket.addLog(player.getName(), "Re-opened ticket");
                    ticket.setStatus(true);
                    SQLTicket.updateTicket(ticket, "log", player.getName());
                    ChatTools.formatAndSend(
                            "<option>"
                                    + SQLTicket.updateTicket(ticket, "status"),
                            "HelpTicket", player);
                    HelpTicket.informStaff("<white>" + player.getName()
                            + " <gray> re-opened Ticket #<yellow>"
                            + ticket.getID());
                    if (ticket.isOpen()) {
                        Player tmp = plugin.getServer().getPlayer(
                                ticket.getOwner());
                        if (tmp != null && !tmp.isOnline())
                            SQLTicket.updateTicket(ticket, "notread");
                    }
                } else {
                    plugin.informPlayer(player.getName(),
                            "Only staff can reopen tickets");
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

            if (ticket == null) {
                ChatTools.formatAndSend("<option>Ticket #" + id
                        + " does not exist.", "HelpTicket", player);
                return;
            }
            if (ticket.isOpen()) {
                player.teleport(ticket.getLocation(plugin.getWorld(ticket
                        .getWorld())));
                ticket.setAssignee(player.getName());
                SQLTicket.updateTicket(ticket, "assignee");

                out.add(ChatTools.formatSitTitle(ticket.getOwner()));
                out.add(ChatTools.formatSituation("ID Number",
                        "" + ticket.getID(), ""));
                if (ticket.getRawPriority() != -1) {
                    out.add(ChatTools.formatSituation("Priority",
                            ticket.getPriority(), ""));
                }
                if (ticket.getAssignee() != null) {
                    out.add(ChatTools.formatSituation("Assigned To",
                            ticket.getAssignee(), ""));
                }
                out.add(ChatTools.formatSituation("Situation",
                        ticket.getInfo(), ""));
                out.add(ChatTools.formatSituation("Status",
                        (!ticket.isOpen() ? "Complete" : "Incomplete"), ""));

                if (ticket.getLog().size() >= 1) {
                    out.add(ChatTools.formatSituation("Comments", "", ""));
                    for (String[] s : ticket.getLog()) {
                        out.add(ChatTools.formatComment(" ", s[0], s[1]));
                    }
                } else
                    out.add(ChatTools.formatSituation("No Comments", "", ""));

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
                ChatTools.formatAndSend("<option>Ticket #" + ticket.getID()
                        + " is closed.", "HelpTicket", player);
                ChatTools.formatAndSend("<option>Please use \"/ticket view "
                        + ticket.getID() + "\" to view the Ticket.",
                        "HelpTicket", player);
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

                ChatTools
                        .formatAndSend(
                                "<option>"
                                        + SQLTicket.updateTicket(ticket,
                                                "assignee"), "HelpTicket",
                                player);
                plugin.informPlayer(
                        ticket.getOwner(),
                        "<white>"
                                + ticket.getAssignee()
                                + " <gray>has been assigned to your Ticket <yellow>[ID #"
                                + ticket.getID() + "]");
                if (ticket.isOpen()) {
                    Player tmp = plugin.getServer()
                            .getPlayer(ticket.getOwner());
                    if (tmp != null && !tmp.isOnline())
                        SQLTicket.updateTicket(ticket, "read");
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
                player.sendMessage(ChatTools
                        .formatSitTitle(name + "'s Tickets"));
                for (Ticket t : ticket) {
                    player.sendMessage(t.getSentence(true));
                }

            } else
                ChatTools.formatAndSend("<option><white>" + name
                        + "<gray> hasn't submitted any tickets.", "HelpTicket",
                        player);
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
                    ChatTools.formatAndSend(
                            "<option>"
                                    + SQLTicket.updateTicket(ticket, "log",
                                            player.getName()), "HelpTicket",
                            player);
                    plugin.informPlayer(
                            ticket.getOwner(),
                            "<white>"
                                    + player.getName()
                                    + " <gray>commented on your ticket. <yellow>[ID #"
                                    + ticket.getID() + "]");

                } else if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
                    ticket.addLog(player.getName(), comment);
                    ChatTools.formatAndSend(
                            "<option>"
                                    + SQLTicket.updateTicket(ticket, "log",
                                            player.getName()), "HelpTicket",
                            player);
                }
                if (ticket.isOpen()) {
                    Player tmp = plugin.getServer()
                            .getPlayer(ticket.getOwner());
                    if (tmp != null && !tmp.isOnline())
                        SQLTicket.updateTicket(ticket, "notread");
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
                ChatTools.formatAndSend(
                        "<option><red>Priority can only be 0-4", "HelpTicket",
                        player);
                return;
            }
            Ticket ticket = SQLTicket.getSpecificTicket(id);
            if (ticket != null) {
                if (plugin.isStaff(player)) {

                    ticket.setPriority(priority);
                    ChatTools.formatAndSend(
                            "<option>"
                                    + SQLTicket.updateTicket(ticket,
                                            "priority", player.getName()),
                            "HelpTicket", player);
                    plugin.informPlayer(
                            ticket.getOwner(),
                            "<white>" + player.getName()
                                    + " <gray>set your ticket to "
                                    + ticket.getPriority()
                                    + " priority. <yellow>[ID #"
                                    + ticket.getID() + "]");

                }
                if (ticket.isOpen()) {
                    Player tmp = plugin.getServer()
                            .getPlayer(ticket.getOwner());
                    if (tmp != null && !!tmp.isOnline())
                        SQLTicket.updateTicket(ticket, "notread");
                }
            }

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