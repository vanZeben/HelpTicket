package com.imdeity.helpticket.cmds.helpticket;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketLanguageHelper;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.enums.OpenStatusType;
import com.imdeity.helpticket.enums.ReadStatusType;
import com.imdeity.helpticket.obj.PlayerSession;
import com.imdeity.helpticket.obj.Ticket;
import com.imdeity.helpticket.obj.TicketManager;

public class TicketCloseCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length < 1) return false;
        String reason;
        String commenter;
        int ticketId = -1;
        try {
            ticketId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            HelpTicketMain.plugin.chat.outWarn("The Ticket id \"" + args[0] + "\" is invalid");
            return true;
        }
        
        if (args.length < 1) commenter = args[1];
        else commenter = "Console";
        
        if (args.length < 2) reason = "Closed the ticket - " + DeityAPI.getAPI().getUtilAPI().getStringUtils().join(args);
        else reason = "Closed the ticket";
        
        Ticket ticket = TicketManager.getTicket(ticketId);
        if (ticket == null) {
            HelpTicketMain.plugin.chat.outWarn("The Ticket with the id \"" + ticketId + "\" is invalid");
            return true;
        }
        ticket.setOpenStatus(OpenStatusType.CLOSED);
        ticket.setReadStatus(ReadStatusType.UNREAD);
        ticket.addComment(commenter, reason);
        ticket.save();
        TicketManager.closeTicket(ticket);
        HelpTicketMain.plugin.chat.out(HelpTicketMain.replace(HelpTicketLanguageHelper.TICKET_CLOSE_SUCCESS, ticket));
        if ((ticket.getPlayerOwner() != null) && (ticket.getPlayerOwner().isOnline())) {
            HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), HelpTicketLanguageHelper.TICKET_NEW_UPDATE, ticket);
        }
        return true;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        String reason;
        if (args.length < 1) reason = "Closed the ticket - " + DeityAPI.getAPI().getUtilAPI().getStringUtils().join(args);
        else reason = "Closed the ticket";
        if (PlayerSession.getPlayerSession(player.getName()) == null) {
            HelpTicketMain.plugin.chat.sendPlayerMessage(player,
                    HelpTicketMain.plugin.language.getNode(HelpTicketLanguageHelper.TICKET_INFO_FAIL_SESSION_INVALID));
            return true;
        }
        Ticket ticket = PlayerSession.getPlayerSession(player.getName()).getTicket();
        if (!ticket.getOwner().equalsIgnoreCase(player.getName()) && !HelpTicketMain.isAdmin(player)) {
            PlayerSession.removePlayerSession(player.getName());
            HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_INFO_FAIL_TICKET_INVALID, ticket);
            return true;
        }
        ticket.setOpenStatus(OpenStatusType.CLOSED);
        if (!ticket.getOwner().equalsIgnoreCase(player.getName())) {
            ticket.setReadStatus(ReadStatusType.UNREAD);
        }
        ticket.addComment(player.getName(), reason);
        ticket.save();
        TicketManager.closeTicket(ticket);
        HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_CLOSE_SUCCESS, ticket);
        if ((!player.getName().equalsIgnoreCase(ticket.getOwner())) && (ticket.getPlayerOwner() != null)
                && (ticket.getPlayerOwner().isOnline())) {
            HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), HelpTicketLanguageHelper.TICKET_NEW_UPDATE, ticket);
        }
        return true;
    }
}