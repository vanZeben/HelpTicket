package com.imdeity.helpticket.cmds.helpticket;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketLanguageHelper;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.obj.PlayerSession;
import com.imdeity.helpticket.obj.Ticket;
import com.imdeity.helpticket.obj.TicketManager;

public class TicketCreateCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        String info = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(args);
        new CreateTicket(player, info);
        return true;
    }
    
    public class CreateTicket implements Runnable {
        private Player player;
        private String info;
        
        public CreateTicket(Player player, String info) {
            this.player = player;
            this.info = info;
            HelpTicketMain.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(HelpTicketMain.plugin, this);
        }
        
        public void run() {
            Ticket ticket = TicketManager.getTicketFromPlayer(this.player.getName(), this.info);
            if (ticket == null) {
                ticket = TicketManager.addNewTicket(this.player.getName(), this.player.getLocation(), this.info);
                PlayerSession.addPlayerSession(player.getName(), ticket.getId());
                HelpTicketMain.replaceAndSend(this.player, HelpTicketLanguageHelper.TICKET_CREATE_STAFF, ticket);
                for (Player p : HelpTicketMain.plugin.getServer().getOnlinePlayers()) {
                    if (HelpTicketMain.isAdmin(p) && !this.player.getName().equalsIgnoreCase(p.getName())) {
                        HelpTicketMain.replaceAndSend(p, HelpTicketLanguageHelper.TICKET_CREATE_STAFF, ticket);
                    }
                }
            } else {
                HelpTicketMain.replaceAndSend(this.player, HelpTicketLanguageHelper.TICKET_CREATE_FAIL, ticket);
            }
        }
    }
}