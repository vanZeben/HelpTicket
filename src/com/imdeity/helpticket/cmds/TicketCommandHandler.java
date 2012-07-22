package com.imdeity.helpticket.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.helpticket.cmds.helpticket.TicketAssignCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketCloseCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketCommentCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketCreateCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketInfoCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketListCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketPriorityCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketSelectCommand;
import com.imdeity.helpticket.cmds.helpticket.TicketTpCommand;

public class TicketCommandHandler extends DeityCommandHandler
{
  public TicketCommandHandler(String pluginName)
  {
    super(pluginName, "Ticket");
  }

  protected void initRegisteredCommands()
  {
    registerCommand("list", "<OPEN/CLOSED> <page-number>", "Shows all tickets", new TicketListCommand(), "helpticket.general.list");
    registerCommand("create", "[message]", "Creates a ticket", new TicketCreateCommand(), "helpticket.general.create");
    registerCommand("info", "<page-number>", "Shows the selected tickets information", new TicketInfoCommand(), "helpticket.general.info");
    registerCommand("select", "[ticket-id]", "Shows the selected tickets information", new TicketSelectCommand(), "helpticket.general.select");
    registerCommand("tp", "", "Teleports to the selected ticket", new TicketTpCommand(), "helpticket.admin.tp");
    registerCommand("comment", "[message]", "Comments on the selected ticket", new TicketCommentCommand(), "helpticket.general.comment");
    registerCommand("close", "<message>", "Closes the selected ticket", new TicketCloseCommand(), "helpticket.general.close");
    registerCommand("assign", "[staff-name]", "Assigns the selected ticket", new TicketAssignCommand(), "helpticket.admin.assign");
    registerCommand("priority", "[increase/decrease]", "Alters the priority of the selected ticket", new TicketPriorityCommand(), "helpticket.admin.priority");
  }
}