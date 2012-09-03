package com.imdeity.helpticket.cmds.helpticket;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketLanguageHelper;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.enums.ReadStatusType;
import com.imdeity.helpticket.obj.PlayerSession;
import com.imdeity.helpticket.obj.Ticket;
import com.imdeity.helpticket.obj.TicketManager;

public class TicketAssignCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length < 2) return false;
        int ticketId = -1;
        try {
            ticketId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            HelpTicketMain.plugin.chat.outWarn("The Ticket id \"" + args[0] + "\" is invalid");
            return true;
        }
        Ticket ticket = TicketManager.getTicket(ticketId);
        if (ticket == null) {
            HelpTicketMain.plugin.chat.outWarn("The Ticket with the id \"" + ticketId + "\" is invalid");
            return true;
        }
        ticket.setAssignee(args[1]);
        ticket.setReadStatus(ReadStatusType.UNREAD);
        ticket.save();
        HelpTicketMain.plugin.chat.out(HelpTicketMain.replace(HelpTicketLanguageHelper.TICKET_ASSIGN_SUCCESS, ticket));
        if ((ticket.getPlayerOwner() != null) && (ticket.getPlayerOwner().isOnline())) {
            HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), HelpTicketLanguageHelper.TICKET_NEW_UPDATE, ticket);
        }
        return true;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 1) return false;
        if (PlayerSession.getPlayerSession(player.getName()) == null) {
            HelpTicketMain.plugin.chat.sendPlayerMessage(player,
                    HelpTicketMain.plugin.language.getNode(HelpTicketLanguageHelper.TICKET_INFO_FAIL_SESSION_INVALID));
            return true;
        }
        Ticket ticket = PlayerSession.getPlayerSession(player.getName()).getTicket();
        ticket.setAssignee(args[0]);
        ticket.setReadStatus(ReadStatusType.UNREAD);
        ticket.save();
        HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_ASSIGN_SUCCESS, ticket);
        if ((!player.getName().equalsIgnoreCase(ticket.getOwner())) && (ticket.getPlayerOwner() != null)
                && (ticket.getPlayerOwner().isOnline())) {
            HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), HelpTicketLanguageHelper.TICKET_NEW_UPDATE, ticket);
        }
        return true;
    }
}