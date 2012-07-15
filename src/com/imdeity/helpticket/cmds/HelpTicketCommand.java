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

public class HelpTicketCommand implements CommandExecutor {
    
    private HelpTicket plugin;
    
    public HelpTicketCommand(HelpTicket instance) {
        this.plugin = instance;
        
    }
    
    public void sendCommands(Player player) {
        List<String> output = new ArrayList<String>();
        output.add(ChatTools.formatTitle("/ticket"));
        output.add(ChatTools.formatCommand("", "/ticket", "", "Checks your open Ticket's"));
        output.add(ChatTools.formatCommand("", "/ticket", "new [msg]", "Creates a new ticket"));
        output.add(ChatTools.formatCommand("", "/ticket", "view [id]", "Views a specific Ticket"));
        output.add(ChatTools.formatCommand("", "/ticket", "comment [id] [message]", "Comments on a Ticket"));
        output.add(ChatTools.formatCommand("", "/ticket", "close [id]", "Closes a Ticket"));
        if (plugin.isStaff(player)) {
            output.add(ChatTools.formatCommand("Moderator", "/ticket", "reopen [id]", "Reopen a closed ticket"));
            output.add(ChatTools.formatCommand("Moderator", "/ticket", "port [id]", "Teleport to a ticket"));
            output.add(ChatTools.formatCommand("Moderator", "/ticket", "assign [id] [player]", "Assigns a ticket to the specified player."));
            output.add(ChatTools.formatCommand("Moderator", "/ticket", "set-priority [id] [level]", "Sets the priority of a ticket"));
            output.add(ChatTools.formatCommand("Moderator", "/ticket", "search [-a] [name]", "Searches a players past tickets"));
            if (plugin.isAdmin(player)) {
                output.add(ChatTools.formatCommand("Admin", "/ticket", "set", "Sets language file options"));
            }
        }
        for (String s : output) {
            ChatTools.formatAndSend(s, player);
        }
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("helpticket.help") || player.isOp()) {
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
        } else if (split[0].equalsIgnoreCase("new") || split[0].equalsIgnoreCase("n") || split[0].equalsIgnoreCase("create")) {
            if (player.hasPermission("helpticket.help.new") || player.hasPermission("helpticket.mod.new") || player.hasPermission("helpticket.admin.new")) newCommand(player, split);
        } else if (split[0].equalsIgnoreCase("view") || split[0].equalsIgnoreCase("v")) {
            if (player.hasPermission("helpticket.help.view") || player.hasPermission("helpticket.mod.view") || player.hasPermission("helpticket.admin.view")) viewCommand(player, split);
        } else if (split[0].equalsIgnoreCase("comment") || split[0].equalsIgnoreCase("cmt")) {
            if (player.hasPermission("helpticket.help.comment") || player.hasPermission("helpticket.mod.comment") || player.hasPermission("helpticket.admin.comment")) commentCommand(player, split);
        } else if (split[0].equalsIgnoreCase("close") || split[0].equalsIgnoreCase("c")) {
            if (player.hasPermission("helpticket.help.close") || player.hasPermission("helpticket.mod.close") || player.hasPermission("helpticket.admin.close")) closeCommand(player, split);
        } else if (split[0].equalsIgnoreCase("reopen") || split[0].equalsIgnoreCase("ro")) {
            if (player.hasPermission("helpticket.mod.reopen") || player.hasPermission("helpticket.admin.reopen")) reopenCommand(player, split);
        } else if (split[0].equalsIgnoreCase("help") || split[0].equalsIgnoreCase("?")) {
            if (player.hasPermission("helpticket.help.help") || player.hasPermission("helpticket.mod.help") || player.hasPermission("helpticket.admin.help")) sendCommands(player);
        } else if (split[0].equalsIgnoreCase("port") || split[0].equalsIgnoreCase("tp") || split[0].equalsIgnoreCase("teleport") || split[0].equalsIgnoreCase("warp")) {
            if (player.hasPermission("helpticket.mod.teleport") || player.hasPermission("helpticket.admin.teleport")) portCommand(player, split);
        } else if (split[0].equalsIgnoreCase("assign") || split[0].equalsIgnoreCase("a")) {
            if (player.hasPermission("helpticket.mod.assign") || player.hasPermission("helpticket.admin.assign")) assignCommand(player, split);
        } else if (split[0].equalsIgnoreCase("search") || split[0].equalsIgnoreCase("s")) {
            if (player.hasPermission("helpticket.mod.search") || player.hasPermission("helpticket.admin.search")) searchCommand(player, split);
        } else if (split[0].equalsIgnoreCase("set-priority") || split[0].equalsIgnoreCase("sp")) {
            if (player.hasPermission("helpticket.mod.priority") || player.hasPermission("helpticket.admin.priority")) priorityCommand(player, split);
        } else if (split[0].equalsIgnoreCase("set-language") || split[0].equalsIgnoreCase("sl")) {
            this.setLanguageCommand(player, split);
        } else {
            help(player);
        }
    }
    
    public void newCommand(Player player, String[] split) {
        String message = "";
        if (split.length - 1 <= plugin.config.getMinWordCount()) {
            ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getTicketMinWordMessage().replaceAll("%numWords", plugin.config.getMinWordCount() + ""), player);
            return;
        }
        for (int i = 1; i < split.length; i++) {
            if (i == 1) message += split[i];
            else message += " " + split[i];
        }
        Ticket t = new Ticket(player.getName(), player.getWorld().getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getPitch(), player.getLocation().getYaw(), message, "", true, false, 0);
        SQLTicket.newTicket(t);
        ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getTicketSubmittedPlayer().replaceAll("%ticketId", "" + SQLTicket.getNewestTicketID(player.getName())), player);
        plugin.informStaff(plugin.language.getTicketSubmittedStaff().replaceAll("%ticketId", "" + SQLTicket.getNewestTicketID(player.getName())).replaceAll("%player", player.getName()));
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
                    ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getTicketNotExist().replaceAll("%ticketId", "" + id), player);
                    return;
                }
                if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
                    ticket.setHasRead(true);
                }
                
            } else {
                ticket = SQLTicket.getSpecificTicket(id);
                if (ticket == null) {
                    ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getTicketNotExist().replaceAll("%ticketId", "" + id), player);
                    return;
                }
                if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
                    ticket.setHasRead(true);
                }
            }
            for (String line : ticket.preformReplace(plugin.language.getTicketFullInfo())) {
                ChatTools.formatAndSend(line, player);
            }
        } else {
            help(player);
        }
    }
    
    public void viewAllCommand(Player player, String[] split) {
        if (!plugin.isStaff(player)) {
            ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getOpenTicketsMessagePlayer(), player);
            ChatTools.formatAndSend(plugin.language.getTicketColorCoding(), player);
            for (Ticket t : SQLTicket.getPlayersOpenTickets(player.getName())) {
                for (String line : t.preformReplace(plugin.language.getTicketShortInfo())) {
                    ChatTools.formatAndSend(line, player);
                }
            }
        } else {
            ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getOpenTicketsMessageStaff(), player);
            ChatTools.formatAndSend(plugin.language.getTicketColorCoding(), player);
            for (Ticket t : SQLTicket.getAllOpenTickets()) {
                for (String line : t.preformReplace(plugin.language.getTicketShortInfo())) {
                    ChatTools.formatAndSend(line, player);
                }
            }
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
                if (!ticket.isOpen()) {
                    for (String line : ticket.preformReplace(plugin.language.getTicketClosed())) {
                        ChatTools.formatAndSend(plugin.language.getHeader() + line, player);
                    }
                    this.help(player);
                    return;
                }
                if (plugin.isStaff(player) && !ticket.getOwner().equalsIgnoreCase(player.getName())) {
                    ticket.setAssignee(player.getName());
                    ticket.addLog(player.getName(), plugin.language.getClosedLog());
                    ticket.setStatus(false);
                    ticket.setPriorityClose();
                    for (String s : ticket.preformReplace(plugin.language.getClosedPlayer().replaceAll("%player", player.getName()))) {
                        plugin.sendPlayerMessage(ticket.getOwner(), s);
                    }
                    // Inform staff of close
                    for (String s : ticket.preformReplace(plugin.language.getClosedStaff().replaceAll("%player", player.getName()))) {
                        plugin.informStaff(s);
                    }
                    this.informPlayerOfChange(ticket);
                } else if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
                    ticket.setStatus(false);
                    ticket.setHasRead(true);
                    ticket.setPriorityClose();
                    for (String s : ticket.preformReplace(plugin.language.getClosedUser().replaceAll("%player", player.getName()))) {
                        plugin.sendPlayerMessage(player.getName(), s);
                    }
                    for (String s : ticket.preformReplace(plugin.language.getClosedSelf().replaceAll("%player", player.getName()))) {
                        plugin.informStaff(s);
                    }
                } else {
                    this.playerNotStaff(player);
                    return;
                }
            } else help(player);
        } else if (split.length >= 3) {
            int id = 0;
            String comment = "";
            
            try {
                id = Integer.parseInt(split[1]);
            } catch (NumberFormatException ex) {
                invalid(player);
            }
            
            for (int i = 2; i < split.length; i++) {
                if (i == 2) comment = split[i];
                else comment += " " + split[i];
            }
            
            Ticket ticket = SQLTicket.getSpecificTicket(id);
            if (ticket != null) {
                if (plugin.isStaff(player)) {
                    ticket.addLog(player.getName(), plugin.language.getClosedLogComment().replaceAll("%comment", comment).replaceAll("%player", player.getName()));
                    ticket.setStatus(false);
                    for (String s : ticket.preformReplace(plugin.language.getClosedStaffComment().replaceAll("%comment", comment).replaceAll("%player", player.getName()))) {
                        plugin.informStaff(s);
                    }
                    for (String s : ticket.preformReplace(plugin.language.getClosedPlayerComment().replaceAll("%comment", comment).replaceAll("%player", player.getName()))) {
                        plugin.sendPlayerMessage(ticket.getOwner(), s);
                    }
                    this.informPlayerOfChange(ticket);
                } else if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
                    ticket.addLog(player.getName(), plugin.language.getClosedLogComment().replaceAll("%comment", comment).replaceAll("%player", player.getName()));
                    ticket.setStatus(false);
                    ticket.setHasRead(true);
                    for (String s : ticket.preformReplace(plugin.language.getClosedUserComment().replaceAll("%comment", comment).replaceAll("%player", player.getName()))) {
                        plugin.sendPlayerMessage(ticket.getOwner(), s);
                    }
                    for (String s : ticket.preformReplace(plugin.language.getClosedSelfComment().replaceAll("%comment", comment).replaceAll("%player", player.getName()))) {
                        plugin.informStaff(s);
                    }
                } else {
                    this.playerNotStaff(player);
                    return;
                }
            } else help(player);
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
                if (ticket.isOpen()) {
                    for (String line : ticket.preformReplace(plugin.language.getTicketIsOpen())) {
                        ChatTools.formatAndSend(plugin.language.getHeader() + line, player);
                    }
                    this.help(player);
                    return;
                }
                if (plugin.isStaff(player)) {
                    ticket.addLog(player.getName(), plugin.language.getReopenLog());
                    ticket.setStatus(true);
                    for (String s : ticket.preformReplace(plugin.language.getReopenUser().replaceAll("%player", player.getName()))) {
                        plugin.sendPlayerMessage(ticket.getOwner(), s);
                    }
                    for (String s : ticket.preformReplace(plugin.language.getReopenStaff().replaceAll("%player", player.getName()))) {
                        plugin.informStaff(s);
                    }
                    this.informPlayerOfChange(ticket);
                } else {
                    this.playerNotStaff(player);
                    return;
                }
            } else help(player);
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
                ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getTicketNotExist().replaceAll("%ticketId", "" + id), player);
                return;
            }
            if (ticket.isOpen()) {
                if (plugin.isStaff(player)) {
                    player.teleport(ticket.getLocation(plugin.getWorld(ticket.getWorld())));
                    ticket.setAssignee(player.getName());
                    
                    for (String line : ticket.preformReplace(plugin.language.getTicketFullInfo())) {
                        ChatTools.formatAndSend(line, player);
                    }
                    
                    for (String line : ticket.preformReplace(plugin.language.getTicketFullInfo().replaceAll("%player", player.getName()))) {
                        plugin.sendPlayerMessage(ticket.getOwner(), line);
                    }
                } else {
                    this.playerNotStaff(player);
                    return;
                }
            } else {
                for (String line : ticket.preformReplace(plugin.language.getTicketClosed())) {
                    ChatTools.formatAndSend(plugin.language.getHeader() + line, player);
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
            if (ticket != null) {
                if (plugin.isStaff(player)) {
                    if (!ticket.isOpen()) {
                        for (String line : ticket.preformReplace(plugin.language.getTicketClosed())) {
                            ChatTools.formatAndSend(plugin.language.getHeader() + line, player);
                        }
                        this.help(player);
                        return;
                    }
                    if (assignee != null) {
                        ticket.setAssignee(assignee);
                        for (String line : ticket.preformReplace(plugin.language.getAssignUser().replaceAll("%player", player.getName()))) {
                            plugin.sendPlayerMessage(ticket.getOwner(), line);
                        }
                        for (String line : ticket.preformReplace(plugin.language.getAssignStaff().replaceAll("%player", player.getName()))) {
                            plugin.informStaff(line);
                        }
                        this.informPlayerOfChange(ticket);
                    }
                } else {
                    this.playerNotStaff(player);
                    return;
                }
            } else help(player);
        } else {
            help(player);
        }
    }
    
    public void searchCommand(Player player, String[] split) {
        if (!plugin.isStaff(player)) {
            this.playerNotStaff(player);
            return;
        }
        if (split.length == 2) {
            String name = (split[1]);
            ArrayList<Ticket> ticket = SQLTicket.getPlayersTickets(name);
            if (!ticket.isEmpty()) {
                ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getSearchTicketTitle().replaceAll("%player", name), player);
                for (Ticket t : ticket) {
                    for (String line : t.preformReplace(plugin.language.getTicketShortInfo())) {
                        ChatTools.formatAndSend(line, player);
                    }
                }
                
            } else ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getSearchInvalid().replaceAll("%player", name), player);
        } else if (split.length == 3 && split[1].equalsIgnoreCase("-a")) {
            String name = (split[2]);
            ArrayList<Ticket> ticket = SQLTicket.getPlayersAssignedTickets(name);
            if (!ticket.isEmpty()) {
                ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getSearchTicketTitle().replaceAll("%player", name), player);
                for (Ticket t : ticket) {
                    for (String line : t.preformReplace(plugin.language.getTicketShortInfo())) {
                        ChatTools.formatAndSend(line, player);
                    }
                }
                int i = SQLTicket.getNumAssigned(name);
                if (i == 1) {
                    ChatTools.formatAndSend(plugin.language.getHeader() + "&b" + name + " &fhas closed &b" + i + " &fticket", player);
                } else {
                    ChatTools.formatAndSend(plugin.language.getHeader() + "&b" + name + " &fhas closed &b" + i + " &ftickets", player);
                }
            } else ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getSearchInvalid().replaceAll("%player", name), player);
            
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
                if (i == 2) comment = split[i];
                else comment += " " + split[i];
            }
            Ticket ticket = SQLTicket.getSpecificTicket(id);
            if (ticket != null) {
                if (plugin.isStaff(player)) {
                    ticket.addLog(player.getName(), comment);
                    for (String line : ticket.preformReplace(plugin.language.getTicketCommentUser().replaceAll("%player", player.getName()).replaceAll("%comment", comment))) {
                        plugin.sendPlayerMessage(ticket.getOwner(), line);
                    }
                    for (String line : ticket.preformReplace(plugin.language.getTicketCommentStaff().replaceAll("%player", player.getName()).replaceAll("%comment", comment))) {
                        plugin.informStaff(line);
                    }
                    this.informPlayerOfChange(ticket);
                } else if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
                    ticket.addLog(player.getName(), comment);
                    for (String line : ticket.preformReplace(plugin.language.getTicketCommentStaff().replaceAll("%player", player.getName()).replaceAll("%comment", comment))) {
                        plugin.informStaff(line);
                    }
                } else {
                    this.playerNotStaff(player);
                    return;
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
                priority = (split[2].equalsIgnoreCase("lowest") ? 0 : (split[2].equalsIgnoreCase("low") ? 1 : (split[2].equalsIgnoreCase("medium") ? 2 : (split[2].equalsIgnoreCase("high") ? 3 : (split[2].equalsIgnoreCase("highest") ? 4 : Integer.parseInt(split[2]))))));
            } catch (NumberFormatException ex) {
                invalid(player);
            }
            if (priority > 4 || priority < 0) {
                ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getTicketPriorityError(), player);
                return;
            }
            Ticket ticket = SQLTicket.getSpecificTicket(id);
            if (ticket != null) {
                if (plugin.isStaff(player)) {
                    ticket.setPriority(priority);
                    for (String line : ticket.preformReplace(plugin.language.getTicketPriorityUser().replaceAll("%player", player.getName()))) {
                        plugin.sendPlayerMessage(ticket.getOwner(), line);
                    }
                    for (String line : ticket.preformReplace(plugin.language.getTicketPriorityStaff().replaceAll("%player", player.getName()))) {
                        plugin.informStaff(line);
                    }
                    this.informPlayerOfChange(ticket);
                } else {
                    this.playerNotStaff(player);
                    return;
                }
            }
        }
    }
    
    public void setLanguageCommand(Player player, String[] split) {
        if (split.length > 3) {
            String path = split[1];
            String value = "";
            for (int i = 2; i < split.length; i++) {
                if (i == 2) value = split[i];
                else value += " " + split[i];
            }
            if (plugin.isAdmin(player)) {
                plugin.language.setString(path, value);
                plugin.language.save();
                ChatTools.formatAndSend(plugin.language.getHeader() + "Set language varible of " + path + " to " + value, player);
            } else {
                this.playerNotStaff(player);
                return;
            }
        } else {
            
        }
    }
    
    private void informPlayerOfChange(Ticket ticket) {
        Player tmp = plugin.getServer().getPlayer(ticket.getOwner());
        if (tmp == null || (tmp != null && !tmp.isOnline())) {
            ticket.setHasRead(false);
            plugin.sendMailToPlayer(ticket.getOwner(), ticket.preformReplaceSingle(plugin.language.getUpdateMessage()));
        }
    }
    
    private void playerNotStaff(Player player) {
        ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getNotStaffMessage(), player);
        this.help(player);
    }
    
    private void help(Player player) {
        ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getHelp(), player);
    }
    
    private void invalid(Player player) {
        ChatTools.formatAndSend(plugin.language.getHeader() + plugin.language.getHelpInvalid(), player);
    }
}