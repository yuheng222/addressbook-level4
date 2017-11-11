package systemtests;

import org.junit.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.*;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalPersons.*;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIENDS;

public class FilterCommandSystemTest extends AddressBookSystemTest {
    @Test
    public void filter() {
        /* Case: filter one tag in address book to display multiple persons, command with leading spaces and trailing spaces
         * -> 2 persons found
         */
        String command = "   " + FilterCommand.COMMAND_WORD + " " + "  neighbours  ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, ALICE, GEORGE); // They all have tag "friends"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous filter command where person list is displaying the persons we are filtering
         * -> 2 persons found
         */
        command = FilterCommand.COMMAND_WORD + " " + "neighbours";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter persons where person list is not displaying all the persons we are finding
         * -> 1 person found
         */
        command = FilterCommand.COMMAND_WORD + " nicePerson";
        ModelHelper.setFilteredList(expectedModel, CARL, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter multiple tags in address book, 2 keywords -> 2 persons found */
        command = FilterCommand.COMMAND_WORD + " colleagues boss";
        ModelHelper.setFilteredList(expectedModel, CARL, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter multiple tags in address book, 2 keywords in reversed order -> 2 persons found */
        command = FilterCommand.COMMAND_WORD + " boss colleagues";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter multiple tags in address book, 2 keywords with 1 repeat -> 2 persons found */
        command = FilterCommand.COMMAND_WORD + " colleagues boss colleagues";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter multiple persons in address book, 2 matching keywords and 1 non-matching keyword
         * -> 2 persons found
         */
        command = FilterCommand.COMMAND_WORD + " colleagues boss NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: filter persons with same tags in address book after deleting 1 of them -> 1 person found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assert !getModel().getAddressBook().getPersonList().contains(CARL);
        command = FilterCommand.COMMAND_WORD + " " + "nicePerson";
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter person in address book, keyword is same as name but of different case -> 2 person found */
        command = FilterCommand.COMMAND_WORD + " BOSs";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person in address book, keyword is substring of tag -> 0 persons found */
        command = FilterCommand.COMMAND_WORD + " bo";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter person in address book, tag is substring of keyword -> 0 persons found */
        command = FilterCommand.COMMAND_WORD + " bosses";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter tag not in address book -> 0 persons found */
        command = FilterCommand.COMMAND_WORD + " Girlfriend";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter by phone number of person in address book -> 0 persons found */
        command = FilterCommand.COMMAND_WORD + " " + DANIEL.getPhone().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter by address of person in address book -> 0 persons found */
        command = FilterCommand.COMMAND_WORD + " " + DANIEL.getAddress().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter by email of person in address book -> 0 persons found */
        command = FilterCommand.COMMAND_WORD + " " + DANIEL.getEmail().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter by name of person in address book -> 0 persons found */
        String name = DANIEL.getName().fullName;
        command = FilterCommand.COMMAND_WORD + " " + name;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter while a person is selected -> selected card deselected */
        showAllPersons();
        selectPerson(Index.fromOneBased(1));
        assert !getPersonListPanel().getHandleToSelectedCard().getName().equals(DANIEL.getName().fullName);
        command = FilterCommand.COMMAND_WORD + " boss";
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: filter person in empty address book -> 0 persons found */
        executeCommand(ClearCommand.COMMAND_WORD);
        assert getModel().getAddressBook().getPersonList().size() == 0;
        command = FilterCommand.COMMAND_WORD + " " + VALID_TAG_FRIENDS;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> accepted */
        command = "FiLter OweSmonEY";
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
    }
    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_PERSONS_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
