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
			lang.set(
					"auto_staff_message.singular",
					"&7There is &e %numTicket &7open ticket. &7Please attend to it with &e\"/ticket\".");
		}
		if (!lang.contains("auto_staff_message.plural")) {
			lang.set(
					"auto_staff_message.plural",
					"&7There are &e %numTicket &7open tickets. &7Please attend to them with &e\"/ticket\".");
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
					"%ticketPriorityColor[%ticketId] &f%ticketOwner -> %ticketAssignee&f: &7%ticketShortMessage &7[%ticketNumberComments]");
		}
		if (!lang.contains("ticket.full_info.order")) {
			lang.set(
					"ticket.full_info.order",
					"%header_line%newline%id_line%newline%owner_line%newline%assignee_line%newline%priority_line%newline%message_line%newline%status_line%newline%comment_header%newline%ticket_comments");
		}
		if (!lang.contains("ticket.full_info.header_line")) {

			lang.set("ticket.full_info.header_line",
					"&c--[Ticket Information]--");
		}
		if (!lang.contains("ticket.full_info.id_line")) {
			lang.set("ticket.full_info.id_line", "&3Id: &b%ticketId");
		}
		if (!lang.contains("ticket.full_info.owner_line")) {
			lang.set("ticket.full_info.owner_line", "&3Owner: &b%ticketOwner");
		}
		if (!lang.contains("ticket.full_info.assignee_line")) {
			lang.set("ticket.full_info.assignee_line",
					"&3Assigned To: &b%ticketAssignee");
		}
		if (!lang.contains("ticket.full_info.priority_line")) {
			lang.set("ticket.full_info.priority_line",
					"&3Priority: &b%ticketPriority");
		}
		if (!lang.contains("ticket.full_info.message_line")) {
			lang.set("ticket.full_info.message_line",
					"&3Situation: &b%ticketLongMessage");
		}
		if (!lang.contains("ticket.full_info.status_line")) {
			lang.set("ticket.full_info.status_line",
					"&3Status: &b%ticketStatus");
		}
		if (!lang.contains("ticket.full_info.comments.comment_header")) {
			lang.set("ticket.full_info.comments.comment_header", "&3Comments:");
		}
		if (!lang.contains("ticket.full_info.comments.message")) {
			lang.set("ticket.full_info.comments.message",
					"&b=> &f%ticketCommentOwner: &7%ticketCommentMessage");
		}
		if (!lang.contains("ticket.full_info.comments.none")) {
			lang.set("ticket.full_info.comments.none",
					"&bNo Comments To Display");
		}

		if (!lang.contains("ticket.open_ticket.player")) {
			lang.set("ticket.open_ticket.player", "&3Your Open Tickets:");
		}
		if (!lang.contains("ticket.open_ticket.staff")) {
			lang.set("ticket.open_ticket.staff", "&3Open Tickets:");
		}
		if (!lang.contains("ticket.priority.coding")) {
			lang.set(
					"ticket.priority.coding",
					"&7[%priorityColorLowestlowest&7, %priorityColorLowlow&7, %priorityColorMediummedium&7, %priorityColorHighhigh&7, %priorityColorHighesthighest&7]");
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
		if (!lang.contains("ticket.log.close")) {
			lang.set("ticket.log.close", "%player closed the ticket");
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
		return lang
				.getString(
						"auto_staff_message.singular",
						"&7There is &e %numTicket &7open ticket. &7Please attend to it with &e\"/ticket\".");
	}

	public String getAutoStaffMessagePlural() {
		return lang
				.getString(
						"auto_staff_message.plural",
						"&7There are &e %numTicket &7open tickets. &7Please attend to them with &e\"/ticket\".");
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
		return lang.getString("ticket.full_info.header_line",
				"&c--[Ticket Information]--");
	}

	public String getTicketFullInfoId() {
		return lang.getString("ticket.full_info.id_line", "&3Id: &b%ticketId");
	}

	public String getTicketFullInfoOwner() {
		return lang.getString("ticket.full_info.owner_line",
				"&3Owner: &b%ticketOwner");
	}

	public String getTicketFullInfoAssignee() {
		return lang.getString("ticket.full_info.assignee_line",
				"&3Assigned To: &b%ticketAssignee");

	}

	public String getTicketFullInfoPriority() {
		return lang.getString("ticket.full_info.priority_line",
				"&3Priority: %ticketPriorityColor%ticketPriority");

	}

	public String getTicketFullInfoMessage() {
		return lang.getString("ticket.full_info.message_line",
				"&3Situation: &b%ticketFullMessage");

	}

	public String getTicketFullInfoStatus() {
		return lang.getString("ticket.full_info.status_line",
				"&3Status: &b%ticketStatus");

	}

	public String getTicketFullInfoCommentHeader() {
		return lang.getString("ticket.full_info.comments.comment_header",
				"&3Comments:");

	}

	public String getTicketFullInfoCommentMessage() {
		return lang.getString("ticket.full_info.comments.message",
				"&b=> &f%ticketCommentOwner: &7%ticketCommentMessage");

	}

	public String getTicketFullInfoNoCommentMessage() {
		return lang.getString("ticket.full_info.comments.none",
				"&bNo Comments To Display");

	}

	public String getTicketFullInfoOrder() {
		return lang
				.getString(
						"ticket.full_info.order",
						"%header_line%newline%id_line%newline%owner_line%newline%assignee_line%newline%priority_line%newline%message_line%newline%status_line%newline%comment_header%newline%ticket_comments");

	}

	public String getTicketFullInfo() {
		return this
				.getTicketFullInfoOrder()
				.replaceAll("%header_line", this.getTicketFullInfoHeader())
				.replaceAll("%id_line", this.getTicketFullInfoId())
				.replaceAll("%owner_line", this.getTicketFullInfoOwner())
				.replaceAll("%assignee_line", this.getTicketFullInfoAssignee())
				.replaceAll("%priority_line", this.getTicketFullInfoPriority())
				.replaceAll("%message_line", this.getTicketFullInfoMessage())
				.replaceAll("%status_line", this.getTicketFullInfoStatus())
				.replaceAll("%comment_header",
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
		String tmp = lang
				.getString(
						"ticket.priority.coding",
						"&7[%priorityColorLowestlowest&7, %priorityColorLowlow&7, %priorityColorMediummedium&7, %priorityColorHighhigh&7, %priorityColorHighesthighest&7]");
		tmp = tmp
				.replaceAll("%priorityColorLowest",
						this.getTicketPriorityLowestColor())
				.replaceAll("%priorityColorLow",
						this.getTicketPriorityLowColor())
				.replaceAll("%priorityColorMedium",
						this.getTicketPriorityMediumColor())
				.replaceAll("%priorityColorHighest",
						this.getTicketPriorityHighestColor())
				.replaceAll("%priorityColorHigh",
						this.getTicketPriorityHighColor());
				
		return tmp;

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

	public String getClosedLog() {
		return lang.getString("ticket.close.log", "Closed the ticket");
	}

	public String getClosedLogComment() {
		return lang.getString("ticket.close.log.comment",
				"Closed the ticket - %comment");
	}

	public String getClosedUser() {
		return lang.getString("ticket.close.user",
				"&fYou closed ticket &a#%ticketId");
	}
	
	public String getClosedUserComment() {
		return lang.getString("ticket.close.user.comment",
				"&fYou closed ticket &a#%ticketId - %comment");
	}

	public String getClosedSelf() {
		return lang.getString("ticket.close.self",
				"&a%ticketOwner &fclosed ticket &a#%ticketId");
	}
	
	public String getClosedSelfComment() {
		return lang.getString("ticket.close.self.comment",
				"&a%ticketOwner &fclosed ticket &a#%ticketId - %comment");
	}


	public String getClosedPlayer() {
		return lang.getString("ticket.close.player",
				"&a%player &fclosed your ticket &a#%ticketId");
	}

	public String getClosedPlayerComment() {
		return lang.getString("ticket.close.player.comment",
				"&a%player&f closed your ticket &a#%ticketId - %comment");
	}

	public String getClosedStaff() {
		return lang.getString("ticket.close.staff",
				"&a%player&f closed ticket &a#%ticketId");
	}

	public String getClosedStaffComment() {
		return lang.getString("ticket.close.staff.comment",
				"&a%player&f closed ticket &a#%ticketId - %comment");
	}
	
	public String getReopenLog() {
		return lang.getString("ticket.reopen.log",
				"Reopened ticket");
	}
	
	public String getReopenStaff() {
		return lang.getString("ticket.reopen.staff",
				"&a%player &freopened ticket &a#%ticketId");
	}
	
	public String getReopenUser() {
		return lang.getString("ticket.reopen.user",
				"&a%player &freopened ticket &a#%ticketId");
	}
	
	public String getReopenError() {
		return lang.getString("ticket.reopen.error",
				"&cOnly staff can reopen tickets");
	}

}