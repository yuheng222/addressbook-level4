//@@author Ryan Teo

package seedu.address.logic.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.CaseInsensitiveExactNamePredicate;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Deletes a person identified using the person's exact name. Case insensitive.
 * IMPORTANT: Does not depend on last shown list.
 */

public class DeleteByNameCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "deletebyname";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the case-insensitive input NAME.\n"
            + "Parameters: NAME\n"
            + "Example: " + COMMAND_WORD + " John";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    public static final String MESSAGE_MULTIPLE_PERSON_WITH_SAME_NAME = "There are multiple persons with the same name"
            + " detected who are listed below for your convenience. "
            + "Please use the general delete method shown below. \n" + DeleteCommand.MESSAGE_USAGE;

    public static final String MESSAGE_SUGGESTED_PERSONS = "Could not find the person to delete. "
            + "Here are some suggestions. You may edit the input name or use the general delete method shown below.\n"
            + DeleteCommand.MESSAGE_USAGE;

    private final Name nameToBeDeleted;

    public DeleteByNameCommand(Name nameToBeDeleted) {
        this.nameToBeDeleted = nameToBeDeleted;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();

        Stream<ReadOnlyPerson> filteredPersonStream = personList.stream()
                .filter(person -> person.getName().toString().toLowerCase()
                        .equals(nameToBeDeleted.toString().toLowerCase()));

        List<ReadOnlyPerson> filteredPersonList = filteredPersonStream.collect(Collectors.toList());

        if (filteredPersonList.isEmpty()) { // No matching name found
            // Do a generic name search
            List<String> keywords = Arrays.asList(nameToBeDeleted.toString().split(" "));
            NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);
            Stream<ReadOnlyPerson> suggestedPersonStream = personList.stream().filter(predicate);
            List<ReadOnlyPerson> suggestedPersonList = suggestedPersonStream.collect(Collectors.toList());

            if (!suggestedPersonList.isEmpty()) { // Show suggested persons to delete
                model.updateFilteredPersonList(predicate);
                throw new CommandException(MESSAGE_SUGGESTED_PERSONS);
            } else { // No such person found
                throw new CommandException(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
            }

        } else if (filteredPersonList.size() > 1) { // More than 1 person with exact name
            model.updateFilteredPersonList(new CaseInsensitiveExactNamePredicate(nameToBeDeleted));
            throw new CommandException(MESSAGE_MULTIPLE_PERSON_WITH_SAME_NAME);
        }

        ReadOnlyPerson personToDelete = filteredPersonList.get(0); // Get the person to delete

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
