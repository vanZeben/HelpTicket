package com.imdeity.helpticket.cmds.helpticket;

import java.util.regex.Matcher;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.obj.PlayerSession;

public class TicketSelectCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 1) { return false; }
        int ticketId;
        try {
            ticketId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            ticketId = -1;
        }
        PlayerSession session = PlayerSession.addPlayerSession(player.getName(), ticketId);
        if (session.getTicket() == null) {
            PlayerSession.removePlayerSession(player.getName());
            HelpTicketMain.plugin.chat.sendPlayerMessage(player, HelpTicketMain.plugin.language.getNode("helpticket.commands.select.fail").replaceAll("%ticketId%", Matcher.quoteReplacement(ticketId + "")));
            return true;
        }
        if ((!player.getName().equalsIgnoreCase(session.getTicket().getOwner())) && (!session.getTicket().getOwner().equalsIgnoreCase(player.getName())) && (!HelpTicketMain.isAdmin(player))) {
            PlayerSession.removePlayerSession(player.getName());
            HelpTicketMain.plugin.chat.sendPlayerMessage(player, HelpTicketMain.plugin.language.getNode("helpticket.commands.select.fail").replaceAll("%ticketId%", Matcher.quoteReplacement(ticketId + "")));
            return true;
        }
        HelpTicketMain.replaceAndSend(player, "helpticket.commands.select.success", session.getTicket());
        return true;
    }
}