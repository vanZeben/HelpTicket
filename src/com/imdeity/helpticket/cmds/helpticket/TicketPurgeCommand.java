package com.imdeity.helpticket.cmds.helpticket;

import java.util.regex.Matcher;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketLanguageHelper;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.obj.TicketManager;

public class TicketPurgeCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length == 0) { return false; }
        TicketManager.removeAllTicketsFromPlayer(args[0]);
        HelpTicketMain.plugin.chat.out(HelpTicketMain.plugin.language.getNode(HelpTicketLanguageHelper.TICKET_PURGE_SUCCESS)
                .replaceAll("%player%", Matcher.quoteReplacement(args[0])));
        return true;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length == 0) { return false; }
        TicketManager.removeAllTicketsFromPlayer(args[0]);
        HelpTicketMain.plugin.chat.sendPlayerMessage(
                player,
                HelpTicketMain.plugin.language.getNode(HelpTicketLanguageHelper.TICKET_PURGE_SUCCESS).replaceAll("%player%",
                        Matcher.quoteReplacement(args[0])));
        return true;
    }
}