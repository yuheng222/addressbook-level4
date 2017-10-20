package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ListThemeRequestEvent;

/**
 * Shows all existing themes in the themes list.
 */
public class ListThemeCommand extends Command {

    public static final String COMMAND_WORD = "listthemes";
    public static final String COMMAND_ALIAS = "lt";

    public static final String MESSAGE_SUCCESS = "Listed all themes";


    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ListThemeRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
