package com.imdeity.helpticket.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.helpticket.cmds.helpticket.TicketAssignCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketCloseCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketCommentCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketCreateCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketInfoCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketListCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketPriorityCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketPurgeCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketSelectCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketTpCommand;

public class TicketCommandHandler extends DeityCommandHandler {
    public TicketCommandHandler(String pluginName) {
        super(pluginName, "Ticket");
    }
    
    protected void initRegisteredCommands() {
        String[] createAliases = { "new" };
        String[] selectAliases = { "sel" };
        String[] teleportAliases = { "tp" };
        String[] commentAliases = { "cmt" };
        String[] priorityAliases = { "pri" };
        registerCommand("list", null, "<OPEN/CLOSED> <page-number>", "Shows all tickets", new TicketListCommand(), "helpticket.general.list");
        registerCommand("create", createAliases, "[message]", "Creates a ticket", new TicketCreateCommand(), "helpticket.general.create");
        registerCommand("info", null, "<page-number>", "Shows the selected tickets information", new TicketInfoCommand(), "helpticket.general.info");
        registerCommand("select", selectAliases, "[ticket-id]", "Shows the selected tickets information", new TicketSelectCommand(), "helpticket.general.select");
        registerCommand("teleport", teleportAliases, "", "Teleports to the selected ticket", new TicketTpCommand(), "helpticket.admin.tp");
        registerCommand("comment", commentAliases, "[message]", "Comments on the selected ticket", new TicketCommentCommand(), "helpticket.general.comment");
        registerCommand("close", null, "<message>", "Closes the selected ticket", new TicketCloseCommand(), "helpticket.general.close");
        registerCommand("assign", null, "[staff-name]", "Assigns the selected ticket", new TicketAssignCommand(), "helpticket.admin.assign");
        registerCommand("priority", priorityAliases, "[increase/decrease]", "Alters the priority of the selected ticket", new TicketPriorityCommand(), "helpticket.admin.priority");
        registerCommand("purge", null, "[player-name]", "Deletes all of a players tickets", new TicketPurgeCommand(), "helpticket.admin.purge");
    }
}