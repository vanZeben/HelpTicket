package com.imdeity.helpticket.object;

import java.util.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.helpticket.utils.ChatTools;
import com.imdeity.helpticket.utils.StringMgmt;

public class Ticket {

    private int id = 0;
    private String owner = "";
    private String title = "";
    private String world = null;
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;
    private float pitch = 0.0f;
    private float yaw = 0.0f;
    private String assignee = "";
    private ArrayList<String> log = new ArrayList<String>();
    private boolean status = false;

    public Ticket(int id, String owner, String world, double x, double y,
            double z, float pitch, float yaw, String title, String assignee,
            boolean status) {
        setID(id);
        setTitle(title);
        setOwner(owner);
        setLocation(world, x, y, z, pitch, yaw);
        setTitle(title);
        setAssignee(assignee);
        setStatus(status);
    }

    public Ticket(String owner, String world, double x, double y, double z,
            float pitch, float yaw, String title, String assignee,
            boolean status) {
        setTitle(title);
        setOwner(owner);
        setLocation(world, x, y, z, pitch, yaw);
        setTitle(title);
        setAssignee(assignee);
        setStatus(status);
    }

    public Ticket() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSentence(boolean check) {
        String out = "";
        if (check) {
            out = StringMgmt.maxLength(title, 20);
        } else {
            out = title;
        }
        return ChatTools.Gold
                + "#"
                + this.id
                + " "
                + ChatTools.White
                + this.owner
                + (assignee != null ? ChatTools.Green + " -> "
                        + ChatTools.White + this.assignee : "") + ": "
                + ChatTools.LightGray + out;
    }

    public String getHeader() {

        return ChatTools.Gold + "#" + this.id + " " + ChatTools.White
                + this.owner + ": " + ChatTools.Gold + this.title;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(Player player) {
        world = player.getWorld().getName();
    }

    public ArrayList<String> getLog() {
        return log;
    }

    public void addLog(Player player, String message) {
        String moderator = (player != null ? player.getName() : "(Console)");

        log.add((message.equals("") ? "" : moderator + ": " + message));
    }

    public Boolean isOpen() {
        return status;
    }

    public void setStatus(boolean open) {
        this.status = open;
    }

    public void setLocation(String world, double x, double y, double z,
            float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Location getLocation(World world) {
        return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }
}
