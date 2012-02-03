package com.imdeity.helpticket.object;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class Language {

	public static YamlConfiguration lang = new YamlConfiguration();

	public void loadDefaults() {
		lang = YamlConfiguration.loadConfiguration(new File("plugins/HelpTicket/language.yml"));
		if (!lang.contains("header")) {
			lang.set("header", "&7[&cHelpTicket&7] ");
		}
		if (!lang.contains("mail_sender")) {
			lang.set("mail_sender", "HelpTicketUpdate ");
		}
		if (!lang.contains("auto_staff_message.singular")) {
			lang.set("auto_staff_message.singular", "&7There is &e%numTickets &7open ticket. &7Please attend to it with &e\"/ticket\".");
		}
		if (!lang.contains("auto_staff_message.plural")) {
			lang.set("auto_staff_message.plural", "&7There are &e%numTickets &7open tickets. &7Please attend to them with &e\"/ticket\".");
		}
		if (!lang.contains("not_staff")) {
			lang.set("not_staff", "&cYou need to be at least a moderator to perform this action");
		}
		if (!lang.contains("ticket.submitted.player")) {
			lang.set("ticket.submitted.player", "&aYour Ticket has been submitted. You can check it with &b\"/ticket view %ticketId\"");
		}
		if (!lang.contains("ticket.submitted.staff")) {
			lang.set("ticket.submitted.staff", "&c%player has opened a new ticket &7[%ticketId]&c. Please attend to it");
		}
		if (!lang.contains("ticket.not_exist")) {
			lang.set("ticket.not_exist", "&cTicket #%ticketId does not exist");
		}
		if (!lang.contains("ticket.short_info")) {
			lang.set("ticket.short_info", "%ticketPriorityColor[%ticketId] &f%ticketOwner%ticketAssigneeFull&f: &7%ticketShortMessage&8%ticketNumberComments");
		}
		if (!lang.contains("ticket.full_info.order")) {
			lang.set("ticket.full_info.order", "%header_line%newline%id_line%newline%owner_line%newline%assignee_line%newline%priority_line%newline%message_line%newline%status_line%newline%comment_header%newline%ticket_comments");
		}
		if (!lang.contains("ticket.full_info.header_line")) {
			lang.set("ticket.full_info.header_line", "&c--[Ticket Information]--");
		}
		if (!lang.contains("ticket.full_info.id_line")) {
			lang.set("ticket.full_info.id_line", "&3Id: &b%ticketId");
		}
		if (!lang.contains("ticket.full_info.owner_line")) {
			lang.set("ticket.full_info.owner_line", "&3Owner: &b%ticketOwner");
		}
		if (!lang.contains("ticket.full_info.assignee_line")) {
			lang.set("ticket.full_info.assignee_line", "&3Assigned To: &b%ticketAssigneeName");
		}
		if (!lang.contains("ticket.full_info.priority_line")) {
			lang.set("ticket.full_info.priority_line", "&3Priority: &b%ticketPriority");
		}
		if (!lang.contains("ticket.full_info.message_line")) {
			lang.set("ticket.full_info.message_line", "&3Situation: &b%ticketLongMessage");
		}
		if (!lang.contains("ticket.full_info.status_line")) {
			lang.set("ticket.full_info.status_line", "&3Status: &b%ticketStatus");
		}
		if (!lang.contains("ticket.full_info.comments.comment_header")) {
			lang.set("ticket.full_info.comments.comment_header", "&3Comments:");
		}
		if (!lang.contains("ticket.full_info.comments.message")) {
			lang.set("ticket.full_info.comments.message", "&b=> &f%ticketCommentOwner: &7%ticketCommentMessage");
		}
		if (!lang.contains("ticket.full_info.comments.none")) {
			lang.set("ticket.full_info.comments.none", "&bNo Comments To Display");
		}
		if (!lang.contains("ticket.open_ticket.player")) {
			lang.set("ticket.open_ticket.player", "&3Your Open Tickets:");
		}
		if (!lang.contains("ticket.open_ticket.staff")) {
			lang.set("ticket.open_ticket.staff", "&3Open Tickets:");
		}
		if (!lang.contains("ticket.priority.coding")) {
			lang.set("ticket.priority.coding", "&7[%priorityColorLowestlowest&7, %priorityColorLowlow&7, %priorityColorMediummedium&7, %priorityColorHighhigh&7, %priorityColorHighesthighest&7]");
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
		if (!lang.contains("ticket.priority.error")) {
			lang.set("ticket.priority.error", "&cPriority can only be from 0-4");
		}
		if (!lang.contains("ticket.priority.user")) {
			lang.set("ticket.priority.user", "&b%player &f set the priority of ticket %ticketId to %ticketPriority");
		}
		if (!lang.contains("ticket.priority.staff")) {
			lang.set("ticket.priority.staff", "&b%player &f set the priority of ticket %ticketId to %ticketPriority");
		}
		if (!lang.contains("ticket.close.log.nocomment")) {
			lang.set("ticket.close.log.nocomment", "%player closed the ticket");
		}
		if (!lang.contains("ticket.close.log.comment")) {
			lang.set("ticket.close.log.comment", "Closed the ticket - %comment");
		}
		if (!lang.contains("ticket.close.user")) {
			lang.set("ticket.close.user", "&fYou closed ticket &a#%ticketId");
		}
		if (!lang.contains("ticket.close.user.comment")) {
			lang.set("ticket.close.user.comment", "&fYou closed ticket &a#%ticketId - %comment");
		}
		if (!lang.contains("ticket.close.self")) {
			lang.set("ticket.close.self", "&a%ticketOwner &fclosed ticket &a#%ticketId");
		}
		if (!lang.contains("ticket.close.self.comment")) {
			lang.set("ticket.close.self.comment", "&a%ticketOwner &fclosed ticket &a#%ticketId - %comment");
		}
		if (!lang.contains("ticket.close.player")) {
			lang.set("ticket.close.player", "&a%player &fclosed your ticket &a#%ticketId");
		}
		if (!lang.contains("ticket.close.player.comment")) {
			lang.set("ticket.close.player.comment", "&a%player&f closed your ticket &a#%ticketId - %comment");
		}
		if (!lang.contains("ticket.close.staff")) {
			lang.set("ticket.close.staff", "&a%player&f closed ticket &a#%ticketId");
		}
		if (!lang.contains("ticket.close.staff.comment")) {
			lang.set("ticket.close.staff.comment", "&a%player&f closed ticket &a#%ticketId - %comment");
		}
		if (!lang.contains("ticket.reopen.log")) {
			lang.set("ticket.reopen.log", "Reopened ticket");
		}
		if (!lang.contains("ticket.reopen.staff")) {
			lang.set("ticket.reopen.staff", "&a%player &freopened ticket &a#%ticketId");
		}
		if (!lang.contains("ticket.reopen.user")) {
			lang.set("ticket.reopen.user", "&a%player &freopened ticket &a#%ticketId");
		}
		if (!lang.contains("ticket.reopen.error")) {
			lang.set("ticket.reopen.error", "&cOnly staff can reopen tickets");
		}
		if (!lang.contains("ticket.port.review")) {
			lang.set("ticket.port.review", "&b%player &fis reviewing your ticket &7[%ticketId]");
		}
		if (!lang.contains("ticket.is_closed")) {
			lang.set("ticket.is_closed", "&cThat ticket is closed.");
		}
		if (!lang.contains("ticket.is_already_open")) {
			lang.set("ticket.is_already_open", "&cThat ticket is already open.");
		}
		if (!lang.contains("ticket.assign.user")) {
			lang.set("ticket.assign.user", "&b%ticketAssigneeName has been assigned to your ticket &7[%ticketId]");
		}
		if (!lang.contains("ticket.assign.staff")) {
			lang.set("ticket.assign.staff", "&b%ticketAssigneeName has been assigned to ticket %ticketId by %player");
		}
		if (!lang.contains("ticket.search.title")) {
			lang.set("ticket.search.title", "&3%player's tickets");
		}
		if (!lang.contains("ticket.search.invalid")) {
			lang.set("ticket.search.invalid", "&c%player has no tickets");
		}
		if (!lang.contains("ticket.comment.user")) {
			lang.set("ticket.comment.user", "&b%player &fcommented on ticket %ticketId - %comment");
		}
		if (!lang.contains("ticket.comment.staff")) {
			lang.set("ticket.comment.staff", "&b%player &fcommented on ticket %ticketId - %comment");
		}
		if (!lang.contains("help.help")) {
			lang.set("help.help", "&eUse \"/ticket ?\" for help");
		}
		if (!lang.contains("help.invalid")) {
			lang.set("help.invalid", "&eInvalid syntax! Use \"/ticket ?\" for help");
		}
		if (!lang.contains("ticket.updated")) {
			lang.set("ticket.updated", "&fTicket %ticketId has been updated.");
		}
		if (!lang.contains("ticket.min_word_message")) {
			lang.set("ticket.min_word_message", "&cSorry but can you please explain your issue more.");
		}
		if (!lang.contains("ticket.comment.number.format.none")) {
			lang.set("ticket.comment.number.format.none", "");
		}
		if (!lang.contains("ticket.comment.number.format.singular")) {
			lang.set("ticket.comment.number.format.singular", " [%num Comment]");
		}
		if (!lang.contains("ticket.comment.number.format.plural")) {
			lang.set("ticket.comment.number.format.plural", " [%num Comments]");
		}
		if (!lang.contains("ticket.assign.format.none")) {
			lang.set("ticket.assign.format.none", "");
		}
		if (!lang.contains("ticket.assign.format.assigned")) {
			lang.set("ticket.assign.format.assigned", " -> %assignee");
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
		return lang.getString("header");
	}

	public String getMailSender() {
		return lang.getString("mail_sender");
	}

	public String getAutoStaffMessageSingular() {
		return lang.getString("auto_staff_message.singular");
	}

	public String getAutoStaffMessagePlural() {
		return lang.getString("auto_staff_message.plural");
	}

	public String getNotStaffMessage() {
		return lang.getString("not_staff");
	}

	public String getTicketSubmittedPlayer() {
		return lang.getString("ticket.submitted.player");
	}

	public String getTicketSubmittedStaff() {
		return lang.getString("ticket.submitted.staff");
	}

	public String getTicketNotExist() {
		return lang.getString("ticket.not_exist");
	}

	public String getTicketFullInfoHeader() {
		return lang.getString("ticket.full_info.header_line");
	}

	public String getTicketFullInfoId() {
		return lang.getString("ticket.full_info.id_line");
	}

	public String getTicketFullInfoOwner() {
		return lang.getString("ticket.full_info.owner_line");
	}

	public String getTicketFullInfoAssignee() {
		return lang.getString("ticket.full_info.assignee_line");
	}

	public String getTicketFullInfoPriority() {
		return lang.getString("ticket.full_info.priority_line");
	}

	public String getTicketFullInfoMessage() {
		return lang.getString("ticket.full_info.message_line");
	}

	public String getTicketFullInfoStatus() {
		return lang.getString("ticket.full_info.status_line");
	}

	public String getTicketFullInfoCommentHeader() {
		return lang.getString("ticket.full_info.comments.comment_header");
	}

	public String getTicketFullInfoCommentMessage() {
		return lang.getString("ticket.full_info.comments.message");
	}

	public String getTicketFullInfoNoCommentMessage() {
		return lang.getString("ticket.full_info.comments.none");
	}

	public String getTicketFullInfoOrder() {
		return lang.getString("ticket.full_info.order");
	}

	public String getTicketFullInfo() {
		return this.getTicketFullInfoOrder().replaceAll("%header_line", this.getTicketFullInfoHeader()).replaceAll("%id_line", this.getTicketFullInfoId()).replaceAll("%owner_line", this.getTicketFullInfoOwner()).replaceAll("%assignee_line", this.getTicketFullInfoAssignee()).replaceAll("%priority_line", this.getTicketFullInfoPriority()).replaceAll("%message_line", this.getTicketFullInfoMessage()).replaceAll("%status_line", this.getTicketFullInfoStatus()).replaceAll("%comment_header", this.getTicketFullInfoCommentHeader());
	}

	public String getTicketShortInfo() {
		return lang.getString("ticket.short_info");
	}

	public String getOpenTicketsMessagePlayer() {
		return lang.getString("ticket.open_ticket.player");
	}

	public String getOpenTicketsMessageStaff() {
		return lang.getString("ticket.open_ticket.staff");

	}

	public String getTicketColorCoding() {
		String tmp = lang.getString("ticket.priority.coding");
		tmp = tmp.replaceAll("%priorityColorLowest", this.getTicketPriorityLowestColor()).replaceAll("%priorityColorLow", this.getTicketPriorityLowColor()).replaceAll("%priorityColorMedium", this.getTicketPriorityMediumColor()).replaceAll("%priorityColorHighest", this.getTicketPriorityHighestColor()).replaceAll("%priorityColorHigh", this.getTicketPriorityHighColor());

		return tmp;

	}

	public String getTicketPriorityLowestColor() {
		return lang.getString("ticket.priority.lowest");
	}

	public String getTicketPriorityLowColor() {
		return lang.getString("ticket.priority.low");
	}

	public String getTicketPriorityMediumColor() {
		return lang.getString("ticket.priority.medium");
	}

	public String getTicketPriorityHighColor() {
		return lang.getString("ticket.priority.high");
	}

	public String getTicketPriorityHighestColor() {
		return lang.getString("ticket.priority.highest");
	}

	public String getClosedLog() {
		return lang.getString("ticket.close.log.nocomment");
	}

	public String getClosedLogComment() {
		return lang.getString("ticket.close.log.comment");
	}

	public String getClosedUser() {
		return lang.getString("ticket.close.user");
	}

	public String getClosedUserComment() {
		return lang.getString("ticket.close.user.comment");
	}

	public String getClosedSelf() {
		return lang.getString("ticket.close.self");
	}

	public String getClosedSelfComment() {
		return lang.getString("ticket.close.self.comment");
	}

	public String getClosedPlayer() {
		return lang.getString("ticket.close.player");
	}

	public String getClosedPlayerComment() {
		return lang.getString("ticket.close.player.comment");
	}

	public String getClosedStaff() {
		return lang.getString("ticket.close.staff");
	}

	public String getClosedStaffComment() {
		return lang.getString("ticket.close.staff.comment");
	}

	public String getReopenLog() {
		return lang.getString("ticket.reopen.log");
	}

	public String getReopenStaff() {
		return lang.getString("ticket.reopen.staff");
	}

	public String getReopenUser() {
		return lang.getString("ticket.reopen.user");
	}

	public String getReopenError() {
		return lang.getString("ticket.reopen.error");
	}

	public String getReviewingTicket() {
		return lang.getString("ticket.port.review");
	}

	public String getTicketClosed() {
		return lang.getString("ticket.is_closed");
	}

	public String getTicketIsOpen() {
		return lang.getString("ticket.is_already_open");
	}

	public String getAssignUser() {
		return lang.getString("ticket.assign.user");

	}

	public String getAssignStaff() {
		return lang.getString("ticket.assign.staff");

	}

	public String getSearchTicketTitle() {
		return lang.getString("ticket.search.title");

	}

	public String getSearchInvalid() {
		return lang.getString("ticket.search.invalid");

	}

	public String getTicketCommentUser() {
		return lang.getString("ticket.comment.user");

	}

	public String getTicketCommentStaff() {
		return lang.getString("ticket.comment.staff");

	}

	public String getTicketPriorityError() {
		return lang.getString("ticket.priority.error");

	}

	public String getTicketPriorityUser() {
		return lang.getString("ticket.priority.user");

	}

	public String getTicketPriorityStaff() {
		return lang.getString("ticket.priority.staff");
	}

	public String getHelp() {
		return lang.getString("help.help");
	}

	public String getHelpInvalid() {
		return lang.getString("help.invalid");
	}

	public String getUpdateMessage() {
		return lang.getString("ticket.updated");
	}

	public String getTicketMinWordMessage() {
		return lang.getString("ticket.min_word_message");
	}

	public String getTicketCommentNumberFormatNone() {
		return lang.getString("ticket.comment.number.format.none");
	}

	public String getTicketCommentNumberFormatSingle() {
		return lang.getString("ticket.comment.number.format.singular");
	}

	public String getTicketCommentNumberFormatPlural() {
		return lang.getString("ticket.comment.number.format.plural");
	}

	public String getTicketAssignFormatNone() {
		return lang.getString("ticket.assign.format.none");
	}

	public String getTicketAssignFormatAssigned() {
		return lang.getString("ticket.assign.format.assigned");
	}

	public void setString(String path, String value) {
		lang.set(path, value);
	}
}
