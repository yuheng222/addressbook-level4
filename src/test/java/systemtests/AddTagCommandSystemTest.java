package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.INVALID_TAG;
import static seedu.address.testutil.TypicalPersons.VALID_TAG_FRIENDS;
import static seedu.address.testutil.TypicalPersons.VALID_TAG_NEIGHBOURS;
import static seedu.address.testutil.TypicalPersons.VALID_TAG_ONE;
import static seedu.address.testutil.TypicalPersons.VALID_TAG_OWESMONEY;
import static seedu.address.testutil.TypicalPersons.VALID_TAG_TWO;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddTagCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void addtag() throws Exception {
        Model model = getModel();

        /* ----------------- Performing addtag operation while an unfiltered list is being shown ---------------------- */

        /* Case: add multiple tags, command with leading spaces,
         * trailing spaces and multiple spaces between each keywords
         * -> tags updated
         */
        Index index = INDEX_FIRST_PERSON;
        String command = " " + AddTagCommand.COMMAND_WORD + "  " + index.getOneBased() + "  "
                + " CS2101   CS2103  ";
        ReadOnlyPerson personToEdit = getModel().getFilteredPersonList().get(index.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(VALID_TAG_FRIENDS, VALID_TAG_NEIGHBOURS, "CS2101", "CS2103").build();
        assertCommandSuccess(command, index, editedPerson);

        /* Case: undo editing the last person in the list -> last person restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last person in the list -> last person edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updatePerson(
                getModel().getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), editedPerson);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add tag with same name, but with a different case
         * -> tags updated
         */
        command = " " + AddTagCommand.COMMAND_WORD + "  " + index.getOneBased() + " " + "FRIENDS";
        personToEdit = getModel().getFilteredPersonList().get(index.getZeroBased());
        editedPerson = new PersonBuilder(personToEdit)
                .withTags(VALID_TAG_FRIENDS, VALID_TAG_NEIGHBOURS, "CS2101", "CS2103", "FRIENDS")
                .build();
        assertCommandSuccess(command, index, editedPerson);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered person list, addtag index within bounds of address book and person list -> tags updated */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_PERSON;
        assertTrue(index.getZeroBased() < getModel().getFilteredPersonList().size());
        command = AddTagCommand.COMMAND_WORD + " " + index.getOneBased() + " " + VALID_TAG_ONE;
        personToEdit = getModel().getFilteredPersonList().get(index.getZeroBased());
        editedPerson = new PersonBuilder(personToEdit).withTags(VALID_TAG_OWESMONEY, VALID_TAG_FRIENDS, VALID_TAG_ONE)
                .build();
        assertCommandSuccess(command, index, editedPerson);

        /* Case: filtered person list, edit index within bounds of address book but out of bounds of person list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getAddressBook().getPersonList().size();
        assertCommandFailure(AddTagCommand.COMMAND_WORD + " " + invalidIndex + " " + VALID_TAG_ONE,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(AddTagCommand.COMMAND_WORD + " 0" + " " + VALID_TAG_ONE,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(AddTagCommand.COMMAND_WORD + " -1" + " " + VALID_TAG_ONE,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredPersonList().size() + 1;
        assertCommandFailure(AddTagCommand.COMMAND_WORD + " " + invalidIndex + " " + VALID_TAG_ONE,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(AddTagCommand.COMMAND_WORD + " " + VALID_TAG_ONE,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));

        /* Case: missing tags -> rejected */
        assertCommandFailure(AddTagCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased(),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));

        /* Case: invalid tag -> rejected */
        assertCommandFailure(AddTagCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
                + " " + INVALID_TAG, Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: add a tag that the person already has -> rejected */
        index = INDEX_FIRST_PERSON;
        command = AddTagCommand.COMMAND_WORD + " " + index.getOneBased() + " " + VALID_TAG_FRIENDS
                + " " + VALID_TAG_NEIGHBOURS;
        assertCommandFailure(command, AddTagCommand.MESSAGE_DUPLICATE_TAG +VALID_TAG_FRIENDS);
        
        /* Case: add multiple tags, one of them already exists in the person -> rejected */
        command = AddTagCommand.COMMAND_WORD + " " + index.getOneBased() + " " + VALID_TAG_TWO + " " + VALID_TAG_FRIENDS;
        assertCommandFailure(command, AddTagCommand.MESSAGE_DUPLICATE_TAG + VALID_TAG_FRIENDS); 
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, ReadOnlyPerson, Index)} except that
     * the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see AddTagCommandSystemTest#assertCommandSuccess(String, Index, ReadOnlyPerson, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, ReadOnlyPerson editedPerson) {
        assertCommandSuccess(command, toEdit, editedPerson, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the person at index {@code toEdit} being
     * updated to values specified {@code editedPerson}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see AddTagCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, ReadOnlyPerson editedPerson,
                                      Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        try {
            expectedModel.updatePerson(
                    expectedModel.getFilteredPersonList().get(toEdit.getZeroBased()), editedPerson);
            expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        } catch (DuplicatePersonException | PersonNotFoundException e) {
            throw new IllegalArgumentException(
                    "editedPerson is a duplicate in expectedModel, or it isn't found in the model.");
        }

        assertCommandSuccess(command, expectedModel,
                String.format(AddTagCommand.MESSAGE_ADD_TAG_PERSON_SUCCESS, editedPerson), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     * @see AddTagCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
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