package com.imdeity.helpticket.cmds.helpticket;

import java.util.regex.Matcher;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.enums.ReadStatusType;
import com.imdeity.helpticket.obj.PlayerSession;
import com.imdeity.helpticket.obj.Ticket;

public class TicketCommentCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 1) return false;
        if (PlayerSession.getPlayerSession(player.getName()) == null) {
            HelpTicketMain.plugin.chat.sendPlayerMessage(player, HelpTicketMain.plugin.language.getNode("helpticket.commands.info.fail.session_invalid"));
            return true;
        }
        String comment = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(args);
        Ticket ticket = PlayerSession.getPlayerSession(player.getName()).getTicket();
        if ((!ticket.getOwner().equalsIgnoreCase(player.getName())) && (!HelpTicketMain.isAdmin(player))) {
            PlayerSession.removePlayerSession(player.getName());
            HelpTicketMain.plugin.chat.sendPlayerMessage(player, HelpTicketMain.plugin.language.getNode("helpticket.commands.select.fail").replaceAll("%ticketId%", Matcher.quoteReplacement(ticket.getId() + "")));
            return true;
        }
        if (!ticket.getOwner().equalsIgnoreCase(player.getName())) {
            ticket.setReadStatus(ReadStatusType.UNREAD);
        }
        ticket.addComment(player.getName(), comment);
        ticket.save();
        HelpTicketMain.replaceAndSend(player, "helpticket.commands.comment.success", ticket);
        if ((!player.getName().equalsIgnoreCase(ticket.getOwner())) && (ticket.getPlayerOwner() != null) && (ticket.getPlayerOwner().isOnline())) {
            HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), "helpticket.commands.comment.success", ticket);
        }
        return true;
    }
}