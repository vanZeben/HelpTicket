package com.imdeity.helpticket.object;

import java.util.ArrayList;
import java.util.HashMap;

import com.imdeity.helpticket.HelpTicket;
import com.imdeity.helpticket.db.MySQLConnector;

public class SQLTicket {

	public static Ticket getSpecificTicket(int instanceId) {
		String sql = "";

		int id = instanceId;
		String owner = "";
		String world = null;
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		float pitch = 0.0f;
		float yaw = 0.0f;
		String info = "";
		String assignee = "";
		boolean status = false;
		boolean hasRead = false;
		int priority;

		sql = "SELECT * FROM " + MySQLConnector.tableName("data")
				+ " WHERE `id` = '" + id + "';";

		HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);

		if (out.isEmpty()) {
			return null;
		}
		try {
			owner = out.get(1).get(1);
			world = out.get(1).get(2);
			x = Double.parseDouble(out.get(1).get(3));
			y = Double.parseDouble(out.get(1).get(4));
			z = Double.parseDouble(out.get(1).get(5));
			pitch = Float.parseFloat(out.get(1).get(6));
			yaw = Float.parseFloat(out.get(1).get(7));
			info = out.get(1).get(8);
			assignee = out.get(1).get(9);
			status = (Integer.parseInt(out.get(1).get(10)) == 1 ? false : true);
			hasRead = (Integer.parseInt(out.get(1).get(11)) == 1 ? false : true);
			priority = Integer.parseInt(out.get(1).get(13));
		} catch (NumberFormatException ex) {
			System.out.println("[HelpTicket] Input Mismatch on id of " + id);
			ex.printStackTrace();
			return null;
		}

