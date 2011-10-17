package com.imdeity.helpticket;

import java.io.*;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.logging.Logger;

import com.imdeity.helpticket.cmds.HelpTicketCommand;
import com.imdeity.helpticket.db.MySQLConnector;
import com.imdeity.helpticket.event.HelpTicketPlayerListener;
import com.imdeity.helpticket.utils.ChatTools;
import com.imdeity.helpticket.utils.FileMgmt;
import com.imdeity.helpticket.utils.StringMgmt;

import java.util.ArrayList;
import java.util.List;

public class HelpTicket extends JavaPlugin {

    public static final Logger logger = Logger.getLogger("minecraft");
    public static PermissionsEx Permissions = null;
    public static MySQLConnector database = null;

    public static ArrayList<Player> staff = new ArrayList<Player>();
    private HelpTicketPlayerListener playerListener = new HelpTicketPlayerListener(
            this);
    private static int taskId = -1;
    private boolean error = false;

    public void onEnable() {

        this.checkPlugins();
        if (error) {
            out("Permissions system not detected. Resorting to Op Only.");
        }
        try {
            this.loadSettings();
            this.loadDatabase();
        } catch (IOException e) {
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

    public void onDisable() {
        this.toggleTimerTask();
        out("Disabled");
    }

    private void checkPlugins() {
        List<String> using = new ArrayList<String>();
        Plugin test;
        test = getServer().getPluginManager().getPlugin("Permissions");
        if (test != null) {
            PermissionsEx tmp = (PermissionsEx) test;
            Permissions = tmp;
            if (HelpTicketSettings.isUsingPermissions())
                using.add("Permissions");
        }
        if (using.size() > 0)
            out("Using: " + StringMgmt.join(using, ", "));
    }

    public void setupCommands() {
        getCommand("ticket").setExecutor(new HelpTicketCommand(this));
    }

    public void setupEvents() {
        this.getServer()
                .getPluginManager()
                .registerEvent(Event.Type.PLAYER_JOIN, playerListener,
                        Priority.Normal, this);
        this.getServer()
                .getPluginManager()
                .registerEvent(Event.Type.PLAYER_QUIT, playerListener,
                        Priority.Low, this);
    }

    public void loadDatabase() {
        database = new MySQLConnector(this);
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
            taskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new TicketTimerTask(this), 0, (20 * 60 * HelpTicketSettings.getNotificationTimer())); 
        }
    }
    
    public void loadSettings() throws IOException {
        FileMgmt.checkFolders(new String[] {getRootFolder()});
        HelpTicketSettings.loadConfig(
                getRootFolder() + FileMgmt.fileSeparator() + "config.yml",
                "/config.yml");

    }

    public String getRootFolder() {
        if (this != null)
            return this.getDataFolder().getPath();
        else
            return "";
    }

    public void informPlayer(String playerName, String msg) {
        Player player = getServer().getPlayer(playerName);
        if (player != null && player.isOnline())
            ChatTools.formatAndSend("<option>"+msg, "HelpTicket", player);
        return;
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
    
    public static void informStaff(String msg) {
        for (Player player : getOnlineStaff()) {
            ChatTools.formatAndSend("<option>"+msg, "HelpTicket", player);
        }
    }
    
    public boolean isStaff(Player player) {
        if (player == null) {
            return false;
        }
        if (HelpTicketSettings.isUsingPermissions()) {
            if (PermissionsEx.has(player, "helpticket.mod")
                    || PermissionsEx.has(player, "helpticket.admin")
                    || player.isOp()) {
                return true;
            }
        } else {
            if (player.isOp())
                return true;
        }
        return false;
    }
    
    public boolean isStaff(String playerName) {
        if (this.getServer().getPlayer(playerName) == null) {
            return false;
        }
        if (HelpTicketSettings.isUsingPermissions()) {
            
            if (PermissionsEx.has(this.getServer().getPlayer(playerName), "helpticket.mod")
                    || PermissionsEx.has(this.getServer().getPlayer(playerName), "helpticket.admin")
                    || this.getServer().getPlayer(playerName).isOp()) {
                return true;
            }
        } else {
            if ((this.getServer().getPlayer(playerName)).isOp())
                return true;
        }
        return false;
    }

    public World getWorld(String name) {
        return getServer().getWorld(name);
    }
}
