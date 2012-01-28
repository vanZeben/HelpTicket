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
	private String info = "";
	private String world = null;
	private double x = 0.0;
	private double y = 0.0;
	private double z = 0.0;
	private float pitch = 0.0f;
	private float yaw = 0.0f;
	private String assignee = "";
	private ArrayList<String[]> log = new ArrayList<String[]>();
	private boolean status = false;
	private boolean hasRead = false;
	private int priority = 0;

	public Ticket(int id, String owner, String world, double x, double y,
			double z, float pitch, float yaw, String info, String assignee,
			boolean status, boolean hasRead, int priority) {
		setID(id);
		setInfo(info);
		setOwner(owner);
		setLocation(world, x, y, z, pitch, yaw);
		setInfo(info);
		setAssignee(assignee);
		setStatus(status);
		setHasRead(hasRead);
		setPriority(priority);
	}

	public Ticket(String owner, String world, double x, double y, double z,
			float pitch, float yaw, String info, String assignee,
			boolean status, boolean hasRead, int priority) {
		setInfo(info);
		setOwner(owner);
		setLocation(world, x, y, z, pitch, yaw);
		setInfo(info);
		setAssignee(assignee);
		setStatus(status);
		setHasRead(hasRead);
		setPriority(priority);
	}

	public Ticket() {
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSentence(boolean check) {
		String out = "";
		if (check) {
			out = StringMgmt.maxLength(info, 30);
		} else {
			out = info;
		}
		String tmp = "<darkgray>[";
		switch (priority) {
		case 0:
			tmp += "<darkblue>#" + this.id;
			break;
		case 1:
			tmp += "<blue>#" + this.id;
			break;
		case 2:
			tmp += "<yellow>#" + this.id;
			break;
		case 3:
			tmp += "<red>#" + this.id;
			break;
		case 4:
			tmp += "<darkred>#" + this.id;
			break;
		default:
			tmp += "<yellow>#" + this.id;
			break;
		}
		tmp += "<darkgray>]<white> "
				+ this.owner
				+ (assignee != null ? "<green> -> <white>" + this.assignee : "")
				+ "<gray>: "
				+ out
				+ (log.isEmpty() ? "" : "<darkgray> (" + log.size()
						+ (log.size() == 1 ? " Comment)" : " Comments)"));
		return tmp;
	}

	public String getHeader() {
		return ChatTools.Gold + "#" + this.id + " " + ChatTools.White
				+ this.owner + ": " + ChatTools.Gold + this.info;
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

	public ArrayList<String[]> getLog() {
		return log;
	}

	public void addLog(String player, String message) {
		String moderator = (player != null ? player : "(Console)");
		if (message.isEmpty())
			return;
		String[] tmp = { moderator, message };
		log.add(tmp);
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

	public boolean getHasRead() {
		return hasRead;
	}

	public void setHasRead(boolean hasRead) {
		this.hasRead = hasRead;
	}

	public String getPriority() {
		switch (priority) {
		case 0:
			return "lowest";
		case 1:
			return "low";
		case 2:
			return "medium";
		case 3:
			return "high";
		case 4:
			return "highest";
		}
		return "";
	}

	public int getRawPriority() {

		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
