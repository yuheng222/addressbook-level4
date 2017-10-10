package systemtests;

//import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.DeleteByNameCommand.MESSAGE_DELETE_PERSON_SUCCESS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteByNameCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.PersonNotFoundException;


public class DeleteByNameCommandSystemTest extends AddressBookSystemTest {

    private static final String MESSAGE_INVALID_DELETE_BY_NAME_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteByNameCommand.MESSAGE_USAGE);

    @Test
    public void deleteByName() {
        /*----------------- Performing delete operation while an unfiltered list is being shown --------------------*/

        /* Case: delete ALICE (first person) in the list, command with leading and trailing spaces -> deleted */
        Model expectedModel = getModel();
        Index firstIndex = INDEX_FIRST_PERSON;
        ReadOnlyPerson firstIndexToDelete = getModel().getFilteredPersonList().get(firstIndex.getZeroBased());
        String command = "     " + DeleteByNameCommand.COMMAND_WORD + "      "
                + firstIndexToDelete.getName() + "       ";

        ReadOnlyPerson deletedPerson = removePerson(expectedModel, firstIndexToDelete);
        String expectedResultMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: delete GEORGE (last person) in the list -> deleted */
        Model modelBeforeDeletingLast = getModel();
        Index lastIndex = getLastIndex(expectedModel);
        ReadOnlyPerson lastIndexToDelete = getModel().getFilteredPersonList().get(lastIndex.getZeroBased());
        assertCommandSuccess(lastIndexToDelete);

        /* Case: undo deleting GEORGE (last person) in the list -> last person restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: redo deleting GEORGE (last person) in the list -> last person deleted again */
        command = RedoCommand.COMMAND_WORD;
        removePerson(modelBeforeDeletingLast, lastIndexToDelete);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: delete CARL (middle person) in the list -> deleted */
        Model modelBeforeDeletingMid = getModel();
        Index midIndex = getMidIndex(modelBeforeDeletingMid);
        ReadOnlyPerson midIndexToDelete = getModel().getFilteredPersonList().get(midIndex.getZeroBased());
        assertCommandSuccess(midIndexToDelete);

        /* Case: undo deleting CARL -> CARL restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingMid, expectedResultMessage);

        /*------------------ Performing delete operation while a filtered list is being shown ----------------------*/

        /* Case: filtered person list, delete first person shown in person list -> deleted */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        Index firstPersonInFilteredList = INDEX_FIRST_PERSON;
        ReadOnlyPerson deleteFirstPersonInFilteredList = getModel().getFilteredPersonList().get(
                firstPersonInFilteredList.getZeroBased());
        assertCommandSuccess(deleteFirstPersonInFilteredList);

        /* Case: filtered person list, delete person NOT shown in person list -> deleted */
        assertCommandSuccess(FIONA);

        /*--------------------- Performing delete operation while a person card is selected ------------------------*/

        /* Case: delete the selected person -> person list panel selects the person before the deleted person */
        showAllPersons();
        expectedModel = getModel();
        Index selectedIndex = getLastIndex(expectedModel);
        ReadOnlyPerson selectedIndexToDelete = getModel().getFilteredPersonList().get(selectedIndex.getZeroBased());
        Index expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectPerson(selectedIndex);
        command = DeleteByNameCommand.COMMAND_WORD + " " + selectedIndexToDelete.getName();
        deletedPerson = removePerson(expectedModel, selectedIndexToDelete);
        expectedResultMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

    }

    /**
     * Removes the specified {@code ReadOnlyPerson} in {@code model}'s address book.
     * @return the removed person
     */
    private ReadOnlyPerson removePerson(Model model, ReadOnlyPerson person) {
        ReadOnlyPerson targetPerson = person;
        try {
            model.deletePerson(targetPerson);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("targetPerson is retrieved from model.");
        }
        return targetPerson;
    }

    /**
     * Deletes the person in {@code model}'s address book by creating a default {@code DeleteByNameCommand}
     * using {@code toDelete} and performs the same verification as
     * {@code assertCommandSuccess(String, Model, String)}.
     * @see DeleteByNameCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(ReadOnlyPerson toDelete) {
        Model expectedModel = getModel();
        ReadOnlyPerson deletedPerson = removePerson(expectedModel, toDelete);
        String expectedResultMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedPerson);

        assertCommandSuccess(
                DeleteByNameCommand.COMMAND_WORD + " " + toDelete.getName(), expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card remains unchanged.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the browser url
     * and selected card are expected to update accordingly depending on the card at {@code expectedSelectedCardIndex}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

}
