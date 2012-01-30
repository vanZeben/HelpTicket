package com.imdeity.helpticket.object;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class Language {

	public static YamlConfiguration lang = new YamlConfiguration();

	public void loadDefaults() {
		lang = YamlConfiguration.loadConfiguration(new File(
				"plugins/HelpTicket/language.yml"));
		if (!lang.contains("header")) {
			lang.set("header", "&7[&cHelpTicket&7] ");
		}
		if (!lang.contains("mail_sender")) {
			lang.set("mail_sender", "HelpTicketUpdate ");
		}
		if (!lang.contains("auto_staff_message.singular")) {
			lang.set("auto_staff_message.singular",
					"&7There is &e %numTicket &7open ticket.");
		}
		if (!lang.contains("auto_staff_message.plural")) {
			lang.set("auto_staff_message.plural",
					"&7There are &e %numTicket &7open tickets.");
		}
		if (!lang.contains("auto_staff_message.attend.singular")) {
			lang.set("auto_staff_message.attend.singular",
					"&7Please attend to it with &e\"/ticket\".");
		}
		if (!lang.contains("auto_staff_message.attend.plural")) {
			lang.set("auto_staff_message.attend.plural",
					"&7Please attend to them with &e\"/ticket\".");
		}
		if (!lang.contains("not_staff")) {
			lang.set("not_staff",
					"&cYou need to be at least a moderator to perform this action");
		}
		if (!lang.contains("ticket.submitted.player")) {
			lang.set(
					"ticket.submitted.player",
					"&aYour Ticket has been submitted. You can check it with &b\"/ticket view %ticketId\"");
		}
		if (!lang.contains("ticket.submitted.staff")) {
			lang.set("ticket.submitted.staff",
					"&c%player has opened a new ticket &7[%ticketId]&c. Please attend to it");
		}
		if (!lang.contains("ticket.not_exist")) {
			lang.set("ticket.not_exist", "&cTicket #%ticketId does not exist");
		}
		if (!lang.contains("ticket.short_info")) {
			lang.set(
					"ticket.short_info",
					"&7[%ticketId] &f%ticketOwner -> %ticketAssignee&f: &7%ticketShortMessage &7[%ticketNumberComments]");
		}
		if (!lang.contains("ticket.full_info.header")) {

			lang.set("ticket.full_info.header", "&c--[Ticket Information]--");
		}
		if (!lang.contains("ticket.full_info.id")) {
			lang.set("ticket.full_info.id", "&3Id: &b%ticketId");
		}
		if (!lang.contains("ticket.full_info.owner")) {
			lang.set("ticket.full_info.owner", "&3Owner: &b%ticketOwner");
		}
		if (!lang.contains("ticket.full_info.assignee")) {
			lang.set("ticket.full_info.assignee",
					"&3Assigned To: &b%ticketAssignee");
		}
		if (!lang.contains("ticket.full_info.priority")) {
			lang.set("ticket.full_info.priority",
					"&3Priority: &b%ticketPriority");
		}
		if (!lang.contains("ticket.full_info.message")) {
			lang.set("ticket.full_info.message",
					"&3Situation: &b%ticketLongMessage");
		}
		if (!lang.contains("ticket.full_info.status")) {
			lang.set("ticket.full_info.status", "&3Status: &b%ticketStatus");
		}
		if (!lang.contains("ticket.full_info.comments.header")) {
			lang.set("ticket.full_info.comments.header", "&3Comments:");
		}
		if (!lang.contains("ticket.full_info.comments.message")) {
			lang.set("ticket.full_info.comments.message",
					"&b=> &f%commentOwner: &7%commentMessage");
		}
		if (!lang.contains("ticket.full_info.comments.none")) {
			lang.set("ticket.full_info.comments.none",
					"&bNo Comments To Display");
		}
		if (!lang.contains("ticket.full_info.order")) {
			lang.set(
					"ticket.full_info.order",
					"%headerLine%newline%idLine%newline%ownerLine%newline%assigneeLine%newline%priorityLine%newline%messageLine%newline%statusLine%newline%commentHeader%newline%comments");
		}
		if (!lang.contains("ticket.open_ticket.player")) {
			lang.set("ticket.open_ticket.player", "&3Your Open Tickets:");
		}
		if (!lang.contains("ticket.open_ticket.staff")) {
			lang.set("ticket.open_ticket.staff", "&3Open Tickets:");
		}
		if (!lang.contains("ticket.priority.coding")) {
			lang.set("ticket.priority.coding",
					"&7[&1lowest&7, &9low&7, &emedium&7, &chigh&7, &4highest&7]");
		}
		if (!lang.contains("ticket.priority.lowest")) {
			lang.set("ticket.priority.lowest", "&1");
		}
		if (!lang.contains("ticket.priority.low")) {
			lang.set("ticket.priority.low", "&9");
		}
		if (!lang.contains("ticket.priority.medium")) {
			lang.set("ticket.priority.medium", "&e");
		}
		if (!lang.contains("ticket.priority.high")) {
			lang.set("ticket.priority.high", "&c");
		}
		if (!lang.contains("ticket.priority.highest")) {
			lang.set("ticket.priority.highest", "&4");
		}
		this.save();
	}

	public void save() {
		try {
			lang.save(new File("plugins/HelpTicket/language.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getHeader() {
		return lang.getString("header", "&7[&cHelpTicket&7] ");
	}

	public String getMailSender() {
		return lang.getString("mail_sender", "HelpTicketUpdate ");
	}

	public String getAutoStaffMessageSingular() {
		return lang.getString("auto_staff_message.singular",
				"&7There is &e %numTicket &7open ticket.");
	}

	public String getAutoStaffMessagePlural() {
		return lang.getString("auto_staff_message.plural",
				"&7There are &e %numTicket &7open tickets.");
	}

	public String getAutoStaffMessageAttendSingular() {
		return lang.getString("auto_staff_message.attend.singular",
				"&7Please attend to it with &e\"/ticket\".");
	}

	public String getAutoStaffMessageAttendPlural() {
		return lang.getString("auto_staff_message.attend.plural",
				"&7Please attend to them with &e\"/ticket\".");
	}

	public String getNotStaffMessage() {
		return lang.getString("not_staff",
				"&cYou need to be at least a moderator to perform this action");
	}

	public String getTicketSubmittedPlayer() {
		return lang
				.getString(
						"ticket.submitted.player",
						"&aYour Ticket has been submitted. You can check it with &b\"/ticket view %ticketId\"");
	}

	public String getTicketSubmittedStaff() {
		return lang
				.getString("ticket.submitted.staff",
						"&c%player has opened a new ticket &7[%ticketId]&c. Please attend to it");
	}

	public String getTicketNotExist() {
		return lang.getString("ticket.not_exist",
				"&cTicket #%ticketId does not exist");
	}

	public String getTicketFullInfoHeader() {
		return lang
				.getString("ticket.full_info.header", "&c--[Ticket Information]--");
	}

	public String getTicketFullInfoId() {
		return lang.getString("ticket.full_info.id", "&3Id: &b%ticketId");
	}

	public String getTicketFullInfoOwner() {
		return lang.getString("ticket.full_info.owner",
				"&3Owner: &b%ticketOwner");
	}

	public String getTicketFullInfoAssignee() {
		return lang.getString("ticket.full_info.assignee",
				"&3Assigned To: &b%ticketAssignee");

	}

	public String getTicketFullInfoPriority() {
		return lang.getString("ticket.full_info.priority",
				"&3Priority: %ticketPriorityColor%ticketPriority");

	}

	public String getTicketFullInfoMessage() {
		return lang.getString("ticket.full_info.message",
				"&3Situation: &b%ticketFullMessage");

	}

	public String getTicketFullInfoStatus() {
		return lang.getString("ticket.full_info.status",
				"&3Status: &b%ticketStatus");

	}

	public String getTicketFullInfoCommentHeader() {
		return lang
				.getString("ticket.full_info.comments.header", "&3Comments:");

	}

	public String getTicketFullInfoCommentMessage() {
		return lang.getString("ticket.full_info.comments.message",
				"&b=> &f%commentOwner: &7%commentMessage");

	}

	public String getTicketFullInfoNoCommentMessage() {
		return lang.getString("ticket.full_info.comments.none",
				"&bNo Comments To Display");

	}

	public String getTicketFullInfoOrder() {
		return lang
				.getString(
						"ticket.full_info.order",
						"%headerLine%newline%idLine%newline%ownerLine%newline%assigneeLine%newline%priorityLine%newline%messageLine%newline%statusLine%newline%commentHeader%newline%comments");

	}

	public String getTicketFullInfo() {
		return this
				.getTicketFullInfoOrder()
				.replaceAll("%headerLine", this.getTicketFullInfoHeader())
				.replaceAll("%idLine", this.getTicketFullInfoId())
				.replaceAll("%ownerLine", this.getTicketFullInfoOwner())
				.replaceAll("%assigneeLine", this.getTicketFullInfoAssignee())

				.replaceAll("%priorityLine", this.getTicketFullInfoPriority())
				.replaceAll("%messageLine", this.getTicketFullInfoMessage())
				.replaceAll("%statusLine", this.getTicketFullInfoStatus())
				.replaceAll("%commentHeader",
						this.getTicketFullInfoCommentHeader());
	}

	public String getTicketShortInfo() {
		return lang
				.getString(
						"ticket.short_info",
						"%ticketPriorityColor[%ticketId] &f%ticketOwner -> %ticketAssignee&f: %ticketShortMessage &7[%ticketNumberComments]");

	}

	public String getOpenTicketsMessagePlayer() {
		return lang.getString("ticket.open_ticket.player",
				"&3Your Open Tickets:");

	}

	public String getOpenTicketsMessageStaff() {
		return lang.getString("ticket.open_ticket.staff", "&3Open Tickets:");

	}

	public String getTicketColorCoding() {
		return lang.getString(
				"ticket.priority.coding",
				"&7[" + this.getTicketPriorityLowestColor() + "lowest&7, "
						+ this.getTicketPriorityLowColor() + "low&7, "
						+ this.getTicketPriorityMediumColor() + "medium&7, "
						+ this.getTicketPriorityHighColor() + "high&7, "
						+ this.getTicketPriorityHighestColor() + "highest&7]");
	}

	public String getTicketPriorityLowestColor() {
		return lang.getString("ticket.priority.lowest", "&1");
	}

	public String getTicketPriorityLowColor() {
		return lang.getString("ticket.priority.low", "&9");
	}

	public String getTicketPriorityMediumColor() {
		return lang.getString("ticket.priority.medium", "&e");

	}

	public String getTicketPriorityHighColor() {
		return lang.getString("ticket.priority.high", "&c");

	}

	public String getTicketPriorityHighestColor() {
		return lang.getString("ticket.priority.highest", "&4");
	}
}
