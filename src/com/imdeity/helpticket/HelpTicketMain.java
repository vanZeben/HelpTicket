package com.imdeity.helpticket;

import java.util.regex.Matcher;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityPlugin;
import com.imdeity.helpticket.cmds.TicketCommandHandler;
import com.imdeity.helpticket.events.HelpTicketListener;
import com.imdeity.helpticket.obj.Ticket;
import com.imdeity.helpticket.obj.TicketManager;

public class HelpTicketMain extends DeityPlugin {
    public static HelpTicketMain plugin;
    private final TicketCommandHandler ticketCommandHandler = new TicketCommandHandler("HelpTicket");
    
    protected void initConfig() {
        this.config.addDefaultConfigValue("helpticket.mysql.server.address", "localhost");
        this.config.addDefaultConfigValue("helpticket.mysql.server.port", Integer.valueOf(3306));
        this.config.addDefaultConfigValue("helpticket.mysql.database.password", "root");
        this.config.addDefaultConfigValue("helpticket.mysql.database.username", "root");
        this.config.addDefaultConfigValue("helpticket.mysql.database.name", "kingdoms");
    }
    
    protected void initLanguage() {
        this.language.addDefaultLanguageValue("helpticket.login_message", "Ticket %ticket_id% has been updated");
        this.language.addDefaultLanguageValue("helpticket.commands.create.success", "Your ticket has been submitted");
        this.language.addDefaultLanguageValue("helpticket.commands.create.fail", "Instead of resubmitting that ticket. Please use &6/ticket info %ticket_id%");
        this.language.addDefaultLanguageValue("helpticket.commands.select.success", "You have selected ticket %ticket_id%");
        this.language.addDefaultLanguageValue("helpticket.commands.select.fail", "Ticket %ticketId% does not exist");
        this.language.addDefaultLanguageValue("helpticket.commands.info.fail.session_invalid", "Before you can use this command you first must &c/ticket select [ticket-id]");
        this.language.addDefaultLanguageValue("helpticket.commands.info.fail.ticket_invalid", "Ticket %ticket_id% does not exist");
        this.language.addDefaultLanguageValue("helpticket.commands.priority.fail.priority_too_high", "This ticket already has the manimum priority");
        this.language.addDefaultLanguageValue("helpticket.commands.priority.fail.priority_too_low", "This ticket already has the lowest priority");
        this.language.addDefaultLanguageValue("helpticket.commands.priority.success", "Ticket %ticket_id%'s priority was changed to %ticket_priority%");
        this.language.addDefaultLanguageValue("helpticket.commands.assign.success", "Ticket %ticket_id% was assigned to %assignee%");
        this.language.addDefaultLanguageValue("helpticket.commands.close.success", "Ticket %ticket_id% was closed");
        this.language.addDefaultLanguageValue("helpticket.commands.comment.success", "Ticket %ticket_id% had a comment added to it");
    }
    
    protected void initCmds() {
        registerCommand(this.ticketCommandHandler);
    }
    
    protected void initListeners() {
        registerListener(new HelpTicketListener());
    }
    
    protected void initDatabase() {
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getTicketsTableName() + " (" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT, " + "`owner` VARCHAR(20) NOT NULL, " + "`location_id` INT( 16 ) NOT NULL, " + "`info` TEXT NOT NULL, " + "`assignee` VARCHAR(40) NULL DEFAULT NULL, "
                        + "`priority` VARCHAR(8) NOT NULL DEFAULT 'MEDIUM', " + "`read_status` VARCHAR(8) NOT NULL DEFAULT 'READ', " + "`open_status` VARCHAR(8) NOT NULL DEFAULT 'OPEN', " + "`creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + "PRIMARY KEY (`id`)," + "INDEX (`owner`))"
                        + " ENGINE = MYISAM;", new Object[0]);
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getTicketLocationsTableName() + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, " + " `world` VARCHAR(64) NOT NULL, " + " `x_coord` INT(16) NOT NULL, " + " `y_coord` INT(16) NOT NULL, " + " `z_coord` INT(16) NOT NULL, "
                        + " `pitch` INT(16) NOT NULL, " + " `yaw` INT(16) NOT NULL, " + " `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + " PRIMARY KEY (`id`) " + ") ENGINE = MYISAM;", new Object[0]);
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getTicketCommentsTableName() + " (" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT ," + "`ticket_id` INT( 16 ) NOT NULL DEFAULT '0'," + "`commenter` VARCHAR( 16 ) NOT NULL ," + "`comment` TEXT NOT NULL,"
                        + "`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + "PRIMARY KEY (`id`)," + "INDEX (`ticket_id`)" + ") ENGINE = MYISAM;", new Object[0]);
    }
    
    protected void initTasks() {
    }
    
    protected void initInternalDatamembers() {
        TicketManager.loadAllTickets();
    }
    
    protected void initPlugin() {
        plugin = this;
    }
    
    public static String getTicketsTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("helpticket_", "tickets");
    }
    
    public static String getTicketCommentsTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("helpticket_", "comments");
    }
    
    public static String getTicketLocationsTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("helpticket_", "locations");
    }
    
    public static boolean isAdmin(Player player) {
        return (player.hasPermission("HelpTicket.admin")) || (player.isOp());
    }
    
    public static void replaceAndSend(Player player, String node, Ticket ticket) {
        if ((ticket == null) || (plugin.language.getNode(node) == null)) {
            plugin.chat.sendPlayerMessage(player, "An error has occured, Please consult an admin");
            return;
        }
        plugin.chat.sendPlayerMessage(
                player,
                plugin.language.getNode(node).replaceAll("%ticket_id%", Matcher.quoteReplacement(ticket.getId() + "")).replaceAll("%ticket_owner%", Matcher.quoteReplacement(ticket.getOwner()))
                        .replaceAll("%ticket_assignee%", Matcher.quoteReplacement(ticket.getAssignee() == null ? "" : ticket.getAssignee())).replaceAll("%ticket_info%", Matcher.quoteReplacement(ticket.getInfo())).replaceAll("%ticket_priority%", Matcher.quoteReplacement(ticket.getPriority().name()))
                        .replaceAll("%ticket_open_status%", Matcher.quoteReplacement(ticket.getOpenStatus().name())).replaceAll("%ticket_read_status%", Matcher.quoteReplacement(ticket.getReadStatus().name()))
                        .replaceAll("%ticket_creation_date%", Matcher.quoteReplacement(ticket.getFormattedCreationDate())).replaceAll("%ticket_comments_number%", Matcher.quoteReplacement(ticket.getComments().size() + "")));
    }
}