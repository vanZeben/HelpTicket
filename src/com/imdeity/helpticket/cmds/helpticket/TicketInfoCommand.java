package com.imdeity.helpticket.cmds.helpticket;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketLanguageHelper;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.enums.ReadStatusType;
import com.imdeity.helpticket.obj.PlayerSession;
import com.imdeity.helpticket.obj.Ticket;

public class TicketInfoCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        int commentPage = -1;
        if (args.length == 0) commentPage = 1;
        else {
            try {
                commentPage = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                commentPage = 1;
            }
        }
        if (PlayerSession.getPlayerSession(player.getName()) == null) {
            HelpTicketMain.plugin.chat.sendPlayerMessage(player, HelpTicketMain.plugin.language.getNode(HelpTicketLanguageHelper.TICKET_INFO_FAIL_SESSION_INVALID));
            return true;
        }
        Ticket ticket = PlayerSession.getPlayerSession(player.getName()).getTicket();
        if ((!ticket.getOwner().equalsIgnoreCase(player.getName())) && (!HelpTicketMain.isAdmin(player))) {
            HelpTicketMain.replaceAndSend(player, HelpTicketMain.plugin.language.getNode(HelpTicketLanguageHelper.TICKET_INFO_FAIL_TICKET_INVALID), ticket);
            return true;
        }
        if (ticket.getOwner().equalsIgnoreCase(player.getName())) {
            ticket.setReadStatus(ReadStatusType.READ);
            ticket.save();
        }
        for (String s : ticket.showLongInfo(commentPage)) {
            HelpTicketMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
        }
        return true;
    }
}