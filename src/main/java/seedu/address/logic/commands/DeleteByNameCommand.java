package seedu.address.logic.commands;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Name;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Deletes a person identified using the person's exact name. Case-sensitive.
 * IMPORTANT: Does not depend on last shown list.
 */

public class DeleteByNameCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "deletebyname";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the CASE-SENSITIVE input.\n"
            + "Parameters: NAME\n"
            + "Example: " + COMMAND_WORD + " John";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Name nameToBeDeleted;

    public DeleteByNameCommand(Name nameToBeDeleted) {
        this.nameToBeDeleted = nameToBeDeleted;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();

        Stream<ReadOnlyPerson> filteredPersonStream = personList.stream()
                .filter(person -> person.getName().equals(nameToBeDeleted));

        List<ReadOnlyPerson> filteredList = filteredPersonStream.collect(Collectors.toList());
        ReadOnlyPerson personToDelete = null;

        for (ReadOnlyPerson person: filteredList) {
            if (person.getName().equals(nameToBeDeleted)) {
                personToDelete = person;
                break;
            }
        }

        if (personToDelete == null) {
            throw new CommandException(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        }

        try {
            model.deletePerson(personToDelete);
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target person cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteByNameCommand // instanceof handles nulls
                && this.nameToBeDeleted.equals(((DeleteByNameCommand) other).nameToBeDeleted)); // state check
    }

}
