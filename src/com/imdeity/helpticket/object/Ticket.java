package com.imdeity.helpticket.object;

import java.util.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.helpticket.HelpTicket;
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

	public String getShortInfo() {
		return StringMgmt.maxLength(info, 30);
	}

	public void setInfo(String info) {
		this.info = info;
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
		default:
			return "medium";
		}
	}

	public String getPriorityColor() {
		switch (priority) {
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
	}

	public String getStatus() {
		return !this.isOpen() ? "Complete" : "Incomplete";
	}

	public String[] preformReplace(String replacer) {
		replacer = replacer.replaceAll("%ticketId", this.getID() + "")
				.replaceAll("%ticketOwner", this.getOwner())
				.replaceAll("%ticketPriority", this.getPriority())
				.replaceAll("%ticketPriorityColor",
						this.getPriorityColor())
				.replaceAll("%ticketAssignee", this.getAssignee())
				.replaceAll("%ticketFullMessage", this.getInfo())
				.replaceAll("%ticketShortMessage", this.getShortInfo())
				.replaceAll("%ticketStatus", this.getStatus())
				.replaceAll(
						"%ticketNumberComments",
						(log.isEmpty() ? "No Comments" : log.size()
								+ (log.size() == 1 ? " Comment" : " Comments")))
				.replaceAll("%ticketWorld", this.getWorld());
		if (replacer.contains("%comments")) {
			String[] ticketComments = replacer.split("%comments");
			for (int i = 0; i < ticketComments.length; i++) {
				if (i == 0) {
					if (this.getLog().size() >= 1) {
						for (String[] s : this.getLog()) {
							ticketComments[i] += HelpTicket.plugin.language
									.getTicketFullInfoCommentMessage()
									.replaceAll("%commentOwner", s[0])
									.replaceAll("%commentMessage", s[1])
									+ "\n";
						}
					} else {
						ticketComments[i] += HelpTicket.plugin.language
								.getTicketFullInfoNoCommentMessage() + "\n";
					}
				}
			}
			replacer = StringMgmt.join(ticketComments);
		}
		return replacer.split("\n");
	}
}
