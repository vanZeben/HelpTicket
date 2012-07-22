package com.imdeity.helpticket.cmds.helpticket;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.obj.PlayerSession;
import com.imdeity.helpticket.obj.Ticket;

public class TicketTpCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (PlayerSession.getPlayerSession(player.getName()) == null) {
            HelpTicketMain.plugin.chat.sendPlayerMessage(player, HelpTicketMain.plugin.language.getNode("helpticket.commands.info.fail.session_invalid"));
            return true;
        }
        Ticket ticket = PlayerSession.getPlayerSession(player.getName()).getTicket();
        player.teleport(ticket.getCreationLocation());
        return true;
    }
}