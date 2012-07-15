package com.imdeity.helpticket;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.helpticket.cmds.HelpTicketCommand;
import com.imdeity.helpticket.db.MySQLConnector;
import com.imdeity.helpticket.event.HelpTicketPlayerListener;
import com.imdeity.helpticket.object.Language;
import com.imdeity.helpticket.object.Settings;
import com.imdeity.helpticket.utils.ChatTools;
import com.imdeity.helpticket.utils.StringMgmt;
import com.imdeity.mail.Mail;

public class HelpTicket extends JavaPlugin {
    
    public static final Logger logger = Logger.getLogger("minecraft");
    public static MySQLConnector database = null;
    public Mail mail = null;
    public static HelpTicket plugin = null;
    public Language language = null;
    public Settings config = null;
    public static ArrayList<Player> staff = new ArrayList<Player>();
    private HelpTicketPlayerListener playerListener = new HelpTicketPlayerListener(this);
    private static int taskId = -1;
    
    public void onEnable() {
        HelpTicket.plugin = this;
        try {
            this.checkPlugins();
            this.loadSettings();
            this.loadDatabase();
        } catch (Exception e) {
            out("Error:" + e.getMessage() + ", Disabling Plugin.");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.toggleTimerTask();
        this.setupEvents();
        this.setupCommands();
        out("Enabled");
    }
    
    private void checkPlugins() {
        List<String> using = new ArrayList<String>();
        Plugin test = getServer().getPluginManager().getPlugin("Mail");
        if (test != null) {
            mail = (Mail) test;
            using.add("Mail");
            if (using.size() > 0) out("Using: " + StringMgmt.join(using, ", "));
        }
    }
    
    public void onDisable() {
        this.toggleTimerTask();
        out("Disabled");
    }
    
    public void setupCommands() {
        getCommand("ticket").setExecutor(new HelpTicketCommand(this));
    }
    
    public void setupEvents() {
        this.getServer().getPluginManager().registerEvents(playerListener, this);
    }
    
    public void loadSettings() throws IOException {
        this.language = new Language();
        language.loadDefaults();
        
        this.config = new Settings();
        config.loadDefaults();
    }
    
    public void loadDatabase() throws Exception {
        database = new MySQLConnector();
        database.validataDatabaseTables();
    }
    
    public void out(String message) {
        PluginDescriptionFile pdfFile = this.getDescription();
        logger.info("[" + pdfFile.getName() + "] " + message);
    }
    
    public void toggleTimerTask() {
        if (taskId != -1) {
            getServer().getScheduler().cancelTask(taskId);
        } else {
            taskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new TicketTimerTask(this), 0, (20 * 60 * this.config.getNotifyDelay()));
        }
    }
    
    public String getRootFolder() {
        if (this != null) return this.getDataFolder().getPath();
        else return "";
    }
    
    public void sendMailToPlayer(String playerName, String msg) {
        if (this.mail != null) {
            try {
                Mail.sendMailToPlayer(this.language.getMailSender(), playerName, msg);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return;
    }
    
    public void sendPlayerMessage(String playerName, String msg) {
        Player player = getServer().getPlayer(playerName);
        if (player != null && player.isOnline()) {
            ChatTools.formatAndSend(this.language.getHeader() + msg, player);
        }
    }
    
    public static ArrayList<Player> getOnlineStaff() {
        return HelpTicket.staff;
    }
    
    public void addToStaff(Player player) {
        HelpTicket.staff.add(player);
    }
    
    public void removeFromStaff(Player player) {
        HelpTicket.staff.remove(player);
    }
    
    public void informStaff(String msg) {
        for (Player player : getOnlineStaff()) {
            ChatTools.formatAndSend(this.language.getHeader() + msg, player);
        }
    }
    
    public boolean isStaff(Player player) {
        if (player == null) { return false; }
        if (player.hasPermission("helpticket.mod") || player.hasPermission("helpticket.admin") || player.isOp()) { return true; }
        return false;
    }
    
    public boolean isStaff(String playerName) {
        if (this.getServer().getPlayer(playerName) == null) { return false; }
        if (this.getServer().getPlayer(playerName).hasPermission("helpticket.mod") || this.getServer().getPlayer(playerName).hasPermission("helpticket.admin") || this.getServer().getPlayer(playerName).isOp()) { return true; }
        return false;
    }
    
    public boolean isAdmin(Player player) {
        if (player == null) { return false; }
        if (player.hasPermission("helpticket.admin") || player.isOp()) { return true; }
        return false;
    }
    
    public World getWorld(String name) {
        return getServer().getWorld(name);
    }
}