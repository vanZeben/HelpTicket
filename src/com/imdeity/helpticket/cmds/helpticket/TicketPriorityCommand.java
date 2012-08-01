package com.imdeity.helpticket.cmds.helpticket;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketLanguageHelper;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.enums.PriorityType;
import com.imdeity.helpticket.enums.ReadStatusType;
import com.imdeity.helpticket.obj.PlayerSession;
import com.imdeity.helpticket.obj.Ticket;

public class TicketPriorityCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 1) return false;
        if (PlayerSession.getPlayerSession(player.getName()) == null) {
            HelpTicketMain.plugin.chat.sendPlayerMessage(player, HelpTicketMain.plugin.language.getNode(HelpTicketLanguageHelper.TICKET_INFO_FAIL_SESSION_INVALID));
            return true;
        }
        Ticket ticket = PlayerSession.getPlayerSession(player.getName()).getTicket();
        if (args[0].equalsIgnoreCase("increase")) {
            if (ticket.getPriority() == PriorityType.HIGH) {
                HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_PRIORITY_FAIL_TOO_HIGH, ticket);
                return true;
            }
            ticket.increasePriority();
            ticket.setReadStatus(ReadStatusType.UNREAD);
            ticket.save();
            HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_PRIORITY_SUCCESS, ticket);
            if ((!player.getName().equalsIgnoreCase(ticket.getOwner())) && (ticket.getPlayerOwner() != null) && (ticket.getPlayerOwner().isOnline())) {
                HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), HelpTicketLanguageHelper.TICKET_NEW_UPDATE, ticket);
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("decrease")) {
            if (ticket.getPriority() == PriorityType.LOW) {
                HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_PRIORITY_FAIL_TOO_LOW, ticket);
                return true;
            }
            ticket.decreasePriority();
            ticket.setReadStatus(ReadStatusType.UNREAD);
            ticket.save();
            HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_PRIORITY_SUCCESS, ticket);
            if ((!player.getName().equalsIgnoreCase(ticket.getOwner())) && (ticket.getPlayerOwner() != null) && (ticket.getPlayerOwner().isOnline())) {
                HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), HelpTicketLanguageHelper.TICKET_NEW_UPDATE, ticket);
            }
            return true;
        }
        return false;
    }
}