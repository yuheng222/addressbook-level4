package seedu.address.logic.commands;

import java.lang.StringBuilder;
import java.util.List;


import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Exports the current Address Book data into a CSV file.
 */

public class ExportCommand extends Command {

    public static final String MESSAGE_EMPTY_ADDRESS_BOOK = "Export failed. Current Address Book is empty.";

    private final List<ReadOnlyPerson> currentData;

    public ExportCommand() {this.currentData = model.getAddressBook().getPersonList();}

    @Override
    public CommandResult execute() throws CommandException {

        if (currentData.isEmpty()) {
            throw new CommandException(MESSAGE_EMPTY_ADDRESS_BOOK);
        }

        return null;
    }

    private String constructHeaders() {
        ReadOnlyPerson samplePerson = currentData.get(0);
        List<String> propertyNames = samplePerson.getPropertyNamesAsList();

        final StringBuilder builder = new StringBuilder();

        for (String field : propertyNames) {
            builder.append(field);
            builder.append(',');
        }

        int lastChar = builder.lastIndexOf(",");
        builder.setCharAt(lastChar, '\n');

        return builder.toString();
    }

}
