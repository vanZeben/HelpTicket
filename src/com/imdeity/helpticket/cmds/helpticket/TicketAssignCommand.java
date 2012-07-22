package com.imdeity.helpticket.cmds.helpticket;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.enums.ReadStatusType;
import com.imdeity.helpticket.obj.PlayerSession;
import com.imdeity.helpticket.obj.Ticket;

public class TicketAssignCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 1) return false;
        if (PlayerSession.getPlayerSession(player.getName()) == null) {
            HelpTicketMain.plugin.chat.sendPlayerMessage(player, HelpTicketMain.plugin.language.getNode("helpticket.commands.info.fail.session_invalid"));
            return true;
        }
        Ticket ticket = PlayerSession.getPlayerSession(player.getName()).getTicket();
        ticket.setAssignee(args[0]);
        ticket.setReadStatus(ReadStatusType.UNREAD);
        ticket.save();
        HelpTicketMain.replaceAndSend(player, "helpticket.commands.assign.success", ticket);
        if ((!player.getName().equalsIgnoreCase(ticket.getOwner())) && (ticket.getPlayerOwner() != null) && (ticket.getPlayerOwner().isOnline())) {
            HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), "helpticket.commands.create.fail", ticket);
        }
        return true;
    }
}