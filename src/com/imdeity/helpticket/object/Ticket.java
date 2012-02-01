package com.imdeity.helpticket.object;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.helpticket.HelpTicket;
import com.imdeity.helpticket.db.MySQLConnector;
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

	public Ticket(int id, String owner, String world, double x, double y, double z, float pitch, float yaw, String info, String assignee, boolean status, boolean hasRead, int priority) {
		this.id = id;
		this.info = info;
		this.owner = owner;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
		this.assignee = assignee;
		this.status = status;
		this.hasRead = hasRead;
		this.priority = priority;
		this.getLogFromDB();
	}

	public Ticket(String owner, String world, double x, double y, double z, float pitch, float yaw, String info, String assignee, boolean status, boolean hasRead, int priority) {
		this.info = info;
		this.owner = owner;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
		this.assignee = assignee;
		this.status = status;
		this.hasRead = hasRead;
		this.priority = priority;
		this.getLogFromDB();
	}

	public Ticket() {
	}

	public String getInfo() {
		return info;
	}

	public String getShortInfo() {
		return StringMgmt.maxLength(info, 30);
	}

	public void setInfo(String info) {
		this.info = info;
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
		String sql = "UPDATE " + MySQLConnector.tableName("data") + " SET  `assignee` =  '" + this.getAssignee() + "' WHERE `id` = '" + this.getID() + "';";
		HelpTicket.database.Write(sql);
	}

	public String getAssignee() {
		if (this.assignee == null || this.assignee.equalsIgnoreCase("")) {
			return "None";
		} else {
			return assignee;
		}
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

	public void getLogFromDB() {
		String sql = "SELECT * FROM " + MySQLConnector.tableName("comments") + " WHERE" + "`ticket_id` = '" + this.getID() + "';";
		HashMap<Integer, ArrayList<String>> query = HelpTicket.database.Read(sql);
		for (int i = 1; i <= query.size(); i++) {
			String[] tmp = { query.get(i).get(3), query.get(i).get(4) };
			log.add(tmp);
		}
	}

	public void addLog(String player, String message) {
		String moderator = (player != null ? player : "(Console)");
		if (message.isEmpty())
			return;
		String[] tmp = { moderator, message };
		log.add(tmp);
		String sql = "INSERT INTO " + MySQLConnector.tableName("comments") + " (" + "`ticket_id`," + " `owner`," + " `commenter`," + " `comment`" + ") VALUES (?,?,?,?);";
		HelpTicket.database.Write(sql, this.getID(), this.getOwner(), moderator, message);
	}

	public Boolean isOpen() {
		return status;
	}

	public void setStatus(boolean open) {
		this.status = open;
		String sql = "UPDATE " + MySQLConnector.tableName("data") + " SET  `status` =  '" + (this.isOpen() ? 0 : 1) + "' WHERE `id` = '" + this.getID() + "';";
		HelpTicket.database.Write(sql);
	}

	public void setLocation(String world, double x, double y, double z, float pitch, float yaw) {
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
		String sql = "UPDATE " + MySQLConnector.tableName("data") + " SET " + "`has_read` = ? WHERE `id` = " + this.getID() + ";";
		HelpTicket.database.Write(sql, (this.hasRead ? 0 : 1));
	}

	public String getPriority() {
		switch (this.priority) {
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
		default:
			return "medium";
		}
	}

	public String getPriorityColor() {
		switch (this.priority) {
		case 0:
			return HelpTicket.plugin.language.getTicketPriorityLowestColor();
		case 1:
			return HelpTicket.plugin.language.getTicketPriorityLowColor();
		case 2:
			return HelpTicket.plugin.language.getTicketPriorityMediumColor();
		case 3:
			return HelpTicket.plugin.language.getTicketPriorityHighColor();
		case 4:
			return HelpTicket.plugin.language.getTicketPriorityHighestColor();
		default:
			return HelpTicket.plugin.language.getTicketPriorityMediumColor();
		}
	}

	public int getRawPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
		String sql = "UPDATE " + MySQLConnector.tableName("data") + " SET " + "`priority` = '" + priority + "' WHERE `id` = " + this.getID() + ";";
		HelpTicket.database.Write(sql);
	}

	public void setPriorityClose() {
		String sql = "UPDATE " + MySQLConnector.tableName("data") + " SET " + "`priority` = '-1' WHERE `id` = " + this.getID() + ";";
		HelpTicket.database.Write(sql);
	}

	public String getStatus() {
		return !this.isOpen() ? "Complete" : "Incomplete";
	}

	public String[] preformReplace(String replacer) {
		replacer = replacer.replaceAll("%ticketId", this.getID() + "").replaceAll("%ticketOwner", this.getOwner()).replaceAll("%ticketPriorityColor", this.getPriorityColor()).replaceAll("%ticketPriority", this.getPriority()).replaceAll("%ticketAssignee", this.getAssignee()).replaceAll("%ticketLongMessage", this.getInfo()).replaceAll("%ticketShortMessage", this.getShortInfo()).replaceAll("%ticketStatus", this.getStatus()).replaceAll("%ticketNumberComments", (log.isEmpty() ? "No Comments" : log.size() + (log.size() == 1 ? " Comment" : " Comments"))).replaceAll("%ticketWorld", this.getWorld());
		if (replacer.contains("%ticket_comments")) {
			String[] ticketComments = replacer.split("%ticket_comments");
			for (int i = 0; i < ticketComments.length; i++) {
				if (i == 0) {
					if (this.getLog().size() >= 1) {
						for (String[] s : this.getLog()) {
							ticketComments[i] += HelpTicket.plugin.language.getTicketFullInfoCommentMessage().replaceAll("%ticketCommentOwner", s[0]).replaceAll("%ticketCommentMessage", s[1]) + "%newline";
						}
					} else {
						ticketComments[i] += HelpTicket.plugin.language.getTicketFullInfoNoCommentMessage() + "%newline";
					}
				}
			}
			replacer = StringMgmt.join(ticketComments);
		}
		return replacer.split("%newline");
	}

	public String preformReplaceSingle(String replacer) {
		replacer = replacer.replaceAll("%ticketId", this.getID() + "").replaceAll("%ticketOwner", this.getOwner()).replaceAll("%ticketPriorityColor", this.getPriorityColor()).replaceAll("%ticketPriority", this.getPriority()).replaceAll("%ticketAssignee", this.getAssignee()).replaceAll("%ticketLongMessage", this.getInfo()).replaceAll("%ticketShortMessage", this.getShortInfo()).replaceAll("%ticketStatus", this.getStatus()).replaceAll("%ticketNumberComments", (log.isEmpty() ? "No Comments" : log.size() + (log.size() == 1 ? " Comment" : " Comments"))).replaceAll("%ticketWorld", this.getWorld());
		return replacer;
	}
}