		Ticket tmp = new Ticket(id, owner, world, x, y, z, pitch, yaw, info,
				assignee, status, hasRead, priority);
		return tmp;
	}

	public static Ticket getSpecificTicket(int instanceId, String playerName) {
		String sql = "";

		int id = instanceId;
		String owner = "";
		String world = null;
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		float pitch = 0.0f;
		float yaw = 0.0f;
		String info = "";
		String assignee = "";
		boolean status = false;
		boolean hasRead = false;
		int priority;

		sql = "SELECT * FROM " + MySQLConnector.tableName("data")
				+ " WHERE `id` = '" + id + "' && `owner` = '" + playerName
				+ "';";

		HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);

		if (out.isEmpty()) {
			return null;
		}
		try {
			owner = out.get(1).get(1);
			world = out.get(1).get(2);
			x = Double.parseDouble(out.get(1).get(3));
			y = Double.parseDouble(out.get(1).get(4));
			z = Double.parseDouble(out.get(1).get(5));
			pitch = Float.parseFloat(out.get(1).get(6));
			yaw = Float.parseFloat(out.get(1).get(7));
			info = out.get(1).get(8);
			assignee = out.get(1).get(9);
			status = (Integer.parseInt(out.get(1).get(10)) == 1 ? false : true);
			hasRead = (Integer.parseInt(out.get(1).get(11)) == 1 ? false : true);
			priority = Integer.parseInt(out.get(1).get(13));
		} catch (NumberFormatException ex) {
			System.out.println("[HelpTicket] Input Mismatch on id of " + id);
			ex.printStackTrace();
			return null;
		}

		Ticket tmp = new Ticket(id, owner, world, x, y, z, pitch, yaw, info,
				assignee, status, hasRead, priority);
		return tmp;
	}

	public static ArrayList<Ticket> getPlayersOpenTickets(String playerName) {
		String sql = "";
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();

		int id = 0;
		String owner = "";
		String world = null;
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		float pitch = 0.0f;
		float yaw = 0.0f;
		String info = "";
		String assignee = "";
		boolean status = false;
		boolean hasRead = false;
		int priority;

		sql = "SELECT * FROM " + MySQLConnector.tableName("data")
				+ " WHERE `owner` = '" + playerName + "' && `status` = '0';";

		HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);
		for (int i = 1; i <= out.size(); i++) {
			try {
				id = Integer.parseInt(out.get(i).get(0));
				owner = out.get(i).get(1);
				world = out.get(i).get(2);
				x = Double.parseDouble(out.get(i).get(3));
				y = Double.parseDouble(out.get(i).get(4));
				z = Double.parseDouble(out.get(i).get(5));
				pitch = Float.parseFloat(out.get(i).get(6));
				yaw = Float.parseFloat(out.get(i).get(7));
				info = out.get(i).get(8);
				assignee = out.get(i).get(9);
				status = (Integer.parseInt(out.get(1).get(10)) == 1 ? false
						: true);
				hasRead = (Integer.parseInt(out.get(1).get(11)) == 1 ? false
						: true);
				priority = Integer.parseInt(out.get(1).get(13));
				tickets.add(new Ticket(id, owner, world, x, y, z, pitch, yaw,
						info, assignee, status, hasRead, priority));
			} catch (NumberFormatException ex) {
				System.out
						.println("[HelpTicket] Input Mismatch on id of " + id);
				ex.printStackTrace();
				return null;
			}
		}

		return tickets;
	}

	public static ArrayList<Ticket> getPlayersTickets(String playerName) {
		String sql = "";
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();

		int id = 0;
		String owner = "";
		String world = null;
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		float pitch = 0.0f;
		float yaw = 0.0f;
		String info = "";
		String assignee = "";
		boolean status = false;
		boolean hasRead = false;
		int priority;

		sql = "SELECT * FROM " + MySQLConnector.tableName("data")
				+ " WHERE `owner` = '" + playerName + "';";

		HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);
		for (int i = 1; i <= out.size(); i++) {
			try {
				id = Integer.parseInt(out.get(i).get(0));
				owner = out.get(i).get(1);
				world = out.get(i).get(2);
				x = Double.parseDouble(out.get(i).get(3));
				y = Double.parseDouble(out.get(i).get(4));
				z = Double.parseDouble(out.get(i).get(5));
				pitch = Float.parseFloat(out.get(i).get(6));
				yaw = Float.parseFloat(out.get(i).get(7));
				info = out.get(i).get(8);
				assignee = out.get(i).get(9);
				status = (Integer.parseInt(out.get(1).get(10)) == 1 ? false
						: true);
				hasRead = (Integer.parseInt(out.get(1).get(11)) == 1 ? false
						: true);
				priority = Integer.parseInt(out.get(1).get(13));
				tickets.add(new Ticket(id, owner, world, x, y, z, pitch, yaw,
						info, assignee, status, hasRead, priority));
			} catch (NumberFormatException ex) {
				System.out
						.println("[HelpTicket] Input Mismatch on id of " + id);
				ex.printStackTrace();
				return null;
			}
		}

		return tickets;
	}

	public static ArrayList<Ticket> getAllOpenTickets() {
		String sql = "";
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();

		int id = 0;
		String owner = "";
		String world = null;
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		float pitch = 0.0f;
		float yaw = 0.0f;
		String info = "";
		String assignee = "";
		boolean status = false;
		boolean hasRead = false;
		int priority;

		sql = "SELECT * FROM " + MySQLConnector.tableName("data")
				+ " WHERE `status` = '0';";

		HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);
		for (int i = 1; i <= out.size(); i++) {
			try {
				id = Integer.parseInt(out.get(i).get(0));
				owner = out.get(i).get(1);
				world = out.get(i).get(2);
				x = Double.parseDouble(out.get(i).get(3));
				y = Double.parseDouble(out.get(i).get(4));
				z = Double.parseDouble(out.get(i).get(5));
				pitch = Float.parseFloat(out.get(i).get(6));
				yaw = Float.parseFloat(out.get(i).get(7));
				info = out.get(i).get(8);
				assignee = out.get(i).get(9);
				status = (Integer.parseInt(out.get(1).get(10)) == 1 ? false
						: true);
				hasRead = (Integer.parseInt(out.get(1).get(11)) == 1 ? false
						: true);
				priority = Integer.parseInt(out.get(1).get(13));
				Ticket t = new Ticket(id, owner, world, x, y, z, pitch, yaw,
						info, assignee, status, hasRead, priority);
				tickets.add(t);
			} catch (NumberFormatException ex) {
				System.out
						.println("[HelpTicket] Input Mismatch on id of " + id);
				ex.printStackTrace();
				return null;
			}
		}

		return tickets;
	}

	public static int getNewestTicketID(String playerName) {
		String sql = "";

		int id = 0;
		String owner = playerName;

		sql = "SELECT `id` FROM " + MySQLConnector.tableName("data")
				+ " WHERE `owner` = '" + owner
				+ "' ORDER BY `id` DESC LIMIT 1;";

		HashMap<Integer, ArrayList<String>> out = HelpTicket.database.Read(sql);

		try {
			if (!out.isEmpty()) {
				id = Integer.parseInt(out.get(1).get(0));
			} else {
				id = 1;
			}

		} catch (NumberFormatException ex) {
			System.out.println("[HelpTicket] Input Mismatch on id of " + id);
			return -1;
		}

		return id;
	}

	public static boolean newTicket(Ticket ticket) {
		String sql = "";

		sql = "INSERT INTO " + MySQLConnector.tableName("data") + " ("
				+ "`owner`," + " `world`," + " `x_coord`," + " `y_coord`,"
				+ " `z_coord`," + " `pitch`," + " `yaw`," + " `status`,"
				+ " `info`, " + " `creation_time`,"
				+ "`priority` ) VALUES (?,?,?,?,?,?,?,?,?, NOW(), 2);";

		HelpTicket.database.Write(sql, ticket.getOwner(), ticket.getWorld(),
				ticket.getX(), ticket.getY(), ticket.getZ(), ticket.getPitch(),
				ticket.getYaw(), 0, ticket.getInfo());
		return true;
	}


	public static void setPriority(Ticket ticket) {
		String sql = "";

		sql = "SELECT * FROM " + MySQLConnector.tableName("data") + " WHERE"
				+ "`id` = '" + ticket.getID() + "';";
		HashMap<Integer, ArrayList<String>> query = HelpTicket.database
				.Read(sql);
		ticket.setPriority(Integer.parseInt(query.get(1).get(13)));
	}

	public static void setHasRead(Ticket ticket) {
		String sql = "";

		sql = "SELECT * FROM " + MySQLConnector.tableName("data") + " WHERE"
				+ "`id` = '" + ticket.getID() + "';";
		HashMap<Integer, ArrayList<String>> query = HelpTicket.database
				.Read(sql);

		for (int i = 1; i <= query.size(); i++) {
			ticket.setHasRead(Integer.parseInt(query.get(1).get(11)) == 1 ? false
					: true);
		}
	}

}
